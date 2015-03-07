<!doctype html>
<html lang="en" ng-app="webmonitor">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Welcome to Web Monitor</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ngDialog.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ngProgress.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <header ng-include="'templates/nav.html'"></header>
  <div id="views" class="container" ui-view></div>

  <script src="${pageContext.request.contextPath}/js/angular/angular.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/angular/plugins/angular-ui-router.js"></script>
  <script src="${pageContext.request.contextPath}/js/angular/plugins/ngDialog.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/angular/plugins/ngProgress.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/app.js"></script>
  <script src="${pageContext.request.contextPath}/js/services.js"></script>
  <script src="${pageContext.request.contextPath}/js/controllers/configCtrl.js"></script>
  <script src="${pageContext.request.contextPath}/js/controllers/logCtrl.js"></script>
  <script src="${pageContext.request.contextPath}/js/filters.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives.js"></script>

</body>
</html>
