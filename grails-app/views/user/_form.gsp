<%@ page import="grails.converters.JSON;" %>
<form:hidden name="id" entity="${user}"/>
<form:hidden name="role" value="${params.role}"/>

<form:field fieldName="userInfo.firstName">
    <form:textBox name="firstName" style="width:500px" entity="${user}" validation="required"/>
</form:field>

<form:field fieldName="userInfo.lastName">
    <form:textBox name="lastName" style="width:500px" entity="${user}" validation="required"/>
</form:field>

<form:field fieldName="userInfo.clientNo">
    <form:textBox name="clientNo" style="width:500px" entity="${user}"/>
</form:field>

<form:field fieldName="userInfo.sex">
    <form:select name="sex" entity="${user}"
                 items="${[[text: message(code: 'user.sex.male'), value: 'male'], [text: message(code: 'user.sex.female'), value: 'female']]}"
                 validation="required" style="width:500px;"/>
</form:field>

<form:field fieldName="userInfo.mobile">
    <form:textBox name="mobile" style="width:500px" entity="${user}" validation="required"/>
</form:field>

<form:field fieldName="userInfo.email">
    <form:textBox name="email" style="width:500px" entity="${user}" validation="required"/>
</form:field>

<form:field fieldName="userInfo.password">
    <form:password name="password" style="width:500px"/>
</form:field>

<form:field fieldName="userInfo.status">
    <div style="width: 500px;">
        <div>
            <form:checkbox name="enabled" checked="${user?.enabled}" text="${message(code: 'userInfo.enabled.label')}"/>
        </div>

        <div>
            <form:checkbox name="accountExpired" checked="${user?.accountExpired}"
                           text="${message(code: 'userInfo.accountExpired.label')}"/>
        </div>

        <div>
            <form:checkbox name="accountLocked" checked="${user?.accountLocked}"
                           text="${message(code: 'userInfo.accountLocked.label')}"/>
        </div>

        <div>
            <form:checkbox name="passwordExpired" checked="${user?.passwordExpired}"
                           text="${message(code: 'userInfo.passwordExpired.label')}"/>
        </div>
    </div>
</form:field>

<form:field fieldName="userInfo.roles">
    <div style="width: 500px;">
        <g:each in="${availableRoles}" var="role" status="i">
            <div>
                <form:checkbox name="roles_${role}" checked="${params.action == 'create' ? role == params.role : roles.contains(role)}"
                               text="${message(code: "user.role.${role}")}"/>
            </div>
        </g:each>
    </div>
</form:field>