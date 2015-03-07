'use strict';

// Declare app level module which depends on filters, and services
angular.module('webmonitor',
    [ 'webmonitor.directives', 'webmonitor.filters', 'webmonitor.services', 'ui.router', 'ngDialog', 'ngProgress' ]).config(
    [ '$urlRouterProvider', '$stateProvider', function($urlRouterProvider, $stateProvider) {
      $stateProvider.state('config', {
        url : '/',
        templateUrl : 'templates/config.html',
        controller : 'configCtrl'
      });
      $stateProvider.state('log', {
        url : '/log',
        controller : 'logCtrl',
        templateUrl : 'templates/log.html'
      });
      $urlRouterProvider.otherwise('/');

    } ]);
