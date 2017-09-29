package best.service.dispatcher

import grails.converters.JSON
import grails.plugin.springsecurity.authentication.encoding.BCryptPasswordEncoder

class MobileController {

    def springSecurityService
    def dispatchService

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

        if (springSecurityService.passwordEncoder.isPasswordValid(user.password, params.password?.toString(), null)) {
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
                drafts.addAll(ServiceDraft.findAllByCustomerAndServiceDefinitionAndDone(signature.customer, signature.service, false) ?: [])
        }
        drafts = drafts.findAll { !ServiceDraftSignature.findByDraftAndUser(it, user) }
        render([
                status: 's',
                body  : drafts.collect { ServiceDraft draft ->
                    [
                            id      : draft?.id,
                            customer: draft?.customer?.name,
                            service : draft?.serviceDefinition?.name,
                            date    : format.jalaliDate(date: draft?.dateCreated, hm: 'true'),
                            done    : draft.done,
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
                        done      : draft.done,
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
        signature.save()

        dispatchService.executeDraft(draft)

        render([
                status: 's',
                body  : ''
        ] as JSON)
    }

}
