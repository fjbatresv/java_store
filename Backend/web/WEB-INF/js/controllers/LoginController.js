'use strict';
angular.module('doorman.controllers', [])
        .controller('LoginController', function ($rootScope, $scope, $location, $cookies, SessionFactory) {
            $scope.ingresar = function () {
                SessionFactory.login($scope.login, function (res) {
                    console.log(res);
                    if (res.codigo !== 0) {
                        $scope.error = res.mensaje;
                    } else {
                        console.log(res.response.value);
                        $cookies.put('user', res.response.value);
                        $location.path('/');
                        window.location.reload();
                    }
                });
            }
        });