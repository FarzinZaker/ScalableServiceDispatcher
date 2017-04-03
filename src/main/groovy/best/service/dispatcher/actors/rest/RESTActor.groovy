package best.service.dispatcher.actors.rest

import akka.actor.UntypedActor
import akka.dispatch.Futures
import akka.pattern.Patterns
import grails.converters.JSON
import groovy.transform.Immutable
import org.springframework.context.annotation.Scope
import scala.concurrent.Future

import javax.inject.Inject
import javax.inject.Named
import java.util.concurrent.Callable

@Named("RESTActor")
@Scope("prototype")
class RESTActor extends UntypedActor {

    @Immutable
    public static class ServiceCall {
        Long customerId
        Long serviceInstanceId
        String jsonData
    }

    // the service that will be automatically injected
    RESTService restService;

    @Inject
    public RESTActor(@Named("RESTService") RESTService restService) {
        this.restService = restService
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ServiceCall) {
            ServiceCall query = message
            Callable<Object> op = new Callable<Object>() {
                @Override
                Object call() throws Exception {
                    return restService.execute(query?.customerId, query?.serviceInstanceId, JSON.parse(query?.jsonData))
                }
            }
            Future<Object> futureOpResults = Futures.future(op, this.context.dispatcher())
            Patterns.pipe(futureOpResults, context().dispatcher()).to(sender, self)
        } else {
            unhandled(message);
        }
    }

}