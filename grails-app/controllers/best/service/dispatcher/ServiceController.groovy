package best.service.dispatcher

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.util.Timeout
import best.service.dispatcher.actors.rest.RestActor
import grails.async.Promises
import grails.core.GrailsApplication
import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

import java.util.concurrent.TimeUnit

import static akka.pattern.Patterns.ask
import static conf.SpringExtension.SpringExtProvider

class ServiceController {

    static final FiniteDuration DURATION_3_SECONDS = FiniteDuration.create(3, TimeUnit.SECONDS)
    static final TIMEOUT_3_SECONDS = Timeout.durationToTimeout(DURATION_3_SECONDS)

    ActorSystem actorSystem
    GrailsApplication grailsApplication

    def execute() {

//        some print statements to help debug things
        println("actorSystem: ${actorSystem}")
        println("beans: ${grailsApplication.mainContext.beanDefinitionNames}")

        def requestId = UUID.randomUUID().toString()

        // describe the properties of the desired actor
        def actorType = "RestActor"
        Props props = SpringExtProvider.get(actorSystem).props(actorType)

        // ask akka to create the actor
        // use unique actor name because this will be an ephemeral, stateless actor
        // managed as a prototype bean in the spring context
        def actorName = "${actorType}-${requestId}"
        ActorRef actorRef = actorSystem.actorOf(props, actorName)

        def query = new RestActor.ServiceCall(id: requestId, max: Integer.MAX_VALUE)
        Future<Object> futureResults = ask(actorRef, query, TIMEOUT_3_SECONDS)

        Promises.task {
            def randomIntegerResults = Await.result(futureResults, DURATION_3_SECONDS)
            println("randomIntegerResults: " + randomIntegerResults)

            actorSystem.stop(actorRef)

//            render(contentType: 'application/json') {
//                id = requestId
//                randomInteger = randomIntegerResults
//            }
            render randomIntegerResults
        }
    }
}
