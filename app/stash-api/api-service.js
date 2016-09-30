angular
	.module('stashApi', [])
	.factory('stashApiService', ['$http', function($http) {
		var ApiService = { 
			http: $http,
			apiBase: '/api'
		}

		/**
		 * Return a single object from the Secret Stash API
		 */
		ApiService.getObject = function(projectId, environmentId, secretId) {
			return this.http.get(this.createObjectUrl(projectId, environmentId, secretId));
		};
		
		/**
		 * Create a new object in the Secret Stash API
		 */
		ApiService.putObject = function(data, projectId, environmentId) {
			return this.http.put(this.createListUrl(projectId, environmentId), data);
		};
		
		/**
		 * Return a list of objects from the Secret Stash API
		 */
		ApiService.getList = function(projectId, environmentId) {
			return this.http.get(this.createListUrl(projectId, environmentId));
		};

		ApiService.createObjectUrl = function(projectId, environmentId, secretId) {
		  projectId = projectId || null;
		  environmentId = environmentId || null;
		  secretId = secretId || null;

		  var url = this.apiBase + '/projects/';
		  
		  if (projectId != null) {
		    url = url + projectId;
		  } else {
		    throw 'projectId is required';
		  }
		  
		  if (environmentId != null) {
		    url = url + '/environments/' + environmentId;
		  }
		  
		  if (secretId != null) {
		    url = url + '/secrets/' + environmentId;
		  }
		  
		  return url;
		}
		
		ApiService.createListUrl = function(projectId, environmentId) {
		  projectId = projectId || null;
		  environmentId = environmentId || null;

		  var url = this.apiBase + '/projects';
		  
		  if (projectId != null) {
		    url = url + '/' + projectId + '/environments';
		  }
		  
		  if (environmentId != null) {
		    url = url + '/' + environmentId + '/secrets'
		  }
		  
		  return url;
		}
		
		return ApiService;
	}]);
