'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'ngWebSocket',
  'myApp.main',
  'myApp.example',
  'myApp.info',
  'myApp.version'
]).
config(['$routeProvider', function($routeProvider) {
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

}]);
