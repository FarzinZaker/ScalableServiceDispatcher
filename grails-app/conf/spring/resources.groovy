

import best.service.dispatcher.CustomUserDetailsService
import best.service.dispatcher.security.LoginEvent
import conf.AkkaSpringConfiguration
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler

// Place your Spring DSL code here
beans = {
    importBeans(AkkaSpringConfiguration)

    userDetailsService(CustomUserDetailsService) {
        grailsApplication = ref('grailsApplication')
    }

    securityContextLogoutHandler(SecurityContextLogoutHandler) {
        invalidateHttpSession = false
    }

    authenticationSuccessHandler(LoginEvent) {

        def conf = SpringSecurityUtils.securityConfig
        requestCache = ref('requestCache')
        defaultTargetUrl = conf.successHandler.defaultTargetUrl
        alwaysUseDefaultTargetUrl = conf.successHandler.alwaysUseDefault
        targetUrlParameter = conf.successHandler.targetUrlParameter
        useReferer = conf.successHandler.useReferer
        redirectStrategy = ref('redirectStrategy')
    }
}
