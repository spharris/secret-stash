angular
	.module('stashApi', [])
	.factory('stashApiService', ['$http', function($http) {
		var ApiService = { 
			http: $http,
			apiBase: '/api'
		}

		/**
		 * Create a new object in the Secret Stash API
		 */
		ApiService.putObject = function(data, projectId, environmentId) {
			return this.http.put(this.createListUrl(projectId, environmentId), data);
		};
		
		/**
		 * Return a single object from the Secret Stash API
		 */
		ApiService.getObject = function(projectId, environmentId, secretId, params) {
			return this.http.get(this.createObjectUrl(projectId, environmentId, secretId),
			    {params: params});
		};
		
		/**
		 * Delete a single object from the Secret Stash API
		 */
		ApiService.deleteObject = function(projectId, environmentId, secretId) {
			return this.http.delete(this.createObjectUrl(projectId, environmentId, secretId));
		};
		
		/**
		 * Return a list of objects from the Secret Stash API
		 */
		ApiService.getList = function(projectId, environmentId) {
			return this.http.get(this.createListUrl(projectId, environmentId));
		};

		ApiService.createObjectUrl = function(projectId, environmentId, secretId) {
		  var url = this.apiBase + '/projects/';
		  
		  if (projectId) {
		    url = url + projectId;
		  } else {
		    throw 'projectId is required';
		  }
		  
		  if (environmentId) {
		    url = url + '/environments/' + environmentId;
		  }
		  
		  if (secretId) {
		    url = url + '/secrets/' + secretId;
		  }
		  
		  return url;
		}
		
		ApiService.createListUrl = function(projectId, environmentId) {
		  var url = this.apiBase + '/projects';
		  
		  if (projectId) {
		    url = url + '/' + projectId + '/environments';
		  }
		  
		  if (environmentId) {
		    url = url + '/' + environmentId + '/secrets'
		  }
		  
		  return url;
		}
		
		return ApiService;
	}]);
