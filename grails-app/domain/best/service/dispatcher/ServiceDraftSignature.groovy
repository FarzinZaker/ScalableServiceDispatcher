package best.service.dispatcher

class ServiceDraftSignature {

    ServiceDraft draft
    User user
    String decision

    static constraints = {
        decision inList: ServiceDraftSignature.DECISION_TYPES
    }
    static transient DECISION_TYPES = ['accept', 'reject']

}
