modules = {
    application {

        resource url: 'css/bootstrap.min.css'
        resource url: 'css/font-awesome.min.css'
        resource url: 'css/jgrowl.css'
        resource url: 'css/plugins/jquery.jgrowl.css'
        resource url: 'css/plugins/jquery.visualize.css'
        resource url: 'css/site.css'

        resource url: 'js/thirdParty/jquery.min.js', disposition: 'head'
        resource url: 'js/thirdParty/jquery-ui.min.js', disposition: 'head'
        resource url: 'js/thirdParty/modernizr.js', disposition: 'head'
        resource url: 'js/thirdParty/selectivizr.js', disposition: 'head'
        resource url: 'js/thirdParty/lodash.min.js', disposition: 'head'

        resource url: 'js/thirdParty/angular.min.js'
        resource url: 'js/thirdParty/angular.resource.min.js'
        resource url: 'js/thirdParty/angular.cookies.min.js'
        resource url: 'js/thirdParty/angular.sanitize.min.js'
        resource url: 'js/thirdParty/angular.bootstrap.tpls.min.js'
        resource url: 'js/app.js'
        resource url: 'js/filters.js'
        resource url: 'js/services.js'
        resource url: 'js/controllers.js'
        resource url: 'js/directives.js'

        //other scripts
        resource url: 'js/site.js'
        resource url: 'js/thirdParty/bootstrap.min.js'
        resource url: 'js/thirdParty/jquery.jgrowl.js'
        resource url: 'js/thirdParty/jquery.visualize.min.js'
    }
}