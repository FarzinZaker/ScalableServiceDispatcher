package best.service.dispatcher

class ServiceDefinition {

    String name
    String type
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name()
        type(inList: ['rest'])
    }
}
