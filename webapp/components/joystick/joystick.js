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

    $scope.joystickSize;

    setSize();


    function setSize() {
      if($attrs.size){
        $scope.joystickSize = $attrs.size;
      } else {
        $scope.joystickSize = 150;
      }
    }

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
      //console.log(data);
      console.log('radian'+data.angle.radian +'   distance: '+data.distance);
      //console.log('x: '+data.position.x +'   y: '+data.position.y);
      //console.log('manager x: '+options.position.x +'   y: '+options.position.y);
    });

    manager.on('end',function (evt,data) {
      console.log('halt stop');
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