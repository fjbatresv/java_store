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
        .controller('TransaccionDetalleController', function ($scope, $routeParams, $location, TransaccionFactory, DetalleTransaccionFactory) {
            $scope.transaccion = {};
            $scope.titulo = 'Detalle de la transaccion ';
            TransaccionFactory.find({id: $routeParams.id}, function (res) {
                $scope.transaccion = res;
                $scope.titulo = 'Detalle de la transaccion "' + res.id + '"';
            });
            DetalleTransaccionFactory.byTransaccion({id: $routeParams.id}, function (res) {
                $scope.detalles = res;
            });
            $scope.cancel = function () {
                window.history.back();
            }
        });