package best.service.dispatcher

class ServiceDefinition {

    String name
    String type

    static hasMany = [locations: ServiceLocation, parameters: ServiceParameter]

    static constraints = {
        name()
        type(inList: ['rest'])
    }
}
