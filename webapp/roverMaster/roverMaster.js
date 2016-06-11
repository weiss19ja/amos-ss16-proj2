'use strict';

angular.module('myApp.roverMaster', [])
.controller('RoverMasterCtrl', ['roverService', '$scope', '$location', '$mdMedia',
  function(roverService, $scope, $location, $mdMedia) {
    $scope.mjpegStreamURL = $location.protocol() + '://' + $location.host() + ':9000/stream/video.mjpeg';
    $scope.roverState = roverService.roverState;

    console.log('Enter Driver Mode (from roverMaster)');
    /**
     * As soon as the view is opened, we try to register this user as a driver.
     */
    roverService.enterDriverMode();

    $scope.stop = function() {
      roverService.stop();
    };

    $scope.$on('$routeChangeStart', function(event, next, current) {
      console.log('Exit Driver Mode, changing to URL' + next.$$route.originalPath);
      roverService.exitDriverMode();
    });
  }]);