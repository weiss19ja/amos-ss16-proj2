/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
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
