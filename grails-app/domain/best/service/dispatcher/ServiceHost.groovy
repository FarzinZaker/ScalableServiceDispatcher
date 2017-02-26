package best.service.dispatcher

class ServiceHost {

    String name
    String protocol
    String address

    static constraints = {
        name()
        protocol(inList: ['http', 'https'])
        address()
    }
}
