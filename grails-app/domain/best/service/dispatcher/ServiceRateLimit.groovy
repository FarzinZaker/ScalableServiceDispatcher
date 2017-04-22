package best.service.dispatcher

class ServiceRateLimit {

    CustomerService customerService
    String period
    Integer limit
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        period inList: ServiceRateLimit.PERIODS
        limit min: 1
    }

    static transient PERIODS = ['minute', 'hour', 'day', 'week', 'month', 'year']
}
