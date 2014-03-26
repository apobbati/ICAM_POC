'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('MyCtrl1', ['$scope', '$http', '$sce', function($scope, $http, $sce) {
    $scope.localResult = '';

    $scope.remoteResult = '';

    $scope.endpoint = $sce.trustAsResourceUrl('http://externalapp.acorn.ads.example.com:8080/');

    $scope.callLocalService = function() {
      console.log('Calling local service');

      $http.get('/endpoint')
        .success(function(data) {
          $scope.localResult = data;
        })
        .error(function(data) {
          console.error('Error invoking local service!');
        });
    };

    $scope.callRemoteService = function() {
      //var endpoint = 'https://cors-test.appspot.com/test';
      var endpoint = 'http://externalapp.acorn.ads.example.com:8080/endpoint';
      console.log('Calling remote service: ' + endpoint);

      $http.get(endpoint)
        .success(function(data) {
          $scope.remoteResult = data;
        })
        .error(function(data, status, headers, config) {
          console.error('Error invoking remote service: ' + status);
        });
    };
  }])
  .controller('MyCtrl2', ['FooService', function(FooService) {
  }]);
