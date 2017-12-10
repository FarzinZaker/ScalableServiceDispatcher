<%@ page import="best.service.dispatcher.ServiceParameterCondition; best.service.dispatcher.ServiceParameter; best.service.dispatcher.ServiceRateLimit; best.service.dispatcher.ServiceDefinition" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:hidden name="limit.id" id="customerServiceId" value="${item?.limit?.id ?: params.limit}"/>
    <form:hidden name="type" id="customerServiceId" value="${item?.type}"/>
    <form:field fieldName="serviceParameterCondition.parameter">
        <form:select name="parameter.id" id="parameterId" entity="${item}" style="width:400px;"
                     preSelect="0"
                     items="${ServiceParameter?.findAllByServiceDefinitionAndDeleted(item?.limit?.customerService?.service, false)?.collect {
                         [text: it.name, value: it.id]
                     }}"/>
    </form:field>
    <form:field fieldName="serviceParameterCondition.count">
        <form:numericTextBox name="count" entity="${item}" style="width:400px;"/>
    </form:field>
    <form:field fieldName="serviceParameterCondition.unit">
        <form:select name="unit" entity="${item}" style="width:400px;" preSelect="0"
                     items="${ServiceParameterCondition.UNITS.collect {
                         [text: message(code: "serviceParameterCondition.unit.${it}"), value: it]
                     }}"/>
    </form:field>
    <form:field fieldName="serviceParameterCondition.operator">
        <form:select name="operator" entity="${item}" style="width:400px;" preSelect="0"
                     items="${ServiceParameterCondition.OPERATORS.collect {
                         [text: message(code: "serviceParameterCondition.operator.${it}"), value: it]
                     }}"/>
    </form:field>
    <form:field fieldName="serviceParameterCondition.value">
        <form:textArea name="value" entity="${item}" style="width:400px;height:80px;direction: ltr;"/>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>