'use strict';

angular.module('myApp.developer', [])
    .controller('DeveloperCtrl', ['$scope', 'roverService', function($scope, roverService) {
        $scope.killswitchText = 'allowed';
        $scope.killswitch = {
          isEnabled : roverService.killswitchState
        };
        roverService.getKillswitchState();

        $scope.onChange = function(killswitchEnabled) {
            if(killswitchEnabled == true){
                $scope.killswitchText = 'blocked';
            }
            else{
                $scope.killswitchText = 'allowed';
            }
            console.log('Killswitch: onChange('+killswitchEnabled+')');
            // roverService.setKillswitch(killswitchEnabled);
        };

    }]);
