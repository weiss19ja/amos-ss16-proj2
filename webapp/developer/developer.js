'use strict';

angular.module('myApp.developer', [])
    .controller('DeveloperCtrl', ['$scope', 'roverService', function($scope, roverService) {
        $scope.killswitchText = 'allowed';
        $scope.killswitch = roverService.killswitch;
        roverService.getKillswitchState();

        $scope.alertMsgToSend = "";

        $scope.setBlocked = function(roverService, cbState) {
            console.log("setBlocked");
            roverService.setKillswitch(cbState);
        }


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
            roverService.setKillswitch(cbState);
        };

      $scope.sendAlertMsg = function () {
        if($scope.alertMsgToSend != ""){
          console.log("Send alert notification with text: "+$scope.alertMsgToSend);
          $scope.alertMsgToSend = "";
        }
      }

    }]);
