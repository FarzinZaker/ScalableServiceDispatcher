<%@ page import="best.service.dispatcher.Customer; best.service.dispatcher.AppCustomer; best.service.dispatcher.CustomerUser; best.service.dispatcher.security.RoleHelper; best.service.dispatcher.Role; best.service.dispatcher.UserRole" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:hidden name="appCustomer.id" id="appCustomerId" value="${appCustomer?.id}"/>
    <form:field fieldName="appCustomerUser.customerUser">
        <form:select name="customerUser.id" id="customerUserId" value="${item?.customerUser?.id ?: 0}" validation="required" style="width:400px;"
                     preSelect="0"
                     items="${users?.collect {
                         [text: it.user?.toString(), value: it?.id]
                     }}"/>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>