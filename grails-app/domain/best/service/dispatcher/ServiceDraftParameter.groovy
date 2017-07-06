package best.service.dispatcher

class ServiceDraftParameter {

    ServiceDraft draft
    ServiceParameter parameter
    String value

    static constraints = {
        value nullable: true
    }
}
