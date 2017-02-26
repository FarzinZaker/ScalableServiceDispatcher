package best.service.dispatcher

class ServiceParameter {

    String name
    String type
    Boolean required

    static constraints = {
        name()
        type(inList: ['String', 'Integer', 'Long', 'Double', 'Float', 'Date', 'Boolean'])
        required()
    }
}
