'use strict';

angular.module('myApp.roverDrive', [])
  .controller('RoverDriveCtrl', ['roverService', '$scope', '$location', function (roverService, $scope, $location) {
    $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';
    $scope.roverState = roverService.roverState;

    console.log('Enter Driver Mode');
    roverService.enterDriverMode();

    $scope.stop = function () {
      roverService.stop();
    };

    $scope.$on('$routeChangeStart', function (event, next, current) {
      console.log('Exit Driver Mode, changing to URL: ' + next.$$route.originalPath);
      roverService.exitDriverMode();
    });
  }]);
