package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class ServiceRateLimitController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
        [customerService: CustomerService.get(params.id)]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {
        def customerService = CustomerService.get(params.customerService)
        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = ServiceRateLimit.findAllByCustomerServiceAndDeleted(customerService, false, parameters)
        value.total = ServiceRateLimit.countByCustomerServiceAndDeleted(customerService, false)

        value.data = list.collect {
            [
                    id         : it.id,
                    period     : message(code: "serviceRateLimit.period.${it.period}"),
                    limit      : it.limit,
                    lastUpdated: format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        render(template: 'form', model: [item: new ServiceRateLimit(customerService: CustomerService.get(params.customerService))])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        render(template: 'form', model: [item: ServiceRateLimit.get(params.id)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = ServiceRateLimit.get(params.id)
        } else {
            item = new ServiceRateLimit()
        }
        item.customerService = CustomerService.get(params.customerService.id)
        item.period = params.period
        item.limit = params.limit as Integer
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = ServiceRateLimit.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }
}
