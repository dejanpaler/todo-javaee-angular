/*global describe, beforeEach, it, expect, inject, module*/
'use strict';

describe('TodosCtrl', function () {
  var ctrl;

  beforeEach(module('todos'));

  beforeEach(inject(function ($rootScope, $controller, $location, TodosResource, filterFilter) {
    ctrl = $controller('TodosCtrl', {
      $scope: $rootScope.$new(),
      $location: $location,
      TodosResource: TodosResource,
      filterFilter: filterFilter
    });
  }));

  it('should have initialized todos', function () {
    expect(ctrl.newTodo).to.be.equal('');
  });
});
