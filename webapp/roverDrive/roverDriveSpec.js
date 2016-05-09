'use strict';

describe('myApp.roverDrive module', function() {

  var scope;
  beforeEach(module('myApp.roverDrive'));

  describe('roverDrive controller', function(){

    it('should ....', inject(function($controller, $rootScope) {
      //spec body
      var roverDriveCtrl = $controller('RoverDriveCtrl', {$scope: scope});
      expect(roverDriveCtrl).toBeDefined();
    }));

  });
});
