'use strict';

angular.module('myApp.developer', [])
    .controller('DeveloperCtrl', ['$scope', '$location', 'roverService', function($scope, $location, roverService) {
        // Killswitch
        $scope.killswitchText = 'allowed';
        $scope.roverState = roverService.roverState;
        roverService.getKillswitchState();

        $scope.alertMsgToSend = "";
      
        $scope.entries = [];

        /**
         * not used?
        $scope.setBlocked = function(roverService, cbState) {
            console.log("setBlocked");
            roverService.setKillswitch(cbState);
        };
        */

        // Connected Users
        $scope.connectedUsers = roverService.connectedUsers;
        $scope.blockedUsers = roverService.blockedUsers;


        // change text if switch changes
        $scope.$watch(function($scope) { return $scope.roverState.isKillswitchEnabled },
            function() {
            if($scope.roverState.isKillswitchEnabled){
                $scope.killswitchText = 'blocked';
            }
            else{
                $scope.killswitchText = 'allowed'
            }
        });

        // inform server about change made by the user
        $scope.onChange = function(isKillswitchEnabled) {
            console.debug("Killswitch button changed to:" + isKillswitchEnabled);
            var notificationMessage;
            if(isKillswitchEnabled == true){
                notificationMessage = "All interactions with the rover are blocked";
            }
            else{
                notificationMessage = "Interactions with the rover are allowed";
            }
            roverService.setKillswitch(isKillswitchEnabled, notificationMessage);
        };

      $scope.sendAlertMsg = function () {
        if($scope.alertMsgToSend != ""){
          roverService.sendAlertNotification($scope.alertMsgToSend);
          $scope.alertMsgToSend = "";
        }
      };

      $scope.goToLogEntries = function () {
        $location.path('/logs')
      };

    }]);
