package best.service.dispatcher

class ServiceLog {

    Customer customer
    ServiceDefinition serviceDefinition
    ServiceInstance serviceInstance
    Date requestTime
    Long responseTime
    String parameters
    String response


    static constraints = {
    }
}
