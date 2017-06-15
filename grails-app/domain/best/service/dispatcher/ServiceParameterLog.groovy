package best.service.dispatcher

class ServiceLogParameter {

    ServiceLog log
    ServiceParameter parameter
    String value

    static constraints = {
        value nullable: true
    }
}
