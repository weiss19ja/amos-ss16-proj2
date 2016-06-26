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
  .controller('JoystickDpadSwitchCtrl', ['$scope','$attrs','$timeout','$mdMedia','joystickService', function($scope, $attrs,$timeout,$mdMedia,joystickService) {

    var zone = 'zone_joystick_1';
    
    $scope.joystickState = joystickService.joystickState;
    
    $scope.onChangeJoystickShow = function () {
      console.log('onChangeJoystickShow');
      if($scope.joystickState.isJoystickEnabled){
        initJoystick();
      }
    };

    $scope.$watch(function() { return $mdMedia('xs'); }, function(big) {
      console.log("screen is xs");
    });

    $scope.$watch(function() { return $mdMedia('gt-xs'); }, function(big) {
      console.log("screen is gt-xs");
    });
    
    function initJoystick() {
      $timeout(function () {
        joystickService.initJoystick(zone);
      },100);
    }


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