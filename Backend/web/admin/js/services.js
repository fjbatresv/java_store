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
            return $resource('/rest/usuario/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                findAll: {method: 'GET', isArray: true},
                add: {method: 'POST', isArray: false},
                edit: {method: 'PUT', isArray: false},
                delete: {method: 'DELETE', isArray: false},
                roles: {method: 'GET', isArray: true, url: '/rest/usuario/roles/:id', params: {id: '@id'}},
                setRoles: {method: 'PUT', isArray: false, url: '/rest/usuario/roles/:id', params: {id: '@id'}}
            });
        })
        .factory("RolFactory", function ($resource) {
            return $resource('/rest/rol/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                findAll: {method: 'GET', isArray: true},
                add: {method: 'POST', isArray: false},
                edit: {method: 'PUT', isArray: false},
                delete: {method: 'DELETE', isArray: false},
                menus: {method: 'GET', isArray: true, url: '/rest/rol/menus/:id', params: {id: '@id'}},
                setMenus: {method: 'PUT', isArray: false, url: '/rest/rol/menus/:id', params: {id: '@id'}}
            });
        })
        .factory("CategoriaFactory", function ($resource) {
            return $resource('/rest/categoria/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                findAll: {method: 'GET', isArray: true},
                add: {method: 'POST', isArray: false},
                edit: {method: 'PUT', isArray: false},
                delete: {method: 'DELETE', isArray: false},
            });
        })
        .factory("ProductoFactory", function ($resource) {
            return $resource('/rest/producto/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                findAll: {method: 'GET', isArray: true},
                add: {method: 'POST', isArray: false},
                edit: {method: 'PUT', isArray: false},
                delete: {method: 'DELETE', isArray: false},
                categorias: {method: 'GET', isArray: true, url: '/rest/producto/categorias/:id', params: {id: '@id'}},
                setCategorias: {method: 'PUT', isArray: false, url: '/rest/producto/categorias/:id', params: {id: '@id'}},
                existencia: {method: 'POST', isArray: false, url: '/rest/producto/existencia/:id', params: {id: '@id'}}
            });
        })
        .factory("ExistenciaFactory", function ($resource) {
            return $resource('/rest/existencia/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                findAll: {method: 'GET', isArray: true},
                producto: {method: 'GET', isArray: true, url: '/rest/existencia/producto/:id', params: {id: '@id'}}
            });
        })
        .factory('ClienteFactory', function ($resource) {
            return $resource('/rest/cliente/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                findAll: {method: 'GET', isArray: true},
                active: {method: 'PUT', isArray: false, url: '/rest/cliente/active/:id', params: {id: '@id'}}
            });
        })
        .factory('TransaccionFactory', function ($resource) {
            return $resource('/rest/transaccion/:id', {id: '@id'}, {
                find: {mehtod: 'GET', isArray: false},
                findAll: {method: 'GET', isArray: true},
                byCliente: {method: 'GET', isArray: true, url: '/rest/transaccion/cliente/:id', params: {id: '@id'}},
                cobrar: {method: 'PUT', isArray: false, url: '/rest/transaccion/cobro/:id/:uid', params: {id: '@id', uid: '@uid'}},
                enviar: {method: 'PUT', isArray: false, url: '/rest/transaccion/enviar/:id/:uid', params: {id: '@id', uid: '@uid'}},
                entrega: {method: 'PUT', isArray: false, url: '/rest/transaccion/entrega/:id/:uid', params: {id: '@id', uid: '@uid'}}
            });
        })
        .factory("DetalleTransaccionFactory", function ($resource) {
            return $resource('/rest/detalle-transaccion/:id', {id: '@id'}, {
                byTransaccion: {method: 'GET', isArray: true, url: '/rest/detalle-transaccion/transaccion/:id', params: {id: '@id'}}
            });
        })
        .factory("MenuFactory", function ($resource) {
            return $resource('/rest/menu/:id', {id: '@id'}, {
                findAll: {method: 'GET', isArray: true},
            });
        })
        .factory("SessionFactory", function ($resource) {
            return $resource("/rest/session/:id", {id: '@id'}, {
                login: {method: 'POST', isArray: false},
                active: {method: 'GET', isArray: false},
                menus: {method: 'GET', isArray: true, url: "/rest/session/menus/:id", params: {id: '@id'}}
            });
        });