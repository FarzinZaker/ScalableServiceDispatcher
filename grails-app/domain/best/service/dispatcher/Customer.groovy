package best.service.dispatcher

import org.apache.commons.lang.RandomStringUtils

class Customer {

    String name
    String englishName
    String clientNo
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated
    String key

    static constraints = {
        englishName unique: true
        key nullable: true
    }

    void generateKey() {
        if (!key) {
            int randomStringLength = 255
            String charset = (('a'..'z') + ('A'..'Z') + ('0'..'9') + ['!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '~']).join('')
            key = RandomStringUtils.random(randomStringLength, charset.toCharArray())
        }
    }

    def beforeInsert() {
        generateKey()
    }

    def beforeUpdate() {
        generateKey()
    }
}
