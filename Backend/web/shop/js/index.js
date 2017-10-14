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
                    .when('/carrito', {
                        controller: 'CarritoListController as ctr',
                        templateUrl: 'views/carrito/show.html',
                    })
                    .when('/categorias/:id', {
                        controller: 'CategoriaController as ctr',
                        templateUrl: 'views/categoria/show.html',
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
                    } else if ('Iniciar sesión' === clickedItem['name']) {
                        $location.path('/login');
                    }
                });
            }
        })
        .controller("SidenavController", function ($cookies, $scope, $mdBottomSheet, $mdSidenav, $mdDialog, $mdToast, CategoriaFactory) {
            $scope.rolMenu = {};
            $scope.menu = {};
            CategoriaFactory.findAll(function (res) {
                $scope.menu = res;
            });
        })
        .controller('SettingsController', function ($scope, $mdBottomSheet, $cookies) {
            $scope.items = [];
            if ($cookies.get('client') !== null && $cookies.get('client') !== undefined) {
                $scope.items.push({name: 'Cerrar sesión', icon: 'launch'});
            } else {
                $scope.items.push({name: 'Iniciar sesión', icon: 'launch'});
            }
            $scope.listItemClick = function ($index) {
                var clickedItem = $scope.items[$index];
                $mdBottomSheet.hide(clickedItem);
            };
        });
;