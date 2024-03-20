## <h1>Gateway Spring Boot basé sur Keycloack</h1>
***
<img src="https://www.xpand-it.com/wp-content/uploads/2020/06/Keycloak-logo.png" alt="drawing" height="240px"/>



## Informations Générales
***
Mise en place d'une Gateway qui s'appuit sur la solution Spring Cloud Gateway
## Technologies
***
Technologies utilisées:
* Java 17 
* Maven 3.6
* Spring-boot: 3.2
## Instalation
***
Configuration de la route
```
 @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        logger.info("[Service GW Springboot] SpringCloudConfig : gatewayRoutes");

        return builder.routes()
                .route(r -> r.path("/security")
                        .uri("http://dev2.neogiciel.com"))

                .build();
    }
```
Lancement de l'application Spring-boot<br>
```
$ mvn  clean
$ mvn spring-boot:run
```
Le service est accessible sur http://localhost:8088

## FAQs
***
**Présentation de la Spring Clioud Gateway**
* https://spring.io/projects/spring-cloud-gateway



