package best.service.dispatcher

import grails.util.Holders
import org.springframework.context.MessageSource

/**
 * Created by root on 4/3/17.
 */
class ServiceException extends Exception {

    private ServiceParameterLimit parameterLimit
    private Integer errorCode
    private List args

    ServiceException(Integer errorCode, List args) {
        this.errorCode = errorCode
        this.args = args
    }

    ServiceException(Integer errorCode) {
        this(errorCode, [])
    }

    ServiceException(ServiceParameterLimit parameterLimit) {
        this.parameterLimit = parameterLimit
        errorCode = 300
    }

    Integer getErrorCode() {
        errorCode
    }

    @Override
    String getMessage() {
        if (parameterLimit)
            parameterLimit.name
        else {
            MessageSource messageSource = Holders.applicationContext.getBean("messageSource")
            messageSource.getMessage("service.exception.${errorCode}", args?.toArray(), Locale.default)
        }
    }
}
