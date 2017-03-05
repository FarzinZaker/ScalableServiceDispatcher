package best.service.dispatcher

class Customer {

    String name
    String englishName
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        englishName unique: true
    }
}
