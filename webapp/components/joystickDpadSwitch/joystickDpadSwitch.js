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
  .controller('JoystickDpadSwitchCtrl', ['$scope','$attrs','joystickService', function($scope, $attrs,joystickService) {

    var zone = 'zone_joystick_1';
    
    $scope.joystickState = joystickService.joystickState;
    
    $scope.onChangeJoystickShow = function () {
      if($scope.joystickState.isJoystickEnabled){
        joystickService.initJoystick(zone);
      }
    };
    
    function init() {
      if($attrs.zone){
        zone = $attrs.zone;
      }
    };
    init();
    
    
  }])
  .component('joystickDpadSwitch', {
    restrict: 'EA',
    templateUrl: 'components/joystickDpadSwitch/joystickDpadSwitch.html',
    controller: 'JoystickDpadSwitchCtrl',
    bindings: {
      zone: '<'
    }
  });