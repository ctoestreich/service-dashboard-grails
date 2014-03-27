serviceDashboard.directive('serviceEndpoint', ['$http', function ($http) {
    return {
        restrict: 'E',
        scope: {
            endpoint: '=',
            viewEndpoint: '=view',
            deleteEndpoint: '=delete',
            editEndpoint: '=edit',
            copyEndpoint: '=copy',
            viewEndpointResponse: '=status',
            viewError: '=error'
        },
        templateUrl: '/service-dashboard/views/service-endpoint.html',
        link: function (scope) {
            scope.class = "spinner";
            scope.icon = "icon-spinner icon-spin";

            scope.$on('$destroy', function () {
                scope.clearTimer();
            });

            scope.$watch('endpoint.shouldRefresh', function () {
                if (scope.endpoint && scope.endpoint.shouldRefresh) {
                    scope.endpoint.shouldRefresh = false;
                    scope.refresh();
                }
            });

            scope.refresh = function () {
                scope.class = "service-fetching";
                scope.icon = "icon-spinner icon-spin";
                scope.error = "";
                $http({url: "/service-dashboard/polling/refresh/" + scope.endpoint.id, async : true, method: 'GET', cache: false})
                    .success(statusSuccess)
                    .error(statusError);
            };

            scope.fetch = function () {
                scope.class = "service-fetching";
                scope.icon = "icon-spinner icon-spin";
                scope.error = "";
                $http.get("/service-dashboard/polling/status/" + scope.endpoint.id)
                    .success(statusSuccess)
                    .error(statusError);
            };

            scope.clearTimer = function () {
                clearInterval(scope.interval);
            };

            scope.fetch();

            scope.interval = setInterval(scope.fetch, scope.endpoint.pollIntervalMilliSeconds);

            function statusSuccess(data) {
                scope.shouldRefresh = false;
                scope.class = data.success ? "service-success" : "service-failure";
                scope.icon = data.success ? "icon-arrow-up" : "icon-ban-circle";
                scope.reachable = data.up ? 'reachable' : 'unreachable';
                if (data.down) {
                    scope.class = "service-down";
                    scope.icon = "icon-exclamation-sign";
                }
                if (data.exception) {
                    scope.error = data.exception;
                }
            }

            function statusError(error) {
                console.log('error', error);
                scope.shouldRefresh = false;
                scope.reachable = 'unreachable';
                scope.class = "service-failure";
                scope.icon = "icon-ban-circle";
                scope.error = "Service Error";
            }
        }
    }
}]);