package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class ServiceParameterLimitController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
        [customerService: CustomerService.get(params.id)]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {
        def customerService = CustomerService.get(params.customerService)
        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = ServiceParameterLimit.findAllByCustomerServiceAndDeleted(customerService, false, parameters)
        value.total = ServiceParameterLimit.countByCustomerServiceAndDeleted(customerService, false)

        value.data = list.collect {
            [
                    id         : it.id,
                    name       : it.name,
                    lastUpdated: format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        render(template: 'form', model: [item: new ServiceParameterLimit(customerService: CustomerService.get(params.customerService))])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        render(template: 'form', model: [item: ServiceParameterLimit.get(params.id)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = ServiceParameterLimit.get(params.id)
        } else {
            item = new ServiceParameterLimit()
        }
        item.customerService = CustomerService.get(params.customerService.id)
        item.name = params.name
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = ServiceParameterLimit.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }
}
