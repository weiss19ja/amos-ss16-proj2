'use strict';

angular.module('myApp.driverCard', [])
  .controller('DriverCardCtrl', ['$scope', function($scope) {
  
    $scope.showJoystick = false;
  
  }])
.component('drivercard', {
  restrict: 'EA',
  templateUrl: 'components/driverCard/driverCard.html',
  css: 'components/driverCard/driverCard.css',
  controller: 'DriverCardCtrl',
  bindings: {
  }
});