package com.example.stock_dividend.model;

import lombok.Data;

import java.util.List;

public class Auth {
    @Data
    public static class SignIn{
        String username;
        String password;
    }

    @Data
    public static class SignUp{
        String username;
        String password;
        private List<String> roles;

        public MemberEntity toEntity(){
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .roles(this.roles)
                    .build();
        }


    }
}
