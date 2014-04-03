describe("Controller Tests", function () {
    var scope, EndpointService, ServiceEndpointCtrl, DataService;


    DataService = function () {
        return {
            query: function (data) {
                if (data.action === 'serviceTypes') {
                    return [{"enumType":"com.bsb.ServiceType","name":"SOAP"},{"enumType":"com.bsb.ServiceType","name":"REST"}]
                }
                if (data.action === 'environmentTypes') {
                    return [{"enumType":"com.bsb.EnvironmentType","name":"DEV"},{"enumType":"com.bsb.EnvironmentType","name":"TEST"},{"enumType":"com.bsb.EnvironmentType","name":"STAGE"},{"enumType":"com.bsb.EnvironmentType","name":"PROD"}]
                }
            }
        }
    };

    EndpointService = function (scope) {
        return {
            query: function () {
                scope.endpoints = [
                    {"class": "com.bsb.ServiceEndpoint", "id": 1, "endpointName": "Address Standardization", "environmentType": {"enumType": "com.bsb.EnvironmentType", "name": "PROD"}, "pollIntervalMilliSeconds": 30000, "requestBody": "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:web=\"http://www.webserviceX.NET/\">\n   <soap:Body>\n      <web:ConversionRate>\n         <web:FromCurrency>USD<\u002fweb:FromCurrency>\n         <web:ToCurrency>CAD<\u002fweb:ToCurrency>\n      <\u002fweb:ConversionRate>\n   <\u002fsoap:Body>\n<\u002fsoap:Envelope>", "requestUrl": "http://www.webservicex.net/CurrencyConvertor.asmx", "serviceEndpointStatus": {"class": "ServiceEndpointStatus", "id": 1}, "serviceType": {"enumType": "com.bsb.ServiceType", "name": "SOAP"}, "soapAction": null, "successNode": "ConversionRateResult", "successValue": "\\d+\\.*\\d*"},
                    {"class": "com.bsb.ServiceEndpoint", "id": 2, "endpointName": "Address Standardization Failure", "environmentType": {"enumType": "com.bsb.EnvironmentType", "name": "TEST"}, "pollIntervalMilliSeconds": 30000, "requestBody": "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:web=\"http://www.webserviceX.NET/\">\n   <soap:Body>\n      <web:ConversionRate>\n         <web:FromCurrency>USD<\u002fweb:FromCurrency>\n         <web:ToCurrency>CAD<\u002fweb:ToCurrency>\n      <\u002fweb:ConversionRate>\n   <\u002fsoap:Body>\n<\u002fsoap:Envelope>", "requestUrl": "http://www.webservicex.net/CurrencyConvertor.asmx", "serviceEndpointStatus": {"class": "ServiceEndpointStatus", "id": 2}, "serviceType": {"enumType": "com.bsb.ServiceType", "name": "SOAP"}, "soapAction": null, "successNode": "ConversionRateResult", "successValue": "This Will Error"},
                    {"class": "com.bsb.ServiceEndpoint", "id": 3, "endpointName": "Github User Account", "environmentType": {"enumType": "com.bsb.EnvironmentType", "name": "STAGE"}, "pollIntervalMilliSeconds": 30000, "requestBody": null, "requestUrl": "https://api.github.com/users/grails-plugin-consortium", "serviceEndpointStatus": {"class": "ServiceEndpointStatus", "id": 3}, "serviceType": {"enumType": "com.bsb.ServiceType", "name": "REST"}, "soapAction": null, "successNode": "login", "successValue": "Grails-Plugin-Consortium"},
                    {"class": "com.bsb.ServiceEndpoint", "id": 4, "endpointName": "Github User Repos", "environmentType": {"enumType": "com.bsb.EnvironmentType", "name": "DEV"}, "pollIntervalMilliSeconds": 30000, "requestBody": null, "requestUrl": "https://api.github.com/users/Grails-Plugin-Consortium/repos", "serviceEndpointStatus": {"class": "ServiceEndpointStatus", "id": 4}, "serviceType": {"enumType": "com.bsb.ServiceType", "name": "REST"}, "soapAction": null, "successNode": "owner.login", "successValue": "Grails-Plugin-Consortium"}
                ];
            }
        }
    };

    beforeEach(angular.mock.module('serviceDashboard'));
    beforeEach(angular.mock.inject(function ($rootScope, $controller) {
        scope = $rootScope.$new();
        ServiceEndpointCtrl = $controller('ServiceEndpointCtrl',
            {
                $scope: scope,
                EndpointService: new EndpointService(scope),
                DataService: new DataService()
            });
    }));


    // tests start here
    it('Initial state for controller scopes', function () {
        expect(scope.endpoints).toBeDefined();
        expect(scope.orderByField).toBe('endpointName');
        expect(scope.serviceTypes.length).toBe(2);
        expect(scope.environmentTypes.length).toBe(4);
        expect(scope.endpoints.length).toBe(4);
        expect(scope.endpoints[0].id).toBe(1);
    });
});