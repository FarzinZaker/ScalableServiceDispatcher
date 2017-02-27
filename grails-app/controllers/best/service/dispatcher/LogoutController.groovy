package best.service.dispatcher

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

class LogoutController {

    // Bean where Spring Security store logout handlers
    def logoutHandlers

    def index() {
        // Logout programmatically
        Authentication auth = SecurityContextHolder.context.authentication
        if (auth) {
            logoutHandlers.each { handler ->
                handler.logout(request, response, auth)
            }
        }
        redirect uri: '/'
    }
}
