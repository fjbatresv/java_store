'use strict';
angular.module('doorman.controllers')
        .controller('ClienteListController', function ($rootScope, $scope, $location, $cookies,
                ClienteFactory, $mdToast, $mdDialog) {
            $scope.clientes = [];
            $scope.cargar = function () {
                $scope.clientes = [];
                ClienteFactory.findAll(function (res) {
                    $scope.clientes = res;
                });
            }
            $scope.cargar();
            $scope.activar = function (rol) {
                var accion = "desactivara";
                if (!rol.activo) {
                    accion = "activara";
                }
                var confirm = $mdDialog.confirm()
                        .title('¿Estas seguro?')
                        .textContent('Se ' + accion + ' la categoria "' + rol.email + '"')
                        .ok('Continuar')
                        .cancel('Cancelar');
                $mdDialog.show(confirm).then(function () {
                    ClienteFactory.active({'id': rol.id}, function (res) {
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
        .controller("ClienteTransaccionController", function ($scope, $routeParams, $location, $mdToast, $mdDialog, ClienteFactory, TransaccionFactory) {
            $scope.cliente = {};
            $scope.titulo = 'Transacciones del cliente ';
            $scope.transacciones = [];
            ClienteFactory.find({id: $routeParams.id}, function (res) {
                $scope.cliente = res;
                $scope.titulo = 'Transacciones del cliente "' + res.email + '"';
            });
            TransaccionFactory.byCliente({id: $routeParams.id}, function (res) {
                $scope.transacciones = res;
            });
            
        });