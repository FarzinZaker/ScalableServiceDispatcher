<%@ page import="best.service.dispatcher.security.RoleHelper" %>

<sec:ifLoggedIn>
    <sec:ifAnyGranted roles="${RoleHelper.ROLE_ADMIN}">
        <g:render template="/layouts/admin/header"/>
    </sec:ifAnyGranted>
    <sec:ifNotGranted roles="${RoleHelper.ROLE_ADMIN}">
        <g:render template="/layouts/user/header"/>
    </sec:ifNotGranted>
</sec:ifLoggedIn>
<sec:ifNotLoggedIn>
    <g:render template="/layouts/user/header"/>
</sec:ifNotLoggedIn>