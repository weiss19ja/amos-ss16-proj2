/**
 * This file is part of Mobile Robot Framework.
 * Mobile Robot Framework is free software under the terms of GNU AFFERO GENERAL PUBLIC LICENSE.
 */

'use strict';

/* https://github.com/angular/protractor/blob/master/docs/toc.md */

describe('mobile robot framework app', function() {

  beforeEach(function() {
    browser.get('index.html');
  });

  it('should automatically redirect to /observe when location hash/fragment is empty', function() {
    expect(browser.getLocationAbsUrl()).toMatch("/observe");
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
    expect (mainViewButtons.count()).toBe(2);
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

describe ('Camera and Drive view in portrait mode', function() {

  beforeEach(function() {
    // nexus 5x portrait mode size
    browser.manage().window().setSize(411, 731);
    browser.get('#/drive');
  });

  it('should display the driver card', function() {
    expect(element(by.tagName('drivercard')).isDisplayed()).toBe(true);
  });

  it('should display the camera stream', function() {
    expect(browser.findElement(by.className('md-card-image')).getText()).toBeDefined;
  });

});


describe ('Camera and Drive view in landscape / laptop mode', function() {

  var joystickSwitchInLandscapeMode;
  var dpadInLandscapeMode;
  var joystickInLandscapeMode;

  beforeEach(function() {
    // nexus landscape mode size
    browser.manage().window().setSize(773, 435);
    browser.get('#/drive');
    joystickSwitchInLandscapeMode = element.all(by.tagName('joystick-dpad-switch')).get(0);
    dpadInLandscapeMode = element.all(by.tagName('dpad')).get(0);
    joystickInLandscapeMode = browser.findElement(by.id('zone_joystick_2'));
  });

  it('should not display the driver card', function() {
    expect(element(by.tagName('drivercard')).isDisplayed()).toBe(false);
  });

  it('should display the camera stream', function() {
    expect(browser.findElement(by.className('md-card-image')).getText()).toBeDefined;
  });

  it('should display the joystick when switch is clicked', function() {
    expect(dpadInLandscapeMode.isDisplayed()).toBe(true);
    expect(joystickInLandscapeMode.isDisplayed()).toBe(false);
    joystickSwitchInLandscapeMode.click();
    expect(dpadInLandscapeMode.isDisplayed()).toBe(false);
    expect(joystickInLandscapeMode.isDisplayed()).toBe(true);
  });

});

/**
 * here are made the e2e tests for the drive card too
 */
describe ('Driving only view', function() {

  var dpad;
  var joystick;
  var joystickSwitch;
  var parkingSensors;

  beforeEach(function() {
    browser.manage().window().setSize(773, 435);
    browser.get('#/drive/driveonly');
    parkingSensors = element(by.tagName('parking-sensors'));
    joystickSwitch = element(by.tagName('joystick-dpad-switch'));
    joystick = browser.findElement(by.id('zone_joystick_1'));
    dpad = element(by.tagName('dpad'));

  });

  it('should display the driver card', function() {
    expect(element(by.tagName('drivercard')).isDisplayed()).toBe(true);
  });

  it('should display the joystick when the switch is clicked', function() {
    expect(parkingSensors.isDisplayed()).toBe(true);
    expect(dpad.isDisplayed()).toBe(true);
    expect(joystick.isDisplayed()).toBe(false);
    joystickSwitch.click();
    expect(dpad.isDisplayed()).toBe(false);
    expect(joystick.isDisplayed()).toBe(true);
  });

});

describe ('Emergency stop view', function() {

  beforeEach(function() {
    browser.manage().window().setSize(773, 435);
    browser.get('#/stop');
  });

  it('should display the stop button', function() {
    expect(browser.findElement(by.id('stopView')).getText()).toBeDefined();
  });

});

describe ('Hide views for desktop devices', function() {

  beforeEach(function() {
    browser.manage().window().setSize(1400, 1024);
  });

  it('should hide the "Driving Only" view and redirect to /observe', function() {
    browser.get('#/drive/driveonly');
    expect(browser.getLocationAbsUrl()).toMatch("/observe");
  });

  it('should hide the "Emergency Stop" view and redirect to /observe', function() {
    browser.get('#/stop')
    expect(browser.getLocationAbsUrl()).toMatch("/observe");
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

describe ('Camera Controller view', function() {

  beforeEach(function() {
    browser.manage().window().setSize(773, 435);
    browser.get('#/observe/cameraController');
  });

  it('should display the camera controller view headline', function() {
    expect(element(by.tagName('h3')).getText()).toBe('Camera Controller Mode');
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
    var input = element(by.model('roverState.isKillswitchEnabled'));
    input.click();
    expect(element(by.binding('killswitchText')).getText()).toBe('User interaction with rover: blocked');
    input.click();
    expect(element(by.binding('killswitchText')).getText()).toBe('User interaction with rover: allowed');
  });

  it('should have the same text after page reload', function(){
    var textBefore = element(by.binding('killswitchText')).getText();
    browser.refresh();
    expect(element(by.binding('killswitchText')).getText()).toBe(textBefore);
  });

  it('should display the systems uptime label', function () {
    expect(element(by.binding('systemUpTimeString')).getText()).toBeDefined();
  });

});

describe ('sidebar navigation for smartphones', function() {

  var sidebarItems;
  var sideBarToggleButton;

  beforeEach(function() {
    browser.manage().window().setSize(773, 435);
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


  it('should be displayed', function() {
    expect (sidebarItems.get(2).isDisplayed()).toBe(true);
  });

  it('should have thirteen entries', function() {
    // 12 entries with hidden developer view + desktop view
    expect (sidebarItems.count()).toBe(11);
  });

  it('should redirect to main page when main is clicked', function() {
    expect (sidebarItems.get(0).getText()).toBe('Main');
    sidebarItems.get(0).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/main");
  });

  it('should not display "Rover Master" in sidebar for smartphones', function() {
    expect (sidebarItems.get(1).isDisplayed()).toBe(false);
  });

  it('should be display "Drive Control" in sidebar', function() {
    expect (sidebarItems.get(2).getText()).toBe('Drive Control');
  });

  it('should redirect to /drive when "Camera & Driving" is clicked', function() {
    expect (sidebarItems.get(3).getText()).toBe('Camera & Driving');
    sidebarItems.get(3).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/drive");
  });

  it('should redirect to /drive/driveonly when "Driving only" is clicked', function() {
    expect (sidebarItems.get(4).getText()).toBe('Driving only');
    sidebarItems.get(4).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/drive/driveonly");
  });

  it('should redirect to /stop when "Emergency Stop" is clicked', function() {
    expect (sidebarItems.get(5).getText()).toBe('Emergency Stop');
    sidebarItems.get(5).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/stop");
  });

  it('should display "Camera Control" in sidebar', function() {
    expect (sidebarItems.get(6).getText()).toBe('Camera Control');
  });

  it('should redirect to /observe/cameracontroller page when "Camera Movement" is clicked', function() {
    expect (sidebarItems.get(7).getText()).toBe('Camera Movement');
    sidebarItems.get(7).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/observe/cameraController");
  });

  it('should redirect to /observe page when "Camera View" is clicked', function() {
    expect (sidebarItems.get(8).getText()).toBe('Camera View');
    sidebarItems.get(8).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/observe");
  });

  it('should check if view "Developer Options" exists', function() {
    expect(sidebarItems.get(10).isDisplayed()).toBe(true);
  });

  it('should check if view "About" exists', function() {
    expect(sidebarItems.get(9).isDisplayed()).toBe(true);
    sidebarItems.get(9).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/info");
  });

});

describe ('sidebar navigation for laptops', function() {

  var sidebarItems;
  var sideBarToggleButton;

  beforeEach(function() {
    browser.manage().window().setSize(1400, 1024);
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


  it('should be displayed', function() {
    expect (sidebarItems.get(0).isDisplayed()).toBe(true);
  });

  it('should have also thirteen entries', function() {
    // 12 entries with hidden developer view + desktop rover master view
    expect (sidebarItems.count()).toBe(11);
  });

  it('should display "Rover Master" in sidebar', function() {
    expect (sidebarItems.get(1).isDisplayed()).toBe(true);
  });

  it('should be redirect to /drive/roverMaster when "Rover Master" is clicked', function() {
    expect (sidebarItems.get(1).getText()).toBe('Rover Master');
    sidebarItems.get(1).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/drive/roverMaster");
  });

  it('should display "Drive Control" in sidebar', function() {
    expect (sidebarItems.get(2).getText()).toBe('Drive Control');
  });

  it('should redirect to /drive when "Camera & Driving" is clicked', function() {
    expect (sidebarItems.get(3).getText()).toBe('Camera & Driving');
    sidebarItems.get(3).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/drive");
  });

  it('should not view "Driving only"', function() {
    expect(sidebarItems.get(4).isDisplayed()).toBe(false);
  });

  it('should not view "Emergency Stop"', function() {
    expect(sidebarItems.get(5).isDisplayed()).toBe(false);
  });

  it('should check if view "Developer Options" exists', function() {
    expect(sidebarItems.get(10).isDisplayed()).toBe(true);
  });

  it('should check if view "About" exists', function() {
    expect(sidebarItems.get(9).isDisplayed()).toBe(true);
    sidebarItems.get(9).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/info");
  });

  it('should display "Camera Control" in sidebar', function() {
    expect (sidebarItems.get(6).getText()).toBe('Camera Control');
  });

  it('should redirect to /observe/cameracontroller page when "Camera Movement" is clicked', function() {
    expect (sidebarItems.get(7).getText()).toBe('Camera Movement');
    sidebarItems.get(7).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/observe/cameraController");
  });

  it('should redirect to /observe page when "Camera View" is clicked', function() {
    expect (sidebarItems.get(8).getText()).toBe('Camera View');
    sidebarItems.get(8).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/observe");
  });

});
