serviceDashboard.factory('EndpointService', function ($resource) {
    return $resource('/service-dashboard/api/endpoints/:id');
}).factory('FilteringService', function ($resource) {
    return $resource('/service-dashboard/filtering/filter');
}).factory('SettingsService', function ($resource) {
    return $resource('/service-dashboard/api/settings/:id');
}).factory('StatusService', function ($resource) {
    return $resource('/service-dashboard/api/endpoints/status/:id');
}).factory('DataService', function ($resource) {
    return $resource('/service-dashboard/data/:action');
}).factory('GrowlService', function () {
    return {
        growl: function (message, life, theme) {
            $.jGrowl(message, {life: life || 5000, theme: theme || 'success'});
        }
    }
}).factory('DataFormatService', function () {
    return {
        formatJson: function(data){
            var json = eval('(' + data + ')');
            return json ? JSON.stringify(json, null, 3) : '';
        },
        formatXml: function (xml) {
            var formatted = '';
            var reg = /(>)(<)(\/*)/g;
            xml = xml.replace(reg, '$1\r\n$2$3');
            var pad = 0;

            jQuery.each(xml.split('\r\n'), function (index, node) {
                var indent = 0;
                if (node.match(/.+<\/\w[^>]*>$/)) {
                    indent = 0;
                }
                else if (node.match(/^<\/\w/)) {
                    if (pad != 0) {
                        pad -= 1;
                    }
                }
                else if (node.match(/^<\w[^>]*[^\/]>.*$/)) {
                    indent = 1;
                }
                else {
                    indent = 0;
                }
                var padding = '';
                for (var i = 0; i < pad; i++) {
                    padding += '  ';
                }
                formatted += padding + node + '\r\n';
                pad += indent;
            });

            return formatted;
        }
    }
});