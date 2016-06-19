'use strict';
/**
 * Joystick component to control the rover movement.
 *
 * Changeable attributes are:
 * size: size of the joystick
 *
 * For communication with the rover 'roverService' is used.
 */
angular.module('myApp.joystick', [])
  .controller('JoystickCtrl', ['$scope', 'roverService','$attrs', function($scope, roverService, $attrs) {

    function init() {
      if($attrs.size){
        $scope.joystickSize = $attrs.size;
      } else {
        $scope.joystickSize = 150;
      }
    }

    init();

    $scope.joystickSize;

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


    var options = {
      zone: document.getElementById('zone_joystick'),
      color: '#4CAF50',
      size: $scope.joystickSize,
      position: {left: '50%', top: '50%'},
      mode: 'static',
      restOpacity: 0.7
    };
    var manager = nipplejs.create(options);

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
    
  }])
  .component('joystick', {
    restrict: 'EA',
    templateUrl: 'components/joystick/joystick.html',
    css: 'components/joystick/joystick.css',
    controller: 'JoystickCtrl',
    bindings: {
      size: '<'
    }
  });