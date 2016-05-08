'use strict';

describe('myApp.camera module', function() {

  var scope;
  beforeEach(module('myApp.camera'));

  describe('camera controller', function(){

    it('should ....', inject(function($controller, $rootScope) {
      scope = $rootScope.$new();
      //spec body
      var cameraCtrl = $controller('CameraCtrl', {$scope: scope} );
      expect(cameraCtrl).toBeDefined();
    }));

  });
});
