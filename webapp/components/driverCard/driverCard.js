/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

angular.module('myApp.driverCard', [])
  .controller('DriverCardCtrl', ['$scope', 'joystickService', function($scope, joystickService) {

    $scope.joystickState = joystickService.joystickState;
  
  }])
.component('drivercard', {
  restrict: 'EA',
  templateUrl: 'components/driverCard/driverCard.html',
  css: 'components/driverCard/driverCard.css',
  controller: 'DriverCardCtrl',
  bindings: {
  }
});