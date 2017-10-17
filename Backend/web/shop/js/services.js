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
        .factory("CategoriaFactory", function ($resource) {
            return $resource('/rest/categoria/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                findAll: {method: 'GET', isArray: true},
            });
        })
        .factory("ProductoFactory", function ($resource) {
            return $resource('/rest/producto/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                findAll: {method: 'GET', isArray: true},
                byCategoria: {method: 'GET', isArray: true, url: '/rest/producto/by-categoria/:id', params: {id: '@id'}}
            });
        })
        .factory('ClienteFactory', function ($resource) {
            return $resource('/rest/cliente/:id', {id: '@id'}, {
                add: {method: 'POST', isArray: false},
                find: {method: 'GET', isArray: false},
                active: {method: 'PUT', isArray: false, url: '/rest/cliente/active/:id', params: {id: '@id'}}
            });
        })
        .factory('TransaccionFactory', function ($resource) {
            return $resource('/rest/transaccion/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                byCliente: {method: 'GET', isArray: true, url: '/rest/transaccion/cliente/:id', params: {id: '@id'}}
            });
        })
        .factory("DetalleTransaccionFactory", function ($resource) {
            return $resource('/rest/detalle-transaccion/:id', {id: '@id'}, {
                recent: {menthod: 'GET', isArray: true, url: '/rest/detalle-transaccion/recent'},
                byTransaccion: {method: 'GET', isArray: true, url: '/rest/detalle-transaccion/transaccion/:id', params: {id: '@id'}}
            });
        })
        .factory("SessionFactory", function ($resource) {
            return $resource("/rest/client-session/:id", {id: '@id'}, {
                login: {method: 'POST', isArray: false},
                active: {method: 'GET', isArray: false},
                menus: {method: 'GET', isArray: true, url: "/rest/client-session/menus/:id", params: {id: '@id'}}
            });
        })
        .factory("CarritoFactory", function ($resource) {
            return $resource('/rest/carrito/:id', {id: '@id'}, {
                add: {method: 'POST', isArray: false},
                byUser: {method: 'GET', isArray: true},
                delete: {method: 'DELETE', isArray: false},
                deleteAll: {method: 'DELETE', isArray: false, url: '/rest/carrito/all/:id', params: {id: '@id'}},
                checkout: {method: 'PUT', isArray: false}
            });
        });