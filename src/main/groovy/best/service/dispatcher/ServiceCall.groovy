package best.service.dispatcher

import groovy.transform.Immutable

/**
 * Created by root on 4/22/17.
 */

@Immutable
public class ServiceCall {
    Long customerId
    Long serviceInstanceId
    Map<String, Object> parameters
}
