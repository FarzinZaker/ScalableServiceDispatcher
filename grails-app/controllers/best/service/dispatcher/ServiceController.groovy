package best.service.dispatcher

import best.service.dispatcher.security.CrossPlatformEncryption
import grails.converters.JSON
import org.grails.datastore.mapping.model.types.Custom

class ServiceController {

    def dispatchService
    def springSecurityService

    def authenticate() {
        if (!params.username || !params.password || !params.app) {
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

        def customer
        if (params.customer)
            customer = Customer.findByEnglishNameAndDeleted(params.customer, false)
        else {
            def list = CustomerUser.findAllByUserAndDeleted(user, false)?.customer
            customer = list.find { AppCustomer.findByAppAndCustomerAndDeleted(app, it, false) }
            if (!customer)
                customer = CustomerUser.findByUserAndDeleted(user, false)?.customer
        }
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
                clientNo: customer?.clientNo,
                customer: customer?.englishName,
                userId  : user?.id
        ] as JSON)
    }

    def changePassword() {
        if (!params.user || !params.password || !params.newPassword) {
            render([
                    status: 'f',
                    error : [
                            code   : 900,
                            message: message(code: 'service.exception.900')
                    ]
            ] as JSON)
            return
        }

        def user = User.get(params.user?.toString()?.toLong())
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

        if (springSecurityService.passwordEncoder.isPasswordValid(user.password, new CrossPlatformEncryption().decrypt(params.password?.toString(), 'BESTKEY'), null)) {
            User.withNewTransaction {
                user.password = new CrossPlatformEncryption().decrypt(params.newPassword?.toString(), 'BESTKEY')
                user.save(flash: true)
            }
            render([
                    status: 's'
            ] as JSON)
        } else {
            render([
                    status: 'f',
                    error : [
                            code   : 902,
                            message: message(code: 'service.exception.902')
                    ]
            ] as JSON)
        }
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

    def draftHistory() {

        ServiceDraft.findAllByApprovedIsNull().each {
            dispatchService.checkDraft(it)
        }

        if (!params.user) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.insufficientParams')
            ] as JSON)

        }
        def minId = 0
        if (params.after)
            minId = params.after?.toString()?.toLong()
        def user = User.get(params.user as Long)
        if (!user) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.noSuchUser')
            ] as JSON)
            return
        }
        def signatures = []
        try {
            signatures = CustomerServiceSignature.findAllByCustomerUserInListAndDeleted(CustomerUser.findAllByUserAndDeleted(user, false), false)?.collect { it.customerService }
        } catch (ignored) {
        }

        def drafts = []
        signatures.each { signature ->
            if (signature?.id > minId)
                drafts.addAll(ServiceDraft.findAllByCustomerAndServiceDefinition(signature.customer, signature.service) ?: [])
        }
//        drafts = drafts.findAll { ServiceDraftSignature.findByDraftAndUser(it, user) }

        render([
                status: 's',
                body  : drafts.collect { ServiceDraft draft ->
                    [
                            id        : draft.id,
                            customer  : draft?.customer?.name,
                            service   : draft?.serviceDefinition?.name,
                            date      : format.jalaliDate(date: draft?.dateCreated, hm: 'true'),
                            done      : draft.done,
                            approved  : draft.approved ?: false,
                            parameters: ServiceDraftParameter.findAllByDraft(draft)?.findAll { it.parameter?.displayForSignature }?.collect { parameterValue ->
                                [
                                        name : parameterValue?.parameter?.displayName ?: parameterValue?.parameter?.name,
                                        type : parameterValue?.parameter?.type,
                                        value: parameterValue?.value
                                ]
                            } ?: [],
                            signatures: ServiceDraftSignature.findAllByDraft(draft)?.collect {
                                [
                                        user    : it.user?.toString(),
                                        decision: message(code: "serviceDraftSignature.decision.${it.decision}").toString()
                                ]
                            } ?: []
                    ]
                }
        ] as JSON)
    }

    def history() {

        ServiceDraft.findAllByApprovedIsNull().each {
            dispatchService.checkDraft(it)
        }

        if (!params.user) {
            render([
                    status: 'f',
                    total : 0,
                    body  : message(code: 'decideOnServiceDraft.error.insufficientParams')
            ] as JSON)

        }
        def pageNumber = 1
        if (params.pageNumber)
            pageNumber = params.pageNumber?.toString()?.toLong()
        def pageSize = 10
        if (params.pageSize)
            pageSize = params.pageSize?.toString()?.toLong()
        def user = User.get(params.user as Long)
        if (!user) {
            render([
                    status: 'f',
                    total : 0,
                    body  : message(code: 'decideOnServiceDraft.error.noSuchUser')
            ] as JSON)
            return
        }

        def customer
        if (params.customer)
            customer = Customer.findByEnglishName(params.customer)
        else
            customer = CustomerUser.findByUserAndDeleted(user, false)?.customer

        def services = ServiceLog.findAllByCustomer(customer, [max: pageSize, sort: "id", order: "asc", offset: (pageNumber - 1) * pageSize])
        def total = ServiceLog.countByCustomer(customer)

        render([
                status: 's',
                total : total,
                body  : services.collect { ServiceLog serviceLog ->
                    [
                            id        : serviceLog.id,
                            customer  : serviceLog?.customer?.name,
                            service   : serviceLog?.serviceDefinition?.name,
                            date      : format.jalaliDate(date: serviceLog?.dateCreated, hm: 'true'),
                            done      : true,
                            approved  : true,
                            parameters: ServiceLogParameter.findAllByLog(serviceLog)?.collect { parameterValue ->
                                [
                                        name : parameterValue?.parameter?.displayName ?: parameterValue?.parameter?.name,
                                        type : parameterValue?.parameter?.type,
                                        value: parameterValue?.value
                                ]
                            } ?: []
                    ]
                }
        ] as JSON)
    }

    def access() {
        if (!params.user) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.insufficientParams')
            ] as JSON)

        }

        def user = User.get(params.user as Long)
        if (!user) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.noSuchUser')
            ] as JSON)
            return
        }

        def customerUsers = CustomerUser.findAllByUserAndDeleted(user, false)
        def apps = AppCustomerUser.findAllByCustomerUserInListAndDeleted(customerUsers, false).collect { it.appCustomer?.app }?.unique { it.id }
        def customers = customerUsers?.collect { it.customer }
        render([
                apps     : apps.collect { [id: it.id, name: it.name, englishName: it.englishName] },
                customers: customers.collect { Customer custo ->
                    [
                            id         : custo.id,
                            name       : custo.name,
                            englishName: custo.englishName,
                            clientNo   : custo.clientNo,
                            services   : CustomerService.createCriteria().list {
                                eq('customer', custo)
                                or {
                                    isNull('startDate')
                                    gte('startDate', new Date())
                                }
                                or {
                                    isNull('endDate')
                                    lte('endDate', new Date())
                                }
                            }?.collect { CustomerService customerService ->
                                def service = customerService.service
                                [
                                        id         : service.id,
                                        name       : service.name,
                                        englishName: service.englishName
                                ]
                            } ?: []
                    ]
                }
        ] as JSON)
    }
}
