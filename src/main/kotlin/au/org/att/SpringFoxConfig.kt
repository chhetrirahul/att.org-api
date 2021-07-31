package au.org.att

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SpringFoxConfig {

    @Bean
    fun apiDocket() = Docket(DocumentationType.SWAGGER_2)
        .apiInfo(getApiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("au.org.att"))
        .paths(PathSelectors.ant("/api/**"))
        .build()

    private fun getApiInfo() = ApiInfoBuilder()
        .title("att.org.au API Doc")
        .description("API documentation for att.org.au backend")
        .version("1.0.0")
        .build()
}