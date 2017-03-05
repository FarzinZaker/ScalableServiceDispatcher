package best.service.dispatcher

class ServiceRateLimit {

    CustomerService customerService
    Integer period
    Integer limit
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
    }
}
