package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.plugin.springsecurity.annotation.Secured

class ProfileController {

    def springSecurityService

    @Secured([RoleHelper.ROLE_ADMIN, RoleHelper.ROLE_USER])
    def index() {
        [user: springSecurityService.currentUser as User]
    }

    @Secured([RoleHelper.ROLE_ADMIN, RoleHelper.ROLE_USER])
    def edit() {
        [user: springSecurityService.currentUser as User]
    }

    @Secured([RoleHelper.ROLE_ADMIN, RoleHelper.ROLE_USER])
    def saveProfile() {
        def user = springSecurityService.currentUser as User
        user.firstName = params.firstName
        user.lastName = params.lastName
        user.sex = params.sex
        user.mobile = params.mobile
        user.image = params['image.id'] && params['image.id'] != '' ? Image.get(params['image.id'] as Long) : null
        if (user.save(flush: true)) {
            flash.message = message(code: 'user.profile.savedSuccessfully')
        }
        redirect(action: 'edit')
    }
}
