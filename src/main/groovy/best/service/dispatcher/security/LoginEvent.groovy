package best.service.dispatcher.security

/**
 * Created by root on 9/7/14.
 */

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
public class LoginEvent extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        request.session.removeAttribute('loginErrorsCount')
        super.onAuthenticationSuccess(request, response, authentication)
    }
}
