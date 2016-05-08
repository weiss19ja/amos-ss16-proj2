'use strict';

describe('myApp.logs module', function() {

  beforeEach(module('myApp.logs'));

  describe('info controller', function(){

    it('should ....', inject(function($controller) {
      //spec body
      var logsCtrl = $controller('LogsCtrl');
      expect(logsCtrl).toBeDefined();
    }));

  });
});
