package best.service.dispatcher

class UrlMappings {

    static mappings = {

        "/serve/$customer/$service"(controller: 'service', action: 'execute')

        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(view: "/index")
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
