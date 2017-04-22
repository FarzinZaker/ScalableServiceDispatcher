<%@ page import="best.service.dispatcher.ServiceRateLimit; best.service.dispatcher.ServiceDefinition" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:hidden name="customerService.id" id="customerServiceId" value="${item?.customerService?.id ?: params.customerService}"/>
    <form:field fieldName="serviceRateLimit.period">
        <form:select name="period" id="period" entity="${item}" validation="required" style="width:400px;"
                     preSelect="0"
                     items="${ServiceRateLimit.PERIODS.collect {
                         [text: message(code: "serviceRateLimit.period.${it}"), value: it]
                     }}"/>
    </form:field>
    <form:field fieldName="serviceRateLimit.limit">
        <form:numericTextBox name="limit" entity="${item}" style="width:400px;"/>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>