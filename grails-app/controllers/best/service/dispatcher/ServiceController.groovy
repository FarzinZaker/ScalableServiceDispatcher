package best.service.dispatcher

import grails.converters.JSON

class ServiceController {

    def dispatchService
    def springSecurityService

    def authenticate() {
        if (!params.username || !params.password || !params.customer || !params.app) {
            render([
                    status: 'f',
                    error : [
                            code   : 900,
                            message: message(code: 'service.exception.900')
                    ]
            ] as JSON)
            return
        }

        def user = User.findByUsername(params.username?.toString()?.toLowerCase())
        if (!user || !user.enabled || user.accountExpired || user.passwordExpired) {
            render([
                    status: 'f',
                    error : [
                            code   : 901,
                            message: message(code: 'service.exception.901')
                    ]
            ] as JSON)
            return
        }

        if (!springSecurityService.passwordEncoder.isPasswordValid(user.password, params.password?.toString(), null)) {
            render([
                    status: 'f',
                    error : [
                            code   : 902,
                            message: message(code: 'service.exception.902')
                    ]
            ] as JSON)
            return
        }

        def app = App.findByEnglishNameAndDeleted(params.app, false)
        if (!app) {
            render([
                    status: 'f',
                    error : [
                            code   : 903,
                            message: message(code: 'service.exception.903')
                    ]
            ] as JSON)
            return
        }

        def customer = Customer.findByEnglishNameAndDeleted(params.customer, false)
        if (!customer) {
            render([
                    status: 'f',
                    error : [
                            code   : 904,
                            message: message(code: 'service.exception.904')
                    ]
            ] as JSON)
            return
        }

        def appCustomer = AppCustomer.findByAppAndCustomerAndDeleted(app, customer, false)
        if (!appCustomer || (appCustomer?.startDate && appCustomer?.startDate > new Date()) || (appCustomer?.endDate && appCustomer?.endDate < new Date())) {
            render([
                    status: 'f',
                    error : [
                            code   : 905,
                            message: message(code: 'service.exception.905')
                    ]
            ] as JSON)
            return
        }

        def customerUser = CustomerUser.findByCustomerAndUserAndDeleted(customer, user, false)
        if (!customerUser) {
            render([
                    status: 'f',
                    error : [
                            code   : 906,
                            message: message(code: 'service.exception.906')
                    ]
            ] as JSON)
            return
        }

        def appCustomerUser = AppCustomerUser.findByAppCustomerAndCustomerUserAndDeleted(appCustomer, customerUser, false)
        if (!appCustomerUser) {
            render([
                    status: 'f',
                    error : [
                            code   : 907,
                            message: message(code: 'service.exception.907')
                    ]
            ] as JSON)
            return
        }

        render([
                status  : 's',
                clientNo: customer?.clientNo
        ] as JSON)
    }

    def execute() {
        try {
            if (params.app)
                render dispatchService.executeAsApp(params.app, params.customer, params.service, params.parameters, params.key)
            else
                render dispatchService.executeAsCustomer(params.customer, params.service, params.parameters, params.key)
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
