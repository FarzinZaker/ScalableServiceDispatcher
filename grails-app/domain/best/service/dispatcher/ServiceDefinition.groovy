package best.service.dispatcher

class ServiceDefinition {

    String name
    String englishName
    Boolean deleted = false
    Date dateCreated
    Date lastUpdated

    static constraints = {
        englishName unique: true
    }
}
