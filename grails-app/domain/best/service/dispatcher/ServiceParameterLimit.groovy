package best.service.dispatcher

class ServiceParameterLimit {

    CustomerService customerService
    ServiceParameter parameter
    Integer period
    Double limit
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
    }
}
