package best.service.dispatcher

class CustomerService {

    Customer customer
    ServiceDefinition service
    Date startDate
    Date endDate
    Integer minimumSignatures = 0
    Boolean checkSignaturesWithCore = false
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        customer()
        service()
        startDate(nullable: true)
        endDate(nullable: true)
        minimumSignatures(nullable: true)
        checkSignaturesWithCore(nullable: true)
    }
}
