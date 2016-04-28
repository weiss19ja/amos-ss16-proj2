'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'myApp.main',
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
	otherwise({redirectTo: '/main'});

}]);
