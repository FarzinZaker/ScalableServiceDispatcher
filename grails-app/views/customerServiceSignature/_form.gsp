<%@ page import="best.service.dispatcher.Customer; best.service.dispatcher.CustomerService; best.service.dispatcher.CustomerUser; best.service.dispatcher.security.RoleHelper; best.service.dispatcher.Role; best.service.dispatcher.UserRole" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:hidden name="customerService.id" id="customerServiceId" value="${customerService?.id}"/>
    <form:field fieldName="customerServiceSignature.customerUser">
        <form:select name="customerUser.id" id="customerUserId" value="${item?.customerUser?.id ?: 0}" validation="required" style="width:400px;"
                     preSelect="0"
                     items="${users?.collect {
                         [text: it.user?.toString(), value: it?.id]
                     }}"/>
    </form:field>
    <form:field fieldName="customerServiceSignature.required" showLabel="0">
        <div style="width: 400px;padding-top:20px;">
            <form:checkbox name="required" checked="${item?.required}"
                           text="${message(code: 'customerServiceSignature.required.label')}"/>
        </div>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>