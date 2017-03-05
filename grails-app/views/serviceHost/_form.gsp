<%@ page import="best.service.dispatcher.ServiceHost" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:field fieldName="serviceHost.name">
        <form:textBox name="name" entity="${item}" validation="required" style="width:400px;"/>
    </form:field>
    <form:field fieldName="serviceHost.protocol">
        <form:select name="protocol" entity="${item}" validation="required" style="width:400px;" preSelect="0"
                     items="${ServiceHost.HOST_PROTOCOLS.collect {
                         [text: message(code: "serviceHost.protocol.${it}"), value: it]
                     }}"/>
    </form:field>
    <form:field fieldName="serviceHost.address">
        <form:textBox name="address" entity="${item}" validation="required" style="width:400px;"/>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>