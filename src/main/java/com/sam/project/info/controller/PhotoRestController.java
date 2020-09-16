package com.sam.project.info.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sam.common.annotations.Resources;
import com.sam.common.utils.file.AuthFile;
import com.sam.enums.AuthTypeEnum;
import com.sam.framework.controller.SuperController;
import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.responses.ApiResponses;
import com.sam.framework.utils.ApiAssert;
import com.sam.project.info.model.entity.Photo;
import com.sam.project.info.model.parm.PhotoPARM;
import com.sam.project.info.service.IPhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 图片表 前端控制器
 * </p>
 *
 * @author Sam
 * @since 2020-08-06
 */
@Api(tags = {"图片处理相关接口"})
@Controller
@RequestMapping("/photo")
public class PhotoRestController extends SuperController {

    @Value("${custom.localPath}")
    private String localPath;
    @Value("${custom.restPath}")
    private String restPath;

    @Autowired
    private IPhotoService photoService;

    @Resources
    @ResponseBody
    @ApiOperation(value = "上传回调")
    @PostMapping("/callback")
    public ApiResponses<Integer> callback(PhotoPARM photoPARM) {
        System.out.println("photoPARM = " + photoPARM);
        Photo photo = photoPARM.convert(Photo.class);
        photo.setPhotoStatus("临时");
        photo.setPhotoName(photoPARM.getFilename());
        photo.setPhotoUrl(photoPARM.getUrl());
        photo.setPhotoCreateTime(LocalDateTime.now());
        photoService.save(photo);
        return success(photo.getPhotoId());
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ResponseBody
    @ApiOperation(value = "获取图片token有回调")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bucket", value = "空间属性（公有、私有）", required = true, paramType = "query"),
    })
    @GetMapping("/token/callback")
    public ApiResponses<String> getTokenCallback(@RequestParam(value = "bucket") String bucket) {
        //服务器回调地址
        String callbackUrl = restPath + "/photo/callback";
        bucket = StringUtils.equals(bucket, "私有") ? "prv" : "pub";
        //前端将callbackBody数据作为picData
        String callbackBody = "filename=$(x:filename)&photoBucket=$(x:photoBucket)" +
                "&photoTableId=$(x:photoTableId)&photoTableType=$(x:photoTableType)&photoRandom=" +
                "$(x:photoRandom)&photoMimeType=$(x:photoMimeType)" +
                "&photoId=$(x:photoId)&photoUserId=$(x:photoUserId)";
        String token = AuthFile.generate(AuthFile.CustomAccessKey, AuthFile.CustomSecretKey, bucket, callbackUrl, callbackBody);
        return success(token);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ResponseBody
    @ApiOperation(value = "查询图片", response = Photo.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "photoRandom", value = "通过图片标识（att）", paramType = "query"),
            @ApiImplicitParam(name = "photoTableId", value = "外表ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "photoTableType", value = "外表类型（用户等，待补充）", paramType = "query"),
            @ApiImplicitParam(name = "photoStatus", value = "图片状态", paramType = "query"),
            @ApiImplicitParam(name = "pIndex", value = "优先级 true 默认false", dataType = "boolean", paramType = "query"),
    })
    @GetMapping
    public ApiResponses<List<Photo>> list(@RequestParam(name = "photoRandom", required = false) String photoRandom,
                                          @RequestParam(name = "photoTableId", required = false) Integer photoTableId,
                                          @RequestParam(name = "photoTableType", required = false) String photoTableType,
                                          @RequestParam(name = "photoStatus", required = false) String photoStatus,
                                          @RequestParam(name = "pIndex", required = false, defaultValue = "false") Boolean pIndex) {
        LambdaQueryWrapper<Photo> wrapper = Wrappers.<Photo>lambdaQuery()
                .eq(StringUtils.isNotEmpty(photoRandom), Photo::getPhotoRandom, photoRandom)
                .eq(Objects.nonNull(photoTableId), Photo::getPhotoTableId, photoTableId)
                .eq(StringUtils.isNotEmpty(photoTableType), Photo::getPhotoTableType, photoTableType)
                .eq(StringUtils.isNotEmpty(photoStatus), Photo::getPhotoStatus, photoStatus);
        wrapper.last(pIndex, "order by photo_index is null , photo_index asc");
        List<Photo> photoList = photoService.list(wrapper);
        return success(photoList);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ResponseBody
    @ApiOperation(value = "保存图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "photoRandom", value = "随机数", required = true, paramType = "query"),
            @ApiImplicitParam(name = "photoTableId", value = "外表id", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "photoTableType", value = "外表类型", paramType = "query"),
    })
    @PostMapping
    public ApiResponses<Void> create(@RequestParam(name = "photoRandom") String photoRandom,
                                     @RequestParam(name = "photoTableId", required = false) Integer photoTableId,
                                     @RequestParam(name = "photoTableType") String photoTableType) {
        photoService.savePhoto(photoTableId, photoTableType, photoRandom);
        return success();
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ResponseBody
    @ApiOperation(value = "修改图片状态为失效")
    @PutMapping("/failure/{photoTableId}")
    public ApiResponses<Void> updateFailure(@PathVariable("photoTableId") Integer photoTableId) {
        Photo photo = new Photo();
        photo.setPhotoId(photoTableId);
        photo.setPhotoStatus("失效");
        ApiAssert.isTrue(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("修改状态失败！"), photoService.updateById(photo));
        return success();
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ResponseBody
    @ApiOperation(value = "恢复修改图片时删除的正常图片")
    @ApiImplicitParam(name = "photoTableType", value = "外表类型（用户等，待补充）", paramType = "query")
    @PutMapping("/recovery/{photoTableId}")
    public ApiResponses<Void> recovery(@PathVariable("photoTableId") Integer photoTableId,
                                       @RequestParam(name = "photoTableType") String photoTableType) {
        List<Photo> failurePhotoList = photoService.list(Wrappers.<Photo>lambdaQuery()
                .eq(Photo::getPhotoTableId, photoTableId)
                .eq(Photo::getPhotoTableType, photoTableType)
                .eq(Photo::getPhotoStatus, "失效")
                .isNull(Photo::getPhotoRandom));
        if (CollectionUtils.isNotEmpty(failurePhotoList)) {
            LambdaUpdateWrapper<Photo> wrapper = Wrappers.<Photo>lambdaUpdate()
                    .eq(Photo::getPhotoTableId, photoTableId)
                    .eq(Photo::getPhotoTableType, photoTableType)
                    .isNull(Photo::getPhotoRandom)
                    .set(Photo::getPhotoStatus, "正常");
            ApiAssert.isTrue(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("恢复失败！"), photoService.update(wrapper));
        }
        return success();
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ResponseBody
    @ApiOperation(value = "删除七牛云图片和本地图片")
    @ApiImplicitParam(name = "photoIds", value = "图片ID集合", dataType = "int", required = true, paramType = "query")
    @DeleteMapping
    public ApiResponses<Void> delete(@RequestParam List<Integer> photoIds) {
        List<Photo> photos = photoService.list(new QueryWrapper<Photo>().lambda().in(Photo::getPhotoId, photoIds));
        for (Photo photo : photos) {
            photoService.deletePhoto(photo.getPhotoBucket(), photo.getPhotoUrl());
            photoService.removeById(photo.getPhotoId());
        }
        return success(HttpStatus.NO_CONTENT);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ResponseBody
    @ApiOperation(value = "根据随机数删除临时图片")
    @ApiImplicitParam(name = "photoRandom", value = "随机数", required = true, paramType = "query")
    @DeleteMapping("/temporaryPhoto")
    public ApiResponses<Void> deletePhoto(@RequestParam String photoRandom) {
        LambdaQueryWrapper<Photo> queryWrapper = new QueryWrapper<Photo>().lambda().in(Photo::getPhotoRandom, photoRandom).eq(Photo::getPhotoStatus, "临时");
        List<Photo> photos = photoService.list(queryWrapper);
        for (Photo photo : photos) {
            photoService.deletePhoto(photo.getPhotoBucket(), photo.getPhotoUrl());
            photoService.removeById(photo.getPhotoId());
        }
        return success(HttpStatus.NO_CONTENT);
    }

    @Resources(auth = AuthTypeEnum.AUTH)
    @ResponseBody
    @ApiOperation(value = "生成手机访问的url")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "photoRandom", value = "随机数", required = true, paramType = "query"),
            @ApiImplicitParam(name = "photoTableId", value = "外表ID", paramType = "query"),
            @ApiImplicitParam(name = "photoTableType", value = "外表类型", required = true, paramType = "query"),
            @ApiImplicitParam(name = "photoMimeType", value = "文件类型", required = true, paramType = "query"),
            @ApiImplicitParam(name = "photoBucket", value = "存储空间类型", required = true, paramType = "query"),
    })
    @GetMapping("/getMobUploadUrl")
    public ApiResponses<String> getMobUploadUrl(@RequestParam(value = "photoRandom") String photoRandom,
                                                @RequestParam(value = "photoTableId", required = false) String photoTableId,
                                                @RequestParam(value = "photoTableType") String photoTableType,
                                                @RequestParam(value = "photoMimeType") String photoMimeType,
                                                @RequestParam(value = "photoBucket") String photoBucket) {
        return success(localPath + "/photos/qrUpload?photoRandom=" + photoRandom + (Objects.nonNull(photoTableId) ? ("&photoTableId=" + photoTableId) : "") +
                "&photoTableType=" + photoTableType + "&photoMimeType=" + photoMimeType + "&photoBucket=" + photoBucket + "&photoUserId=" + currentUid());
    }

    @ApiOperation(value = "验证qr及co,通过则跳转上传页，不通过则返回error")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "photoRandom", value = "随机数", required = true, paramType = "query"),
            @ApiImplicitParam(name = "photoTableId", value = "外表ID", paramType = "query"),
            @ApiImplicitParam(name = "photoTableType", required = true, value = "外表类型", paramType = "query"),
            @ApiImplicitParam(name = "photoMimeType", required = true, value = "文件类型", paramType = "query"),
            @ApiImplicitParam(name = "photoBucket", required = true, value = "存储空间类型", paramType = "query"),
            @ApiImplicitParam(name = "photoUserId", required = true, value = "上传人ID", paramType = "query"),
    })
    @GetMapping("/qrUpload")
    public String qrUpload(@RequestParam(value = "photoRandom") String photoRandom,
                           @RequestParam(value = "photoTableId", required = false) Integer photoTableId,
                           @RequestParam(value = "photoTableType") String photoTableType,
                           @RequestParam(value = "photoMimeType") String photoMimeType,
                           @RequestParam(value = "photoBucket") String photoBucket,
                           @RequestParam(value = "photoUserId") Integer photoUserId) {
        System.out.println("photoTableType = " + photoTableType);
        System.out.println("photoMimeType = " + photoMimeType);
        System.out.println("photoBucket = " + photoBucket);
        try {
            photoTableType = URLEncoder.encode(photoTableType, "utf-8");
            photoMimeType = URLEncoder.encode(photoMimeType, "utf-8");
            photoBucket = URLEncoder.encode(photoBucket, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "redirect:" + localPath + "/photo/mobUpload.html?photoRandom=" + photoRandom + (Objects.nonNull(photoTableId) ? ("&photoTableId=" + photoTableId) : "") +
                "&photoTableType=" + photoTableType + "&photoMimeType=" + photoMimeType + "&photoBucket=" + photoBucket + "&photoUserId=" + photoUserId;
    }
}

