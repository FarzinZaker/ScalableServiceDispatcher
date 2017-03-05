<%@ page import="best.service.dispatcher.ServiceDefinition" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:hidden name="customer.id" id="customerId" entity="${item}"/>
    <form:field fieldName="customerService.service">
        <g:if test="${item?.id}">
            <div style="width: 400px;font-size:16px;font-weight:bold;padding-top:10px;">${item?.service?.name}</div>
            <form:hidden name="service.id" id="serviceId" entity="${item}"/>
        </g:if>
        <g:else>
            <form:select name="service.id" id="serviceId" entity="${item}" validation="required" style="width:400px;"
                         preSelect="0"
                         items="${ServiceDefinition?.findAllByDeleted(false)?.collect {
                             [text: it.name, value: it.id]
                         }}"/>
        </g:else>
    </form:field>
    <form:field fieldName="customerService.startDate">
        <form:datePicker name="startDate" entity="${item}" style="width:400px;"
                         value="${format.jalaliDate(date: item?.startDate ?: new Date())}"/>
    </form:field>
    <form:field fieldName="customerService.endDate">
        <form:datePicker name="endDate" entity="${item}" style="width:400px;"
                         value="${format.jalaliDate(date: item?.endDate ?: new Date())}"/>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>