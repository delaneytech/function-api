package io.theground.functionapi

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.router

@Configuration
class FunctionRouter {

    @Bean
    fun route (functionHandler: FunctionHandler) = router {
        (accept(MediaType.APPLICATION_JSON) and "/functions").nest {
            (GET("")).invoke(functionHandler::list)
            (POST("")).and(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA)).invoke(functionHandler::submit)
        }
    }
}