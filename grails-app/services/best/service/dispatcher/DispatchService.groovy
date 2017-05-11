package best.service.dispatcher

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.Timeout
import best.service.dispatcher.actors.rest.RESTActor
import grails.converters.JSON
import grails.transaction.Transactional
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

    def execute(String customerName, String serviceName, String parameters, String key) {
        def requestTime = new Date()
        def customer = getCustomer(customerName)
        checkKey(customerName, customer?.key, serviceName, parameters, key)
        def serviceDefinition = getServiceDefinition(serviceName)
        def customerService = getCustomerService(customer, serviceDefinition)
        rateService.applyRateLimit(customerService)
        def serviceInstance = getServiceInstance(serviceDefinition)


        def actorType = "${messageSource.getMessage("serviceInstance.type.${serviceInstance.type}", null, LCH.getLocale())}Actor"
        Props props = SpringExtProvider.get(actorSystem).props(actorType)
        def actorName = "${serviceDefinition.englishName}-${serviceInstance.id}"
        ActorRef actorRef = actorSystem.actorFor("user/${actorName}")
        if (actorRef?.terminated)
            actorRef = actorSystem.actorOf(props, actorName)

        def query = new ServiceCall(customerId: customer?.id, serviceInstanceId: serviceInstance?.id, parameters: parameters ? parameterService.extractParameters(serviceDefinition, parameters, customer) : [:])
        Future<Object> futureResults = ask(actorRef, query, TIMEOUT)

        def response = Await.result(futureResults, DURATION)
        def responseTime = new Date() - requestTime
        new ServiceLog(customer: customer, serviceDefinition: serviceDefinition, serviceInstance: serviceInstance, requestTime: requestTime, responseTime: responseTime, parameters: parameters ?: '', response: response?.toString() ?: '').save(flush: true)

        response
    }

    private static Customer getCustomer(String customerName) {
        def customer = Customer.findByEnglishNameAndDeleted(customerName as String, false)
        if (!customer)
            throw new ServiceException(101, [customerName])
        customer
    }

    private static void checkKey(String customerName, String customerKey, String serviceName, String parameters, key) {
        def digest = MessageDigest.getInstance("SHA-256")
        byte[] hash = digest.digest("${customerName}${serviceName}${parameters ?: ''}${customerKey}".getBytes(StandardCharsets.UTF_8))
        def hashString =hash.encodeAsHex() //new String(hash, StandardCharsets.UTF_8)
        if (hashString != key)
            throw new ServiceException(102, [key])
    }

    private static ServiceDefinition getServiceDefinition(String serviceName) {
        def serviceDefinition = ServiceDefinition.findByEnglishNameAndDeleted(serviceName, false)
        if (!serviceDefinition)
            throw new ServiceException(103, [serviceName])
        serviceDefinition
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
