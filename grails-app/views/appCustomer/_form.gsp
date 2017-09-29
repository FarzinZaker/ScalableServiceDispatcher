<%@ page import="best.service.dispatcher.Customer" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:hidden name="app.id" id="appId" entity="${item}"/>
    <form:field fieldName="appCustomer.customer">
        <g:if test="${item?.id}">
            <div style="width: 400px;font-size:16px;font-weight:bold;padding-top:10px;">${item?.customer?.name}</div>
            <form:hidden name="customer.id" id="customerId" entity="${item}"/>
        </g:if>
        <g:else>
            <form:select name="customer.id" id="customerId" entity="${item}" validation="required" style="width:400px;"
                         preSelect="0"
                         items="${Customer?.findAllByDeleted(false)?.collect {
                             [text: it.name, value: it.id]
                         }}"/>
        </g:else>
    </form:field>
    <form:field fieldName="appCustomer.startDate">
        <form:datePicker name="startDate" entity="${item}" style="width:400px;"
                         value="${format.jalaliDate(date: item?.id ? item?.startDate : new Date())}"/>
    </form:field>
    <form:field fieldName="appCustomer.endDate">
        <form:datePicker name="endDate" entity="${item}" style="width:400px;"
                         value="${format.jalaliDate(date: item?.id ? item?.endDate : new Date() + 30)}"/>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>