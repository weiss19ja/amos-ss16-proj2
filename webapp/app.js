'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'ngWebSocket',
  'ngMaterial' ,
  'myApp.main',
  'myApp.example',
  'myApp.info',
  'myApp.version'
])
.config(function ($mdThemingProvider) {
		$mdThemingProvider.theme('default')
			.primaryPalette('blue',{
				'default': '800',
				'hue-1': '200',
				'hue-2': '600',
				'hue-3': '800'
			})
			.accentPalette('green',{'default': '500'})
			.warnPalette('red');
	})
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.
	when('/main', {
    	templateUrl: 'main/main.html',
    	controller: 'MainCtrl'
 	}).
	when('/info',{
		templateUrl: 'info/info.html',
		controller: 'InfoCtrl'
	}).
	  when('/example', {
	  templateUrl: 'example/example.html',
      controller: 'ExampleCtrl'
  }).
	otherwise({redirectTo: '/main'});

}])
.controller('IndexCtrl', function ($scope, $timeout, $mdSidenav, $mdUtil, $log) {
  $scope.closeSidebar = function () {
     $mdSidenav('sidebar').close();
   };

  $scope.toggleSidebar = $mdUtil.debounce(function () {
     console.log('toggle sidebar');
     $mdSidenav('sidebar').toggle();
   }, 200);
})
;
