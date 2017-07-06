<%@ page import="best.service.dispatcher.security.RoleHelper; best.service.dispatcher.Role; best.service.dispatcher.UserRole" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:hidden name="customer.id" id="customerId" entity="${item}"/>
    <form:field fieldName="customerUser.user">
        <form:select name="user.id" id="userId" value="${item?.user?.id ?: 0}" validation="required" style="width:400px;"
                     preSelect="0"
                     items="${UserRole?.findAllByRole(Role.findByAuthority(RoleHelper.ROLE_USER))?.collect {
                         [text: it.user?.toString(), value: it.user?.id]
                     }}"/>
    </form:field>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>