'use strict';

angular.module('myApp.developer', [])
    .controller('DeveloperCtrl', ['$scope', 'roverService', function($scope, roverService) {
        $scope.killswitchText = 'allowed';
        $scope.killswitch = {
            isEnabled : roverService.killswitchState
        };
        roverService.getKillswitchState();

        $scope.setBlocked = function(roverService, cbState) {
            console.log("setBlocked");
            roverService.setKillswitch(cbState);
        }

        $scope.onChange = function(cbState) {
            console.debug("Killswitch button changed to:" +cbState);
            if(cbState == true){
                $scope.killswitchText = 'blocked';
            }
            else{
                $scope.killswitchText = 'allowed';
            }
            roverService.setKillswitch(cbState);
        };

    }]);
