'use strict';

angular.module('myApp.roverDrive', [])
  .controller('RoverDriveCtrl', ['roverService', '$scope', '$location','$mdMedia', function (roverService, $scope, $location,$mdMedia) {
    $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';
    $scope.roverState = roverService.roverState;
    routeView();
    console.log('Enter Driver Mode');
    roverService.enterDriverMode();

    $scope.stop = function () {
      roverService.stop();
    };

    $scope.$on('$routeChangeStart', function (event, next, current) {
      console.log('Exit Driver Mode, changing to URL: ' + next.$$route.originalPath);
      roverService.exitDriverMode();
    });

    function routeView() {
      if(urlContainsStr('movementandcollsion') || urlContainsStr('stop')){
        if($mdMedia('gt-md')){
          $location.path('/main');
        }
      }
    };

    function urlContainsStr(text){
      if($location.path().indexOf(text) > -1 ){
        return true;
      } else {
        return false;
      }
    };
  }]);
