'use strict';
angular.module('doorman.controllers')
        .controller('UsuarioListController', function ($rootScope, $scope, $location, $cookies,
                UsuarioFactory) {
            $scope.usuarios = [];
            UsuarioFactory.findAll(function (res) {
                $scope.usuarios = res;
            });
            var originatorEv;
            $scope.openMenu = function ($mdOpenMenu, ev) {
                originatorEv = ev;
                $mdOpenMenu(ev);
            }
            $scope.listado = true;
            $scope.listar = function (i) {
                if (i === 1) {
                    $scope.listado = true;
                } else {
                    $scope.listado = false;
                }
            }
        })
        .controller('UsuarioAddController', function ($location, $scope, $mdToast, $mdDialog, UsuarioFactory) {
            $scope.usuario = {};
            $scope.titulo = 'Nuevo usuario';
            $scope.guardar = function () {
                UsuarioFactory.add($scope.usuario, function (res) {
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
                $location.path('/seguridad/usuarios');
            }
        })
        .controller('UsuarioEditController', function ($location, $scope, $mdToast, $mdDialog, $routeParams, UsuarioFactory) {
            $scope.usuario = {};
            $scope.titulo = 'Editar usuario';
            UsuarioFactory.find({id: $routeParams.id}, function (res) {
                $scope.usuario = res;
                $scope.usuario.password = null;
                $scope.titulo = 'Editar usuario "' + res.email + '"';
            });
            $scope.edit = true;
            $scope.guardar = function () {
                UsuarioFactory.edit({id: $scope.usuario.id}, $scope.usuario, function (res) {
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
                $location.path('/seguridad/usuarios');
            }
        })
        .controller("UsuarioRolEditController", function ($scope, $location, $routeParams, $mdToast, $mdDialog,
                UsuarioFactory, RolFactory) {
            $scope.roles = [];
            $scope.selected = [];
            $scope.listado = [];
            RolFactory.findAll(function (res) {
                $scope.roles = res;
                UsuarioFactory.roles({id: $routeParams.id}, function (rea) {
                    $scope.selected = rea;
                    $scope.order();
                });
            });
            $scope.order = function () {
                angular.forEach($scope.roles, function (rol) {
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
                $location.path('/seguridad/usuarios');
            }
            $scope.guardar = function () {
                var cambios = [];
                console.log('guardando');
                angular.forEach($scope.roles, function (rol) {
                    angular.forEach($scope.listado, function (sel) {
                        if (sel.selected && rol.id === sel.id) {
                            delete rol.selected;
                            cambios.push(rol);
                        }
                    });
                });
                UsuarioFactory.setRoles({id: $routeParams.id}, cambios, function (res) {
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