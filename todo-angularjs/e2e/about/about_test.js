/*global describe, beforeEach, it, browser */
'use strict';

var chai = require('chai')
  , chaiAsPromised = require('chai-as-promised')
  , expect = chai.expect
  , AboutPagePo = require('./about.po');

chai.use(chaiAsPromised);

describe('About page', function () {
  var aboutPage;
  this.timeout(15000);

  beforeEach(function () {
    aboutPage = new AboutPagePo();
    browser.get('/about');
  });

  it('should say AboutCtrl', function () {
    expect(aboutPage.heading.getText()).to.eventually.equal('About');
    expect(aboutPage.text.getText()).to.eventually.equal('AboutCtrl');
  });
});
