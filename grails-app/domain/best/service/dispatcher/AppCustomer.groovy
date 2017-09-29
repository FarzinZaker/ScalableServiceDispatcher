package best.service.dispatcher

class AppCustomer {

    App app
    Customer customer
    Date startDate
    Date endDate
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        app()
        customer()
        startDate(nullable: true)
        endDate(nullable: true)
    }
}
