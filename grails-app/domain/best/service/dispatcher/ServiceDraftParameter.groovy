package best.service.dispatcher

class ServiceDraftParameter {

    ServiceDraft draft
    ServiceParameter parameter
    String value
    static mapping = {
        value sqlType: 'clob'
    }
    static constraints = {
        value nullable: true
    }
}
