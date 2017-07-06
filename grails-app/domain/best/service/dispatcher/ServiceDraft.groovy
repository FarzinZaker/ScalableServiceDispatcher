package best.service.dispatcher

class ServiceDraft {

    Customer customer
    ServiceDefinition serviceDefinition
    Boolean done = false
    Date dateCreated

    static hasMany = [parameters: ServiceDraftParameter, signatures: ServiceDraftSignature]

    static constraints = {
    }
}
