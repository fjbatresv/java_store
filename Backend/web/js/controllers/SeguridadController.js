'use strict';
angular.module('doorman.controllers')
        .controller('SeguridadController', function ($rootScope, $scope, $location, $cookies, SessionFactory) {
            $scope.opciones = [
                {
                    nombre: 'Usuarios',
                    icono: 'person_pin',
                    path: '/seguridad/usuarios'
                }
            ];
        });