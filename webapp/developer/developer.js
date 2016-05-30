'use strict';

angular.module('myApp.developer', [])
    .controller('DeveloperCtrl', ['$scope', 'roverService', function($scope, roverService) {
        $scope.killswitchText = 'allowed';
        $scope.killswitch = {
          isEnabled : false
        };

        $scope.onChange = function(isKillswitchEnabled) {
            if(isKillswitchEnabled == true){
                $scope.killswitchText = 'blocked';
            }
            else{
                $scope.killswitchText = 'allowed';
            }
            console.log('Killswitch: onChange('+isKillswitchEnabled+')');
            roverService.setBlocked(isKillswitchEnabled);
        };

    }]);
