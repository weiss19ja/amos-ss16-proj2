'use strict';

angular.module('myApp.devOpts', [])
    .controller('DevOptsCtrl', ['$scope', 'roverService', function($scope, roverService) {
        $scope.killswitchText = 'allowed';

        $scope.setBlocked = function(roverService, cbState) {
            console.log("setBlocked");
            roverService.setBlocked(cbState);
        }

        $scope.onChange = function(cbState) {
            if(cbState == true){
                $scope.killswitchText = 'blocked';
            }
            else{
                $scope.killswitchText = 'allowed';
            }
            console.debug("blocked = "+ cbState);
            roverService.setBlocked(cbState);
        };

    }]);
