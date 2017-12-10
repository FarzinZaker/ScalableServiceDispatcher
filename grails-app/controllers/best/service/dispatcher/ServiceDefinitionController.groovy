package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class ServiceDefinitionController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {

        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = ServiceDefinition.findAllByDeleted(false, parameters)
        value.total = ServiceDefinition.countByDeleted(false)

        value.data = list.collect {
            [
                    id         : it.id,
                    name       : it.name,
                    englishName: it.englishName,
                    isBulk     : it.isBulk,
                    lastUpdated: format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        render(template: 'form', model: [item: new ServiceDefinition()])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        render(template: 'form', model: [item: ServiceDefinition.get(params.id)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = ServiceDefinition.get(params.id)
            item.properties = params
        } else {
            item = new ServiceDefinition(params)
        }
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = ServiceDefinition.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }
}
