package best.service.dispatcher

class ServiceParameterLimit {

    CustomerService customerService
    String name
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
    }
}
