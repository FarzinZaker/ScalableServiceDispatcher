<%@ page import="best.service.dispatcher.ServiceDefinition" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:field fieldName="serviceDefinition.name">
        <form:textBox name="name" entity="${item}" validation="required" style="width:400px;"/>
    </form:field>
    <form:field fieldName="serviceDefinition.englishName">
        <form:textBox name="englishName" entity="${item}" validation="required" style="width:400px;"/>
    </form:field>
    <form:field fieldName="serviceDefinition.isBulk" showLabel="0">
        <div style="width: 400px;padding-top:20px;">
            <form:checkbox name="isBulk" checked="${item?.isBulk}"
                           text="${message(code: 'serviceDefinition.isBulk.label')}"/>
        </div>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>