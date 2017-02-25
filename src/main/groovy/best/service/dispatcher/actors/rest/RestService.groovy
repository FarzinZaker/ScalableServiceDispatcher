package best.service.dispatcher.actors.rest

import javax.inject.Named

/**
 * Created by root on 2/24/17.
 */
@Named("RestService")
class RestService {

    def execute(definition){
        "service executed"
    }
}

