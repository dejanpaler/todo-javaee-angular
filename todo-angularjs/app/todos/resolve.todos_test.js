/*global describe, beforeEach, it, expect, inject, module*/
/* jshint -W030 */
/* eslint no-unused-expressions:0 */
'use strict';

describe('resolveTodos', function () {
  var service;

  beforeEach(module('todos'));

  beforeEach(inject(function (resolveTodos) {
    service = resolveTodos;
  }));

  it('should have initialized show todo', function () {
    expect(service.showTodo).to.be.empty;
  });

  it('should resolve all todos', function () {
    service.all();

    expect(service.showTodo.active).to.be.ok;
    expect(service.showTodo.completed).to.be.ok;
  });

  it('should resolve active todos', function () {
    service.active();

    expect(service.showTodo.active).to.be.ok;
    expect(service.showTodo.completed).to.be.not.ok;
  });

  it('should resolve active todos', function () {
    service.completed();

    expect(service.showTodo.active).to.be.not.ok;
    expect(service.showTodo.completed).to.be.ok;
  });
});
