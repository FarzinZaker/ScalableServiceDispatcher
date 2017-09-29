<%@ page import="best.service.dispatcher.App" %>
<form:form name="itemForm">
    <form:hidden name="id" entity="${item}"/>
    <form:field fieldName="app.name">
        <form:textBox name="name" entity="${item}" validation="required" style="width:400px;"/>
    </form:field>
    <form:field fieldName="app.englishName">
        <form:textBox name="englishName" entity="${item}" validation="required" style="width:400px;direction: ltr"/>
    </form:field>
    <g:if test="${item?.key}">
        <form:field fieldName="app.key" showHelp="0">
            <form:textArea value="${item?.key}" style="width:800px;height:80px;padding:5px !important;box-sizing: border-box;direction:ltr;text-indent: 0;"/>
        </form:field>
    </g:if>
    <div class="toolbar">
        <form:button onclick="saveItem()" text="${message(code: 'save')}" class="btn-right"/>
        <form:button onclick="cancelEdit()" text="${message(code: 'cancel')}" class="btn-left"/>
    </div>
</form:form>