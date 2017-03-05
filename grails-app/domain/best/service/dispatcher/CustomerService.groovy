package best.service.dispatcher

class CustomerService {

    Customer customer
    ServiceDefinition service
    Date startDate
    Date endDate
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        customer()
        service()
        startDate(nullable: true)
        endDate(nullable: true)
    }
}
