<%@ page import="best.service.dispatcher.security.RoleHelper" %>
<li>
    <a href="#"><g:message code="menu.services"/></a>
    <ul>
        <li>
            <a href="#"><g:message code="menu.services.hosts"/></a>
        </li>
        <li>
            <a href="#"><g:message code="menu.services.definitions"/></a>
        </li>
    </ul>
</li>
<li>
    <a href="#">
        <g:message code="menu.users"/>
    </a>
    <ul>
        <g:each in="${RoleHelper.ROLES}" var="role">
            <li>
                <a href="${createLink(controller: 'user', action: 'list', id: role)}">
                    <g:message code="menu.users.${role}"/>
                </a>
            </li>
        </g:each>
    </ul>
</li>