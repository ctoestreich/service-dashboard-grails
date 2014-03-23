class UrlMappings {

	static mappings = {
//        "/api/serviceEndpoints"(resources:'serviceEndpoint')

        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }

        "/api/endpoints"(resources:'serviceEndpoint')

        "/api/endpoints/status/$id?(.${format})?"(controller:'serviceEndpoint', action: 'status')

        "/api/settings/$id?"(resources: 'settings')

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
