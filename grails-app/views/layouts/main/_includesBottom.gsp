<%@ page import="best.service.dispatcher.security.RoleHelper" %>

<script language="javascript" type="text/javascript"
        src="${resource(dir: 'bootstrap/js', file: 'bootstrap.js')}"></script>
<script language="javascript" type="text/javascript"
        src="${resource(dir: 'js', file: 'common.js')}"></script>

<sec:ifLoggedIn>
    <sec:ifAnyGranted roles="${RoleHelper.ROLE_ADMIN}">
        <g:render template="/layouts/admin/includesBottom"/>
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="${RoleHelper.ROLE_USER}">
        <g:render template="/layouts/user/includesBottom"/>
    </sec:ifAnyGranted>
</sec:ifLoggedIn>
<sec:ifNotLoggedIn>
    <g:render template="/layouts/user/includesBottom"/>
</sec:ifNotLoggedIn>