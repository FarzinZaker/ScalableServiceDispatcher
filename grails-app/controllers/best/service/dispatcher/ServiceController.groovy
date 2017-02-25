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

        println("actorSystem: ${actorSystem}")

        def requestId = UUID.randomUUID().toString()
        def actorType = "RestActor"
        Props props = SpringExtProvider.get(actorSystem).props(actorType)
        def actorName = "${actorType}-${requestId}"
        ActorRef actorRef = actorSystem.actorOf(props, actorName)

        def query = new RestActor.ServiceCall(id: requestId)
        Future<Object> futureResults = ask(actorRef, query, TIMEOUT_3_SECONDS)

        Promises.task {
            def randomIntegerResults = Await.result(futureResults, DURATION_3_SECONDS)
            println("service result: " + randomIntegerResults)
            actorSystem.stop(actorRef)
            render randomIntegerResults
        }
    }
}
