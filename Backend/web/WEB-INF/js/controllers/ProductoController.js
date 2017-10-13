'use strict';
angular.module('doorman.controllers')
        .controller('ProductoListController', function ($rootScope, $scope, $location, $cookies,
                ProductoFactory, $mdToast, $mdDialog) {
            $scope.roles = [];
            $scope.cargar = function () {
                $scope.roles = [];
                ProductoFactory.findAll(function (res) {
                    $scope.roles = res;
                });
            }
            $scope.cargar();
            $scope.eliminar = function (rol) {
                var confirm = $mdDialog.confirm()
                        .title('¿Estas seguro?')
                        .textContent('Se eliminara el producto "' + rol.nombre + '"')
                        .ok('Si, eliminar')
                        .cancel('Cancelar');
                $mdDialog.show(confirm).then(function () {
                    ProductoFactory.delete({'id': rol.id}, function (res) {
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
        .controller('ProductoAddController', function ($location, $scope, $mdToast, $mdDialog, ProductoFactory) {
            $scope.rol = {};
            $scope.titulo = 'Nuevo producto';
            $scope.guardar = function () {
                ProductoFactory.add($scope.rol, function (res) {
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
                $location.path('/catalogos/productos');
            }
        })
        .controller('ProductoEditController', function ($location, $scope, $mdToast, $mdDialog, $routeParams, ProductoFactory) {
            $scope.rol = {};
            $scope.titulo = 'Editar producto';
            ProductoFactory.find({id: $routeParams.id}, function (res) {
                $scope.rol = res;
                $scope.titulo = 'Editar producto "' + res.nombre + '"';
            });
            $scope.edit = true;
            $scope.guardar = function () {
                ProductoFactory.edit({id: $scope.rol.id}, $scope.rol, function (res) {
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
                $location.path('/catalogos/productos');
            }
        })
        .controller('ProductoCategoriasController', function ($location, $scope, $mdToast, $mdDialog, $routeParams,
                ProductoFactory, CategoriaFactory) {
            $scope.categorias = [];
            $scope.selected = [];
            $scope.listado = [];
            $scope.titulo = "Categorias del producto";
            $scope.producto = {};
            ProductoFactory.find({id: $routeParams.id}, function (res) {
                $scope.titulo = 'Categorias del producto "' + res.nombre + '"';
                $scope.producto = res;
            });
            CategoriaFactory.findAll(function (res) {
                $scope.categorias = res;
                ProductoFactory.categorias({id: $routeParams.id}, function (rea) {
                    $scope.selected = rea;
                    $scope.order();
                });
            });
            $scope.order = function () {
                angular.forEach($scope.categorias, function (rol) {
                    rol.selected = false;
                    angular.forEach($scope.selected, function (sel) {
                        if (sel.id === rol.id) {
                            rol.selected = true;
                        }
                    });
                    $scope.listado.push(rol);
                });
                $scope.cancel = function () {
                    $location.path('/catalogos/productos');
                }
                $scope.guardar = function () {
                    var cambios = [];
                    console.log('guardando');
                    angular.forEach($scope.categorias, function (rol) {
                        angular.forEach($scope.listado, function (sel) {
                            if (sel.selected && rol.id === sel.id) {
                                delete rol.selected;
                                cambios.push(rol);
                            }
                        });
                    });
                    ProductoFactory.setCategorias({id: $routeParams.id}, cambios, function (res) {
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
            }
        })
        .controller('ProductoExistenciaController', function ($location, $scope, $mdToast, $mdDialog, $routeParams,
                ProductoFactory, ExistenciaFactory) {
            $scope.producto = {};
            $scope.existencia = {};
            $scope.titulo = 'Existencias del producto';
            ProductoFactory.find({id: $routeParams.id}, function (res) {
                $scope.producto = res;
                $scope.titulo = 'Existencias del producto "' + res.codigo + '"';
            });
            $scope.existencias = [];
            $scope.actualExistencias = function () {
                $scope.existencias = [];
                ExistenciaFactory.producto({id: $routeParams.id}, function (res) {
                    $scope.existencias = res;
                    console.log($scope.existencias);
                    $mdToast.show(
                            $mdToast.simple()
                            .textContent("Se encontraron " + res.length + " registros de existencias")
                            .position('top right')
                            .hideDelay(1500)
                            );
                });
            }
            $scope.actualExistencias();
            $scope.cancel = function () {
                $location.path('/catalogos/productos');
            }
            $scope.guardar = function () {
                ProductoFactory.existencia({id: $routeParams.id}, $scope.existencia, function (res) {
                    if (res.codigo === 0) {
                        $mdToast.show(
                                $mdToast.simple()
                                .textContent(res.mensaje)
                                .position('top right')
                                .hideDelay(1500)
                                );
                        $scope.existencia = {};
                        $scope.actualExistencias();
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