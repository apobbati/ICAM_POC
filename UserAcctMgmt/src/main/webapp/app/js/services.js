'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('myApp.services', []).
  value('version', '0.1').
  service('FooService', function() {
  console.log('FooService loaded!');

  return {
    sayHello: function(n) {
      return "Hello " + n;
    }
  };
});
