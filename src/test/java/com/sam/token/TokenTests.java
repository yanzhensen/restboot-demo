package com.sam.token;

import com.sam.common.utils.JWTUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class TokenTests {

    @Test
    void tokenAnalysis(){
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDc3NjE4ODQsInVpZCI6MSwiaWF0IjoxNjA3NzYwNjg0fQ.E9bK70h1gDRRcz3HgPcjhQowKEuzRCeWzrM0q1fAEOM";
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDc3NjI0MDAsInVpZCI6MSwiaWF0IjoxNjA3NzYxMjAwfQ.dQDbPO81XjYZ7pw8t8gitaAsAxssrxpISOzCmtGp5ZE";
        boolean isExpired = JWTUtils.isExpired(token);
        System.out.println("token是否过期 = " + isExpired);
        Integer uid = JWTUtils.getUid(token);
        System.out.println("uid = " + uid);
        Date issuedAt = JWTUtils.getIssuedAt(token);
        System.out.println("发布时间 = " + issuedAt);
        Date expiration = JWTUtils.getExpiration(token);
        System.out.println("失效时间 = " + expiration);

    }

}
