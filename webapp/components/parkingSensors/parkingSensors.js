'use strict';

angular.module('myApp.parkingSensors', [])
.controller('ParkingSensorsController', ['$scope', 'roverService', '$attrs', function($scope, roverService, $attrs) {


}])
.component('parkingSensors', {
    restrict: 'EA',
    templateUrl: 'components/parkingSensors/parkingSensors.html',
    css: 'components/parkingSensors/parkingSensors.css',
    controller: 'ParkingSensorsController'
});