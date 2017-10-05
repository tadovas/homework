package org.solar.crawlerlog.infrastructure;

import static com.google.common.base.Predicates.not;
import static springfox.documentation.builders.PathSelectors.ant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .enableUrlTemplating(true)
        .useDefaultResponseMessages(false)
        .forCodeGeneration(false)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(not(ant("/error")))
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Crawler-log-service API")
        .description("You have a list of available api operations here. Enjoy")
        .license("Free to the world")
        .version("1.0")
        .build();
  }
}
