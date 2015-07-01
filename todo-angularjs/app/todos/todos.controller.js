(function () {
  'use strict';

  /**
   * @ngdoc object
   * @name todos.controller:TodosCtrl
   *
   * @description
   *
   */
  angular
    .module('todos')
    .controller('TodosCtrl', TodosCtrl);

  function TodosCtrl($location, TodosResource, resolveTodos) {
    var vm = this;

    vm.todos = [];
    vm.newTodo = '';
    vm.editedTodo = null;
    vm.location = $location;
    vm.showTodo = resolveTodos.showTodo;

    vm.addTodo = addTodo;
    vm.editTodo = editTodo;
    vm.doneEditing = doneEditing;
    vm.revertEditing = revertEditing;
    vm.removeTodo = removeTodo;
    vm.todoCompleted = todoCompleted;

    activate();

    function activate() {
      return TodosResource.getTodoItems().then(function (todoItems) {
        vm.todos = todoItems;
      });
    }

    function addTodo() {
      var newTodo, todo;

      newTodo = vm.newTodo.trim();
      todo = {
        title: newTodo,
        completed: false
      };

      if (newTodo.length === 0) {
        return;
      }

      TodosResource.addTodoItem(todo).then(function (todoItem) {
        vm.todos.push(todoItem);
      });

      vm.newTodo = '';
    }

    function editTodo(todo) {
      vm.editedTodo = todo;
      // Clone the original todoItem to restore it on demand.
      vm.originalTodo = angular.extend({}, todo);
    }

    function doneEditing(todo) {
      vm.editedTodo = null;
      todo.title = todo.title.trim();

      if (!todo.title) {
        vm.removeTodo(todo);
      }

      TodosResource.updateTodoItem(todo);
    }

    function revertEditing(todo) {
      vm.todos[vm.todos.indexOf(todo)] = vm.originalTodo;
      vm.doneEditing(vm.originalTodo);
    }

    function removeTodo(todo) {
      vm.todos.splice(vm.todos.indexOf(todo), 1);
      TodosResource.deleteTodoItem(todo);
    }

    function todoCompleted(todo) {
      todo.completed = !todo.completed;
      TodosResource.updateTodoItem(todo);
    }
  }
}());
