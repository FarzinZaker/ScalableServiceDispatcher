package best.service.dispatcher

import grails.converters.JSON

class MobileController {

    def springSecurityService

    def authenticate() {
        if (!params.username || !params.password) {
            render([
                    status: 'f',
                    id    : 0
            ] as JSON)
            return
        }

        def user = User.findByUsername(params.username?.toString()?.toLowerCase())
        if (!user || !user.enabled || user.accountExpired || user.passwordExpired) {
            render([
                    status: 'f',
                    id    : 0
            ] as JSON)
            return
        }

        if (user.password == springSecurityService.encodePassword(params.password?.toString())) {
            render([
                    status: 's',
                    id    : user.id?.toString()
            ] as JSON)
        } else {
            render([
                    status: 'f',
                    id    : 0
            ] as JSON)
        }
    }

    def draftList() {
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
        def signatures = []
        try {
            signatures = CustomerServiceSignature.findAllByCustomerUserInListAndDeleted(CustomerUser.findAllByUserAndDeleted(user, false), false)?.collect { it.customerService }
        } catch (ignored) {
        }

        def drafts = []
        signatures.each { signature ->
            drafts.addAll(ServiceDraft.findAllByCustomerAndServiceDefinitionAndDone(signature.customer, signature.service, false) ?: [])
        }
        render([
                status: 's',
                body  : drafts.collect { ServiceDraft draft ->
                    [
                            id      : draft?.id,
                            customer: draft?.customer?.name,
                            service : draft?.serviceDefinition?.name,
                            date    : format.jalaliDate(date: draft?.dateCreated, hm: 'true')
                    ]
                }
        ] as JSON)
    }

    def draft() {
        if (!params.user || !params.id) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.insufficientParams')
            ] as JSON)
            return
        }
        def user = User.get(params.user as Long)
        if (!user) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.noSuchUser')
            ] as JSON)
            return
        }
        def draft = ServiceDraft.get(params.id as String)
        if (!draft) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.noSuchDraft')
            ] as JSON)
            return
        }
        def customerService = CustomerService.findByCustomerAndServiceAndDeleted(draft.customer, draft.serviceDefinition, false)
        def signatureList = CustomerServiceSignature.findAllByCustomerServiceAndDeleted(customerService, false)
        if (!signatureList?.collect { it.customerUser?.user?.id }?.contains(user?.id)) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.signatureNotAllowed')
            ] as JSON)
            return
        }
        render([
                status: 's',
                body  : [
                        id        : draft.id,
                        customer  : draft?.customer?.name,
                        service   : draft?.serviceDefinition?.name,
                        date      : format.jalaliDate(date: draft?.dateCreated, hm: 'true'),
                        done      : it.done,
                        parameters: ServiceDraftParameter.findAllByDraft(draft)?.collect { parameterValue ->
                            [
                                    name : parameterValue?.parameter?.name,
                                    type : parameterValue?.parameter?.type,
                                    value: parameterValue?.value
                            ]
                        } ?: []
                ]
        ] as JSON)
    }

    def decideOnServiceDraft() {
        if (!params.user || !params.id || !params.decision) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.insufficientParams')
            ] as JSON)
            return
        }
        def user = User.get(params.user as Long)
        if (!user) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.noSuchUser')
            ] as JSON)
            return
        }
        def draft = ServiceDraft.get(params.id as String)
        if (!draft) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.noSuchDraft')
            ] as JSON)
            return
        }
        if (draft.done) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.draftIsAlreadyDone')
            ] as JSON)
            return
        }
        def customerService = CustomerService.findByCustomerAndServiceAndDeleted(draft.customer, draft.serviceDefinition, false)
        def signatureList = CustomerServiceSignature.findAllByCustomerServiceAndDeleted(customerService, false)
        if (!signatureList?.collect { it.customerUser?.user?.id }?.contains(user?.id)) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.signatureNotAllowed')
            ] as JSON)
            return
        }
        def currentSignatures = ServiceDraftSignature.findAllByDraft(draft)
        if (currentSignatures?.collect { it?.user?.id }?.contains(user?.id)) {
            render([
                    status: 'f',
                    body  : message(code: 'decideOnServiceDraft.error.alreadySigned')
            ] as JSON)
            return
        }
        def signature = new ServiceDraftSignature()
        signature.draft = draft
        signature.user = user
        signature.decision = params.decision
        render([
                status: 's',
                body  : ''
        ] as JSON)
    }

}
