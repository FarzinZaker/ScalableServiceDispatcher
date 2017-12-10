package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class ServiceParameterController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
        [serviceDefinition: ServiceDefinition.get(params.id)]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {
        def serviceDefinition = ServiceDefinition.get(params.serviceDefinition)
        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = ServiceParameter.findAllByServiceDefinitionAndDeleted(serviceDefinition, false, parameters)
        value.total = ServiceParameter.countByServiceDefinitionAndDeleted(serviceDefinition, false)

        value.data = list.collect {
            [
                    id                          : it.id,
                    name                        : it.name,
                    type                        : message(code: "serviceParameter.type.${it.type}"),
                    required                    : it.required,
                    displayName                 : it.displayName,
                    useForSignatureCheckWithCore: it.useForSignatureCheckWithCore,
                    useAsAmountToCheckWithCore  : it.useAsAmountToCheckWithCore,
                    displayForSignature         : it.displayForSignature,
                    systemValue                 : message(code: "${it.systemValue}"),
                    aggregateField              : it.aggregateField,
                    lastUpdated                 : format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        render(template: 'form', model: [item: new ServiceParameter(serviceDefinition: ServiceDefinition.get(params.serviceDefinition))])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        render(template: 'form', model: [item: ServiceParameter.get(params.id)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = ServiceParameter.get(params.id)
            item.properties = params
        } else {
            item = new ServiceParameter(params)
        }

        if (item.systemValue)
            item.required = false

        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = ServiceParameter.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }
}
