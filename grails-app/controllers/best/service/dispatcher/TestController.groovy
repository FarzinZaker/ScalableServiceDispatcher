package best.service.dispatcher

import grails.converters.JSON

class TestController {

    def bill() {
        println params
        render([time: new Date(), data: params] as JSON)
    }
}
