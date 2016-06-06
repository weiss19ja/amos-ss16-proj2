'use strict';

describe('myApp.info module', function() {

  beforeEach(module('myApp.info'));

  describe('info controller', function(){
    var scope, infoCtrl;
    beforeEach(inject(function($rootScope, $controller) {
      scope = $rootScope.$new();
      infoCtrl = $controller('InfoCtrl', {$scope: scope});
    }));

    it('should define te info controller', inject(function($controller) {
      //spec body
      expect(infoCtrl).toBeDefined();
    }));

  });
});
