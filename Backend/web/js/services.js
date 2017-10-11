'use strict';
angular.module('doorman.services', [])
        /*Objetos con funcionalidad especial - inicio*/
        .factory("notify", ['$rootScope', '$timeout', function ($scope, $timeout) {
                var currentMessage = {};
                var currentTimeout = false;
                return {
                    setMessage: function (message) {
                        currentMessage = message;
                        $scope.showNotify = true;
                        if (currentTimeout)
                            $timeout.cancel(currentTimeout);
                        currentTimeout = $timeout(function () {
                            $scope.showNotify = false;
                            currentTimeout = false;
                        }, 10000);
                    },
                    getMessage: function () {
                        return currentMessage;
                    }
                };
            }])
        .factory("Inteceptor", ['$q', '$location', '$rootScope', '$window', '$timeout', '$cookieStore', 'notify', function ($q, $location, $scope, $window, $timeout, $cookieStore, notify) {
                return{
                    request: function (config) {
                        $scope.loading = true;
                        return config;
                    },
                    response: function (response) {
                        $scope.loading = false;
                        return response || $q.when(response);
                    },
                    responseError: function (rejection) {
                        $scope.loading = false;
                        if (rejection.status === 403) {
                            notify.setMessage({message: 'No tienes acceso a esta parte. Si crees que es un error, comunicate con el administrador.', type: 'alert-warning'});
                        } else if (rejection.status === 500) {
                            notify.setMessage({message: 'Ups! Hubo un problema, intentalo de nuevo en unos minutos', type: 'alert-danger'});
                        }
                        return $q.reject(rejection);
                    }
                }
            }])
        .factory("UsuarioFactory", function ($resource) {
            return $resource('rest/usuario/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                findAll: {method: 'GET', isArray: true},
                add: {method: 'POST', isArray: false},
                edit: {method: 'PUT', isArray: false},
                delete: {method: 'DELETE', isArray: false},
                roles: {method: 'GET', isArray: true, url: 'rest/usuario/roles/:id', params: {id: '@id'}},
                setRoles: {method: 'PUT', isArray: false, url: 'rest/usuario/roles/:id', params: {id: '@id'}}
            });
        })
        .factory("RolFactory", function ($resource) {
            return $resource('rest/rol/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                findAll: {method: 'GET', isArray: true},
                add: {method: 'POST', isArray: false},
                edit: {method: 'PUT', isArray: false},
                delete: {method: 'DELETE', isArray: false},
                menus: {method: 'GET', isArray: true, url: 'rest/rol/menus/:id', params: {id: '@id'}},
                setMenus: {method: 'PUT', isArray: false, url: 'rest/rol/menus/:id', params: {id: '@id'}}
            });
        })
        .factory("MenuFactory", function ($resource) {
            return $resource('rest/menu/:id', {id: '@id'}, {
                findAll: {method: 'GET', isArray: true},
            });
        })
        .factory("SessionFactory", function ($resource) {
            return $resource("rest/session/:id", {id: '@id'}, {
                login: {method: 'POST', isArray: false},
                active: {method: 'GET', isArray: false},
                menus: {method: 'GET', isArray: true, url: "rest/session/menus"}
            });
        });