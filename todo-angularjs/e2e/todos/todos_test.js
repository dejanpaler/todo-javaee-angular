/*global describe, beforeEach, it, browser, protractor */
'use strict';

var chai = require('chai')
  , chaiAsPromised = require('chai-as-promised')
  , expect = chai.expect
  , TodosPagePo = require('./todos.po');

chai.use(chaiAsPromised);

describe('Todos page', function () {
  var todosPage;
  this.timeout(15000);

  beforeEach(function () {
    todosPage = new TodosPagePo();
    browser.get('/todos');
  });

  it('should insert new todo item', function () {
    todosPage.activeTodos.count().then(function (activeTodosCount) {
      todosPage.newTodo.sendKeys('Adding new todo item.');
      todosPage.newTodo.sendKeys(protractor.Key.ENTER);

      todosPage.activeTodos.count().then(function (newTodoCount) {
        expect(activeTodosCount + 1).to.equal(newTodoCount);
      });
    });
  });
});
