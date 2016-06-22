'use strict';

describe('myApp.developerService service', function () {

  var developerService;

  beforeEach(module('myApp.developerService'));

  beforeEach(inject(function (_developerService_) {
    developerService = _developerService_;
  }));

  it('should show developer entry in sidebar equals false', function () {
    expect(developerService.getShouldShowDevEntryInSidebar()).toBe(false);
  });

  if('should show developer entry in sidebar equals true after allowing it', function () {
    developerService.getShouldShowDevEntryInSidebar();
    expect(developerService.getShouldShowDevEntryInSidebar()).toBe(true);
  });


});
