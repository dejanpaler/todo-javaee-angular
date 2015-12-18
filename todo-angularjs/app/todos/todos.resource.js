(function () {
  'use strict';

  /**
   * @ngdoc service
   * @name todos.factory:TodosResource
   *
   * @description
   *
   */
  angular
    .module('todos')
    .factory('TodosResource', TodosResource);

  function TodosResource($http, $log, $q) {
    var todoItemsUrl, TodosResourceBase, prettyJson;

    todoItemsUrl = 'http://dockerhost:8080/todoapp-javaee/todo-items/';
    TodosResourceBase = {
      getTodoItems: getTodoItems,
      deleteTodoItem: deleteTodoItem,
      addTodoItem: addTodoItem,
      updateTodoItem: updateTodoItem,
      todoItemsUrl: todoItemsUrl
    };

    prettyJson = false;

    return TodosResourceBase;

    function getTodoItems() {
      return $http.get(todoItemsUrl)
        .then(success)
        .catch(fail);

      function success(response) {
        $log.debug('getTodoItems:\n' + angular.toJson(response.data, prettyJson));
        return response.data;
      }

      function fail(error) {
        $log.debug('error at getTodoItems:' + angular.toJson(error.data, prettyJson));
        return $q.reject(error.data);
      }
    }

    function deleteTodoItem(todo) {
      return $http.delete(todoItemsUrl + todo.id)
        .then(success)
        .catch(fail);

      function success(response) {
        $log.debug('deleteTodoItem:\n' + angular.toJson(response.data, prettyJson));
        return response.data;
      }

      function fail(error) {
        $log.debug('error at deleteTodoItem:' + angular.toJson(error.data, prettyJson));
        return $q.reject(error.data);
      }
    }

    function addTodoItem(todo) {
      return $http.post(todoItemsUrl, todo)
        .then(success)
        .catch(fail);

      function success(response) {
        $log.debug('addTodoItem:\n' + angular.toJson(response.data, prettyJson));
        return response.data;
      }

      function fail(error) {
        $log.debug('error at addTodoItem:' + angular.toJson(error.data, prettyJson));
        return $q.reject(error.data);
      }
    }

    function updateTodoItem(todo) {
      return $http.put(todoItemsUrl + todo.id, todo)
        .then(success)
        .catch(fail);

      function success(response) {
        $log.debug('updateTodoItem:\n' + angular.toJson(response.data, prettyJson));
        return response.data;
      }

      function fail(error) {
        $log.debug('error at updateTodoItem:' + angular.toJson(error.data, prettyJson));
        return $q.reject(error.data);
      }
    }
  }
}());
