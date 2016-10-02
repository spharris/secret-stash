angular
  .module('secretStash')
  .component('environmentCreateForm', {
    templateUrl: 'environment/environment-create-form.html',
    controller: ['$scope', '$routeParams', '$http', 'stashApiService', 
      function($scope, $routeParams, $http, api) {
        var self = this;
        
        self.roles = [];
        self.groups = [];
        
        var init = function() {
          $http.get('/api/aws/groups').then(function(result) {
            self.groups = result.data.value;
          });
          
          $http.get('/api/aws/roles').then(function(result) {
            self.roles = result.data.value;
          });
        }
        
        /* Functions */
        self.createEnvironment = function() {
          api.putObject(self.environment, $routeParams.projectId).then(
              function(result) {
                self.environment = {};
                self.onCreate();
              }
          );
        };
        
        init();
      }
    ],
    bindings: {
      modalId: '@',
      onCreate: '&'
    }
  });
