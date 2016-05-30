'use strict';

angular.module('myApp.developer', [])
    .controller('DeveloperCtrl', ['$scope', 'roverService', function($scope, roverService) {
        $scope.killswitchText = 'allowed';

        $scope.onChange = function(isKillswitchEnabled) {
            if(isKillswitchEnabled == true){
                $scope.killswitchText = 'blocked';
            }
            else{
                $scope.killswitchText = 'allowed';
            }
            console.log('onChange('+isKillswitchEnabled+')');
            roverService.setBlocked(isKillswitchEnabled);
        };

    }]);
