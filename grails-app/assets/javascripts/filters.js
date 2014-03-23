serviceDashboard.filter('shorten', function () {
    return function (url, max) {
        max = max || 10;
        return url && (url.length > max) ? url.substr(0, max - 1) + '...' : url;
    }
});