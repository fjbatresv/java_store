'use strict';
angular.module('doorman.controllers')
        .controller('SeguridadController', function ($rootScope, $scope, $location, $cookies, SessionFactory) {
            $scope.opciones = [
                {
                    nombre: 'Usuarios',
                    icono: 'person_pin',
                    path: '/seguridad/usuarios'
                },
                {
                    nombre: 'Roles',
                    icono: 'verified_user',
                    path: '/seguridad/roles'
                }
            ];
        })
        .controller('CatalogosController', function ($rootScope, $scope, $location, $cookies, SessionFactory) {
            $scope.opciones = [
                {
                    nombre: 'Categorias',
                    icono: 'device_hub',
                    path: '/catalogos/categorias'
                },
                {
                    nombre: 'Productos',
                    icono: 'card_giftcard',
                    path: '/catalogos/productos'
                },
            ];
        });