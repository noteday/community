package com.domin.community.community.controller;

import com.domin.community.community.dto.AccesssTokenDTO;
import com.domin.community.community.dto.GithubUser;
import com.domin.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private  String redirectUri;

    @Value("${github.client.id}")
    private String clientId;



    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletResponse response){
        AccesssTokenDTO accesssTokenDTO = new AccesssTokenDTO();
        accesssTokenDTO.setClient_id(clientId);
        accesssTokenDTO.setRedirect_uri(redirectUri);
        accesssTokenDTO.setClient_secret(clientSecret);
        accesssTokenDTO.setCode(code);
        accesssTokenDTO.setState(state);

        String accessToken = githubProvider.getAccesssToken(accesssTokenDTO);

        System.out.println(accessToken);

        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null){
            System.out.println(githubUser.toString());
        }
        return "index" ;
    }

}
