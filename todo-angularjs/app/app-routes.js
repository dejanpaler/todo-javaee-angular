(function () {
  'use strict';

  angular
    .module('todoappAngularjs')
    .config(config);

  function config($routeProvider, $locationProvider) {
    $routeProvider.otherwise({
      redirectTo: '/todos'
    });

    $locationProvider.html5Mode(true);
  }
}());
