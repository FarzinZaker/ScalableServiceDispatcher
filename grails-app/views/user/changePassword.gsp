<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 7/1/14
  Time: 3:12 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="changePassword.title"/></title>
    <script language="javascript" src="${resource(dir:'form-validator', file: 'security.js')}"></script>
</head>

<body>

<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <layout:breadcrumb items="${[
                    [text: '', url: createLink(uri: '/')],
                    [text: '<i class="fa fa-key"></i> ' + message(code: 'changePassword.title'), url: createLink(controller: 'user', action: 'changePassword')]
            ]}"/>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <form:errors message="${flash.validationError}"/>
            <form:form action="saveNewPassword" name="changePasswordForm">
                <g:if test="${askForOldPassword}">
                    <form:field fieldName="password.old">
                        <form:password name="oldPassword" validation="required" style="width:500px;"/>
                    </form:field>
                </g:if>
                <form:field fieldName="password.new">
                    <form:password name="newPassword_confirmation" validation="required" style="width:500px;"/>
                </form:field>
                <form:field fieldName="password.new.confirm">
                    <form:password name="newPassword" validation="confirmation" style="width:500px;"/>
                </form:field>
                <form:submitButton name="submit" text="${message(code: 'changePassword.button.label')}"/>
            </form:form>
        </div>
    </div>
</div>
</body>
</html>