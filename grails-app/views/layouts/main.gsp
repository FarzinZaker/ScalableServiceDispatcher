<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6" ng-app='stocks' xmlns:ng="http://angularjs.org" id="ng-app"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7" ng-app='stocks' xmlns:ng="http://angularjs.org" id="ng-app"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8" ng-app='stocks' xmlns:ng="http://angularjs.org" id="ng-app"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9" ng-app='stocks' xmlns:ng="http://angularjs.org" id="ng-app"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js" ng-app='stocks' xmlns:ng="http://angularjs.org"
                                     id="ng-app"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Stocks Admin"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="google-signin-client_id"
          content="784184303586-693j0vqr1gmut4ucdudsphjdcv5peqqc.apps.googleusercontent.com">

    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">

    <g:render template="/layouts/main/includesTop"/>
    <g:layoutHead/>
</head>

<body>
<div class="page-wrap">
    <g:render template="/layouts/main/header"/>
    <g:render template="/layouts/main/menu"/>
    <g:layoutBody/>

    <g:render template="/layouts/common/confirm"/>
    <g:render template="/layouts/common/alert"/>
    <g:render template="/layouts/common/info"/>
    <div id="spinner" class="spinner" style="display:none;">
        <g:message code="spinner.alt" default="Loading&hellip;"/>
    </div>

</div>

<g:render template="/layouts/common/footer"/>
<g:render template="/layouts/main/includesBottom"/>
</body>
</html>
