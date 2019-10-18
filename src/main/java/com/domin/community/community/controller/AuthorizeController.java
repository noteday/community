package com.domin.community.community.controller;

import com.domin.community.community.dto.AccesssTokenDTO;
import com.domin.community.community.dto.GithubUser;
import com.domin.community.community.mapper.UserMapper;
import com.domin.community.community.model.User;
import com.domin.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.util.UUID;

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

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
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
        if(githubUser!=null && githubUser.getId() != 0){
           // System.out.println(githubUser.toString());

            User user = new User();

            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            user.setBio(githubUser.getBio());
            user.setName(githubUser.getLogin());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            user.setGmtCreate(System.currentTimeMillis());
            user.setgmtModified(user.getgmtModified());

            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
            request.getSession().setAttribute("user",user);
            return "redirect:/";

        }
        return "index" ;
    }

}
