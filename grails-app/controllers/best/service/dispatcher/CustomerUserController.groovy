package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import fi.joensuu.joyds1.calendar.JalaliCalendar
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class CustomerUserController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
        [customer: Customer.get(params.id)]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {
        def customer = Customer.get(params.customer)
        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = CustomerUser.findAllByCustomerAndDeleted(customer, false, parameters)
        value.total = CustomerUser.countByCustomerAndDeleted(customer, false)

        value.data = list.collect {
            [
                    id         : it.id,
                    user    : it.user?.toString(),
                    lastUpdated: format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        render(template: 'form', model: [item: new CustomerUser(customer: Customer.get(params.customer))])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        render(template: 'form', model: [item: CustomerUser.get(params.id)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = CustomerUser.get(params.id)
        } else {
            item = new CustomerUser()
        }
        item.customer = Customer.get(params.customer.id)
        item.user = User.get(params.user.id)
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = CustomerUser.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }
}
