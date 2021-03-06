package best.service.dispatcher

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes = 'username')
@ToString(includes = 'username', includeNames = true, includePackage = false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    transient springSecurityService

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    String firstName
    String lastName
    String sex
    String email
    String mobile
    Image image

    String clientNo

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this)*.role
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }

    static transients = ['springSecurityService']

    static constraints = {
        password blank: false, password: true
        username blank: false, unique: true
        firstName()
        lastName()
        sex inList: ['male', 'female']
        email()
        mobile nullable: true
        image nullable: true

        clientNo nullable: true
    }

    static mapping = {
        table 'user_accounts'
        password column: '`password`'
    }

    @Override
    String toString() {
        "$firstName $lastName"
    }
}
