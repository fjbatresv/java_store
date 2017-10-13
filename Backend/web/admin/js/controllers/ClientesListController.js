'use strict';
angular.module('doorman.controllers')
    .controller('ClientesListController', function ($scope, $mdDialog, $mdToast, $sanitize) {
        $scope.lineas = [];
        $scope.listado = true;
        const clientes = firebase.database().ref().child('clientes');
        clientes.on('child_added', function (data) {
            if (data.val()) {
                var item = data.val();
                $scope.lineas.push(item);
                $scope.$apply();
            }
        });
        $scope.eliminar = function (cliente) {
            var confirm = $mdDialog.confirm()
                .title('Esta seguro?')
                .textContent('Se eliminara el cliente "' + cliente.nombre + '"')
                .ok('Si, eliminar')
                .cancel('Cancelar');
            $mdDialog.show(confirm).then(function () {
                clientes.child(cliente.cui).remove();
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Cliente eliminado')
                        .position('top right')
                        .hideDelay(1500)
                );
                $scope.lineas = [];
                clientes.on('child_added', function (data) {
                    if (data.val()) {
                        var item = data.val();
                        $scope.lineas.push(item);
                        $scope.$apply();
                    }
                });
            }, function () {
            });
        }
    }).controller('ClientesNuevoController', function ($scope, $mdDialog, $mdToast, $sanitize, $location) {
    $scope.cancel = function () {
        $location.path('/clientes');
    }
    $scope.titulo = 'Nuevo Cliente';
    $scope.cliente = {};
    $scope.cliente.cui = guid();
    $scope.logoLoading = false;
    var storage = firebase.storage().ref();
    $scope.logoChange = function (element) {
        $scope.$apply(function (scope) {
            $scope.logo = element.files[0];
            var uploadTask = storage.child('images/' + $scope.cliente.cui + '/admin/clientes/' + $scope.logo.name).put($scope.logo);
            uploadTask.on('state_changed', function (snapshot) {
                $scope.logoLoading = true;
                console.log("loading");
                $scope.$apply();
            }, function (error) {
                $scope.logoLoading = false;
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#popupContainer')))
                        .clickOutsideToClose(true)
                        .title('Hubo un problema')
                        .textContent(error.message)
                        .ok('Ok')
                );
            }, function () {
                $scope.cliente.avatar = uploadTask.snapshot.downloadURL;
                $scope.logoLoading = false;
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Imagen "' + $scope.logo.name + '" cargada')
                        .position('top right')
                        .hideDelay(1500)
                );
                $scope.$apply();
            });
        });
    }
    $scope.guardar = function () {
        $scope.cliente.fecha = $scope.fecha.getFullYear() + '-' + ("0"+($scope.fecha.getMonth()+1)).slice(-2) + '-' + ("0" + $scope.fecha.getDate()).slice(-2);
        console.log($scope.cliente);
        var evento = firebase.database().ref().child('clientes').child($scope.cliente.cui);
        evento.set($scope.cliente).then(function () {
            $mdToast.show(
                $mdToast.simple()
                    .textContent('Cliente creado')
                    .position('top right')
                    .hideDelay(1500)
            );
            $scope.cancel();
        }, function (error) {
            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.querySelector('#popupContainer')))
                    .clickOutsideToClose(true)
                    .title('Hubo un problema')
                    .textContent(error.message)
                    .ok('Ok')
            );
        });
    }
})
    .controller('ClientesEditController', function ($scope, $routeParams, $location, $mdToast, $mdDialog) {
        const evento = firebase.database().ref().child('clientes').child($routeParams.cui);
        $scope.titulo = 'Editar Cliente';
        $scope.cancel = function () {
            $location.path('/clientes');
        }
        var storage = firebase.storage().ref();
        evento.once('value', function (data) {
            $scope.cliente = data.val();
            $scope.fecha = new Date($scope.cliente.fecha);
            $scope.titulo = 'Editar evento "' + $scope.cliente.nombre + '"';
            $scope.$apply();
        });
        $scope.logoChange = function (element) {
            $scope.$apply(function (scope) {
                $scope.logo = element.files[0];
                var uploadTask = storage.child('images/' + $scope.cliente.cui + '/admin/' + $scope.logo.name).put($scope.logo);
                uploadTask.on('state_changed', function (snapshot) {
                    $scope.logoLoading = true;
                    $scope.$apply();
                }, function (error) {
                    $scope.logoLoading = false;
                    $mdDialog.show(
                        $mdDialog.alert()
                            .parent(angular.element(document.querySelector('#popupContainer')))
                            .clickOutsideToClose(true)
                            .title('Hubo un problema')
                            .textContent(error.message)
                            .ok('Ok')
                    );
                }, function () {
                    $scope.logoLoading = false;
                    $scope.cliente.avatar = uploadTask.snapshot.downloadURL;
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('Imagen "' + $scope.logo.name + '" cargada')
                            .position('top right')
                            .hideDelay(1500)
                    );
                    $scope.$apply();
                });
            });
        }
        $scope.guardar = function () {
            $scope.cliente.fecha = $scope.fecha.getFullYear() + '-' + ('0' + ($scope.fecha.getMonth()+1)).slice(-2) + '-' + ("0" + $scope.fecha.getDate()).slice(-2);
            evento.set($scope.cliente).then(function () {
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('Cliente editado')
                        .position('top right')
                        .hideDelay(1500)
                );
                $scope.cancel();
            }, function (error) {
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#popupContainer')))
                        .clickOutsideToClose(true)
                        .title('Hubo un problema')
                        .textContent(error.message)
                        .ok('Ok')
                );
            });
        }
    });

function guid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }

    return s4() + s4() + s4() + s4() +
        s4() + s4() + s4() + s4();
}