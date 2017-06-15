package best.service.dispatcher

class ServiceLog {

    Customer customer
    ServiceDefinition serviceDefinition
    ServiceInstance serviceInstance
    Long responseTime
    String response
    Date dateCreated

    static hasMany = [parameters: ServiceLogParameter]

    static mapping = {
        response sqlType: 'clob'
    }

    static constraints = {
    }
}
