(function () {
  'use strict';

  angular
    .module('about')
    .config(config);

  function config($routeProvider) {
    $routeProvider
      .when('/about', {
        templateUrl: 'about/about.tpl.html',
        controller: 'AboutCtrl',
        controllerAs: 'vm'
      });
  }
}());
