package best.service.dispatcher

class ServiceHost {

    String name
    String protocol
    String address
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name()
        protocol(inList: ServiceHost.HOST_PROTOCOLS)
        address()
    }

    static transient HOST_PROTOCOLS = ['http', 'https']
}
