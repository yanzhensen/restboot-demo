package com.sam;

import com.sam.common.utils.file.AuthFile;
import com.sam.framework.config.Global;
import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.utils.ApiAssert;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestbootApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("Global = " + Global.getName());
        //秘钥
        String accessKey = "87csdas54dd80g";
        String secretKey = "47486asz4k4366";
        String token = AuthFile.generate(accessKey, secretKey, "", "callbackUrl", "callbackBody");
        System.out.println("token = " + token);
        ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, token);
        Claims claims = AuthFile.getClaim(token);
        System.out.println("token = " + token);
        System.out.println("claims = " + claims);
        ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, claims);
        String secret_key = claims.get(AuthFile.SECRET_KEY, String.class);
        System.out.println(secret_key);
    }

}
