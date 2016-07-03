/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */
'use strict';

angular.module('myApp.roverDrive', [])
    .controller('RoverDriveCtrl', ['roverService', '$scope', '$location', '$mdMedia', 'joystickService', function (roverService, $scope, $location, $mdMedia, joystickService) {
        $scope.mjpegStreamURL = 'http://' + $location.host() + ':9000/stream/video.mjpeg';
        $scope.roverState = roverService.roverState;
        $scope.joystickState = joystickService.joystickState;

        routeView();
        reinitJoystickOnImageLoad();

        /**
         * do not enter driver mode when accessing the amazing stop view
         */
        if (!urlContainsStr($location.path(), 'stop')) {
            console.log('Enter Driver Mode');
            roverService.enterDriverMode();
        }

        /**
         * The stop function to stop the rover movement
         * UI element is only visible for driver
         */
        $scope.stop = function () {
            roverService.stop();
        };

        /**
         * when route changes we exit the driver mode
         * except for changing to another driver view --> i.e. /drive to /drive/driveonly
         */
        $scope.$on('$routeChangeStart', function (event, next, current) {
            var nextRoutePath = next.$$route.originalPath;

            // drive and roverMaster target pages are valid for drive mode - do not log out user here
            if (!urlContainsStr(nextRoutePath, 'drive')) {
                console.log('Exit Driver Mode, changing to URL: ' + nextRoutePath);
                roverService.exitDriverMode();
            }

            if (urlContainsStr(nextRoutePath, 'driveonly')) {
                console.log('init joystick zone 1, changing to URL: ' + nextRoutePath);
                joystickService.initJoystick('zone_joystick_1');
            }
        });

        /**
         * route to /observe when view should only be visible to smartphone (not greater than md)
         */
        function routeView() {
            if (urlContainsStr($location.path(), 'driveonly') || urlContainsStr($location.path(), 'stop')) {
                if ($mdMedia('gt-md')) {
                    $location.path('/observe');
                }
            }
        };

        /**
         * Portrait
         */
        $scope.$watch(function () {
            return $mdMedia('xs');
        }, function () {
            if ($mdMedia('xs') && (!urlContainsStr($location.path(), 'driveonly'))) {
                joystickService.initJoystick('zone_joystick_1');
            }
        });

        /**
         * Lanscape
         */
        $scope.$watch(function () {
            return $mdMedia('gt-xs');
        }, function () {
            if ($mdMedia('gt-xs') && (!urlContainsStr($location.path(), 'driveonly'))) {
                joystickService.initJoystick('zone_joystick_2');
            }
        });

        /**
         * function reinitialized the joystick when stream/fallback image is loaded
         *
         * when the fallback image load is done the coordinates for the joystick component changes and we need to reinitialize (destroy and create)
         * the joystick for the specific zone in the view in order to get the touch function working
         */
        function reinitJoystickOnImageLoad() {
            setTimeout(function () {
                var images = angular.element(document).find('img');
                console.log(images);
                if (images.length > 0) {
                    images[0].onload = function () {
                        console.log('image loaded');
                        if ($mdMedia('xs')) {
                            joystickService.initJoystick('zone_joystick_1');
                        } else {
                            joystickService.initJoystick('zone_joystick_2');
                        }
                    };
                }
            }, 1);
        };


        /**
         * helper function to find string in URL
         */
        function urlContainsStr(route, text) {
            if (route.indexOf(text) > -1) {
                return true;
            } else {
                return false;
            }
        };
    }]);
