/*global describe, beforeEach, it, expect, inject, module*/
/* jshint -W030 */
/* eslint no-unused-expressions:0 */
'use strict';

describe('dpActiveMatchRoute', function () {
  var scope,
    location,
    element;

  beforeEach(module('widgets'));

  beforeEach(inject(function ($compile, $rootScope, $location) {
    scope = $rootScope;
    element = $compile(angular.element('<div data-dp-active-match-route="/path"></div>'))(scope);
    location = $location;
  }));

  it('should add active class to element when route matches', function () {
    location.path('/path');
    scope.$apply();

    expect(element.hasClass('active')).to.be.true;
  });

  it('should remove active class to element when route matches', function () {
    location.path('/pathOne');
    scope.$apply();

    expect(element.hasClass('active')).to.be.false;
  });
});
