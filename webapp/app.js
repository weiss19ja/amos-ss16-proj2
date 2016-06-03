'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'ngWebSocket',
  'ngMaterial',
  'angularCSS',
  'dcbImgFallback',
  'myApp.main',
  'myApp.roverDrive',
  'myApp.roverObserve',
  'myApp.roverService',
  'myApp.logs',
  'myApp.settings',
  'myApp.example',
  'myApp.info',
  'myApp.developer',
  'myApp.version',
  'myApp.dpad',
  'myApp.parkingSensors'
])
    .config(function ($mdThemingProvider) {
      $mdThemingProvider.theme('default')
          .primaryPalette('cyan',{
            'default': '700',
            'hue-1': '400',
            'hue-2': '700',
            'hue-3': '800'
          })
          .accentPalette('grey',{'default': '400'})
          .warnPalette('indigo', {'default': '900'});
    })
  .config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/main', {
      templateUrl: 'main/main.html',
      controller: 'MainCtrl'
    }).when('/drive', {
      templateUrl: 'roverDrive/roverDrive.html',
      controller: 'RoverDriveCtrl'
    }).when('/observe', {
      templateUrl: 'roverObserve/roverObserve.html',
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
    }).when('/example', {
      templateUrl: 'example/example.html',
      controller: 'ExampleCtrl'
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
