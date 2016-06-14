'use strict';

angular.module('myApp.logs', [])
.controller('LogsCtrl',  ['$scope', '$anchorScroll', 'roverService', function($scope, $anchorScroll, roverService) {

  $scope.entries = [];
  var allEntries = [];
  var lastLogEntry = "";
  getLogEntriesFromBackend();

  $scope.refreshLogEntries = function(clickEvent) {
    getLogEntriesFromBackend();
  };
  
  $scope.scrollToTag = function (tag) {
    $anchorScroll(tag);
  }

  $scope.goToTop = function () {
    $anchorScroll('top');
  };

  $scope.goToBottom = function () {
    $anchorScroll('bottom');
  }

  function getLogEntriesFromBackend() {
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
  }

}]);
