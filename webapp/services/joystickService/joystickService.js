/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

'use strict';

/**
 * Service for joystick management.
 */
angular.module("myApp.joystickService", [])
  .factory("joystickService", function (roverService) {

    var actualZone;
    var lastAngle = 0;
    var lastSpeed = 0;
    var angleSteps = 30;
    var speedLevels = 5;

    var options = {
      zone: document.getElementById('zone_joystick'),
      color: '#4CAF50',
      size: 150,
      position: {left: '50%', top: '50%'},
      mode: 'static',
      restOpacity: 0.7
    };
    
    var manager = {
      destroy:function () {}
    };

    var joystickState = {
      isJoystickEnabled : false
    };
    
    function initJoystick(zone) {
      console.log('init joystick for id '+ zone);
      actualZone = zone;
      manager.destroy();
      options.zone = document.getElementById(zone);
      manager = nipplejs.create(options);
/**
      manager.on('dir:up',function (evt,data) {
        console.log('joystick:up');
        //$scope.up();
      });

      manager.on('dir:down',function (evt,data) {
        console.log('joystick:down');
        //$scope.down();
      });

      manager.on('dir:left',function (evt,data) {
        console.log('joystick:left');
        //$scope.left();
      });

      manager.on('dir:right',function (evt,data) {
        console.log('joystick:right');
        //$scope.right();

      });
*/
      manager.on('end',function (evt,data) {
        console.log('joystick:stop');
        onStop();
      });

      manager.on('move',function (evt,data) {
        var speed = getSpeedLevel(data.distance);
        var angle = getAngleSector(data.angle.degree);

        if(angle != lastAngle || speed != lastSpeed){
          onMove(angle,speed);
          lastAngle = angle;
          lastSpeed = speed;
        }

      });
    }
    
    function getAngleSector(angle) {
      var targetAngle;
      var remainder = angle % angleSteps;
      if(remainder > (angleSteps / 2)){
        targetAngle = angle - remainder + angleSteps;
      } else {
        targetAngle = angle - remainder;
      }
      return Math.round(targetAngle);
    }

    function getSpeedLevel(speed) {
      var normalisedSpeed = Math.round(speed / (options.size / 2) * speedLevels);
      return Math.round(normalisedSpeed * 100/speedLevels);
    }

    function onMove(angle,speed) {
      roverService.driveContinuously(angle,speed);
    }

    function onStop() {
      roverService.stop();
    }

    return {

      joystickState : joystickState,

      initJoystick:function (zone) {
        setTimeout(function () {
          initJoystick(zone);
        },100)
      },

      stop:onStop,
      move:onMove
    };
  })
;