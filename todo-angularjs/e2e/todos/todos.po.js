/*global element, by*/
'use strict';

function TodosPage() {
  this.newTodo = element(by.id('new-todo'));
  this.activeTodos = element.all(by.repeater('todo in activeTodos = (todos.todos | filter: {completed: false})'));
}

module.exports = TodosPage;
