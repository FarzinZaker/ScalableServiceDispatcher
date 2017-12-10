package best.service.dispatcher


class ServiceParameter {

    ServiceDefinition serviceDefinition
    String name
    String type
    Boolean required
    String systemValue
    String displayName
    Boolean displayForSignature
    String aggregateField
    Boolean useForSignatureCheckWithCore = false
    Boolean useAsAmountToCheckWithCore = false
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static mapping = {
        useForSignatureCheckWithCore column: 'forChkWithCore'
        useAsAmountToCheckWithCore column: 'useAsAmntToChkWithCore'
    }

    static constraints = {
        name()
        type(inList: ServiceParameter.PARAMETER_TYPES)
        required()
        systemValue(nullable: true)
        displayName(nullable: true)
        displayForSignature(nullable: true)
        aggregateField(nullable: true)
        useForSignatureCheckWithCore(nullable: true)
        useAsAmountToCheckWithCore(nullable: true)
    }

    static transient PARAMETER_TYPES = ['String', 'Integer', 'Long', 'Double', 'Float', 'Date', 'Boolean', 'JSON']
}
