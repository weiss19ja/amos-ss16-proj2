'use strict';

angular.module('myApp.info', [])
.controller('InfoCtrl', ['$scope','$location',function($scope,$location) {

  $scope.hideDownload = true;

  var setDownloadVisibility = function () {
    if($location.host().indexOf('osr-amos.cs.fau.de') > -1){
      $scope.hideDownload = false;
    }
  };

  setDownloadVisibility();

}]);
