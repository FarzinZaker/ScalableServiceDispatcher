package best.service.dispatcher

import fi.joensuu.joyds1.calendar.JalaliCalendar
import grails.transaction.Transactional

@Transactional
class ParameterLimitService {

    def applyParameterLimits(CustomerService customerService, Map parameters) {
        def limits = ServiceParameterLimit.findAllByCustomerServiceAndDeleted(customerService, false)
        limits.each { limit ->

            def preConditions = ServiceParameterCondition.findAllByLimitAndTypeAndDeleted(limit, 'pre', false)
            if (!preConditions.any { !checkCondition(it, customerService, parameters) }) {
                def postConditions = ServiceParameterCondition.findAllByLimitAndTypeAndDeleted(limit, 'post', false)
                postConditions.each {
                    if (!checkCondition(it, customerService, parameters))
                        throw new ServiceException(limit)
                }
            }
        }
    }

    Boolean checkCondition(ServiceParameterCondition condition, CustomerService customerService, Map parameters) {
        def oldValues = []
        if (condition.unit == 'day') {
            def periodStart = new Date().clearTime()
            oldValues = getParameterValues(customerService, condition.parameter, periodStart)
        }
        if (condition.unit == 'month') {
            def cal = Calendar.getInstance()
            cal.setTime(new Date())
            def jc = new JalaliCalendar(cal)
            def periodStart = (new Date() - jc.getDay() + 1).clearTime()
            oldValues = getParameterValues(customerService, condition.parameter, periodStart)
        }
        def valueList = oldValues + [parameters[condition.parameter.name]]
        def sum
        def value
        switch (condition.parameter.type) {
            case 'String':
                sum = valueList.collect { it as String }.join(' ')
                value = condition.value as String
                break
            case 'Date':
                sum = valueList.collect { it as Date }.max()
                value = condition.value as Date
                break
            case 'Boolean':
                sum = valueList.collect { it as Boolean }.any()
                value = condition.value as Boolean
                break
            default:
                sum = valueList.collect { it as Double }.sum()
                value = condition.value as Double
        }
        switch (condition.operator) {
            case 'eq':
                if (sum != value)
                    return false
                break
            case 'gt':
                if (sum <= value)
                    return false
                break
            case 'gte':
                if (sum < value)
                    return false
                break
            case 'lt':
                if (sum >= value)
                    return false
                break
            case 'lte':
                if (sum > value)
                    return false
                break
        }
        true
    }

    List getParameterValues(CustomerService customerService, ServiceParameter parameter, Date periodStart) {
        (ServiceLogParameter.createCriteria().list {
            createAlias('log', 'serviceLog')
            gte('serviceLog.dateCreated', periodStart)
            createAlias('serviceLog.customer', 'customer')
            createAlias('serviceLog.serviceDefinition', 'serviceDefinition')
            eq('customer.id', customerService?.customer?.id)
            eq('serviceDefinition.id', customerService?.service?.id)
            createAlias('parameter', 'param')
            eq('param.id', parameter?.id)
            projections {
                property('value')
            }
        } ?: []) as List
    }
}
