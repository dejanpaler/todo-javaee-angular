/*global element, by*/
'use strict';

function AboutPage() {
  this.text = element(by.tagName('p'));
  this.heading = element(by.tagName('h2'));
}

module.exports = AboutPage;
