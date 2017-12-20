package best.service.dispatcher

import grails.converters.JSON
import grails.transaction.Transactional

@Transactional
class CoreConnectionService {

    private
    static key = 'jQs3LjL0Q76nVWj2zVJ+hIrxU4pyZyXLLDnwuPbNy7mla6+tnp+7/s6KJ9s1YBGXVm5gEln/5aIpqURbKV/Saxpfcb64WcDJJaw5Bfqg2BvPk2DUAaEugGjm4YvA0tKoQfNYybU9riQMigFEJVEXbQSL+2fQUTerkWROaEhnKcmDLswaTmzY4hXms5XkFxnnWXPgz/FwWxPC53OzsdSzjd+ekXFBuOqYh+XojgnI/jsFWeeS1KuZ3Qmfxd3PlNlBZTh6w89iJBXcU/kWjLAyv5V76pKsAm4S7Q4fEdq7JD68g/jAPeh9GEO10bgFfeZ/Z+QRYu/cN5bmy6WQ7mDtdp0c/+lT3Au9Hg=='
    private static baseUrl = 'http://172.20.34.113:8080' //live
//    private static baseUrl = 'http://mobiletest.middleeastbank.ir:12427' //test

    Boolean requiresCheckWithCore(ServiceDraft serviceDraft) {
        CustomerService.findByCustomerAndServiceAndDeletedAndStartDateLessThanEqualsAndEndDateGreaterThan(serviceDraft?.customer, serviceDraft?.serviceDefinition, false, new Date(), new Date())?.checkSignaturesWithCore ?: false
    }

    def getAccountSignConditions(ServiceDraft serviceDraft) {
        try {
            String url = "${baseUrl}/openBanking/accountSignConditions"

            def parameter = ServiceParameter.findByServiceDefinitionAndUseForSignatureCheckWithCoreAndDeleted(serviceDraft?.serviceDefinition, true, false)
            def accountNumber = ServiceDraftParameter.findByDraftAndParameter(serviceDraft, parameter)?.value
            def params = [accountNo: accountNumber]

            final String USER_AGENT = "Mozilla/5.0"

            URL obj = new URL(url)
            def con = obj.openConnection()

            //add reuqest header
            con.setRequestMethod("POST")
            con.setRequestProperty("User-Agent", USER_AGENT)
            con.setRequestProperty("Accept-Language", "en-US,enq=0.5")

            String urlParameters = params.sort { it.key }.collect { "${it.key}=${it.value}" }.join('&') + "&key="
            urlParameters += "${params.sort { it.key } as JSON}${key}".encodeAsSHA256()
            // Send post request
            con.setDoOutput(true)
            DataOutputStream wr = new DataOutputStream(con.getOutputStream())
            wr.writeBytes(urlParameters)
            wr.flush()
            wr.close()

            int responseCode = con.getResponseCode()
            System.out.println("\nSending 'POST' request to URL : " + url)
            System.out.println("Post parameters : " + urlParameters)
            System.out.println("Response Code : " + responseCode)

            def res = con.getInputStream().readLines()
            con.getInputStream().close()

            //print result
            JSON.parse(res.toString())
        } catch (ignored) {
            []
        }
    }

    Boolean checkSignatures(ServiceDraft serviceDraft) {
        try {
            if (!requiresCheckWithCore(serviceDraft))
                return true

            String url = "${baseUrl}/openBanking/checkSigns"

            def accountNumberParameter = ServiceParameter.findByServiceDefinitionAndUseForSignatureCheckWithCoreAndDeleted(serviceDraft?.serviceDefinition, true, false)
            def accountNumber = ServiceDraftParameter.findByDraftAndParameter(serviceDraft, accountNumberParameter)?.value
            def amountParameter = ServiceParameter.findByServiceDefinitionAndUseAsAmountToCheckWithCoreAndDeleted(serviceDraft?.serviceDefinition, true, false)
            def amount = ServiceDraftParameter.findByDraftAndParameter(serviceDraft, amountParameter)?.value
            def signatures = CustomerUser.findAllByUserInListAndDeleted(ServiceDraftSignature.findAllByDraftAndDecision(serviceDraft, 'accept')?.collect { it.user } ?: [], false)?.collect {
                it?.user?.clientNo ?: it.customer?.clientNo
            }
            def params = [accountNo: accountNumber, amount: amount, signs: signatures?.join(',')]

            final String USER_AGENT = "Mozilla/5.0"

            URL obj = new URL(url)
            def con = obj.openConnection()

            //add reuqest header
            con.setRequestMethod("POST")
            con.setRequestProperty("User-Agent", USER_AGENT)
            con.setRequestProperty("Accept-Language", "en-US,enq=0.5")

            String urlParameters = params.sort { it.key }.collect { "${it.key}=${it.value}" }.join('&') + "&key="
            def tohash = "${params.sort { it.key } as JSON}${key}"
            println tohash

            urlParameters += tohash.encodeAsSHA256()
            // Send post request
            con.setDoOutput(true)
            DataOutputStream wr = new DataOutputStream(con.getOutputStream())
            wr.writeBytes(urlParameters)
            wr.flush()
            wr.close()

            int responseCode = con.getResponseCode()
            System.out.println("\nSending 'POST' request to URL : " + url)
            System.out.println("Post parameters : " + urlParameters)
            System.out.println("Response Code : " + responseCode)

            def res = con.getInputStream().readLines()
            con.getInputStream().close()

            //print result
            !JSON.parse(res.toString())?.any{!it.valid}
        } catch (ignored) {
            false
        }
    }
}
