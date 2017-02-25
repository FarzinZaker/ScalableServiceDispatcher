package best.service.dispatcher.actors.rest

import akka.actor.UntypedActor
import akka.dispatch.Futures
import akka.pattern.Patterns
import groovy.transform.Immutable
import org.springframework.context.annotation.Scope
import scala.concurrent.Future

import javax.inject.Inject
import javax.inject.Named
import java.util.concurrent.Callable

@Named("RestActor")
@Scope("prototype")
class RestActor extends UntypedActor {

    @Immutable
    public static class ServiceCall {
        String id
        Integer max
    }

    // the service that will be automatically injected
    RestService restService;

    @Inject
    public RestActor(@Named("RestService") RestService restService) {
        this.restService = restService;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ServiceCall) {
            ServiceCall query = message
            Callable<Object> op = new Callable<Object>() {
                @Override
                Object call() throws Exception {
                    return restService.execute(null)
                }
            }
            Future<Object> futureOpResults = Futures.future(op, this.context.dispatcher())
            Patterns.pipe(futureOpResults, context().dispatcher()).to(sender, self)
        } else {
            unhandled(message);
        }
    }

}