package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class ServiceInstanceController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
        [serviceDefinition: ServiceDefinition.get(params.id)]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {
        def serviceDefinition = ServiceDefinition.get(params.serviceDefinition)
        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = ServiceInstance.findAllByServiceDefinitionAndDeleted(serviceDefinition, false, parameters)
        value.total = ServiceInstance.countByServiceDefinitionAndDeleted(serviceDefinition, false)

        value.data = list.collect {
            [
                    id         : it.id,
                    host       : it.host?.name,
                    path       : it.path,
                    type       : message(code: "serviceInstance.type.${it.type}"),
                    lastUpdated: format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        render(template: 'form', model: [item: new ServiceInstance(serviceDefinition: ServiceDefinition.get(params.serviceDefinition))])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        render(template: 'form', model: [item: ServiceInstance.get(params.id)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = ServiceInstance.get(params.id)
            item.properties = params
        } else {
            item = new ServiceInstance(params)
        }
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = ServiceInstance.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }
}
