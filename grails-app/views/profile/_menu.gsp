<%@ page import="best.service.dispatcher.security.RoleHelper" %>
<div class="profileMenu">
    <ul>
        <li class="${params.controller == 'profile' && params.action == 'index' || params.action == null ? 'active' : ''}">
            <a href="${createLink(controller: 'profile', action: 'index')}">
                <i class="fa fa-user"></i>
                <span>
                    <g:message code="menu.currentUser.profile"/>
                </span>

                <div class="clear-fix"></div>
            </a>
        </li>
        <li class="${params.controller == 'profile' && params.action == 'edit' ? 'active' : ''}">
            <a href="${createLink(controller: 'profile', action: 'edit')}">
                <i class="fa fa-edit"></i>
                <span>
                    <g:message code="user.profile.edit"/>
                </span>

                <div class="clear-fix"></div>
            </a>
        </li>
    </ul>
</div>
<script language="javascript" type="text/javascript">
    function resizeProfileMenu() {
        return;
        var profileMenu = $('.profileMenu');
        profileMenu.stop().hide();
        var container = profileMenu.parent();
        while (!container.hasClass('container'))
            container = container.parent();
        profileMenu.height(container.height() - 20).show();
    }

    $(document).ready(function () {
        resizeProfileMenu();
    });

    $(window).resize(function () {
        resizeProfileMenu();
    });
</script>