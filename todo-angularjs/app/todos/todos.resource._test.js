/*global describe, beforeEach, it, expect, inject, module*/
'use strict';

describe('TodosResource', function () {
  var $rootScope, $httpBackend, todos;

  beforeEach(module('todos'));

  beforeEach(inject(function ($injector, TodosResource) {
    todos = TodosResource;
    $rootScope = $injector.get('$rootScope');
    $httpBackend = $injector.get('$httpBackend');
  }));

  it('should have someMethod return TodosResource', function () {
    $httpBackend
      .when('GET', todos.todoItemsUrl)
      .respond(200, {todoName: 'Buy an ice'});

    todos.getTodoItems().then(function (response) {
      expect(response.todoName).to.be.equal('Buy an ice');
    });

    $rootScope.$apply();
    $httpBackend.flush();
  });
});
