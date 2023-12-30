package com.neogiciel.gw.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@CrossOrigin(maxAge = 3600)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  
  @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
  private String issuerUri;

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {

    
    http.cors().configurationSource(getCorsConfigurationSource()).and()
      .authorizeExchange()
      //.pathMatchers("/**").permitAll()
      .pathMatchers("/secured").authenticated()
      .pathMatchers("/auth").authenticated()
      .pathMatchers("/security").authenticated()
      .pathMatchers("/unsecured").permitAll()
      .pathMatchers("/key").permitAll()
      .pathMatchers("/gettoken").permitAll()
      .pathMatchers("/test/**").permitAll()
      .pathMatchers("/api/**").authenticated()
      .pathMatchers("/actuator/**").permitAll()
      .and()
      .oauth2ResourceServer()
      .jwt();
    
       

    return http.build();
  }

  @Bean
  public ReactiveJwtDecoder jwtDecoder() {
      return ReactiveJwtDecoders.fromOidcIssuerLocation(issuerUri);
     
  }

  
  @Bean("corsConfigurationSource")
	public CorsConfigurationSource getCorsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		configuration.applyPermitDefaultValues();
		configuration.addAllowedOrigin("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
