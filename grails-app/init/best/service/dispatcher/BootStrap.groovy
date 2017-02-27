package best.service.dispatcher

import best.service.dispatcher.security.CustomRedirectStrategy
import best.service.dispatcher.security.RoleHelper

class BootStrap {

    def authenticationFailureHandler

    def init = { servletContext ->

        authenticationFailureHandler.redirectStrategy = new CustomRedirectStrategy()

        RoleHelper.ROLES.each {
            def authority = it
            Role.findByAuthority(authority) ?: new Role(authority: authority).save(failOnError: true)
        }
        def adminRole = Role.findByAuthority(RoleHelper.ROLE_ADMIN)

        def adminUser = User.findByUsername('admin@local') ?: new User(
                username: 'admin@local',
                password: 'admin',
                firstName: 'مدیر',
                lastName: 'سیستم',
                email: 'admin@local',
                mobile: '09122110811',
                sex: 'male',
                nationalCode: '2803348446',
                enabled: true).save(failOnError: true)

        adminUser.password = 'admin'
        adminUser.save()

        if (!adminUser.authorities.contains(adminRole)) {
            UserRole.create adminUser, adminRole
        }
    }
    def destroy = {
    }
}
