'use strict';
angular.module('doorman.controllers')
        .controller('CarritoListController', function ($scope, $location, $cookies,
                CarritoFactory, $mdToast, $mdDialog, $routeParams) {
            $scope.titulo = 'Carrito de compra ';
            $scope.carrito = [];
            $scope.cargar = function () {
                $scope.carrito = [];
                CarritoFactory.byUser({id: $cookies.get('client')}, function (res) {
                    $scope.carrito = res;
                });
            }
            $scope.cargar();
            $scope.pago = {};
            $scope.next = function (ev) {
                // Appending dialog to document.body to cover sidenav in docs app
                var confirm = $mdDialog.prompt()
                        .title('¿A donde deseas que lo entreguemos?')
                        .textContent('Debes estar presente para recibirlo')
                        .placeholder('Dirección')
                        .ariaLabel('Dirección')
                        .initialValue('Guatemala')
                        .targetEvent(ev)
                        .ok('Ok!')
                        .cancel('Cancelar');

                $mdDialog.show(confirm).then(function (result) {
                    $scope.pago.destino = result;
                    var cardConfirm = $mdDialog.prompt()
                            .title('Ingresa tu numero de tarjeta de credito')
                            .textContent('Debes estar presente el propietario de la tarjeta')
                            .placeholder('0000-0000-0000-0000')
                            .ariaLabel('card')
                            .targetEvent(ev)
                            .ok('Ok!')
                            .cancel('Cancelar');

                    $mdDialog.show(cardConfirm).then(function (resulta) {
                        $scope.pago.tarjeta = resulta;
                        CarritoFactory.checkout({id: $cookies.get('client')}, $scope.pago, function (res) {
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
                }, function () {

                });
            }
            $scope.eliminarAll = function () {
                var confirm = $mdDialog.confirm()
                        .title('¿Estas seguro?')
                        .textContent('Se eliminara todo el contenido del carrito')
                        .ok('Si, eliminar')
                        .cancel('Cancelar');
                $mdDialog.show(confirm).then(function () {
                    CarritoFactory.deleteAll({'id': $cookies.get('client')}, function (res) {
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
            $scope.eliminar = function (rol) {
                var confirm = $mdDialog.confirm()
                        .title('¿Estas seguro?')
                        .textContent('Se eliminara del carrito el producto "' + rol.productoId.nombre + '"')
                        .ok('Si, eliminar')
                        .cancel('Cancelar');
                $mdDialog.show(confirm).then(function () {
                    CarritoFactory.delete({'id': rol.id}, function (res) {
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
        });