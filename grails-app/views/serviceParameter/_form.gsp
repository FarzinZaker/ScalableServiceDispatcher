<%@ page import="best.service.dispatcher.ServiceHost; best.service.dispatcher.ServiceParameter; best.service.dispatcher.ServiceDefinition" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:hidden name="serviceDefinition.id" id="serviceDefinitionId" entity="${item}"/>
    <form:field fieldName="serviceParameter.name">
        <form:textBox name="name" entity="${item}" validation="required" style="width:400px;"/>
    </form:field>
    <form:field fieldName="serviceParameter.type">
        <form:select name="type" entity="${item}" validation="required" style="width:400px;" preSelect="0"
                     items="${ServiceParameter.PARAMETER_TYPES.collect {
                         [text: message(code: "serviceParameter.type.${it}"), value: it]
                     }}"/>
    </form:field>
    <form:field fieldName="serviceParameter.specifications">
        <div style="width: 400px;padding-top:10px;">
            <form:checkbox name="required" checked="${item?.required}"
                           text="${message(code: 'serviceParameter.required.label')}"/>
        </div>
    </form:field>
    <form:field fieldName="serviceParameter.systemValue">
        <form:select name="systemValue" id="systemValue" entity="${item}" style="width:400px;"
                     preSelect="" allowUserInput="true"
                     items="${[null, 'customer.clientNo'].collect { [text: message(code: "${it}"), value: it] }}"/>
    </form:field>
    <form:field fieldName="serviceParameter.displayForSignature" showLabel="0">
        <div style="width: 400px;padding-top:20px;">
            <form:checkbox name="displayForSignature" checked="${item?.displayForSignature}"
                           text="${message(code: 'serviceParameter.displayForSignature.label')}"/>
        </div>
    </form:field>
    <form:field fieldName="serviceParameter.displayName">
        <form:textBox name="displayName" entity="${item}" style="width:400px;"/>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>