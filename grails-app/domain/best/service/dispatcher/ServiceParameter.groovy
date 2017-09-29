package best.service.dispatcher

class ServiceParameter {

    ServiceDefinition serviceDefinition
    String name
    String type
    Boolean required
    String systemValue
    String displayName
    Boolean displayForSignature
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name()
        type(inList: ServiceParameter.PARAMETER_TYPES)
        required()
        systemValue(nullable: true)
        displayName(nullable: true)
        displayForSignature(nullable: true)
    }

    static transient PARAMETER_TYPES = ['String', 'Integer', 'Long', 'Double', 'Float', 'Date', 'Boolean']
}
