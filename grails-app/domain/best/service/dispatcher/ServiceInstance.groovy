package best.service.dispatcher

class ServiceInstance {

    ServiceDefinition serviceDefinition
    ServiceHost host
    String path
    String type
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        type(inList: ServiceInstance.SERVICE_TYPES)
    }

    static transient SERVICE_TYPES = ['rest']
}
