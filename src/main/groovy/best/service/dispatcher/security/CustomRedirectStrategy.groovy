package best.service.dispatcher.security

/**
 * Created by root on 2/27/17.
 */

import org.grails.web.util.WebUtils
import org.springframework.security.web.DefaultRedirectStrategy
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by Farzin on 4/14/2016.
 */
class CustomRedirectStrategy extends DefaultRedirectStrategy {

    @Override
    void sendRedirect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) throws IOException {

        def session = WebUtils.retrieveGrailsWebRequest().getCurrentRequest().session
        def loginErrorsCount = session["loginErrorsCount"] ?: 0
        session["loginErrorsCount"] = loginErrorsCount + 1

        super.sendRedirect(httpServletRequest, httpServletResponse, s)
    }
}