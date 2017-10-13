'use strict';
angular.module('doorman.controllers')
        .controller('CategoriaListController', function ($rootScope, $scope, $location, $cookies,
                CategoriaFactory, $mdToast, $mdDialog) {
            $scope.roles = [];
            $scope.cargar = function () {
                $scope.roles = [];
                CategoriaFactory.findAll(function (res) {
                    $scope.roles = res;
                });
            }
            $scope.cargar();
            $scope.eliminar = function (rol) {
                var confirm = $mdDialog.confirm()
                        .title('¿Estas seguro?')
                        .textContent('Se eliminara la categoria "' + rol.nombre + '"')
                        .ok('Si, eliminar')
                        .cancel('Cancelar');
                $mdDialog.show(confirm).then(function () {
                    CategoriaFactory.delete({'id': rol.id}, function (res) {
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
        .controller('CategoriaAddController', function ($location, $scope, $mdToast, $mdDialog, CategoriaFactory) {
            $scope.rol = {};
            $scope.titulo = 'Nueva categoria';
            $scope.guardar = function () {
                CategoriaFactory.add($scope.rol, function (res) {
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
                $location.path('/catalogos/categorias');
            }
        })
        .controller('CategoriaEditController', function ($location, $scope, $mdToast, $mdDialog, $routeParams, CategoriaFactory) {
            $scope.rol = {};
            $scope.titulo = 'Editar categoria';
            CategoriaFactory.find({id: $routeParams.id}, function (res) {
                $scope.rol = res;
                $scope.titulo = 'Editar categoria "' + res.nombre + '"';
            });
            $scope.edit = true;
            $scope.guardar = function () {
                CategoriaFactory.edit({id: $scope.rol.id}, $scope.rol, function (res) {
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
                $location.path('/catalogos/categorias');
            }
        });