package best.service.dispatcher

import grails.converters.JSON

class TestController {

    def coreConnectionService

    def bill() {
//        println params
//        println ServiceLogParameter.findAllByParameterInList(ServiceParameter.findAllByName('accountNo')).collect{it.value}.unique()?.join('\n')

        println coreConnectionService.accountSignConditions('1002/10/810/707071770')
//        def parameter = ServiceParameter.findAllByServiceDefinition(ServiceDefinition.get(3469)).find{it.name == 'items'}
//        def logs = ServiceLogParameter.findAllByParameter(ServiceParameter.get(3504))
//        def service = ServiceDefinition.get(3469)
//        def drafts = ServiceDraft.findAllByServiceDefinition(service)
        render([time: new Date(), data: params] as JSON)
    }

    def testopb() {
        def key = 'jQs3LjL0Q76nVWj2zVJ+hIrxU4pyZyXLLDnwuPbNy7mla6+tnp+7/s6KJ9s1YBGXVm5gEln/5aIpqURbKV/Saxpfcb64WcDJJaw5Bfqg2BvPk2DUAaEugGjm4YvA0tKoQfNYybU9riQMigFEJVEXbQSL+2fQUTerkWROaEhnKcmDLswaTmzY4hXms5XkFxnnWXPgz/FwWxPC53OzsdSzjd+ekXFBuOqYh+XojgnI/jsFWeeS1KuZ3Qmfxd3PlNlBZTh6w89iJBXcU/kWjLAyv5V76pKsAm4S7Q4fEdq7JD68g/jAPeh9GEO10bgFfeZ/Z+QRYu/cN5bmy6WQ7mDtdp0c/+lT3Au9Hg=='
        String url = "http://mobiletest.middleeastbank.ir:12427/openBanking/accountSignConditions";
        def acctNo = '1002/10/810/707071770'
        def params = [accountNo: acctNo]

        final String USER_AGENT = "Mozilla/5.0";

        URL obj = new URL(url);
        def con = obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = params.sort { it.key }.collect { "${it.key}=${it.value}" }.join('&') + "&key=";
        urlParameters += "${params.sort { it.key } as JSON}${key}".encodeAsSHA256()
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        def res = con.getInputStream().readLines()
        con.getInputStream().close();

        //print result
        System.out.println(res.toString());
        render res
    }

    def testopbsgn() {
        def key = 'jQs3LjL0Q76nVWj2zVJ+hIrxU4pyZyXLLDnwuPbNy7mla6+tnp+7/s6KJ9s1YBGXVm5gEln/5aIpqURbKV/Saxpfcb64WcDJJaw5Bfqg2BvPk2DUAaEugGjm4YvA0tKoQfNYybU9riQMigFEJVEXbQSL+2fQUTerkWROaEhnKcmDLswaTmzY4hXms5XkFxnnWXPgz/FwWxPC53OzsdSzjd+ekXFBuOqYh+XojgnI/jsFWeeS1KuZ3Qmfxd3PlNlBZTh6w89iJBXcU/kWjLAyv5V76pKsAm4S7Q4fEdq7JD68g/jAPeh9GEO10bgFfeZ/Z+QRYu/cN5bmy6WQ7mDtdp0c/+lT3Au9Hg=='
//        String url = "http://mobiletest.middleeastbank.ir:12427/openBanking/checkSigns";
        String url = "http://mobiletest.middleeastbank.ir:12427/openBanking/checkSigns";

        def acctNo = '1002/10/810/707071770'
        def params = [accountNo: acctNo, amount: '10000', signs: '0000012427,0001234']

        final String USER_AGENT = "Mozilla/5.0";

        URL obj = new URL(url);
        def con = obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = params.sort { it.key }.collect { "${it.key}=${it.value}" }.join('&') + "&key=";
        def tohash = "${params.sort { it.key } as JSON}${key}"
        println tohash

        urlParameters += tohash.encodeAsSHA256()
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        def res = con.getInputStream().readLines()
        con.getInputStream().close();

        //print result
        System.out.println(res.toString());
        render res
    }

}
