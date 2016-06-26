/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';
/**
 * Joystick component to control the rover movement.
 *
 * Changeable attributes are:
 * test: enable for unit tests to prevent using nippleJS
 *
 * For communication with the rover 'roverService' is used.
 */
angular.module('myApp.joystick', [])
  .controller('JoystickCtrl', ['$scope', 'roverService','$attrs','$timeout','joystickService', function($scope, roverService, $attrs,$timeout,joystickService) {

    
    /*
    var manager = {
      on:function () {},
      destroy:function () {}
    };

    var nippleJSActicated = false;

    $scope.joystickSize = 150;
    
    $scope.up = function () {
      roverService.driveForward();
    };

    $scope.down = function () {
      roverService.driveBackward();
    };

    $scope.left = function () {
      roverService.turnLeft();
    };

    $scope.right = function () {
      roverService.turnRight();
    };

    $scope.stop = function () {
      roverService.stop();
    };

    $scope.init = function() {
      console.log("init joystick");
      if(!$attrs.test){

        initNippleJS();
      }
    };

    $timeout(function () {
      console.log("timout " + nippleJSActicated);
      if(nippleJSActicated == false){
       nippleJSActicated = true;
       initNippleJS();
      }
    },1000);

    var options = {
      zone: document.getElementById('zone_joystick'),
      color: '#4CAF50',
      size: $scope.joystickSize,
      position: {left: '50%', top: '50%'},
      mode: 'static',
      restOpacity: 0.7
    };

    function initNippleJS() {
      console.log('use nipple js');
      manager.destroy();
      manager = nipplejs.create(options);
      manager.on('move', function (evt, data) {
        //console.log('radian'+data.angle.radian +'   distance: '+data.distance);
      });

      manager.on('dir:up',function (evt,data) {
        console.log('joystick:up');
        $scope.up();
      });

      manager.on('dir:down',function (evt,data) {
        console.log('joystick:down');
        $scope.down();
      });

      manager.on('dir:left',function (evt,data) {
        console.log('joystick:left');
        $scope.left();
      });

      manager.on('dir:right',function (evt,data) {
        console.log('joystick:right');
        $scope.right();

      });

      manager.on('end',function (evt,data) {
        console.log('joystick:stop');
        $scope.stop();
      });
    }
    
*/
    $scope.joystickId = 'zone_joystick_1';
    $scope.joystickState = joystickService.joystickState;
    /*
    $timeout(function () {
      joystickService.initJoystick($scope.joystickId);
    },100);
*/

  }])
  .component('joystick', {
    restrict: 'EA',
    templateUrl: 'components/joystick/joystick.html',
    css: 'components/joystick/joystick.css',
    controller: 'JoystickCtrl',
    bindings: {
      test: '<'
    }
  });