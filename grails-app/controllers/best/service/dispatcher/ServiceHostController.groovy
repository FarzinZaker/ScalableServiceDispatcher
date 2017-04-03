package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured


class ServiceHostController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {

        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = ServiceHost.findAllByDeleted(false, parameters)
        value.total = ServiceHost.countByDeleted(false)

        value.data = list.collect {
            [
                    id         : it.id,
                    name       : it.name,
                    protocol   : message(code: "serviceHost.protocol.${it.protocol}"),
                    address    : it.address,
                    port       : it.port,
                    lastUpdated: format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        render(template: 'form', model: [item: new ServiceHost()])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        render(template: 'form', model: [item: ServiceHost.get(params.id)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = ServiceHost.get(params.id)
            item.properties = params
        } else {
            item = new ServiceHost(params)
        }
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = ServiceHost.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }
}
