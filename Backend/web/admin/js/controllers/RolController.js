'use strict';
angular.module('doorman.controllers')
        .controller('RolListController', function ($rootScope, $scope, $location, $cookies,
                RolFactory, $mdToast, $mdDialog) {
            $scope.roles = [];
            $scope.cargar = function () {
                $scope.roles = [];
                RolFactory.findAll(function (res) {
                    $scope.roles = res;
                });
            }
            $scope.cargar();
            $scope.eliminar = function (rol) {
                var confirm = $mdDialog.confirm()
                        .title('¿Estas seguro?')
                        .textContent('Se eliminara el rol "' + rol.nombre + '"')
                        .ok('Si, eliminar')
                        .cancel('Cancelar');
                $mdDialog.show(confirm).then(function () {
                    RolFactory.delete({'id': rol.id}, function (res) {
                        if (res.codigo === 0) {
                            $mdToast.show(
                                    $mdToast.simple()
                                    .textContent(res.mensaje)
                                    .position('top right')
                                    .hideDelay(1500)
                                    );
                            $scope.cargar();
                        } else {
                            $mdDialog.show(
                                    $mdDialog.alert()
                                    .parent(angular.element(document.querySelector('#popupContainer')))
                                    .clickOutsideToClose(true)
                                    .title('Hubo un problema')
                                    .textContent(res.mensaje)
                                    .ok('Ok')
                                    );
                        }
                    });
                }, function () {
                });
            }
        })
        .controller('RolAddController', function ($location, $scope, $mdToast, $mdDialog, RolFactory) {
            $scope.rol = {};
            $scope.titulo = 'Nuevo rol';
            $scope.guardar = function () {
                RolFactory.add($scope.rol, function (res) {
                    if (res.codigo === 0) {
                        $mdToast.show(
                                $mdToast.simple()
                                .textContent(res.mensaje)
                                .position('top right')
                                .hideDelay(1500)
                                );
                        $scope.cancel();
                    } else {
                        $mdDialog.show(
                                $mdDialog.alert()
                                .parent(angular.element(document.querySelector('#popupContainer')))
                                .clickOutsideToClose(true)
                                .title('Hubo un problema')
                                .textContent(res.mensaje)
                                .ok('Ok')
                                );
                    }
                });
            }
            $scope.cancel = function () {
                $location.path('/seguridad/roles');
            }
        })
        .controller('RolEditController', function ($location, $scope, $mdToast, $mdDialog, $routeParams, RolFactory) {
            $scope.rol = {};
            $scope.titulo = 'Editar rol';
            RolFactory.find({id: $routeParams.id}, function (res) {
                $scope.rol = res;
                $scope.titulo = 'Editar rol "' + res.nombre + '"';
            });
            $scope.edit = true;
            $scope.guardar = function () {
                RolFactory.edit({id: $scope.rol.id}, $scope.rol, function (res) {
                    if (res.codigo === 0) {
                        $mdToast.show(
                                $mdToast.simple()
                                .textContent(res.mensaje)
                                .position('top right')
                                .hideDelay(1500)
                                );
                        $scope.cancel();
                    } else {
                        $mdDialog.show(
                                $mdDialog.alert()
                                .parent(angular.element(document.querySelector('#popupContainer')))
                                .clickOutsideToClose(true)
                                .title('Hubo un problema')
                                .textContent(res.mensaje)
                                .ok('Ok')
                                );
                    }
                });
            }
            $scope.cancel = function () {
                $location.path('/seguridad/roles');
            }
        })
        .controller('RolMenuEditController', function ($location, $scope, $mdToast, $mdDialog, $routeParams,
                RolFactory, MenuFactory) {
            $scope.rol = {};
            $scope.menus = [];
            $scope.selected = [];
            $scope.listado = [];
            $scope.titulo = 'Editar menus del rol';
            RolFactory.find({id: $routeParams.id}, function (res) {
                $scope.rol = res;
                $scope.titulo = 'Editar menus del rol "' + res.nombre + '"';
            });
            MenuFactory.findAll(function (res) {
                $scope.menus = res;
                RolFactory.menus({id: $routeParams.id}, function (rea) {
                    $scope.selected = rea;
                    $scope.order();
                });
            });
            $scope.order = function () {
                angular.forEach($scope.menus, function (rol) {
                    rol.selected = false;
                    angular.forEach($scope.selected, function (sel) {
                        if (sel.id === rol.id) {
                            rol.selected = true;
                        }
                    });
                    $scope.listado.push(rol);
                });
            }
            $scope.cancel = function () {
                $location.path('/seguridad/roles');
            }
            $scope.guardar = function () {
                var cambios = [];
                console.log('guardando');
                angular.forEach($scope.menus, function (rol) {
                    angular.forEach($scope.listado, function (sel) {
                        if (sel.selected && rol.id === sel.id) {
                            delete rol.selected;
                            cambios.push(rol);
                        }
                    });
                });
                RolFactory.setMenus({id: $routeParams.id}, cambios, function (res) {
                    if (res.codigo === 0) {
                        $mdToast.show(
                                $mdToast.simple()
                                .textContent(res.mensaje)
                                .position('top right')
                                .hideDelay(1500)
                                );
                        $scope.cancel();
                    } else {
                        $mdDialog.show(
                                $mdDialog.alert()
                                .parent(angular.element(document.querySelector('#popupContainer')))
                                .clickOutsideToClose(true)
                                .title('Hubo un problema')
                                .textContent(res.mensaje)
                                .ok('Ok')
                                );
                    }
                });
            };
        });