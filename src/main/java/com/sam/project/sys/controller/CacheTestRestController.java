package com.sam.project.sys.controller;

import com.sam.common.annotations.Resources;
import com.sam.enums.AuthTypeEnum;
import com.sam.framework.controller.SuperController;
import com.sam.framework.responses.ApiResponses;
import com.sam.project.sys.model.entity.User;
import com.sam.project.sys.service.ICacheTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author Sam
 * @since 2020-07-05
 */
@Api(tags = {"缓存测试"})
@Validated
@RestController
@RequestMapping("/cache")
public class CacheTestRestController extends SuperController {

    @Autowired
    private ICacheTestService cacheTestService;

    @Resources(auth = AuthTypeEnum.OPEN)
    @ApiOperation("缓存redis指定key生成")
    @GetMapping("/redisHasKey")
    public ApiResponses<User> redisHasKey(Integer id) {
        User user = cacheTestService.getUserFromRedisHasKey(id);
        return success(user);
    }

    @Resources(auth = AuthTypeEnum.OPEN)
    @ApiOperation("缓存redis不指定key生成")
    @GetMapping("/redisNoKey")
    public ApiResponses<User> redisNoKey(Integer id) {
        User user = cacheTestService.getUserFromRedisNoKey(id);
        return success(user);
    }

    @Resources(auth = AuthTypeEnum.OPEN)
    @ApiOperation("删除redis缓存")
    @GetMapping("/delCacheRedis")
    public ApiResponses<String> delCacheRedis(Integer id) {
        String str = cacheTestService.delCacheRedis(id);
        return success(str);
    }


}

