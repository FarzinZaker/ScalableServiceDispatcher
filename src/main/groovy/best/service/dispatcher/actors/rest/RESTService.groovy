package best.service.dispatcher.actors.rest

import best.service.dispatcher.ServiceException
import best.service.dispatcher.ServiceInstance
import grails.converters.JSON
import groovyx.net.http.RESTClient
import org.grails.web.json.JSONElement

import javax.inject.Named
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

import static groovyx.net.http.ContentType.URLENC

/**
 * Created by root on 2/24/17.
 */
@Named("RESTService")
class RESTService {

    static execute(Long customerId, Long serviceInstanceId, Map<String, Object> parameters) {

        ServiceInstance.withTransaction {
            def serviceInstance = ServiceInstance.get(serviceInstanceId)
            def client = new RESTClient("${serviceInstance?.host?.protocol}://${serviceInstance?.host?.address}:${serviceInstance?.host?.port}")
            def postBody = parameters
            client.ignoreSSLIssues()
            def response = client.post(path: (serviceInstance?.path?.startsWith('/') ? '' : '/') + serviceInstance?.path, body: postBody, requestContentType: URLENC)
            return (response.data as JSON)
        }
    }
}

