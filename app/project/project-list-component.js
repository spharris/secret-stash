angular
  .module('secretStash')
  .component('projectList', {
    templateUrl: 'project/project-list.html',
    controller: ['$scope', '$window', 'stashApiService', 
      function($scope, $window, api) {
        var self = this;
        var init = function() {
          self.updateProjectList();
        }
      
        self.updateProjectList = function() {
          api.getList().then(function(result) {
            $scope.projects = result.data.value;
          });
        }
        
        self.deleteProject = function(projectId) {
          if ($window.confirm('Really delete ' + projectId + '?')) {
            api.deleteObject(projectId).then(self.updateProjectList);
          }
        }
        
        init();
      }
    ]
  });