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
    expect(element(by.cssContainingText('.layout-column', 'Driving')).isDisplayed()).toBe(true);
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

describe ('developer view', function() {

  beforeEach(function() {
    browser.get('#/developer');
  });

  it('should display the developer view headline', function() {
    // there are two h2 headers in ng-scope --> the second one is the settings view text
    expect(element(by.css('.ng-scope')).all(by.tagName('h2')).get(1).getText()).toBe('Developer Options');
  });

  it('should display the killswitch with default text "allowed"', function() {
    expect(element(by.binding('killswitchText')).getText()).toBe('User interaction with rover: allowed');
  });

  it('should click the killswitch and text should be "blocked"', function() {
    var input = element(by.model('killswitch.enabled'));
    input.click();
    expect(element(by.binding('killswitchText')).getText()).toBe('User interaction with rover: blocked');
    input.click();
    expect(element(by.binding('killswitchText')).getText()).toBe('User interaction with rover: allowed');
  });

  it('should have the same text after page reload', function(){
    var input = element(by.model('killswitch.enabled'));
    var textBefore = element(by.binding('killswitchText')).getText();
    browser.refresh();
    expect(element(by.binding('killswitchText')).getText()).toBe(textBefore);
  });

  xit('should show a notification to all clients when killswitch is triggered', function() {
    var input = element(by.model('killswitch.enabled'));
    var notification = element(by.css('md-toast'));
    browser.wait(function(){
      return browser.isElementPresent(notification);
    });
    input.click();
    expect(notification.getText()).toBe('All interactions with the rover are blocked');
    input.click();
    expect(notification.getText()).toBe('Interactions with the rover are allowed');
  });


});

describe ('sidebar navigation', function() {

  var sidebarItems;
  var sideBarToggleButton;

  beforeEach(function() {
    browser.get('#/main');
    sidebarItems = element(by.css('.md-sidenav-left')).all(by.tagName('md-menu-item'));
    sidebarItems.get(1).isDisplayed().then(function(visible) {
      if (! visible)
      {
        sideBarToggleButton = element(by.css('.hide-gt-md'));
        sideBarToggleButton.click();
      }
    });
  });


  it('should have seven entries', function() {
    expect (sidebarItems.count()).toBe(8);
  });


  it('should be displayed', function() {
    expect (sidebarItems.get(1).isDisplayed()).toBe(true);
  });

  it('should be redirect to main page when driver is Main', function() {
    expect (sidebarItems.get(0).getText()).toBe('Main');
    sidebarItems.get(0).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/main");
  });

  it('should be redirect to drive page when driver is clicked', function() {
    expect (sidebarItems.get(1).getText()).toBe('Driver');
    sidebarItems.get(1).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/drive");
  });

  it('should be redirect to observe page when Observer is clicked', function() {
    expect (sidebarItems.get(2).getText()).toBe('Observer');
    sidebarItems.get(2).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/observe");
  });

  it('should be redirect to settings page when Settings is clicked', function() {
    expect (sidebarItems.get(3).getText()).toBe('Settings');
    sidebarItems.get(3).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/settings");
  });


});
