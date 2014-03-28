<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head xmlns:ng="http://angularjs.org">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Service Dashboard"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <g:layoutHead/>
    <!--[if lte IE 8]>
    <script src="${resource(dir:'/js/thirdParty', file:'json2.js')}""></script>
    <![endif]-->
    <asset:stylesheet href="application.css"/>
    <asset:link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
    <asset:link rel="icon" href="/favicon.ico" type="image/x-icon" />
</head>

<body id="ng-app" ng-app="serviceDashboard">
<div class="container">
    <g:layoutBody/>
</div>

<div class="footer" role="contentinfo"></div>

<div id="jGrowl" class="top-right jGrowl"></div>

<asset:javascript src="application.js"/>
</body>
</html>
