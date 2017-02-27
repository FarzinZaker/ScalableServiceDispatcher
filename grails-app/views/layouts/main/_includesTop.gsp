<%@ page import="best.service.dispatcher.security.RoleHelper" %>

<asset:javascript src="jquery-1.11.1.min.js"/>

<link rel="stylesheet" type="text/css" href="${resource(dir: 'kendo.ui/css', file: 'kendo.common-bootstrap.min.css')}"/>
<link rel="stylesheet" type="text/css" href="${resource(dir: 'kendo.ui/css', file: 'kendo.metro.min.css')}"/>
<link rel="stylesheet" type="text/css" href="${resource(dir: 'kendo.ui/css', file: 'kendo.rtl.min.css')}"/>
<asset:stylesheet src="kendo.corrections.less"/>



<script language="javascript" type="text/javascript"
        src="${resource(dir: 'form-validator', file: 'form-validator.js')}"></script>
<script language="javascript" type="text/javascript"
        src="${resource(dir: 'form-validator', file: 'national-code.js')}"></script>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="${resource(dir: 'bootstrap/css', file: 'bootstrap.css')}"/>


<link rel="stylesheet" type="text/css" href="${resource(dir: 'awesome/css', file: 'font-awesome.min.css')}"/>
<asset:stylesheet src="theme.less"/>

<script language="javascript" type="text/javascript"
        src="${resource(dir: 'kendo.ui/js/jalali', file: 'JalaliDate.js')}"></script>
<script language="javascript" type="text/javascript"
        src="${resource(dir: 'kendo.ui/js', file: 'kendo.web.min.js')}"></script>
<script language="javascript" type="text/javascript"
        src="${resource(dir: 'kendo.ui/js/jalali', file: 'fa-IR.js')}"></script>
<script language="javascript" type="text/javascript"
        src="${resource(dir: 'kendo.ui/js/jalali', file: 'kendo.messages.fa-IR.js')}"></script>

<script language="javascript" type="text/javascript"
        src="${resource(dir: 'js', file: 'kendo.corrections.js')}"></script>
<script language="javascript" type="text/javascript" src="${resource(dir: 'list', file: 'list.min.js')}"></script>

<sec:ifLoggedIn>
    <sec:ifAnyGranted roles="${RoleHelper.ROLE_ADMIN}">
        <g:render template="/layouts/admin/includesTop"/>
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="${RoleHelper.ROLE_USER}">
        <g:render template="/layouts/user/includesTop"/>
    </sec:ifAnyGranted>
</sec:ifLoggedIn>
<sec:ifNotLoggedIn>
    <g:render template="/layouts/user/includesTop"/>
</sec:ifNotLoggedIn>