(function () {
  'use strict';

  angular
    .module('todos')
    .config(config);

  function config($routeProvider) {
    $routeProvider
      .when('/todos', {
        templateUrl: 'todos/todos.tpl.html',
        controller: 'TodosCtrl',
        controllerAs: 'todos',
        resolve: {
          allTodos: allTodos
        }
      })
      .when('/todos/active', {
        templateUrl: 'todos/todos.tpl.html',
        controller: 'TodosCtrl',
        controllerAs: 'todos',
        resolve: {
          activeTodos: activeTodos
        }
      })
      .when('/todos/completed', {
        templateUrl: 'todos/todos.tpl.html',
        controller: 'TodosCtrl',
        controllerAs: 'todos',
        resolve: {
          completedTodos: completedTodos
        }
      });
  }

  /* @ngInject */
  function allTodos(resolveTodos) {
    return resolveTodos.all();
  }

  /* @ngInject */
  function activeTodos(resolveTodos) {
    return resolveTodos.active();
  }

  /* @ngInject */
  function completedTodos(resolveTodos) {
    return resolveTodos.completed();
  }
}());
