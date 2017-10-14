'use strict';
angular.module('doorman.controllers')
        .controller("MainController", function ($location, $scope, DetalleTransaccionFactory, CarritoFactory, $cookies, $mdToast, $mdDialog) {
            $scope.productos = [];
            DetalleTransaccionFactory.recent(function (res) {
                $scope.productos = res;
            });

            $scope.detalle = function (producto) {
                console.log(producto);
            }

            $scope.carrito = function (producto) {
                if ($cookies.get('client') !== null && $cookies.get('client') !== undefined) {
                    var carrito = {
                        productoId: producto,
                        precio: producto.precio,
                    };
                    CarritoFactory.add({id: $cookies.get('client')}, carrito, function (res) {
                        if (res.codigo === 0) {
                            $mdToast.show(
                                    $mdToast.simple()
                                    .textContent(res.mensaje)
                                    .position('top right')
                                    .hideDelay(1500)
                                    );
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
                } else {
                    $mdDialog.show(
                            $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#popupContainer')))
                            .clickOutsideToClose(true)
                            .title('Hubo un problema')
                            .textContent("Primero inicia sesión")
                            .ok('Ok')
                            );
                }
            }
        });