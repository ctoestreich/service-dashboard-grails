serviceDashboard.controller('ServiceEndpointCtrl',
    ['$scope', '$http', '$cookies', 'EndpointService', 'SettingsService', 'StatusService', 'DataService', 'GrowlService', 'DataFormatService',
        function ($scope, $http, $cookies, EndpointService, SettingsService, StatusService, DataService, GrowlService, DataFormatService) {
            $scope.endpoints = {};
            $scope.endpointToDelete = {};
            $scope.endpointToEdit = {};
            $scope.endpointStatus = {lastResponse: ''};
            $scope.serviceTypes = DataService.query({action: 'serviceTypes'});
            $scope.environmentTypes = DataService.query({action: 'environmentTypes'});
            $scope.settings = SettingsService.get();
            $scope.orderByField = 'endpointName';
            $scope.reverse = false;

            $scope.search = {
                environmentType: $cookies.environmentType || '',
                serviceType: $cookies.serviceType || '',
                endpointName: $cookies.endpointName || ''
            };

            $scope.saveSettings = function () {
                SettingsService.save($scope.settings, function (data) {
                    $scope.showSuccessMessage(data, 'settings', '', data.id);
                }, function (data) {
                    $scope.showErrorMessage(data, 'settings');
                });
            };

            $scope.resetSettings = function () {
                $scope.settings = SettingsService.get();
            };

            $scope.setOrderByField = function (field) {
                $scope.reverse = (field === $scope.orderByField) ? !$scope.reverse : false;
                $scope.orderByField = field;
            };

            $scope.setDefaultState = function () {
                $cookies.environmentType = $scope.search.environmentType;
                $cookies.serviceType = $scope.search.serviceType;
                $cookies.endpointName = $scope.search.endpointName;

                GrowlService.growl("Set your default state.  Your filters will persist across sessions.", 5500, 'inverse');
            };

            $scope.confirmSave = function () {
                if ($scope.endpointToEdit.id) {
                    $scope.saveExistingEndpoint();
                } else {
                    $scope.saveNewEndpoint();
                }
            };

            $scope.getEndpoints = function () {
                EndpointService.query(function (endpoints) {
                    $scope.endpoints = endpoints;
                    $scope.groups = _.groupBy($scope.endpoints, "environmentType");
                });
            };

            $scope.viewEndpoint = function (endpoint) {
                $scope.endpointToView = endpoint;
            };

            $scope.deleteEndpoint = function (endpoint) {
                $scope.endpointToDelete = endpoint;
            };

            $scope.editEndpoint = function (endpoint) {
                $scope.endpointToEdit = endpoint;
            };

            $scope.viewEndpointResponse = function (endpoint) {
                $scope.endpointStatus = StatusService.get({id: endpoint.serviceEndpointStatus.id});
            };

            $scope.copyEndpoint = function (endpoint) {
                var newEndpoint = _.clone(endpoint);
                newEndpoint.id = null;
                newEndpoint.endpointName = newEndpoint.endpointName += ' - Copy';
                if (endpoint && endpoint.serviceType) newEndpoint.serviceType = endpoint.serviceType.name;
                if (endpoint && endpoint.environmentType) newEndpoint.environmentType = endpoint.environmentType.name;
                EndpointService.save(newEndpoint, function (data) {
                    $scope.showSuccessMessage(data, 'endpoint', data.endpointName, data.id);
                    $scope.endpoints.push(data);
                }, function (data) {
                    $scope.showErrorMessage(data, 'endpoint');
                });

            };

            $scope.confirmDelete = function () {
                if ($scope.endpointToDelete) {
                    EndpointService.delete({id: $scope.endpointToDelete.id}, function () {
                        $scope.endpoints = _.reject($scope.endpoints, { 'id': $scope.endpointToDelete.id });
                    });
                }
            };

            $scope.createEndpoint = function () {
                $scope.endpointToEdit = new EndpointService({endpointName: '',
                    requestUrl: '',
                    soapAction: '',
                    requestBody: '',
                    successNode: '',
                    successValue: '',
                    shouldRefresh: false,
                    pollIntervalMilliSeconds: 30000
                });
            };

            $scope.saveExistingEndpoint = function () {
                var endpoint = _.find($scope.endpoints, function(item){ return item.id == $scope.endpointToEdit.id });
                endpoint.serviceType = endpoint.serviceType.name;
                endpoint.environmentType = endpoint.environmentType.name;
                endpoint.$save(function (data) {
                    $scope.showSuccessMessage(data, 'endpoint', data.endpointName, data.id);
                    endpoint.shouldRefresh = true;
                    $scope.endpointToEdit = {};
                }, function (data) {
                    $scope.showErrorMessage(data, 'endpoint');
                });
            };

            $scope.saveNewEndpoint = function () {
                $scope.endpointToEdit.serviceType = $scope.endpointToEdit.serviceType.name;
                $scope.endpointToEdit.environmentType = $scope.endpointToEdit.environmentType.name;
                EndpointService.save($scope.endpointToEdit, function (data) {
                    $scope.showSuccessMessage(data, 'endpoint', data.endpointName, data.id);
                    $scope.endpoints.push(data);
                }, function (data) {
                    $scope.showErrorMessage(data, 'endpoint');
                });
            };

            $scope.showSuccessMessage = function (data, type, name, id) {
                GrowlService.growl("Saved " + type + " " + name + " with id " + id + " successfully", 5500, 'success');
            };

            $scope.viewError = function (error) {
                $scope.error = error;
            };

            $scope.showErrorMessage = function (data, type) {
                var errors = "";
                if (data && data.data && data.data.errors) {
                    $(data.data.errors).each(function (i, o) {
                        errors += "<li>" + o.message + "</li>";
                    });
                }
                GrowlService.growl("Error saving " + type + "<br/>" + errors, 5500, 'danger');
            };

            $scope.updateServiceType = function () {
                var serviceType = _.find($scope.serviceTypes, function (serviceType) {
                    return serviceType.name === $scope.endpointToEdit.serviceType.name;
                });
                $scope.endpointToEdit.serviceType.name = serviceType.name;
                $scope.endpointToEdit.serviceType.enumType = serviceType.enumType;
            };

            $scope.updateEnvironmentType = function () {
                var environmentType = _.find($scope.environmentTypes, function (environmentType) {
                    return environmentType.name === $scope.endpointToEdit.environmentType.name;
                });
                $scope.endpointToEdit.environmentType.name = environmentType.name;
                $scope.endpointToEdit.environmentType.enumType = environmentType.enumType;
            };

            $scope.formatData = function (data) {
                if (data) {
                    try {
                        return DataFormatService.formatJson(data);
                    } catch (err) {
                        console.log('Could not format json or not json.', err);
                    }

                    try {
                        return DataFormatService.formatXml(data);
                    } catch (err) {
                        console.log('Could not format xml', err);
                    }

                    return data;
                }
            };


            $scope.getEndpoints();
        }]);