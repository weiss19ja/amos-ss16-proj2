/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
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
  'myApp.info',
  'myApp.developer',
  'myApp.version',
  'myApp.dpad',
  'myApp.joystickService',
  'myApp.joystickDpadSwitch',
  'myApp.driverCard',
  'myApp.parkingSensors',
  'myApp.developerService'
])
  .config(function ($mdThemingProvider) {
    $mdThemingProvider.theme('default')
      .primaryPalette('cyan',{
        'default': '700',
        'hue-1': '200',
        'hue-2': '600',
        'hue-3': '800'
      })
      .accentPalette('indigo',{'default': '800'})
      .warnPalette('red', {'default': '800'});

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
    }).when('/drive/roverMaster', {
      templateUrl: 'roverMaster/roverMaster.html',
      controller: 'RoverMasterCtrl'
    }).when('/stop', {
      templateUrl: 'roverDrive/roverStop.html',
      controller: 'RoverDriveCtrl'
    }).when('/observe', {
      templateUrl: 'roverObserve/roverObserve.html',
      controller: 'RoverObserveCtrl'
    }).when('/observe/cameraController', {
      templateUrl: 'roverObserve/roverCameraController.html',
      controller: 'RoverObserveCtrl'
    }).when('/logs', {
      templateUrl: 'logs/logs.html',
      controller: 'LogsCtrl'
    }).when('/info', {
      templateUrl: 'info/info.html',
      controller: 'InfoCtrl'
    }).when('/developer', {
      templateUrl: 'developer/developer.html',
      controller: 'DeveloperCtrl'
    }).otherwise({redirectTo: '/observe'});

  }])
  .controller('SidebarCtrl', function ($scope, $timeout, $mdSidenav, $mdUtil, developerService, roverService) {
    $scope.devService = developerService;
    $scope.roverState = roverService.roverState;

    $scope.closeSidebar = function () {
      $mdSidenav('sidebar').close();
    };

    $scope.toggleSidebar = $mdUtil.debounce(function () {
      console.log('toggle sidebar');
      $mdSidenav('sidebar').toggle();
    }, 200);

  })
;
