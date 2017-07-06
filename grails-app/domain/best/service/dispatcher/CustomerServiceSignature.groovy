package best.service.dispatcher

class CustomerServiceSignature {

    CustomerService customerService
    CustomerUser customerUser
    Boolean required = false
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
    }
}
