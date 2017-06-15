package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class ServiceParameterConditionController {

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
        [limit: ServiceParameterLimit.get(params.id)]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {
        def limit = ServiceParameterLimit.get(params.limit)
        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "lastUpdated", order: params["sort[0][dir]"] ?: "desc"]

        def list = ServiceParameterCondition.findAllByLimitAndTypeAndDeleted(limit, params.type as String, false, parameters)
        value.total = ServiceParameterCondition.countByLimitAndTypeAndDeleted(limit, params.type as String, false)

        value.data = list.collect {
            [
                    id         : it.id,
                    parameter  : it.parameter?.name,
                    count      : it.count,
                    unit       : message(code: "serviceParameterCondition.unit.${it.unit}"),
                    operator   : message(code: "serviceParameterCondition.operator.${it.operator}"),
                    value      : it.value,
                    lastUpdated: format.jalaliDate(date: it.lastUpdated, hm: true)
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        render(template: 'form', model: [item: new ServiceParameterCondition(limit: ServiceParameterLimit.get(params.limit), type: params.type)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        render(template: 'form', model: [item: ServiceParameterCondition.get(params.id)])
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {
        def item
        if (params.id) {
            item = ServiceParameterCondition.get(params.id)
        } else {
            item = new ServiceParameterCondition()
        }
        item.limit = ServiceParameterLimit.get(params.limit.id)
        item.parameter = ServiceParameter.get(params.parameter.id)
        item.type = params.type
        item.count = params.count
        item.unit = params.unit
        item.operator = params.operator
        item.value = params.value
        if (item.save(flush: true))
            render '1'
        else
            render 0
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def delete() {
        def item = ServiceParameterCondition.get(params.id)
        item.deleted = true
        render(item.save(flush: true) ? '1' : '0')
    }
}
