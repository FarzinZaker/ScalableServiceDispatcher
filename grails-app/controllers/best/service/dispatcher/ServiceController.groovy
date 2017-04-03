package best.service.dispatcher

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.Timeout
import best.service.dispatcher.actors.rest.RESTActor
import grails.async.Promises
import grails.converters.JSON
import grails.core.GrailsApplication
import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

import java.util.concurrent.TimeUnit

import static akka.pattern.Patterns.ask
import static conf.SpringExtension.SpringExtProvider

class ServiceController {

    static final FiniteDuration DURATION = FiniteDuration.create(30, TimeUnit.SECONDS)
    static final TIMEOUT = Timeout.durationToTimeout(DURATION)
    static final Random random = new Random()

    ActorSystem actorSystem
    GrailsApplication grailsApplication

    def execute() {

        def customer = getCustomer()
        def serviceDefinition = getServiceDefinition()
        def serviceInstance = getServiceInstance(serviceDefinition)


        def actorType = "${message(code: "serviceInstance.type.${serviceInstance.type}")}Actor"
        Props props = SpringExtProvider.get(actorSystem).props(actorType)
        def actorName = "${serviceDefinition.englishName}-${serviceInstance.id}"
        ActorRef actorRef = actorSystem.actorFor("user/${actorName}")
        if (actorRef?.terminated)
            actorRef = actorSystem.actorOf(props, actorName)

        def query = new RESTActor.ServiceCall(customerId: customer?.id, serviceInstanceId: serviceInstance?.id, jsonData: (params.data ?: [:]) as JSON)
        Future<Object> futureResults = ask(actorRef, query, TIMEOUT)

        Promises.task {
            def result = Await.result(futureResults, DURATION)
            render result
        }
    }

    private Customer getCustomer() {
        def customer = Customer.findByEnglishNameAndDeleted(params.customer as String, false)
        if (!customer)
            throw new ServiceException(101, [params.customer])
        customer
    }

    private ServiceDefinition getServiceDefinition() {
        def serviceDefinition = ServiceDefinition.findByEnglishNameAndDeleted(params.service as String, false)
        if (!serviceDefinition)
            throw new ServiceException(102, [params.service])
        serviceDefinition
    }

    private static List<ServiceInstance> getServiceInstanceList(ServiceDefinition serviceDefinition) {
        def serviceInstanceList = ServiceInstance.findAllByServiceDefinitionAndDeleted(serviceDefinition, false)
        if (!serviceInstanceList?.size())
            throw new ServiceException(103, [serviceDefinition?.name])
        serviceInstanceList
    }

    private static ServiceInstance getServiceInstance(ServiceDefinition serviceDefinition) {
        def serviceInstanceList = getServiceInstanceList(serviceDefinition)
        serviceInstanceList[random.nextInt(serviceInstanceList.size())]
    }
}
