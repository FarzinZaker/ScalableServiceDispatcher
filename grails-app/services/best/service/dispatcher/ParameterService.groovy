package best.service.dispatcher

import grails.converters.JSON
import grails.transaction.Transactional
import org.grails.web.json.JSONObject

@Transactional
class ParameterService {

    Map<String, Object> extractParameters(ServiceDefinition serviceDefinition, String parametersString) {
        HashMap map = JSON.parse(parametersString) as HashMap
        def result = [:]
        ServiceParameter.findAllByServiceDefinitionAndDeleted(serviceDefinition, false).each { serviceParameter ->
            if (serviceParameter.required && !map.containsKey(serviceParameter.name))
                throw new ServiceException(106, [serviceParameter?.name])
            else {
                def value = null
                try {
                    value = extractParameter(map[serviceParameter?.name]?.toString(), serviceParameter)
                }
                catch (ignored) {
                    throw new ServiceException(107, [serviceParameter?.name, serviceParameter?.type])
                }
                result.put(serviceParameter.name, value)
            }
        }
        result
    }

    def extractParameter(String value, ServiceParameter parameter) {
        switch (parameter.type) {
            case 'String':
                return value
            case 'Integer':
                return value?.toInteger()
            case 'Long':
                return value?.toLong()
            case 'Double':
                return value?.toDouble()
            case 'Float':
                return value?.toFloat()
            case 'Date':
                return new Date(value)
            case 'Boolean':
                return value?.toBoolean()
            default:
                return value
        }
    }
}
