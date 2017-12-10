package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import fi.joensuu.joyds1.calendar.JalaliCalendar
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class CustomerServiceController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
        [customer: Customer.get(params.id)]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {
        def customer = Customer.get(params.customer)
        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = CustomerService.findAllByCustomerAndDeleted(customer, false, parameters)
        value.total = CustomerService.countByCustomerAndDeleted(customer, false)

        value.data = list.collect {
            [
                    id         : it.id,
                    service    : it.service?.name,
                    startDate  : it.startDate ? format.jalaliDate(date: it.startDate) : '',
                    checkSignaturesWithCore  : it.checkSignaturesWithCore,
                    endDate    : it.endDate ? format.jalaliDate(date: it.endDate) : '',
                    lastUpdated: format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        render(template: 'form', model: [item: new CustomerService(customer: Customer.get(params.customer))])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        render(template: 'form', model: [item: CustomerService.get(params.id)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = CustomerService.get(params.id)
        } else {
            item = new CustomerService()
        }
        item.customer = Customer.get(params.customer.id)
        item.service = ServiceDefinition.get(params.service.id)
        item.checkSignaturesWithCore = params.checkSignaturesWithCore ? true : false
        item.startDate = parseDate(params.startDate)
        item.endDate = parseDate(params.endDate)
        item.minimumSignatures = params.minimumSignatures as Integer
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = CustomerService.get(params.id)
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
