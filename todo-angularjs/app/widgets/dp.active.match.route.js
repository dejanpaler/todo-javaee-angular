(function () {
  'use strict';

  /**
   * @ngdoc directive
   * @name todos.directive:dpActiveMatchRoute
   * @restrict EA
   * @element
   *
   * @description
   *
   * @example
     <example module="todos">
       <file name="index.html">
        <dp-active-match-route></dp-active-match-route>
       </file>
     </example>
   *
   */
  angular
    .module('widgets')
    .directive('dpActiveMatchRoute', dpActiveMatchRoute);

  function dpActiveMatchRoute($location) {
    var directive;

    directive = {
      restrict: 'A',
      link: link
    };

    return directive;

    function link(scope, element, attrs) {
      scope.$watch(
        function () {
          return $location.path();
        },
        function (newValue) {
          var pattern, regexp;

          pattern = attrs.dpActiveMatchRoute.replace('/', '\\/');
          pattern = '^' + pattern + '$';
          regexp = new RegExp(pattern, 'i');

          if (regexp.test(newValue)) {
            element.addClass('active');
          } else {
            element.removeClass('active');
          }
        }
      );
    }
  }
}());
