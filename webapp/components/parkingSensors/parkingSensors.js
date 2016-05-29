'use strict';

angular.module('myApp.parkingSensors', [])
    .controller('ParkingSensorsController', ['$scope', 'roverService', '$attrs', function($scope, roverService, $attrs) {
        $scope.collisions = {
            frontLeft: true,
            frontRight: false,
            backLeft: true,
            backRight: false
        };

        $scope.toggle = function() {
            $scope.collisions = {
                frontLeft: !$scope.collisions.frontLeft,
                frontRight: !$scope.collisions.frontRight,
                backLeft: !$scope.collisions.backLeft,
                backRight: !$scope.collisions.backRight
            };
        };
    }])
    .component('parkingSensors', {
        restrict: 'EA',
        templateUrl: 'components/parkingSensors/parkingSensors.html',
        css: 'components/parkingSensors/parkingSensors.css',
        controller: 'ParkingSensorsController'
    });