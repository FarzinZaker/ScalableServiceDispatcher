<%@ page import="best.service.dispatcher.security.RoleHelper" %>
<div class="menuContainer">
    <div class="container">
        <div class="row">
            <div class="col-sm-12 k-rtl">
                <ul id="menu" class="hideBeforeLoad">
                    <li>
                        <a href="${createLink(uri: '/')}">
                            <i class="fa fa-home"></i> <g:message code="menu.home"/>
                        </a>
                    </li>
                    <sec:ifLoggedIn>
                        <sec:ifAnyGranted roles="${RoleHelper.ROLE_ADMIN}">
                            <g:render template="/layouts/admin/menu"/>
                        </sec:ifAnyGranted>
                        <sec:ifAnyGranted roles="${RoleHelper.ROLE_USER}">
                            <g:render template="/layouts/user/menu"/>
                        </sec:ifAnyGranted>
                    </sec:ifLoggedIn>
                </ul>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        $("#menu").kendoMenu();
    });
</script>
