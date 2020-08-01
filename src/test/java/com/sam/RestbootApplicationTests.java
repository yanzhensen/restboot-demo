package com.sam;

import com.sam.common.utils.JWTUtils;
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
        String token = JWTUtils.generate(1, "restboot");
        System.out.println("token = " + token);
        ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, token);
        Claims claims = JWTUtils.getClaim(token);
        System.out.println("token = " + token);
        System.out.println("claims = " + claims);
        ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, claims);
        Integer uid = claims.get(JWTUtils.UID, Integer.class);
        String co = claims.get(JWTUtils.CO, String.class);
        System.out.println(uid);
        System.out.println(co);
    }

}
