/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

angular.module('myApp.parkingSensors', [])
    .controller('ParkingSensorsController', ['$scope', 'roverService', '$attrs', function($scope, roverService, $attrs) {
      /**
       * Binds the collision information provided by the rover service.
       */
        $scope.collisions = roverService.collisions;
        $scope.headerText = "Collisions";

      /**
       * For a collision state of an edge and a minimum required state that is needed
       * for this "bar" in the collision view to show, tell if this bar is visible.
       *
       * E.g. frontLeft == "Far" and minimum state == "Medium" -> not visible
       * frontLeft == "Close" and minimum state = "Medium" -> visible
       * @param edgeCollisionState the collision state obtained by the sensors.
       * @param minimumRequiredState the minimum required state for this bar to be visible.
       * @returns {boolean} true if this bar is visible give the current collision; else false
       */
        $scope.barVisible = function(edgeCollisionState, minimumRequiredState) {
          if (minimumRequiredState == "Far") {
            if (edgeCollisionState == "Far"
              || edgeCollisionState == "Medium"
              || edgeCollisionState == "Close") {
              return true;
            }
          }
          if (minimumRequiredState == "Medium") {
            if (edgeCollisionState == "Close"
              || edgeCollisionState == "Medium") {
              return true;
            }
          }
          if (minimumRequiredState == "Close") {
            if (edgeCollisionState == "Close") {
              return true;
            }
          }
          return false;
        }
    }])
    .component('parkingSensors', {
        restrict: 'EA',
        templateUrl: 'components/parkingSensors/parkingSensors.html',
        css: 'components/parkingSensors/parkingSensors.css',
        controller: 'ParkingSensorsController'
    });