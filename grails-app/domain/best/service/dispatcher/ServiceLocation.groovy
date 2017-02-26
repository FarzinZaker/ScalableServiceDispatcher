package best.service.dispatcher

class ServiceLocation {

    ServiceHost host
    String path

    static constraints = {
        host()
        path(maxSize: 2000)
    }
}
