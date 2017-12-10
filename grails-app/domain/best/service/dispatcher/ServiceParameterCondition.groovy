package best.service.dispatcher

class ServiceParameterCondition {

    ServiceParameter parameter
    ServiceParameterLimit limit
    String type
    String count
    String unit
    String operator
    String value
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        type inList: ServiceParameterCondition.TYPES
        unit inList: ServiceParameterCondition.UNITS
        operator inList: ServiceParameterCondition.OPERATORS
    }

    static transient UNITS = ['single', 'day', 'month']
    static transient OPERATORS = ['eq', 'gt', 'gte', 'lt', 'lte', 'cus']
    static transient TYPES = ['pre', 'post']
}
