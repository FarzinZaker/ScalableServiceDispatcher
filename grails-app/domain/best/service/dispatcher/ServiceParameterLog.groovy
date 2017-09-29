package best.service.dispatcher

class ServiceLogParameter {

    ServiceLog log
    ServiceParameter parameter
    String value

    static mapping = {
        value sqlType: 'clob'
    }

    static constraints = {
        value nullable: true
    }
}
