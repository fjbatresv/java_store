'use strict';
angular.module('doorman.controllers', [])
        .controller('LoginController', function ($rootScope, $scope, $location,
                $cookies, SessionFactory, ClienteFactory, $mdToast) {
            $scope.signup = false;
            $scope.nuevo = {};
            $scope.login = {};
            $scope.ingresar = function () {
                $scope.error = null;
                SessionFactory.login($scope.login, function (res) {
                    if (res.codigo !== 0) {
                        $scope.error = res.mensaje;
                    } else {
                        console.log(res.response.value);
                        $cookies.put('client', res.response.value);
                        $location.path('/');
                        window.location.reload();
                    }
                });
            }
            $scope.actionToggle = function () {
                $scope.signup = !$scope.signup;
            }
            $scope.registrar = function () {
                $scope.error = null;
                ClienteFactory.add($scope.nuevo, function (res) {
                    if (res.codigo !== 0) {
                        $scope.error = res.mensaje;
                    } else {
                        $mdToast.show(
                                $mdToast.simple()
                                .textContent(res.mensaje)
                                .position('top right')
                                .hideDelay(1500)
                                );
                        $scope.actionToggle();
                    }
                });
            }
        });