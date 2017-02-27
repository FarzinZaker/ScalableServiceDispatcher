// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'best.service.dispatcher.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'best.service.dispatcher.UserRole'
grails.plugin.springsecurity.authority.className = 'best.service.dispatcher.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        [pattern: '/**', access: ['permitAll']],
//        [pattern: '/', access: ['permitAll']],
//        [pattern: '/error', access: ['permitAll']],
//        [pattern: '/index', access: ['permitAll']],
//        [pattern: '/index.gsp', access: ['permitAll']],
//        [pattern: '/shutdown', access: ['permitAll']],
//        [pattern: '/assets/**', access: ['permitAll']],
//        [pattern: '/**/js/**', access: ['permitAll']],
//        [pattern: '/**/css/**', access: ['permitAll']],
//        [pattern: '/**/images/**', access: ['permitAll']],
//        [pattern: '/**/fonts/**', access: ['permitAll']],
//        [pattern: '/**/favicon.ico', access: ['permitAll']],
//        [pattern: '/login/**', access: ['permitAll']],
//        [pattern: '/**/**', access: ['permitAll']],
//        [pattern: '/logout/**', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
//        [pattern: '/**', filters: 'none'],
        [pattern: '/assets/**', filters: 'none'],
        [pattern: '/**/js/**', filters: 'none'],
        [pattern: '/**/css/**', filters: 'none'],
        [pattern: '/**/images/**', filters: 'none'],
        [pattern: '/**/fonts/**', filters: 'none'],
        [pattern: '/**/favicon.ico', filters: 'none'],
        [pattern: '/**', filters: 'JOINED_FILTERS']
]

