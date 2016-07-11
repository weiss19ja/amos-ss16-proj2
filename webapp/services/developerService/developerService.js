angular.module("myApp.developerService", [])
    .factory("developerService", function () {
      var showDeveloperEntry = false;
      var service = {
        getShouldShowDevEntryInSidebar: getShouldShowDevEntryInSidebar,
        setShouldShowDevEntryInSidebar: setShouldShowDevEntryInSidebar
      };
      return service;

      function getShouldShowDevEntryInSidebar() {
        return showDeveloperEntry;
      }

      function setShouldShowDevEntryInSidebar() {
        showDeveloperEntry = true;
      }
    });