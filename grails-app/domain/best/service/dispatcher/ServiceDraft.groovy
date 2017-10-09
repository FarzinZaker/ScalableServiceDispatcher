package best.service.dispatcher

class ServiceDraft {

    Customer customer
    ServiceDefinition serviceDefinition
    Boolean done = false
    Boolean approved = false
    Date dateCreated

    static hasMany = [parameters: ServiceDraftParameter, signatures: ServiceDraftSignature]

    static constraints = {
        approved(nullable: true)
    }
}
