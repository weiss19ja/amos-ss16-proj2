'use strict';

angular.module('myApp.roverDrive', [])
  .controller('RoverDriveCtrl', ['roverService', '$scope', '$location','$mdMedia', function (roverService, $scope, $location,$mdMedia) {
    $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';
    $scope.roverState = roverService.roverState;

    routeView();

    if(!urlContainsStr($location.path(), 'stop')){
      console.log('Enter Driver Mode');
      roverService.enterDriverMode();
    }
    
    /*
     * The stop function to stop the rover movement
     * UI element is only visible for driver
     */
    $scope.stop = function () {
      roverService.stop();
    };

    /*
     * when route changes we exit the driver mode
     * except for changing to another driver view --> i.e. /drive to /drive/driveonly
     */
    $scope.$on('$routeChangeStart', function (event, next, current) {
      var nextRoutePath = next.$$route.originalPath;
      
      if(!(urlContainsStr(nextRoutePath, 'drive') || urlContainsStr(nextRoutePath, 'roverMaster'))) {
        console.log('Exit Driver Mode, changing to URL: ' + nextRoutePath);
        roverService.exitDriverMode();
      }
    });

    /*
     * route to /main when view should only be visible to smartphone (not greater than md)
     */
    function routeView() {
      if(urlContainsStr($location.path(), 'driveonly')){
        if($mdMedia('gt-md')){
          $location.path('/main');
        }
      }
    };

    /*
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
