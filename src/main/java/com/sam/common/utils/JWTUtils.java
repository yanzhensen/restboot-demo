package com.sam.common.utils;

import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.utils.ApiAssert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * JWT token 生成工具类
 * </p>
 *
 * @author Caratacus
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class JWTUtils {

    public static final String UID = "uid";
    public static final String CO = "co";

    /**
     * 生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。
     * 它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
     */

    //分钟单位
    private static final long EXPIRE = 60 * 1000;

    //密文
    private static String SECRET;
    //分钟
    private static long MINUTE;

    @Value("${custom.token.secret}")
    public void setSecret(String secret) {
        SECRET = secret;
    }

    @Value("${custom.token.expire}")
    public void setMinute(long minute) {
        MINUTE = minute;
    }

    /**
     * 生成token
     * token 生成策略：
     * 1.设置真正的token 7天有效期  7天后强制重登   (主要)
     * 2.登录后设置redis作为判断登录根据 设置每次操作有效时间 30分钟  操作则续时长  (次要)
     * 30分钟未操作 redis key失效 USER_ACCESS_TOKEN_ + "UID"
     * 拦截器：
     * 1.先判断token是否过期  2.判断redis是否失效
     * 异常：（token不建议持久化操作）
     * 1.多人登录生成多个token - 解决 在登录的时候 使用redis实现顶号操作
     * 2.多个token都没过期怕被盗用 - 解决 使用reids做黑名单二次判断
     * 3.想要多人登录怎么办 - 解决 使用redis设置容量 默认最大token数量为3个 多个顶掉
     * 4.项目内互调用 token怎么共享 这要在过滤器中多加判断了
     * 调用时候可以建立一个短时间5分钟的特殊token(token前缀来个特殊标识 如：FREE_+ "")
     * 如果识别到FREE_则校验有效时长 通过则放行
     * 5.redis挂了怎么判断有效时长
     *
     * @param uid
     * @return
     */
    public static String generate(Integer uid) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + EXPIRE * MINUTE);//设置有效时间 单位毫秒
        Map<String, Object> claims = new HashMap<>(1);//创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        claims.put(UID, uid);
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

    /**
     * 获取jwt发布时间
     */
    public static Date getIssuedAt(String token) {
        return getClaim(token).getIssuedAt();
    }

    /**
     * 获取UID
     */
    public static Integer getUid(String token) {
        return TypeUtils.castToInt(getClaim(token).get(UID));
    }

    /**
     * 获取CO
     */
    public static String getCo(String token) {
        return TypeUtils.castToString(getClaim(token).get(CO));
    }

    /**
     * 获取jwt失效时间
     */
    public static Date getExpiration(String token) {
        return getClaim(token).getExpiration();
    }

    /**
     * 验证token是否失效
     *
     * @param token
     * @return true:过期   false:没过期
     */
    public static boolean isExpired(String token) {
        try {
            final Date expiration = getExpiration(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

}
