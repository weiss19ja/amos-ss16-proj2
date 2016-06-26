/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

'use strict';

/**
 * Toggle switch to choose between joystick and dpad control.
 *
 */
angular.module('myApp.joystickDpadSwitch', [])
  .controller('JoystickDpadSwitchCtrl', ['$scope','$attrs','$timeout','joystickService', function($scope, $attrs,$timeout,joystickService) {

    var zoneId = 'zone_joystick_1';
    
    $scope.joystickState = joystickService.joystickState;
    
    $scope.onChangeJoystickShow = function () {
      console.log('onChangeJoystickShow');
      if($scope.joystickState.isJoystickEnabled){
        initJoystick();
      }
    };
    
    function initJoystick() {
      $timeout(function () {
        joystickService.initJoystick($scope.joystickId);
      },100);
    }


    $scope.init = function() {
      if($attrs.zoneId){
        zoneId = $attrs.zoneId;
      }
    };
    
    
  }])
  .component('joystickDpadSwitch', {
    restrict: 'EA',
    templateUrl: 'components/joystickDpadSwitch/joystickDpadSwitch.html',
    controller: 'JoystickDpadSwitchCtrl',
    bindings: {
      zoneId: '<'
    }
  });