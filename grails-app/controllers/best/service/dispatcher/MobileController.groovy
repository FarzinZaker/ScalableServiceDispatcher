package best.service.dispatcher

import best.service.dispatcher.security.CrossPlatformEncryption
import grails.converters.JSON

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

        if (springSecurityService.passwordEncoder.isPasswordValid(user.password, new CrossPlatformEncryption().decrypt(params.password?.toString(), 'BESTKEY'), null)) {
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

    def changePassword() {
        if (!params.user || !params.password || !params.newPassword) {
            render([
                    status: 'f'
            ] as JSON)
            return
        }

        def user = User.get(params.user?.toString()?.toLong())
        if (!user || !user.enabled || user.accountExpired || user.passwordExpired) {
            render([
                    status: 'f'
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
                    status: 'f'
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
                            approved: draft.approved ?: false
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
        drafts = drafts.findAll { ServiceDraftSignature.findByDraftAndUser(it, user) }

        render([
                status: 's',
                body  : drafts.collect { ServiceDraft draft ->
                    [
                            id      : draft?.id,
                            customer: draft?.customer?.name,
                            service : draft?.serviceDefinition?.name,
                            date    : format.jalaliDate(date: draft?.dateCreated, hm: 'true'),
                            done    : draft.done,
                            approved: draft.approved ?: false
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
        def parameters = ServiceDraftParameter.findAllByDraft(draft)?.findAll { it.parameter?.displayForSignature }?.collect { parameterValue ->
            def item = [:]
            if (parameterValue?.parameter?.type == 'JSON' && parameterValue?.parameter?.aggregateField && parameterValue?.parameter?.aggregateField?.trim() != '') {
                def shell = new GroovyShell()
                shell.setVariable('parameterValue', JSON.parse(parameterValue.value))
                def value = shell.evaluate("def evaluator = ${parameterValue?.parameter?.aggregateField}; evaluator(parameterValue)")
                item = [
                        name : parameterValue?.parameter?.displayName ?: parameterValue?.parameter?.name,
                        type : parameterValue?.parameter?.type,
                        value: value
                ]
            } else {
                item = [
                        name : parameterValue?.parameter?.displayName ?: parameterValue?.parameter?.name,
                        type : parameterValue?.parameter?.type,
                        value: parameterValue?.value
                ]
            }
            item
        } ?: []
        render([
                status: 's',
                body  : [
                        id        : draft.id,
                        customer  : draft?.customer?.name,
                        service   : draft?.serviceDefinition?.name,
                        date      : format.jalaliDate(date: draft?.dateCreated, hm: 'true'),
                        done      : draft.done,
                        approved  : draft.approved ?: false,
                        parameters: parameters,
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
