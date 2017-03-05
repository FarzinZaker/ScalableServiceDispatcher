<%@ page import="best.service.dispatcher.ServiceDefinition" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:field fieldName="serviceDefinition.name">
        <form:textBox name="name" entity="${item}" validation="required" style="width:400px;"/>
    </form:field>
    <form:field fieldName="serviceDefinition.englishName">
        <form:textBox name="englishName" entity="${item}" validation="required" style="width:400px;"/>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>