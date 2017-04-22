package best.service.dispatcher

import grails.transaction.Transactional
import groovy.time.TimeCategory

@Transactional
class RateService {

    def applyRateLimit(CustomerService customerService) {
        ServiceRateLimit.findAllByCustomerServiceAndDeleted(customerService, false).each { rateLimit ->
            def mins = null
            switch (rateLimit.period) {
                case 'minute':
                    mins = 1
                    break
                case 'hour':
                    mins = 60
                    break
                case 'day':
                    mins = 24 * 60
                    break
                case 'week':
                    mins = 7 * 24 * 60
                    break
                case 'month':
                    mins = 30 * 24 * 60
                    break
                case 'year':
                    mins = 365 * 24 * 60
                    break
                default:
                    mins = 1
            }
            def startDate = new Date()
            use(TimeCategory) {
                startDate = startDate - mins.minutes
            }
            def requestsCount = ServiceLog.countByCustomerAndServiceDefinitionAndRequestTimeGreaterThan(customerService?.customer, customerService?.service, startDate)
            if (requestsCount >= rateLimit.limit)
                throw new ServiceException(108, [rateLimit.limit, rateLimit.period])
        }
    }
}
