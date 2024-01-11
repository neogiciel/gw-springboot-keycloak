package com.neogiciel.gw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GatewayController {

  //Logger
  Logger logger = LoggerFactory.getLogger(GatewayController.class);

  //keycloak
  //@Value("${spring.security.oauth2.client.registration.keycloak.auth-server-url}")
  //private URI home;



  /*@GetMapping("/secured")
  public Mono<ResponseEntity<byte[]>> secured( @AuthenticationPrincipal Jwt jwt,ProxyExchange<byte[]> proxy) {
    logger.info(">> Appel secured");
    return proxy.uri(home.toString() + "/realms/MyRealm").get();
  }*/

  /*@GetMapping("/unsecured")
  public Mono<ResponseEntity<byte[]>> unsecured(ProxyExchange<byte[]> proxy) {
    logger.info(">> Appel unsecured = "+home.toString() + "/realms/MyRealm");
    return proxy.uri(home.toString() + "/realms/MyRealm").get();
  }*/
  
  @GetMapping("/secured")
  public Map<String,String> secured() {
    logger.info("**********secured*********************");
    Map map = Map.of("secured", "test authentifie");
    return map;   
  }

  @GetMapping("/unsecured")
  public Map<String,String> unsecured() {
        logger.info("**********unsecured*********************");
        Map map = Map.of("unsecured", "test sans authentification");
        return map;   
   }

  
}

