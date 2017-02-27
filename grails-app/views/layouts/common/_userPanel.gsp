<%@ page import="best.service.dispatcher.User" %>

<ul class="user-panel">
    <sec:ifLoggedIn>
        <li>
            <a href="${createLink(uri: '/profile')}">
                <i class="fa fa-user"></i> <span><layout:loggedInUser/></span>
            </a>
        </li>
        <li>
            <a href="${createLink(controller: 'user', action: 'changePassword')}">
                <i class="fa fa-key"></i> <span><g:message code="menu.currentUser.changePassword"/></span>
            </a>
        </li>
        <li>
            <a href="${createLink(uri: '/logout')}">
                <i class="fa fa-sign-out"></i> <span><g:message code="menu.currentUser.logout"/></span>
            </a>
        </li>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
        <li><a href="${createLink(controller: 'login', action: 'auth')}"><g:message code="menu.currentUser.login"/> <i
                class="fa fa-sign-in"></i></a></li>
        %{--<li><a href="${createLink(controller: 'user', action: 'register')}"><g:message--}%
                %{--code="menu.currentUser.register"/> <i--}%
                %{--class="fa fa-user-plus"></i></a></li>--}%
        %{--<li><a href="${createLink(controller: 'user', action: 'forgetPassword')}"><g:message--}%
                %{--code="menu.currentUser.forgetPassword"/> <i--}%
                %{--class="fa fa-key"></i></a></li>--}%
    </sec:ifNotLoggedIn>
</ul>

<div class="clear-fix"></div>