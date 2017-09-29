package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class AppCustomerUserController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
        [appCustomer: AppCustomer.get(params.id)]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {
        def appCustomer = AppCustomer.get(params.appCustomer)
        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = AppCustomerUser.findAllByAppCustomerAndDeleted(appCustomer, false, parameters)
        value.total = AppCustomerUser.countByAppCustomerAndDeleted(appCustomer, false)

        value.data = list.collect {
            [
                    id          : it.id,
                    customerUser: it.customerUser?.user?.toString(),
                    lastUpdated : format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        def appCustomer = AppCustomer.get(params.appCustomer)
        def users = CustomerUser.findAllByCustomerAndDeleted(appCustomer?.customer, false)
        render(template: 'form', model: [item: new AppCustomerUser(appCustomer: appCustomer), users: users, appCustomer: appCustomer])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        def appCustomerUser = AppCustomerUser.get(params.id)
        def users = CustomerUser.findAllByCustomerAndDeleted(appCustomerUser?.appCustomer?.customer, false)
        render(template: 'form', model: [item: appCustomerUser, users: users, appCustomer: appCustomerUser?.appCustomer])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = AppCustomerUser.get(params.id)
        } else {
            item = new AppCustomerUser()
        }
        item.appCustomer = AppCustomer.get(params.appCustomer.id)
        item.customerUser = CustomerUser.get(params.customerUser.id)
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = AppCustomerUser.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }
}
