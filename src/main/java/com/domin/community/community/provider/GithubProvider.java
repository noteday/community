package com.domin.community.community.provider;



import com.alibaba.fastjson.JSON;
import com.domin.community.community.dto.AccesssTokenDTO;
import com.domin.community.community.dto.GithubUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class GithubProvider {
    public String getAccesssToken(AccesssTokenDTO accesssTokenDTO){
        MediaType mediaType = MediaType.get("application/json;charset=utf-8");
        OkHttpClient client =new OkHttpClient();

        System.out.println(accesssTokenDTO.toString());
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accesssTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try(Response response = client.newCall(request).execute()){
            String string = response.body().string();
            System.out.println(string);
            String token = string.split("&")[0].split("=")[1];
            return  token;
        }catch (Exception e) {
            //log.error("",accesssTokenDTO,e);
        }

        return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response =client.newCall(request).execute();
            String string = response.body().string();
            System.out.println(string);
            GithubUser githubUser = JSON.parseObject(string,GithubUser.class);
            return githubUser;
        }catch (Exception e){
            //log.error("");
        }
        return null;
    }
}


