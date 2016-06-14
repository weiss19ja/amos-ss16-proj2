'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'ngWebSocket',
  'ngMaterial',
  'angularCSS',
  'dcbImgFallback',
  'myApp.main',
  'myApp.roverMaster',
  'myApp.roverDrive',
  'myApp.roverObserve',
  'myApp.roverService',
  'myApp.logs',
  'myApp.settings',
  'myApp.info',
  'myApp.developer',
  'myApp.version',
  'myApp.dpad',
  'myApp.parkingSensors'
])
  .config(function ($mdThemingProvider) {
    $mdThemingProvider.theme('default')
      .primaryPalette('blue', {
        'default': '800',
        'hue-1': '200',
        'hue-2': '600',
        'hue-3': '800'
      })
      .accentPalette('green', {'default': '500'})
      .warnPalette('red');
    
    $mdThemingProvider.theme("success-toast");
    $mdThemingProvider.theme("alert-toast");
    $mdThemingProvider.theme("error-toast");
  })
  .config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/main', {
      templateUrl: 'main/main.html',
      controller: 'MainCtrl'
    }).when('/drive', {
      templateUrl: 'roverDrive/roverDrive.html',
      controller: 'RoverDriveCtrl'
    }).when('/drive/driveonly', {
      templateUrl: 'roverDrive/roverDriveOnly.html',
      controller: 'RoverDriveCtrl'
    }).when('/stop', {
      templateUrl: 'roverDrive/roverDriveStop.html',
      controller: 'RoverDriveCtrl'
    }).when('/roverMaster', {
      templateUrl: 'roverMaster/roverMaster.html',
      controller: 'RoverMasterCtrl'
    }).when('/observe', {
      templateUrl: 'roverObserve/roverObserve.html',
      controller: 'RoverObserveCtrl'
    }).when('/observe/cameraController', {
      templateUrl: 'roverObserve/roverCameraController.html',
      controller: 'RoverObserveCtrl'
    }).when('/settings', {
      templateUrl: 'settings/settings.html',
      controller: 'SettingsCtrl'
    }).when('/logs', {
      templateUrl: 'logs/logs.html',
      controller: 'LogsCtrl'
    }).when('/info', {
      templateUrl: 'info/info.html',
      controller: 'InfoCtrl'
    }).when('/developer', {
      templateUrl: 'developer/developer.html',
      controller: 'DeveloperCtrl'
    }).otherwise({redirectTo: '/main'});

  }])
  .controller('SidebarCtrl', function ($scope, $timeout, $mdSidenav, $mdUtil, $log) {
    $scope.closeSidebar = function () {
      $mdSidenav('sidebar').close();
    };

    $scope.toggleSidebar = $mdUtil.debounce(function () {
      console.log('toggle sidebar');
      $mdSidenav('sidebar').toggle();
    }, 200);
  })
;
