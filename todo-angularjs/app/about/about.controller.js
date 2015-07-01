(function () {
  'use strict';

  angular
    .module('about')
    .controller('AboutCtrl', AboutCtrl);

  function AboutCtrl() {
    var vm = this;
    vm.ctrlName = 'AboutCtrl';
  }
}());
