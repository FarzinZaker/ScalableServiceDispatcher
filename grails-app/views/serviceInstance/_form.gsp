<%@ page import="best.service.dispatcher.ServiceHost; best.service.dispatcher.ServiceInstance; best.service.dispatcher.ServiceDefinition" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:hidden name="serviceDefinition.id" id="serviceDefinitionId" entity="${item}"/>
    <form:field fieldName="serviceInstance.host">
        <form:select name="host.id" id="hostId" entity="${item}" validation="required" style="width:400px;"
                     preSelect="0"
                     items="${ServiceHost?.findAllByDeleted(false)?.collect { [text: it.name, value: it.id] }}"/>
    </form:field>
    <form:field fieldName="serviceInstance.path">
        <form:textBox name="path" entity="${item}" validation="required" style="width:400px;"/>
    </form:field>
    <form:field fieldName="serviceInstance.type">
        <form:select name="type" entity="${item}" validation="required" style="width:400px;" preSelect="0"
                     items="${ServiceInstance.SERVICE_TYPES.collect {
                         [text: message(code: "serviceInstance.type.${it}"), value: it]
                     }}"/>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>