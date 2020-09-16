package com.sam.common.utils.file;

import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.utils.ApiAssert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 图片认证
 *
 * @author Sam
 */
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthFile {

    public static final String CustomAccessKey = "87csdas54dd80g";
    public static final String CustomSecretKey = "47486asz4k4366";

    public static final String ACCESS_KEY = "accessKey";
    public static final String SECRET_KEY = "secretKey";
    public static final String BUCKET = "bucket";
    public static final String CALLBACK_URL = "callbackUrl";
    public static final String CALLBACK_BODY = "callbackBody";

    public static final List<String> BUCKET_LIST = Arrays.asList("pub", "prv");

    //单位：分钟
    private static final long EXPIRE = 60 * 1000;

    //密文
    private static String SECRET;
    //分钟
    private static long MINUTE;


    @Value("${custom.pho-token.secret}")
    public void setSecret(String secret) {
        SECRET = secret;
    }

    @Value("${custom.pho-token.expire}")
    public void setMinute(long minute) {
        MINUTE = minute;
    }

    /**
     * 生成token
     *
     * @param accessKey
     * @param secretKey
     * @param bucket
     * @param callbackUrl
     * @param callbackBody
     * @return
     */
    public static String generate(String accessKey, String secretKey, String bucket, String callbackUrl, String callbackBody) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + EXPIRE * MINUTE);//设置有效时间 单位毫秒
        Map<String, Object> claims = new HashMap<>(1);//创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        claims.put(ACCESS_KEY, accessKey);
        claims.put(SECRET_KEY, secretKey);
        claims.put(BUCKET, bucket);
        claims.put(CALLBACK_URL, callbackUrl);
        claims.put(CALLBACK_BODY, callbackBody);
        return Jwts.builder()//这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)//如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setIssuedAt(nowDate)//iat: jwt的签发时间
                .setExpiration(expireDate)//设置过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET)//设置签名使用的签名算法和签名使用的秘钥
                .compact();//就开始压缩为xxxxxxxxxxxxxx.xxxxxxxxxxxxxxx.xxxxxxxxxxxxx这样的jwt
    }

    public static String generateBucket(String accessKey, String secretKey, String bucket) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + EXPIRE * MINUTE);//设置有效时间 单位毫秒
        Map<String, Object> claims = new HashMap<>(1);//创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        claims.put(ACCESS_KEY, accessKey);
        claims.put(SECRET_KEY, secretKey);
        claims.put(BUCKET, bucket);
        return Jwts.builder()//这里其实就是new一个JwtBuilder，设置jwt的body
                .setClaims(claims)//如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setIssuedAt(nowDate)//iat: jwt的签发时间
                .setExpiration(expireDate)//设置过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET)//设置签名使用的签名算法和签名使用的秘钥
                .compact();//就开始压缩为xxxxxxxxxxxxxx.xxxxxxxxxxxxxxx.xxxxxxxxxxxxx这样的jwt
    }

    /**
     * 解析Claims
     *
     * @param token
     * @return
     */
    public static Claims getClaim(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            ApiAssert.failure(ErrorCodeEnum.UNAUTHORIZED);
        }
        return claims;
    }
}
