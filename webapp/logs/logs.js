/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

angular.module('myApp.logs', [])
.controller('LogsCtrl',  ['$scope', '$location', '$anchorScroll', 'roverService', function($scope, $location, $anchorScroll, roverService) {

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
    if($location.host().indexOf('osr-amos.cs.fau.de') > -1) {
      $scope.entries.push("There is no preview available on the osr-amos.cs.fau server.");
    } else {
      roverService.getLoggingEntries(lastLogEntry, function (response) {
        if (response[0] === true) {
          lastLogEntry = response[response.length - 1];
          response.shift();
          if ($scope.entries.length < 1) {
            $scope.entries = response;
            allEntries = response;
          } else {
            $scope.entries = $scope.entries.concat(response);
            allEntries = allEntries.concat(response);
          }
        }
      })
    }
  }

}]);
