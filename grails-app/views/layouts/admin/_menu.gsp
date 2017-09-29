<%@ page import="best.service.dispatcher.security.RoleHelper" %>
<li>
    <a href="#"><g:message code="menu.services"/></a>
    <ul>
        <li>
            <a href="${createLink(controller: 'serviceHost', action: 'list')}"><g:message code="menu.services.serviceHost"/></a>
        </li>
        <li>
            <a href="${createLink(controller: 'serviceDefinition', action: 'list')}"><g:message code="menu.services.serviceDefinition"/></a>
        </li>
    </ul>
</li>
<li>
    <a href="${createLink(controller: 'customer', action: 'list')}"><g:message code="menu.customers"/></a>
</li>
<li>
    <a href="${createLink(controller: 'app', action: 'list')}"><g:message code="menu.apps"/></a>
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