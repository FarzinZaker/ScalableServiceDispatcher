package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import fi.joensuu.joyds1.calendar.JalaliCalendar
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class AppCustomerController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
        [app: App.get(params.id)]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {
        def app = App.get(params.app)
        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = AppCustomer.findAllByAppAndDeleted(app, false, parameters)
        value.total = AppCustomer.countByAppAndDeleted(app, false)

        value.data = list.collect {
            [
                    id         : it.id,
                    customer    : it.customer?.name,
                    startDate  : it.startDate ? format.jalaliDate(date: it.startDate) : '',
                    endDate    : it.endDate ? format.jalaliDate(date: it.endDate) : '',
                    lastUpdated: format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        render(template: 'form', model: [item: new AppCustomer(app: App.get(params.app))])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        render(template: 'form', model: [item: AppCustomer.get(params.id)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = AppCustomer.get(params.id)
        } else {
            item = new AppCustomer()
        }
        item.app = App.get(params.app.id)
        item.customer = Customer.get(params.customer.id)
        item.startDate = parseDate(params.startDate)
        item.endDate = parseDate(params.endDate)
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = AppCustomer.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }


    private static Date parseDate(date) {
        if (!date)
            return null
        def parts = date.split("/").collect { it as Integer }
        JalaliCalendar jc = new JalaliCalendar(parts[0], parts[1], parts[2])
        jc.toJavaUtilGregorianCalendar().time
    }
}
