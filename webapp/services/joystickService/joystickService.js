/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

'use strict';

/**
 * Service for joystick management.
 */
angular.module("myApp.joystickService", [])
  .factory("joystickService", function () {

    var actualZone;

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

      manager.on('end',function (evt,data) {
        console.log('joystick:stop');
        //$scope.stop();
      });
    }

    return {

      joystickState : joystickState,

      initJoystick:function (zone) {
        setTimeout(function () {
          initJoystick(zone);
        },100)
      }
    };
  })
;