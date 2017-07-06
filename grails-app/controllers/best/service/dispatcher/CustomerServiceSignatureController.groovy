package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class CustomerServiceSignatureController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
        [customerService: CustomerService.get(params.id)]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {
        def customerService = CustomerService.get(params.customerService)
        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = CustomerServiceSignature.findAllByCustomerServiceAndDeleted(customerService, false, parameters)
        value.total = CustomerServiceSignature.countByCustomerServiceAndDeleted(customerService, false)

        value.data = list.collect {
            [
                    id          : it.id,
                    customerUser: it.customerUser?.user?.toString(),
                    required    : it.required,
                    lastUpdated : format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        def customerService = CustomerService.get(params.customerService)
        def users = CustomerUser.findAllByCustomerAndDeleted(customerService?.customer, false)
        render(template: 'form', model: [item: new CustomerServiceSignature(customerService: customerService), users: users, customerService: customerService])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        def customerServiceSignature = CustomerServiceSignature.get(params.id)
        def users = CustomerUser.findAllByCustomerAndDeleted(customerServiceSignature?.customerService?.customer, false)
        render(template: 'form', model: [item: customerServiceSignature, users: users, customerService: customerServiceSignature?.customerService])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = CustomerServiceSignature.get(params.id)
        } else {
            item = new CustomerServiceSignature()
        }
        item.customerService = CustomerService.get(params.customerService.id)
        item.customerUser = CustomerUser.get(params.customerUser.id)
        item.required = params.required ? true : false
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = CustomerServiceSignature.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }
}
