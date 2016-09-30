angular
  .module('secretStash')
  .component('projectList', {
    templateUrl: 'project/project-list.html',
    controller: ['$scope', 'stashApiService', 
      function($scope, api) {
        api.getList().then(function(result) {
          $scope.projects = result.data.value;
        });
      }
    ]
  });