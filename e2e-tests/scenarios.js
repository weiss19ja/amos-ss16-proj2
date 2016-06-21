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

describe ('Camera and Drive view', function() {

  beforeEach(function() {
    browser.get('#/drive');
  });

  it('should display the drive dpad', function() {
    expect(element(by.tagName('dpad')).getText()).toBeDefined();
  });
  
  it('should display the parking sensors', function() {
    expect(element(by.tagName('parking-sensors')).getText()).toBeDefined();
  });
  
  it('should display the camera stream', function() {
    expect(browser.findElement(by.className('md-card-image')).getText()).toBeDefined;
  });

});


describe ('Driving only view', function() {

  beforeEach(function() {
    browser.manage().window().setSize(773, 435);
    browser.get('#/drive/driveonly');
  });

  it('should display the drive dpad', function() {
    expect(element(by.tagName('dpad')).getText()).toBeDefined();
  });

  it('should display the parking sensors', function() {
    expect(element(by.tagName('parking-sensors')).getText()).toBeDefined();
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

  it('should hide the "Driving Only" view and redirect to /main', function() {
    browser.get('#/drive/driveonly');
    expect(browser.getLocationAbsUrl()).toMatch("/main");
  });

  it('should hide the "Emergency Stop" view and redirect to /main', function() {
    browser.get('#/stop')
    expect(browser.getLocationAbsUrl()).toMatch("/main");
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
    var input = element(by.model('roverState.isKillswitchEnabled'));
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
    // 13 entries with hidden developer view + desktop view
    expect (sidebarItems.count()).toBe(13);
  });

  it('should redirect to main page when main is clicked', function() {
    expect (sidebarItems.get(0).getText()).toBe('Main');
    sidebarItems.get(0).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/main");
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


  it('should redirect to /settings page when "Settings" is clicked', function() {
    expect (sidebarItems.get(9).getText()).toBe('Settings');
    sidebarItems.get(9).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/settings");
  });

  it('should be redirect to /info when "About" is clicked', function() {
    expect (sidebarItems.get(11).getText()).toBe('About');
    sidebarItems.get(11).click();
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
    // 13 entries with hidden developer view + desktop rover master view
    expect (sidebarItems.count()).toBe(13);
  });

  it('should be display "Rover Master" in sidebar', function() {
    expect (sidebarItems.get(1).isDisplayed()).toBe(true);
  });

  it('should be redirect to /drive/roverMaster when "Rover Master" is clicked', function() {
    expect (sidebarItems.get(1).getText()).toBe('Rover Master');
    sidebarItems.get(1).click();
    browser.refresh();
    expect(browser.getLocationAbsUrl()).toMatch("/drive/roverMaster");
  });

  it('should not view "Camera & Driving"', function() {
    expect(sidebarItems.get(3).isDisplayed()).toBe(false);
  });

  it('should not view "Driving only"', function() {
    expect(sidebarItems.get(3).isDisplayed()).toBe(false);
  });

  it('should not view "Emergency Stop"', function() {
    expect(sidebarItems.get(4).isDisplayed()).toBe(false);
  });



});
