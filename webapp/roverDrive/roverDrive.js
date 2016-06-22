/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

angular.module('myApp.roverDrive', [])
  .controller('RoverDriveCtrl', ['roverService', '$scope', '$location','$mdMedia', function (roverService, $scope, $location,$mdMedia) {
    $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';
    $scope.roverState = roverService.roverState;
    $scope.showJoystick = false;

    routeView();

    // do not enter driver mode when accessing the amazing stop view
    if(!urlContainsStr($location.path(), 'stop')){
      console.log('Enter Driver Mode');
      roverService.enterDriverMode();
    }
    
    /**
     * The stop function to stop the rover movement
     * UI element is only visible for driver
     */
    $scope.stop = function () {
      roverService.stop();
    };

    /**
     * when route changes we exit the driver mode
     * except for changing to another driver view --> i.e. /drive to /drive/driveonly
     */
    $scope.$on('$routeChangeStart', function (event, next, current) {
      var nextRoutePath = next.$$route.originalPath;

      // drive and roverMaster target pages are valid for drive mode - do not log out user here
      if(!urlContainsStr(nextRoutePath, 'drive')) {
        console.log('Exit Driver Mode, changing to URL: ' + nextRoutePath);
        roverService.exitDriverMode();
      }
    });

    /**
     * route to /main when view should only be visible to smartphone (not greater than md)
     */
    function routeView() {
      if(urlContainsStr($location.path(), 'driveonly') || urlContainsStr($location.path(), 'stop')){
        if($mdMedia('gt-md')){
          $location.path('/main');
        }
      }
    };

    /**
     * helper function to find string in URL
     */
    function urlContainsStr(route, text){
      if(route.indexOf(text) > -1 ){
        return true;
      } else {
        return false;
      }
    };
  }]);
