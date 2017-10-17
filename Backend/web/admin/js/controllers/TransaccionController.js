'use strict';
angular.module('doorman.controllers')
        .controller('TransaccionListController', function ($rootScope, $scope, $location, $cookies,
                TransaccionFactory, $mdToast, $mdDialog) {
            $scope.transacciones = [];
            $scope.cargar = function () {
                $scope.transacciones = [];
                TransaccionFactory.findAll(function (res) {
                    $scope.transacciones = res;
                });
            }
            $scope.cargar();
        })
        .controller('TransaccionDetalleController', function ($scope, $routeParams, $location, $cookies,
                TransaccionFactory, DetalleTransaccionFactory, $mdToast, $mdDialog) {
            $scope.transaccion = {};
            $scope.titulo = 'Detalle de la transaccion ';
            $scope.cargar = function () {
                TransaccionFactory.find({id: $routeParams.id}, function (res) {
                    $scope.transaccion = res;
                    $scope.titulo = 'Detalle de la transaccion "' + res.id + '"';
                });
            }
            $scope.cargar();
            $scope.total = 0;
            DetalleTransaccionFactory.byTransaccion({id: $routeParams.id}, function (res) {
                $scope.detalles = res;
                angular.forEach(res, function (linea) {
                    $scope.total = $scope.total + linea.precio;
                });
            });
            $scope.cancel = function () {
                window.history.back();
            }
            $scope.cobrar = function () {
                console.log("cobrar");
                TransaccionFactory.cobrar({id: $routeParams.id, uid: $cookies.get("user")}, function (res) {
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
            }
            $scope.enviar = function () {
                TransaccionFactory.enviar({id: $routeParams.id, uid: $cookies.get("user")}, function (res) {
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
            }
            $scope.entregado = function () {
                TransaccionFactory.entrega({id: $routeParams.id, uid: $cookies.get("user")}, function (res) {
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
            }
            $scope.factura = function () {
                var pre = '<html><head><title>Factura Transaccion #' + $scope.transaccion.id +  '</title></head><body>';
                var post = '</body></html>';
                var data = document.getElementById("factura").innerHTML;
                var datahtml = pre + data + post;
                console.log("Antes de Creación");
                var mywindow = window.open('', 'imprimible', '');
                mywindow.document.write(datahtml);
                mywindow.print();
                mywindow.close();
                console.log("Saliendo....");

            }
        });