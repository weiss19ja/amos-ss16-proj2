/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

angular.module('myApp.developer', [])
    .controller('DeveloperCtrl', ['$scope', '$location', 'roverService', 'developerService', function ($scope, $location, roverService, developerService) {
      developerService.setShouldShowDevEntryInSidebar();

      // Killswitch
      $scope.killswitchText = 'allowed';
      $scope.roverState = roverService.roverState;
      $scope.myIp = roverService.myIp;

      if ($location.host().indexOf('osr-amos.cs.fau.de') > -1) {
        // do noting
      } else {
        roverService.getKillswitchState();
        roverService.getMaxSpeedValue();
      }
      
      $scope.alertMsgToSend = "";

      $scope.entries = [];

      $scope.systemUpTimeString = "Please refresh to get the rovers uptime.";
      getSystemUpTime();

      /**
       * Minimum allowed speed multiplier (default 0).
       * @type {number}
       */
      $scope.maxSpeedMin = 0;
      /**
       * Maximum allowed speed multiplier (default 100).
       * @type {number}
       */
      $scope.maxSpeedMax = 100;

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
      $scope.$watch(function ($scope) {
            return $scope.roverState.isKillswitchEnabled
          },
          function () {
            if ($scope.roverState.isKillswitchEnabled) {
              $scope.killswitchText = 'blocked';
            }
            else {
              $scope.killswitchText = 'allowed'
            }
          });

      /**
       * Inform server about change made by the user
       * @param isKillswitchEnabled state to which the killswitch should be changed
       */
      $scope.onChange = function (isKillswitchEnabled) {
        console.debug("Killswitch button changed to:" + isKillswitchEnabled);
        var notificationMessage;
        if (isKillswitchEnabled == true) {
          notificationMessage = "All interactions with the rover are blocked";
        }
        else {
          notificationMessage = "Interactions with the rover are allowed";
        }

        if ($location.host().indexOf('osr-amos.cs.fau.de') > -1) {
          roverService.showAlertNotification('There is no killswitch function available on the osr-amos.cs.fau server.');
        } else {
          roverService.setKillswitch(isKillswitchEnabled, notificationMessage);
        }
      };

      $scope.sendAlertMsg = function () {
        if ($scope.alertMsgToSend != "") {
          roverService.sendAlertNotification($scope.alertMsgToSend);
          $scope.alertMsgToSend = "";
        }
      };

      $scope.refreshSystemUpTime = function (event) {
        getSystemUpTime();
      };

      $scope.goToLogEntries = function () {
        $location.path('/logs')
      };

      /**
       * Block the selected ipAddress
       * In case it's your own ip, an message is
       * displayed that says can't block yourself
       * @param ipAddress the ipAddress to block
       */
      $scope.blockIp = function(ipAddress) {
          if (ipAddress == $scope.myIp.ipAddress) {
              roverService.showAlertNotification('You do not want to block yourself');
              return;
          }
          console.debug("Blocking ip address: " + ipAddress);

        if ($location.host().indexOf('osr-amos.cs.fau.de') > -1) {
          roverService.showAlertNotification('There is no blocking preview available on the osr-amos.cs.fau server.');
        } else {
          roverService.blockIp(ipAddress);
          }
      };


      function getSystemUpTime() {
        if ($location.host().indexOf('osr-amos.cs.fau.de') > -1) {
          $scope.systemUpTimeString = "There is no uptime preview available on the osr-amos.cs.fau server.";
        } else {
          roverService.getSystemUpTime(function (upTimeString) {
            $scope.systemUpTimeString = "Rover uptime: " + upTimeString;
          });
        }
      }

      /**
       * Unlock the selected ipAddress
       * @param ipAddress the ipAddress to block
       */
      $scope.unblockIp = function(ipAddress) {
        console.debug("Unblocking ip address: "+ ipAddress);
        if ($location.host().indexOf('osr-amos.cs.fau.de') > -1) {
          roverService.showAlertNotification('There is no unblocking preview available on the osr-amos.cs.fau server.');
        } else {
          roverService.unblockIp(ipAddress);
        }
      };
      /**
       * Releases the current driver
       */
      $scope.releaseDriver = function(){
        console.debug("Releasing driver");
        if ($location.host().indexOf('osr-amos.cs.fau.de') > -1) {
          roverService.showAlertNotification('There is no releasingDriver functionality available on the osr-amos.cs.fau server.');
        } else {
          roverService.releaseDriver();
        }
      };
      $scope.sendMaxSpeed = function() {
        console.debug("Sending maximum speed setting: " + $scope.roverState.maxSpeedValue);
        if ($location.host().indexOf('osr-amos.cs.fau.de') > -1) {
          roverService.showAlertNotification("There is no maximum speed functionality avaliable on the osr-amos.cs.fau.de server.");
        } else {
          roverService.setMaxSpeedValue(parseInt($scope.roverState.maxSpeedValue));
        }
      };
    }]);
