package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class CustomerController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {

        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = Customer.findAllByDeleted(false, parameters)
        value.total = Customer.countByDeleted(false)

        value.data = list.collect {
            [
                    id         : it.id,
                    name       : it.name,
                    englishName: it.englishName,
                    lastUpdated: format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        render(template: 'form', model: [item: new Customer()])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        render(template: 'form', model: [item: Customer.get(params.id)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = Customer.get(params.id)
            item.properties = params
        } else {
            item = new Customer(params)
        }
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = Customer.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }
}
