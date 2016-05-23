'use strict';

/* https://github.com/angular/protractor/blob/master/docs/toc.md */

describe('mobile robot framework app', function() {

  beforeEach(function() {
    browser.get('index.html');
  });

  it('should automatically redirect to /main when location hash/fragment is empty', function() {
    expect(browser.getLocationAbsUrl()).toMatch("/main");
  });

  it('should have a title', function() {
    expect(browser.getTitle()).toEqual('Mobile Robot Framework Client');
  });

});

describe ('Main view', function() {

  var mainViewButtons;

  beforeEach(function() {
    browser.get('#/main');
    mainViewButtons = element(by.css('.layout-column')).all(by.css('.mainButton'));
  });

  it('should display the main view headline', function() {
    expect(element(by.css('.layout-column')).element(by.tagName('h2')).getText()).toBe('Welcome to MORFeus Webinterface');
  });

  it('should have three buttons', function() {
    expect (mainViewButtons.count()).toBe(3);
  });

  it('should redirect to drive when drive is clicked', function() {
    mainViewButtons.get(0).click();
    expect(browser.getLocationAbsUrl()).toMatch("/drive");
  });

  it('should redirect to observe when observe is clicked', function() {
    mainViewButtons.get(1).click();
    expect(browser.getLocationAbsUrl()).toMatch("/observe");
  });
  
});

describe ('Drive view', function() {

  beforeEach(function() {
    browser.get('#/drive');
  });

  it('should display the drive dpad', function() {
    expect(element(by.cssContainingText('.layout-column', 'Drive')).isDisplayed()).toBe(true);
  });

  it('should display the camera dpad', function() {
    expect(element(by.cssContainingText('.layout-column', 'Camera')).isDisplayed()).toBe(true);
  });

});

describe ('Observe view', function() {

  beforeEach(function() {
    browser.get('#/observe');
  });

  it('should display the observe view headline', function() {
    expect(element(by.tagName('h3')).getText()).toBe('Observer Mode');
  });


});

describe ('settings view', function() {

  beforeEach(function() {
    browser.get('#/settings');
  });

  it('should display the settings view headline', function() {
    // there are two h2 headers in ng-scope --> the second one is the settings view text
    expect(element(by.css('.ng-scope')).all(by.tagName('h2')).get(1).getText()).toBe('Settings View');
  });


});

describe ('sidebar navigation', function() {


  it('should have 7 elements', function() {

    element.all(by.tagName('md-menu-item')).then(function(items) {
      expect(items.length).toBe(7);
      });
    
    var sidebarItems = element(by.css('.md-sidenav-left')).all(by.tagName('md-menu-item'));
    expect (sidebarItems.count()).toBe(7);

    expect (sidebarItems.get(1).isDisplayed()).toBe(false);

    // erst menü ausklappen --> erneut testen is displayed = true
    // dann klicken --> auf jeweiliger seite

    //sidebarItems.get(1).click();
    //expect (sidebarItems.get(1).getText()).toBe('Test');
    //sidebarItems.get(1).by.css

    //expect(browser.getLocationAbsUrl()).toMatch("/main");

  });


});