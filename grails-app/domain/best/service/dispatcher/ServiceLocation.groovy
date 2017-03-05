package best.service.dispatcher

class ServiceLocation {

    ServiceDefinition serviceDefinition
    ServiceHost host
    String path
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        host()
        path(maxSize: 2000)
    }
}
