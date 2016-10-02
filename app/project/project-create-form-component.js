angular
  .module('secretStash')
  .component('projectCreateForm', {
    templateUrl: 'project/project-create-form.html',
    controller: ['$scope', '$routeParams', 'stashApiService', 
      function($scope, $routeParams, api) {
        var self = this;
        
        /* Functions */
        self.createProject = function() {
          api.putObject(self.project).then(
              function(result) {
                self.project = {};
                self.onCreate();
              }
          );
        };
      }
    ],
    bindings: {
      modalId: '@',
      onCreate: '&'
    }
  });
