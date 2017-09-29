package best.service.dispatcher

import best.service.dispatcher.security.RoleHelper
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

class UserController {

    def springSecurityService

    @Secured([RoleHelper.ROLE_ADMIN, RoleHelper.ROLE_USER])
    def changePassword() {

        def user = User.findByUsername((springSecurityService.currentUser as User).username)
        [askForOldPassword: true]
    }

    @Secured([RoleHelper.ROLE_ADMIN, RoleHelper.ROLE_USER])
    def saveNewPassword() {
        def user = User.findByUsername((springSecurityService.currentUser as User).username)
        if(springSecurityService.passwordEncoder.isPasswordValid(user.password, params.oldPassword, null)){
            if (params.newPassword.trim() != '') {
                if (params.newPassword == params.newPassword_confirmation) {
                    user.password = params.newPassword
                    if (user.validate() && user.save()) {
                        flash.message = message(code: "password.change.success")
                        redirect(action: 'passwordChanged')
                    } else {
                        flash.validationError = message(code: "password.change.fail")
                        redirect(action: 'changePassword')
                    }
                } else {
                    flash.validationError = message(code: "password.change.notMatch")
                    redirect(action: 'changePassword')
                }
            } else {
                flash.validationError = message(code: "password.change.emptyPassword")
                redirect(action: 'changePassword')
            }
        } else {
            flash.validationError = message(code: "password.change.invalidPassword")
            redirect(action: 'changePassword')
        }
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def list() {
        [role: params.id]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def jsonList() {

        def value = [:]
        def parameters = [offset: params.skip, max: params.pageSize, sort: params["sort[0][field]"] ?: "username", order: params["sort[0][dir]"] ?: "asc"]

        def list
        def role = Role.findByAuthority(params.id)
        def idList = UserRole.findAllByRole(role)?.collect { it.userId }
        if (params.search && params.search != '') {
            def searchResult = User.search(params.search?.toString(), max: 1000000).results.collect { it.id }
            list = searchResult?.size() > 0 ? User.findAllByIdInListAndIdInList(searchResult, idList, parameters) : []
            value.total = searchResult?.size() > 0 ? User.countByIdInListAndIdInList(searchResult, idList) : 0
        } else {
            list = User.findAllByIdInList(idList, parameters)
            value.total = User.countByIdInList(idList)
        }

        value.data = list.collect {
            [
                    id       : it.id,
                    firstName: it.firstName,
                    lastName : it.lastName,
                    username : it.username?.replace('@', ' @ '),
                    sex      : message(code: "user.sex.${it.sex}"),
                    mobile   : it.mobile,
                    enabled  : it.enabled,
                    roles    : it.authorities.collect { message(code: "user.role.${it.authority}") }.join(',')
            ]
        }

        render value as JSON
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def create() {
        [
                user          : new User(),
                roles         : [],
                availableRoles: RoleHelper.ROLES
        ]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def edit() {
        def user = User.get(params.id)
        [
                user          : user,
                roles         : UserRole.findAllByUser(user).collect {
                    it.role.authority
                },
                availableRoles: RoleHelper.ROLES
        ]
    }

    @Secured([RoleHelper.ROLE_ADMIN])
    def save() {

        def user
        if (params.id) {
            user = User.get(params.id)
        } else {

            if (User.findByEmail(params.email)) {
                flash.validationError = message(code: 'user.save.error.repetitiveEmail')
                redirect(action: 'build', params: params)
                return
            }

            user = new User()
        }

        user.firstName = params.firstName
        user.lastName = params.lastName
        user.sex = params.sex
        user.mobile = params.mobile
        user.email = params.email

        if (!params.id || (params.password && params.password?.trim() != ''))
            user.password = params.password

        user.enabled = params.enabled ? true : false
        user.accountExpired = params.accountExpired ? true : false
        user.accountLocked = params.accountLocked ? true : false
        user.passwordExpired = params.passwordExpired ? true : false

        user.username = user.email

        if (user.validate() && user.save(flush: true)) {

            UserRole.findAllByUser(user).each { it.delete(flush: true) }
            RoleHelper.ROLES.each { role ->
                if (params."roles_${role}")
                    UserRole.create(user, Role.findByAuthority(role))
            }

            redirect(action: 'list', id: params.role)
        } else {
            flash.validationError = user.errors.toString()
            redirect(action: 'build', params: params)
        }
    }
}

