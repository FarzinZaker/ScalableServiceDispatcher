package best.service.dispatcher

import grails.converters.JSON
import grails.transaction.Transactional
import org.grails.web.json.JSONObject

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

@Transactional
class ParameterService {

    Map<String, Object> extractParameters(ServiceDefinition serviceDefinition, String parametersString, Customer customer) {
        HashMap map = JSON.parse(parametersString) as HashMap
        def result = [:]
        ServiceParameter.findAllByServiceDefinitionAndSystemValueIsNullAndDeleted(serviceDefinition, false).each { serviceParameter ->
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
                if (value && value?.toString()?.trim() != '')
                    result.put(serviceParameter.name, value)
            }
        }

        def shell = new GroovyShell()
        shell.setVariable('customer', customer)
        ServiceParameter.findAllByServiceDefinitionAndSystemValueIsNotNullAndDeleted(serviceDefinition, false).each { serviceParameter ->
            result.put(serviceParameter.name, shell.evaluate(serviceParameter.systemValue))

        }

        result.put('time', "${new Date().time}")

        result.put('key', generateKey(result))

        result
    }

    def extractParameter(String value, ServiceParameter parameter) {
        switch (parameter.type) {
            case 'String':
                return value
            case 'JSON':
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

    static String generateKey(Map parametersMap) {
        parametersMap?.keySet()?.each {
            parametersMap[it] = parametersMap[it]?.toString()
        }
        def parameters = (parametersMap?.sort { it.key } as JSON)?.toString()
//        println parameters
        def key = "jQs3LjL0Q76nVWj2zVJ+hIrxU4pyZyXLLDnwuPbNy7mla6+tnp+7/s6KJ9s1YBGXVm5gEln/5aIpqURbKV/Saxpfcb64WcDJJaw5Bfqg2BvPk2DUAaEugGjm4YvA0tKoQfNYybU9riQMigFEJVEXbQSL+2fQUTerkWROaEhnKcmDLswaTmzY4hXms5XkFxnnWXPgz/FwWxPC53OzsdSzjd+ekXFBuOqYh+XojgnI/jsFWeeS1KuZ3Qmfxd3PlNlBZTh6w89iJBXcU/kWjLAyv5V76pKsAm4S7Q4fEdq7JD68g/jAPeh9GEO10bgFfeZ/Z+QRYu/cN5bmy6WQ7mDtdp0c/+lT3Au9Hg=="
        def digest = MessageDigest.getInstance("SHA-256")
        String hash = digest.digest("${parameters ?: ''}${key}".getBytes(StandardCharsets.UTF_8))?.encodeAsHex()
//        println hash
        hash
    }
}
