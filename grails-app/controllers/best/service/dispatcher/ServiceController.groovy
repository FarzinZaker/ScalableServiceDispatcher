package best.service.dispatcher

import grails.converters.JSON

class ServiceController {

    def dispatchService

    def execute() {
        try {
            render dispatchService.execute(params.customer, params.service, params.parameters, params.key)
        } catch (ServiceException serviceException) {
            render([error: [
                    code   : serviceException.errorCode,
                    message: serviceException.message
            ]
            ] as JSON)
        } catch (ignored) {
            render([error: [
                    code   : 100,
                    message: message(code: 'service.exception.100')
            ]
            ] as JSON)
            ignored.printStackTrace()
        }
    }
}
