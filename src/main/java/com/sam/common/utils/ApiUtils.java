package com.sam.common.utils;

import com.sam.common.spring.ApplicationUtils;
import com.sam.framework.cons.APICons;
import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.utils.ApiAssert;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * API工具类
 *
 * @author Caratacus
 */
@SuppressWarnings("ALL")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public abstract class ApiUtils {

    /**
     * 获取当前用户id,从jwt获取
     */
    public static Integer currentUid() {
        String token = ApplicationUtils.getRequest().getHeader("Authorization");
        ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, token);
        //Authorization: Bearer <token>
        token = token.replaceFirst("Bearer ", "");
        Claims claims = JWTUtils.getClaim(token);
        ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, claims);
        return claims.get(JWTUtils.UID, Integer.class);
    }

    /**
     * 获取当前公司标识
     */
    public static String currentCo() {
        String token = ApplicationUtils.getRequest().getHeader("Authorization");
        ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, token);
        token = token.replaceFirst("Bearer ", "");
        Claims claims = JWTUtils.getClaim(token);
        ApiAssert.notNull(ErrorCodeEnum.UNAUTHORIZED, claims);
        return claims.get(JWTUtils.CO, String.class);
    }

    /**
     * MD5加密
     **/
    public static String toMD5(String plainText, Integer md5Type) {
        try {
            // 生成实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节数组更新摘要。
            md.update(plainText.getBytes());
            // 通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest();
            // 生成具体的md5密码到buf数组
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            if (md5Type == 16) {
                return buf.toString().substring(8, 24);
            } else if (md5Type == 32) {
                return buf.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * list<Map>转换list<bean>
     *
     * @param mapList
     * @param clazz
     * @return
     */
    public static <T> List<T> listMapToListBean(List<Map<String, Object>> mapList, Class<T> clazz) {
        List<T> res = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            res.add(mapToBean(map, clazz));
        }
        return res;
    }

    /**
     * map转实体
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
            if (map != null) {
                Field[] declaredFields = clazz.getDeclaredFields();
                if (declaredFields != null) {
                    for (Field declaredField : declaredFields) {
                        declaredField.setAccessible(true);
                        Set<String> mapKeys = map.keySet();
                        for (String mapKey : mapKeys) {
                            //判断属性类型 进行转换,map中存放的是Object对象需要转换 实体类中有多少类型就加多少类型,实体类属性用包装类;
                            if (declaredField.getType().toString().contains("Integer")) {
                                if (declaredField.getName().equals(mapKey)) {
                                    declaredField.set(t, Integer.valueOf(map.get(mapKey).toString()));
                                    break;
                                }
                            }
                            //判断属性类型 进行转换; Double
                            if (declaredField.getType().toString().contains("Double")) {
                                if (declaredField.getName().equals(mapKey)) {
                                    declaredField.set(t, Double.valueOf(map.get(mapKey).toString()));
                                    break;
                                }
                            }
                            //判断属性类型 进行转换;
                            if (declaredField.getType().toString().contains("String")) {
                                if (declaredField.getName().equals(mapKey)) {
                                    declaredField.set(t, map.get(mapKey));
                                    break;
                                }
                            }
                            //判断属性类型 进行转换;LocalDate
                            if (declaredField.getType().toString().contains("LocalDateTime")) {
                                if (declaredField.getName().equals(mapKey)) {
                                    String value = map.get(mapKey).toString();
                                    if (value.indexOf(".0") > 0) {
                                        value = value.substring(0, value.length() - 2);
                                    }
                                    Timestamp timestamp = Timestamp.valueOf(value);
                                    LocalDateTime times = timestamp.toLocalDateTime();
                                    declaredField.set(t, times);
                                    break;
                                }
                            }
                            //判断属性类型 进行转换;LocalDate
                            if (declaredField.getType().toString().contains("LocalDate")) {
                                if (declaredField.getName().equals(mapKey)) {
                                    LocalDate time = LocalDate.parse(map.get(mapKey).toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    declaredField.set(t, time);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ApiAssert.isFalse(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("bean转换异常"), true);
        }
        return t;
    }

}
