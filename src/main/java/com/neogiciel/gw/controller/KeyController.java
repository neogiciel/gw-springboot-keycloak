package com.neogiciel.gw.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.Keycloak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/")
public class KeyController {


    //@Value("${keycloak.realm}")
    @Value("${spring.security.oauth2.client.registration.keycloak.realm}")
    private String realm;

    //@Value("${keycloak.auth-server-url}")
    @Value("${spring.security.oauth2.client.registration.keycloak.auth-server-url}")
    private String serverUrl;

    //Logger
    Logger logger = LoggerFactory.getLogger(KeyController.class);

    @GetMapping("/key")
    public String hello() {
        logger.info(">> Appel REST key");
        return "Key from KeyController";
    }

    /*
     * gettoken
     */
    @GetMapping("/gettoken")
    public Map<String,String> gettoken(Principal principal,
                                    @RequestParam(defaultValue = "") String client_id,
                                    @RequestParam(defaultValue = "") String username,
                                    @RequestParam(defaultValue = "") String password,
                                    @RequestParam(defaultValue = "") String client_secret) {
    
        logger.info("***********getToken*************");
        logger.info("client_id = "+ client_id);
        logger.info("username = "+ username);
        logger.info("password = "+ password);
        logger.info("client_secret = "+ client_secret);
        
        Map map;

        try{

            Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm) 
                .username(username)
                .password(password)
                .grantType(OAuth2Constants.PASSWORD) 
                .clientId(client_id)
                .clientSecret(client_secret) 
                .build();

                //Trace.info("Get AccesToken = "+ resource);
                AccessTokenResponse tok = keycloak.tokenManager().getAccessToken();
                //logger.info("token = "+ tok.getToken());
                
                map = Map.of("access_token",tok.getToken(),"refresh_token",tok.getRefreshToken());
                //map = Map.of("access_token",tok.getToken());
        }catch(Exception e){
            map = Map.of("access_token","","refresh_token","");    

        };
        
        return map;
    }

    /*
     * auth 
     */
    @GetMapping("/auth")
    public Map<String,String> auth(Principal principal) {
        logger.info("**********auth*********************");
        Map map = Map.of("auth", "Utilisateur authentifie");
        
        return map;   
    }

    /*
     * refresh 
     */
    @GetMapping("/refreshtoken")
    public Map refresh(Principal principal,
                            @RequestParam(defaultValue = "") String client_id,                            
                            @RequestParam(defaultValue = "") String refresh_token,
                            @RequestParam(defaultValue = "") String client_secret) {
       logger.info("**********refresh*********************");
       logger.info("client_id = "+ client_id);
       logger.info("client_secret = "+ client_secret);
       //logger.info("refresh_token = "+ refresh_token);
       
       List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
       urlParameters.add(new BasicNameValuePair("client_id", client_id));
       urlParameters.add(new BasicNameValuePair("refresh_token", refresh_token));
       urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
       urlParameters.add(new BasicNameValuePair("client_secret", client_secret));

      
       Map map;
       try{
            map = sendPost(urlParameters);

       }catch(Exception e){
         Map.of("access_token","","refresh_token","");
         map = Map.of("access_token","","refresh_token","");
       }
      
       return map;
        
    }

    private  Map<String, String> sendPost(List<NameValuePair> urlParameters) throws Exception {

        String access_token="";    
        String refresh_token="";    
        Map<String, String> map;
        HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(serverUrl + "/realms/" + realm + "/protocol/openid-connect/token");

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
            result.append(line);
            logger.info("line= "+line);

            if(line.contains("error") == true){
                return Map.of("access_token","","refresh_token","");
            }else{
                String[] keyValue = line.split(",");
                logger.info("element 1 = "+keyValue[1]);
                access_token = keyValue[0].substring(17, keyValue[0].length()-1);
                logger.info("access_token = "+ access_token);
                
                refresh_token = keyValue[3].substring(17, keyValue[3].length()-1);
                logger.info("refresh_token = "+ refresh_token);            
                return Map.of("access_token",access_token,"refresh_token",refresh_token);
            }
		}

        return Map.of("access_token","","refresh_token","");

	}


}
