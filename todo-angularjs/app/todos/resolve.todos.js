(function () {
  'use strict';

  /**
   * @ngdoc service
   * @name todos.factory:TodosService
   *
   * @description
   *
   */
  angular
    .module('todos')
    .factory('resolveTodos', resolveTodos);

  function resolveTodos() {
    var service, showTodo;

    showTodo = {};
    service = {
      showTodo: showTodo,
      all: resolveAllTodos,
      active: resolveActiveTodos,
      completed: resolveCompletedTodos
    };

    return service;

    function resolveAllTodos() {
      showTodo.active = true;
      showTodo.completed = true;
    }

    function resolveActiveTodos() {
      showTodo.active = true;
      showTodo.completed = false;
    }

    function resolveCompletedTodos() {
      showTodo.active = false;
      showTodo.completed = true;
    }
  }
}());
