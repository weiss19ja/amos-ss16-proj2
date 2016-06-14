'use strict';

/**
 * Module for the roverMaster view.
 */
angular.module('myApp.roverMaster', [])
.controller('RoverMasterCtrl', ['roverService', '$scope', '$location', '$mdMedia',
  function(roverService, $scope, $location, $mdMedia) {
    /**
     * URL where the camera stream is accessible on the rover.
     * @type {string}
     */
    $scope.mjpegStreamURL = $location.protocol() + '://' + $location.host() + ':9000/stream/video.mjpeg';
    /**
     * Expose rover state from rover service.
     * Used to hide controls if driver mode not available
     */
    $scope.roverState = roverService.roverState;

    /**
     * As soon as the view is opened, we try to register this user as a driver.
     */
    console.log('Enter Driver Mode (from roverMaster)');
    roverService.enterDriverMode();

    $scope.stop = function() {
      roverService.stop();
    };

    /**
     * Exit the driver mode as soon as a user navigates to another page.
     */
    $scope.$on('$routeChangeStart', function(event, next, current) {
      console.log('Exit Driver Mode, changing to URL' + next.$$route.originalPath);
      roverService.exitDriverMode();
    });
  }]);