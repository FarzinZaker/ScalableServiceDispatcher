<html>
<head>
    <meta name='layout' content='main'/>
    <title><g:message code="login.title"/></title>
</head>

<body>

<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <layout:breadcrumb items="${[
                    [text: '', url: createLink(uri: '/')],
                    [text: '<i class="fa fa-key"></i> ' + message(code: 'login.title'), url: createLink(controller: 'login', action: 'auth')]
            ]}"/>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <form:errors message="${flash.message}"/>
            <form:info message="${flash.info}"/>

            <form action='${postUrl}' method='POST' id='loginForm' autocomplete='off'>
                <form:field fieldName="login.username">
                    <form:textBox name="username" validation="required" style="width:500px;"/>
                </form:field>
                <form:field fieldName="login.password" border="0">
                    <form:password name="password" validation="required" style="width:500px;"/>
                </form:field>
                <form:field>
                    <form:checkbox name="${rememberMeParameter}" text="${message(code: 'login.rememberMe')}"
                                   style="width:500px;" checked="${hasCookie}"/>
                </form:field>
                <g:if test="${loginErrorsCount > 0}">
                    <form:field fieldName="captcha">
                        <form:captcha name="captcha" id="loginCaptcha" type="login" width="500"/>
                    </form:field>
                </g:if>
                <form:submitButton name="submit" text="${message(code: 'login.button.label')}"
                                   style="padding:5px 15px"/>
                %{--<form:linkButton href="${createLink(controller: 'user', action: 'forgetPassword')}"--}%
                                 %{--text="${message(code: 'menu.currentUser.forgetPassword')}"--}%
                                 %{--style="padding:5px 15px;margin-right:280px;"/>--}%
            </form>
        </div>
    </div>
</div>
<script type='text/javascript'>
    <!--
    (function () {
        document.forms['loginForm'].elements['j_username'].focus();
    })();
    // -->

    $(document).ready(function () {
        $.validate({
            form: '#loginForm'
        });
    });
</script>
</body>
</html>
