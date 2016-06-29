/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
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
     * Initialization:
     * As soon as the view is opened, redirect the user iff he is on a mobile device - this view is not for them!
     * If the user is allowed, we try to register them as a driver.
     */
    redirectWrongDeviceSize();
    console.log('Enter Driver Mode (from roverMaster)');
    roverService.enterDriverMode();

    $scope.stop = function() {
      roverService.stop();
    };

    /**
     * Exit the driver mode as soon as a user navigates to another page.
     */
    $scope.$on('$routeChangeStart', function(event, next, current) {
      var nextRoutePath = next.$$route.originalPath;

      // drive and roverMaster target pages are valid for drive mode - do not log out user here
      if(!urlContainsStr(nextRoutePath, 'drive')) {
        console.log('Exit Driver Mode, changing to URL: ' + next.$$route.originalPath);
        roverService.exitDriverMode();
      }
    });

    /**
     * If the device is larger than medium, redirect the user to the observe page.
     */
    function redirectWrongDeviceSize() {
      if(!$mdMedia('gt-md')) {
        $location.path('/observe');
      }
    }

    /*
     * helper function to find string in URL
     */
    function urlContainsStr(route, text) {
      return route.indexOf(text) > -1;
    }
}]);