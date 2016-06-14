'use strict';

angular.module('myApp.logs', [])
.controller('LogsCtrl',  ['$scope', 'roverService', function($scope, roverService) {

  $scope.entries = [];
  var allEntries = [];
  var lastLogEntry = "";

  $scope.refreshLogEntries = function(clickEvent) {
    roverService.getLoggingEntries(lastLogEntry, function (response) {
      if(response[0] === true) {
        lastLogEntry = response[response.length-1];
        response.shift();
        if($scope.entries.length < 1) {
          $scope.entries = response;
          allEntries = response;
        } else {
          $scope.entries = $scope.entries.concat(response);
          allEntries = allEntries.concat(response);
        }
      }
    })
  };

}]);
