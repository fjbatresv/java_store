'use strict';
angular.module('doorman', ['ngMaterial', 'ngRoute', 'ngCookies', 'ngResource',
    'doorman.services', 'doorman.controllers', 'trNgGrid', 'highcharts-ng', 'ui.grid', 'mdColorPicker',
    'ui.grid.pagination', 'ui.grid.cellNav', 'ui.grid.pinning', 'ui.grid.exporter', 'md.data.table', 'ngSanitize'])
        .config(function ($routeProvider, $httpProvider) {
            $httpProvider.interceptors.push('Inteceptor');
            $routeProvider
                    .when('/login', {
                        controller: 'LoginController as ctr',
                        templateUrl: 'views/login.html',
                    })
                    .when('/catalogos', {
                        controller: 'CatalogosController as ctr',
                        templateUrl: 'views/seguridad/opciones.html',
                    })
                    .when('/procesos', {
                        controller: 'ProcesosController as ctr',
                        templateUrl: 'views/seguridad/opciones.html',
                    })
                    .when('/procesos/transacciones', {
                        controller: 'TransaccionListController as ctr',
                        templateUrl: 'views/transaccion/list.html',
                    })
                    .when('/procesos/transacciones/detalle/:id', {
                        controller: 'TransaccionDetalleController as ctr',
                        templateUrl: 'views/transaccion/form.html',
                    })
                    .when('/procesos/clientes', {
                        controller: 'ClienteListController as ctr',
                        templateUrl: 'views/cliente/list.html',
                    })
                    .when('/procesos/clientes/transacciones/:id', {
                        controller: 'ClienteTransaccionController as ctr',
                        templateUrl: 'views/cliente/transacciones.html',
                    })

                    .when('/catalogos/categorias', {
                        controller: 'CategoriaListController as ctr',
                        templateUrl: 'views/categoria/list.html',
                    })
                    .when('/catalogos/categorias/nuevo', {
                        controller: 'CategoriaAddController as ctr',
                        templateUrl: 'views/categoria/form.html',
                    })
                    .when('/catalogos/categorias/editar/:id', {
                        controller: 'CategoriaEditController as ctr',
                        templateUrl: 'views/categoria/form.html',
                    })
                    .when('/catalogos/productos', {
                        controller: 'ProductoListController as ctr',
                        templateUrl: 'views/producto/list.html',
                    })
                    .when('/catalogos/productos/nuevo', {
                        controller: 'ProductoAddController as ctr',
                        templateUrl: 'views/producto/form.html',
                    })
                    .when('/catalogos/productos/editar/:id', {
                        controller: 'ProductoEditController as ctr',
                        templateUrl: 'views/producto/form.html',
                    })
                    .when('/catalogos/productos/categorias/:id', {
                        controller: 'ProductoCategoriasController as ctr',
                        templateUrl: 'views/producto/categorias.html'
                    })
                    .when('/catalogos/productos/existencia/:id', {
                        controller: 'ProductoExistenciaController as ctr',
                        templateUrl: 'views/producto/existencia.html'
                    })
                    .when('/seguridad', {
                        controller: 'SeguridadController as ctr',
                        templateUrl: 'views/seguridad/opciones.html',
                    })
                    .when('/seguridad/usuarios', {
                        controller: 'UsuarioListController as ctr',
                        templateUrl: 'views/usuario/list.html'
                    })
                    .when('/seguridad/usuarios/nuevo', {
                        controller: 'UsuarioAddController as ctr',
                        templateUrl: 'views/usuario/form.html'
                    })
                    .when('/seguridad/usuarios/editar/:id', {
                        controller: 'UsuarioEditController as ctr',
                        templateUrl: 'views/usuario/form.html'
                    })
                    .when('/seguridad/usuarios/roles/:id', {
                        controller: 'UsuarioRolEditController as ctr',
                        templateUrl: 'views/usuario/roles.html'
                    })
                    .when('/seguridad/roles', {
                        controller: 'RolListController as ctr',
                        templateUrl: 'views/rol/list.html'
                    })
                    .when('/seguridad/roles/nuevo', {
                        controller: 'RolAddController as ctr',
                        templateUrl: 'views/rol/form.html'
                    })
                    .when('/seguridad/roles/editar/:id', {
                        controller: 'RolEditController as ctr',
                        templateUrl: 'views/rol/form.html'
                    })
                    .when('/seguridad/roles/menus/:id', {
                        controller: 'RolMenuEditController as ctr',
                        templateUrl: 'views/rol/menus.html'
                    })
                    .when('/', {
                        controller: 'MainController as ctr',
                        templateUrl: 'views/main.html',
                    })
                    .otherwise({
                        redirectTo: '/login'
                    });
        })
        .directive('goBack', function ($window) {
            return function ($scope, $element) {
                $element.on('click', function () {
                    window.history.back();
                })
            }
        })
        .controller("ToolBarController", function ($rootScope, $location, $cookies, $scope, $mdBottomSheet, $mdSidenav,
                $mdDialog, $mdToast, SessionFactory) {
            if ($cookies.get('user') !== null && $cookies.get('user') !== undefined) {
                console.log($cookies.get('user'));
                SessionFactory.active({id: $cookies.get('user')}, function (res) {
                    $rootScope.user = res;
                });
            } else {
                $rootScope.user = null;
                if ($location.path() !== '/login') {
                    $location.path('/login');
                    window.location.reload();
                }
            }
            $scope.toggleSidenav = function (menuId) {
                $mdSidenav(menuId).toggle();
            }
            $scope.showListBottomSheet = function () {
                $mdBottomSheet.show({
                    template: '<md-bottom-sheet class="md-grid" layout="column">' +
                            '<div ng-cloak>' +
                            '<md-list flex layout="row" layout-align="center center">' +
                            '<md-list-item ng-repeat="item in items">' +
                            '<div>' +
                            '<md-button class="md-grid-item-content" ng-click="listItemClick($index)">' +
                            '<i class="material-icons">{{item.icon}}</i>' +
                            '<div class="md-grid-text"> {{ item.name }} </div>' +
                            '</md-button>' +
                            '</div>' +
                            '</md-list-item>' +
                            '</md-list>' +
                            '</div>' +
                            '</md-bottom-sheet>',
                    controller: 'SettingsController',
                    clickOutsideToClose: true
                }).then(function (clickedItem) {
                    if ('Cerrar sesión' === clickedItem['name']) {
                        $cookies.remove("user");
                        $location.path('/login');
                        window.location.reload();
                    } else if ('Cambiar clave' === clickedItem['name']) {
                        $location.path('/mi-clave');
                    }
                });
            }
        })
        .controller("SidenavController", function ($cookies, $scope, $mdBottomSheet, $mdSidenav, $mdDialog, $mdToast, SessionFactory) {
            $scope.rolMenu = {};
            $scope.menu = {};
            if ($cookies.get('user') !== null && $cookies.get('user') !== undefined) {
                SessionFactory.menus({id: $cookies.get('user')}, function (res) {
                    $scope.menu = res;
                });
            }
        })
        .controller('SettingsController', function ($scope, $mdBottomSheet) {
            $scope.items = [
                {name: 'Cerrar sesión', icon: 'launch'}
            ];

            $scope.listItemClick = function ($index) {
                var clickedItem = $scope.items[$index];
                $mdBottomSheet.hide(clickedItem);
            };
        });
;