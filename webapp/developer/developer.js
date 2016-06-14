'use strict';

angular.module('myApp.developer', [])
    .controller('DeveloperCtrl', ['$scope', '$location', 'roverService', function($scope, $location, roverService) {
        // Killswitch
        $scope.killswitchText = 'allowed';
        $scope.killswitch = roverService.killswitch;
        roverService.getKillswitchState();

        $scope.alertMsgToSend = "";
      
        $scope.entries = [];

        $scope.setBlocked = function(roverService, cbState) {
            console.log("setBlocked");
            roverService.setKillswitch(cbState);
        };

        // Connected Users
        $scope.connectedUsers = roverService.connectedUsers;
        $scope.blockedUsers = roverService.blockedUsers;


        // change text if switch changes
        $scope.$watch(function($scope) { return $scope.killswitch.enabled },
            function() {
            if($scope.killswitch.enabled){
                $scope.killswitchText = 'blocked';
            }
            else{
                $scope.killswitchText = 'allowed'
            }
        });

        // inform server about change made by the user
        $scope.onChange = function(cbState) {
            console.debug("Killswitch button changed to:" +cbState);
            var notificationMessage;
            if(cbState == true){
                notificationMessage = "All interactions with the rover are blocked";
            }
            else{
                notificationMessage = "Interactions with the rover are allowed";
            }
            roverService.setKillswitch(cbState, notificationMessage);
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
