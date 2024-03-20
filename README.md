## <h1>Gateway Spring Boot basé sur Keycloack</h1>
***
<img src="https://www.xpand-it.com/wp-content/uploads/2020/06/Keycloak-logo.png" alt="drawing" height="200px"/>

## Informations Générales
***
Keycloak est un logiciel à code source ouvert permettant d'instaurer une méthode d'authentification unique à travers la gestion par identité et par accès. Initialement développé par les équipes JBoss, le projet est depuis mars 2018 sous la gérance de Red Hat qui l'utilise en amont de sa solution RH-SSO[1]. Keycloak se définit comme une application rendant possible la sécurisation de n'importe quelle application web moderne avec un apport minimum en termes de code.<br>

La mise en place de cette Gateway permet de pouvoir sécurisé les communication de composant angular vers des api. <br>
Nous utilisons ici le protocole OpenID Connect<br>

## Technologies
***
Technologies utilisées:
* Java 17 
* Maven 3.6
* Spring-boot: 3.1.2
## Instalation
***
Deploiement de keycloack via docker-compose<br>

```
version: '3.9'
services:
  mysql-kc:
    image: mysql:8.0.27
    ports:
      - 3366:3306
    restart: unless-stopped
    environment:
      # The user, password and database that Keycloak
      # is going to create and use
      MYSQL_USER: keycloak_user
      MYSQL_PASSWORD: <keycloak_password>
      MYSQL_DATABASE: keycloak_db
      # Self-Explanatory
      MYSQL_ROOT_PASSWORD: root
    volumes:
      - keycloak-and-mysql-volume:/var/lib/mysql
    networks:
      - keycloak-and-mysql-network

  keycloak-w:
    image: jboss/keycloak:16.1.0
    ports:
      - 8181:8080
      - 8443:8443
    restart: unless-stopped
    environment:
      # User and password for the Administration Console
      KEYCLOAK_USER: <login>
      KEYCLOAK_PASSWORD: <password>
      X509_CA_BUNDLE: /etc/x509/https/rootCA.crt
      DB_VENDOR: mysql
      DB_ADDR: mysql-kc
      DB_PORT: 3306
      # Same values as the mysql-kc service
      DB_USER: keycloak_user
      DB_PASSWORD: keycloak_password
      DB_DATABASE: keycloak_db
    depends_on:
      - mysql-kc
    networks:
      - keycloak-and-mysql-network
    volumes:
      - ./x509/linuxtricksCA.crt:/etx/x509/https/tls.crt
      - ./x509/linuxtricksCA.key:/etx/x509/https/tls.key
      - ./x509/esx1.linuxtricks.lan.crt:/etx/x509/https/rootCA.crt
      #- ./opt/jboss/keycloak/standalone/configuration:/opt/jboss/keycloak/standalone/configuration

networks:
  keycloak-and-mysql-network:
  
volumes:
  keycloak-and-mysql-volume:
```
Lancement de keycloack:
docker-compose up -d 

Configuration de la sécurite: (via code source)
```
 
    http.cors().configurationSource(getCorsConfigurationSource()).and()
      .authorizeExchange()
      //.pathMatchers("/**").permitAll()
      .pathMatchers("/secured").authenticated()
      .pathMatchers("/auth").authenticated()
      .pathMatchers("/security").authenticated()
      .pathMatchers("/unsecured").permitAll()
      .pathMatchers("/gettoken").permitAll()
      .pathMatchers("/refreshtoken").permitAll()
      .pathMatchers("/api/**").authenticated()
      .and()
      .oauth2ResourceServer()
      .jwt();```
```
Configuration des routes (via code source)
```
 @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        logger.info("[Service GW Springboot] SpringCloudConfig : gatewayRoutes");

        return builder.routes()
                .route(r -> r.path("/security")
                        .uri("http://dev2.neogiciel.com"))
                
                .route(r -> r.path("/test")
                        .uri("http://dev2.neogiciel.com"))

                .build();
    }
```
Configuration de la sécurite: (via yaml)
```
 routes:
      - id: test
        uri: http://dev2.neogiciel.com
        predicates:
        - Method=GET,POST
        - Path=/test/**
```
Lancement de l'application Spring-boot<br>
```
$ mvn  clean
$ mvn spring-boot:run
```
Le service est accessible sur http://localhost:8089

## FAQs
***
**Présentation de la Spring Cloud Gateway**
* https://spring.io/projects/spring-cloud-gateway



