package best.service.dispatcher

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.Timeout
import grails.converters.JSON
import grails.transaction.Transactional
import grails.util.Environment
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder as LCH
import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

import static akka.pattern.Patterns.ask
import static conf.SpringExtension.SpringExtProvider

@Transactional
class DispatchService {

    static final FiniteDuration DURATION = FiniteDuration.create(30, TimeUnit.SECONDS)
    static final TIMEOUT = Timeout.durationToTimeout(DURATION)
    static final Random random = new Random()

    ActorSystem actorSystem
    MessageSource messageSource

    def parameterService
    def rateService
    def parameterLimitService

    def executeAsCustomer(String customerName, String serviceName, String parameters, String key) {
        def customer = getCustomer(customerName)
        checkKey(customerName, customer?.key, serviceName, parameters, key)
        execute(customer, serviceName, parameters)
    }

    def executeAsApp(String appName, String customerName, String serviceName, String parameters, String key) {
        def app = getApp(appName)
        checkKey(appName, app?.key, serviceName, parameters, key)
        def customer = getCustomer(customerName)
        getAppCustomer(app, customer)
        execute(customer, serviceName, parameters)

    }

    def execute(Customer customer, String serviceName, String parameters) {
        def serviceDefinition = getServiceDefinition(serviceName)
        def customerService = getCustomerService(customer, serviceDefinition)
        rateService.applyRateLimit(customerService)

        def extractedParameters = parameters ? parameterService.extractParameters(serviceDefinition, parameters, customer) : [:]
        parameterLimitService.applyParameterLimits(customerService, extractedParameters)

        if (customerService.minimumSignatures > 0) {
            def serviceDraft = new ServiceDraft(customer: customer, serviceDefinition: serviceDefinition).save(flush: true)
            if (serviceDraft)
                extractedParameters.each {
                    def draftParameter = new ServiceDraftParameter(draft: serviceDraft, parameter: ServiceParameter.findByServiceDefinitionAndNameAndDeleted(serviceDefinition, it.key, false), value: it.value)
                    draftParameter.save(flush: true)
                }
            ([
                    result: 'Service execution is waiting for signatures.'
            ] as JSON)
        } else {
            def requestTime = new Date()
            def serviceInstance = getServiceInstance(serviceDefinition)
            def actorType = "${messageSource.getMessage("serviceInstance.type.${serviceInstance.type}", null, LCH.getLocale())}Actor"
            Props props = SpringExtProvider.get(actorSystem).props(actorType)
            def actorName = "${serviceDefinition.englishName}-${serviceInstance.id}"
            ActorRef actorRef = actorSystem.actorFor("user/${actorName}")
            if (actorRef?.terminated)
                actorRef = actorSystem.actorOf(props, actorName)
            def response = ([result: 'development mode'] as JSON)
            if (!Environment.isDevelopmentMode()) {
                def query = new ServiceCall(customerId: customer?.id, serviceInstanceId: serviceInstance?.id, parameters: extractedParameters)
                Future<Object> futureResults = ask(actorRef, query, TIMEOUT)
                response = Await.result(futureResults, DURATION)
            }
            def responseTime = new Date() - requestTime
            def serviceLog = new ServiceLog(customer: customer, serviceDefinition: serviceDefinition, serviceInstance: serviceInstance, responseTime: responseTime, response: response?.toString() ?: '').save(flush: true)
            if (serviceLog)
                extractedParameters.each {
                    def parameterLog = new ServiceLogParameter(log: serviceLog, parameter: ServiceParameter.findByServiceDefinitionAndNameAndDeleted(serviceDefinition, it.key, false), value: it.value)
                    parameterLog.save(flush: true)
                }
            response
        }
    }

    def checkDraft(ServiceDraft draft) {
        def customerService = CustomerService.findByCustomerAndServiceAndDeleted(draft.customer, draft.serviceDefinition, false)
        if (!customerService)
            return false
        def requiredSignatures = CustomerServiceSignature.findAllByCustomerServiceAndDeleted(customerService, false)
        def currentSignatures = ServiceDraftSignature.findAllByDraft(draft)
        def acceptSignatures = currentSignatures.findAll { it.decision == 'accept' }
        //received required signatures
        if (acceptSignatures.size() >= customerService.minimumSignatures
                && !requiredSignatures.findAll { it.required }.collect { it.customerUser?.user?.id }.any { !acceptSignatures.collect { it.user?.id }.contains(it) }) {
            return true
        }
        //received all signatures (rejected)
        if (!requiredSignatures.collect { it.customerUser?.user?.id }.any { !currentSignatures.collect { it.user?.id }.contains(it) }) {
            draft.done = true
            draft.save()
        }
        return false
    }

    def executeDraft(ServiceDraft draft) {

        if (!checkDraft(draft))
            return

        def requestTime = new Date()
        def serviceDefinition = draft.serviceDefinition
        def customer = draft.customer
        def parametersMap = new HashMap<String, String>()
        ServiceDraftParameter.findAllByDraft(draft).each {
            parametersMap.put(it.parameter?.name, it.value)
        }
        def parameters = (parametersMap as JSON).toString()
        def extractedParameters = parameters ? parameterService.extractParameters(serviceDefinition, parameters, customer) : [:]
        def serviceInstance = getServiceInstance(serviceDefinition)
        def actorType = "${messageSource.getMessage("serviceInstance.type.${serviceInstance.type}", null, LCH.getLocale())}Actor"
        Props props = SpringExtProvider.get(actorSystem).props(actorType)
        def actorName = "${serviceDefinition.englishName}-${serviceInstance.id}"
        ActorRef actorRef = actorSystem.actorFor("user/${actorName}")
        if (actorRef?.terminated)
            actorRef = actorSystem.actorOf(props, actorName)
        def response = ([result: 'development mode'] as JSON)
        if (!Environment.isDevelopmentMode()) {
            def query = new ServiceCall(customerId: customer?.id, serviceInstanceId: serviceInstance?.id, parameters: extractedParameters)
            Future<Object> futureResults = ask(actorRef, query, TIMEOUT)
            response = Await.result(futureResults, DURATION)
        }
        def responseTime = new Date() - requestTime
        def serviceLog = new ServiceLog(customer: customer, serviceDefinition: serviceDefinition, serviceInstance: serviceInstance, responseTime: responseTime, response: response?.toString() ?: '').save(flush: true)
        if (serviceLog)
            extractedParameters.each {
                def parameterLog = new ServiceLogParameter(log: serviceLog, parameter: ServiceParameter.findByServiceDefinitionAndNameAndDeleted(serviceDefinition, it.key, false), value: it.value)
                parameterLog.save(flush: true)
            }
    }

    private static App getApp(String appName) {
        def app = App.findByEnglishNameAndDeleted(appName as String, false)
        if (!app)
            throw new ServiceException(109, [appName])
        app
    }

    private static Customer getCustomer(String customerName) {
        def customer = Customer.findByEnglishNameAndDeleted(customerName as String, false)
        if (!customer)
            throw new ServiceException(101, [customerName])
        customer
    }

    private static void checkKey(String consumerName, String consumerKey, String serviceName, String parameters, key) {
        def digest = MessageDigest.getInstance("SHA-256")
        byte[] hash = digest.digest("${consumerName}${serviceName}${parameters ?: ''}${consumerKey}".getBytes(StandardCharsets.UTF_8))
        def hashString = hash.encodeAsHex() //new String(hash, StandardCharsets.UTF_8)
        if (hashString != key)
            throw new ServiceException(102, [key])
    }

    private static ServiceDefinition getServiceDefinition(String serviceName) {
        def serviceDefinition = ServiceDefinition.findByEnglishNameAndDeleted(serviceName, false)
        if (!serviceDefinition)
            throw new ServiceException(103, [serviceName])
        serviceDefinition
    }

    private static AppCustomer getAppCustomer(App app, Customer customer) {
        def appCustomer = AppCustomer.findByAppAndCustomerAndDeleted(app, customer, false)
        if (!appCustomer)
            throw new ServiceException(110, [customer.name, app.name])
        appCustomer
    }

    private static CustomerService getCustomerService(Customer customer, ServiceDefinition serviceDefinition) {
        def customerService = CustomerService.findByCustomerAndServiceAndDeleted(customer, serviceDefinition, false)
        if (!customerService)
            throw new ServiceException(104, [customer.name, serviceDefinition.name])
        customerService
    }

    private static List<ServiceInstance> getServiceInstanceList(ServiceDefinition serviceDefinition) {
        def serviceInstanceList = ServiceInstance.findAllByServiceDefinitionAndDeleted(serviceDefinition, false)
        if (!serviceInstanceList?.size())
            throw new ServiceException(105, [serviceDefinition?.name])
        serviceInstanceList
    }

    private static ServiceInstance getServiceInstance(ServiceDefinition serviceDefinition) {
        def serviceInstanceList = getServiceInstanceList(serviceDefinition)
        serviceInstanceList[random.nextInt(serviceInstanceList.size())]
    }
}
