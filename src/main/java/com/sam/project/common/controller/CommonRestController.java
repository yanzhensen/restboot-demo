package com.sam.project.common.controller;

import com.sam.common.cons.Constants;
import com.sam.common.utils.file.AuthFile;
import com.sam.common.utils.file.FileUploadUtils;
import com.sam.common.utils.file.FileUtils;
import com.sam.framework.config.Global;
import com.sam.framework.controller.SuperController;
import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.responses.ApiResponses;
import com.sam.framework.utils.ApiAssert;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 公共接口
 * </p>
 *
 * @author szt21002
 * @since 2020-08-05
 */
@Api(tags = {"公共接口"})
@Controller
@RequestMapping("/common")
public class CommonRestController extends SuperController {

    private Logger logger = LoggerFactory.getLogger(CommonRestController.class);
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 配置niginx server_name配置：
     * 问：nginx server中的server_name配置的域名在客户机上无法访问
     * 答：C:\Windows\System32\drivers\etc hosts里面加上server_name 如：127.0.0.1 pho.pub.com
     * 访问：http://pho.pub.com/profile
     * #二级域名 多域名则创建多个  此配置为本地路径转发  不是代理转发
     * server {
     * listen       80;
     * server_name  pho.pub.com;
     * <p>
     * proxy_set_header X-Forwarded-Host $host;
     * proxy_set_header X-Forwarded-Server $host;
     * proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     * <p>
     * location / {
     * root html;
     * index  index.html index.htm;
     * try_files $uri $uri/ /index.html;
     * }
     * <p>
     * #配置图片服务器位置 不在nginx html路径下的配置  在nginx路径下可不配
     * location /profile {
     * #root D:/SamPhoto/uploadPath/pub;
     * alias D:/SamPhoto/uploadPath/pub;
     * #打开目录浏览功能
     * autoindex on;
     * }
     * <p>
     * location = /50x.html {
     * root html;
     * }
     * <p>
     * error_page 500 502 503 504 /50x.html;
     * }
     * <p>
     * String token = auth.uploadToken(bucketName, null, 300, new StringMap()
     * .put("callbackUrl", extranetPath + "/common1/photos/callback")
     * .put("callbackBody", "filename=$(fname)&key=$(key)&mimeType=$(mimeType)&url=" + url +
     * "&photoTableId=$(x:photoTableId)&photoTableType=$(x:photoTableType)&co=$(x:co)&photoRandom=" +
     * "$(x:photoRandom)&photoMimeType=$(x:photoMimeType)&photoBucket=$(x:photoBucket)" +
     * "&photoId=$(x:photoId)&photoUserId=$(x:photoUserId)")
     * .put("saveKey", "$(etag)" + (int) ((Math.random() * 9 + 1) * 1000) + "$(ext)")
     * .put("insertOnly", 1));
     */
    @ApiOperation(value = "图片上传")
    @ResponseBody
    @PostMapping("/upload")
    public ApiResponses<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("token") String token) {
        ApiAssert.isFalse(ErrorCodeEnum.PHO_RESOURCE_NOT_EXIST, file.isEmpty() || StringUtils.isEmpty(file.getOriginalFilename()));
        //取出token解密
        Claims claim = AuthFile.getClaim(token);
        String callbackUrl = claim.get(AuthFile.CALLBACK_URL, String.class);
        String callbackBody = claim.get(AuthFile.CALLBACK_BODY, String.class);
        String bucket = claim.get(AuthFile.BUCKET, String.class);
        ApiAssert.isTrue(ErrorCodeEnum.PHO_BUCKET_NOT_EXIST, AuthFile.BUCKET_LIST.contains(bucket));
        String result = "";
        try {
            // 上传文件路径
            String filePath = Global.getProfile() + "/" + bucket;
            // 上传并返回新文件名称 下载在yml配置的文件里面
            String fileName = FileUploadUtils.upload(filePath, file);
            // fileName = /profile/df517a3e8788499497ad82031749c4ae.png
            //返回一个可请求的路径  以后配置服务器 使用bucket来区分分区
            String url = "http://pho." + bucket + ".com" + fileName;
            //没有设置回调则直接返回url路径
            if (StringUtils.isEmpty(callbackUrl)) {
                return success(url);
            }
            //请求回调函数
            try {
                //post请求 body参数
                MultiValueMap<String, String> map = splitBodyParm(callbackBody, request);
                map.set("url", url);
                result = restTemplate.postForObject(callbackUrl, map, String.class);
            } catch (Exception e) {
                logger.error("服务器回调异常", e);
                deleteFile(fileName, bucket);
                ApiAssert.failure(ErrorCodeEnum.PHO_INTERNAL_SERVER_ERROR.convert("服务器回调异常"));
            }
        } catch (IOException e) {
            logger.error("上传图片，io读取异常", e);
            ApiAssert.failure(ErrorCodeEnum.PHO_INTERNAL_SERVER_ERROR.convert("上传图片，io读取异常"));
        }
        return success(result);
    }

    //todo 暂未测试
    @ApiOperation(value = "图片下载（暂未测试）", notes = "通用下载请求")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名称", paramType = "query"),
            @ApiImplicitParam(name = "delete", value = "是否删除 true/false", paramType = "query"),
    })
    @GetMapping("/download")
    public void fileDownload(@RequestParam("fileName") String fileName,
                             @RequestParam(value = "delete", required = false) Boolean delete,
                             HttpServletRequest request, HttpServletResponse response) {
        try {
            ApiAssert.isTrue(ErrorCodeEnum.PHO_INTERNAL_SERVER_ERROR.convert("文件名称(" + fileName + ")非法，不允许下载。 "), FileUtils.isValidFilename(fileName));
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = Global.getDownloadPath() + fileName;
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (BooleanUtils.isTrue(delete)) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            logger.error("下载文件失败", e);
        }
    }

    //todo 私有图片nginx配置token
    //http://pic-public.fangzhizun.com/FluMy9NouV1Shu65YN40smGGZ4Oz8672.jpg?e=1599446746&token=UNsQR-VxUCoEzgbUIhmqw7vlwbZt06AtiEr6HPOj:eRZ-TwerXsKzITZqKQDnDFqqwCs=
    @ApiOperation(value = "本地图片下载", notes = "本地资源通用下载")
    @GetMapping("/download/resource")
    public void resourceDownload(@RequestParam("resource") String resource, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 路径/profile/df517a3e8788499497ad82031749c4ae.png
            // 数据库资源地址
            String downloadPath = Global.getProfile() + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (IOException e) {
            logger.error("下载文件失败", e);
        }
    }

    @ResponseBody
    @ApiOperation(value = "删除本地图片", notes = "resource = /profile/df517a3e8788499497ad82031749c4ae.png")
    @DeleteMapping("/delete")
    public ApiResponses<Void> delete(@RequestParam("resource") String resource, @RequestParam("token") String token) {
        //校验token格式
        Claims claim = AuthFile.getClaim(token);
        //todo 校验accessKey secretKey有效性
        String bucket = claim.get("bucket", String.class);
        boolean res = deleteFile(resource, bucket);
        ApiAssert.isTrue(ErrorCodeEnum.PHO_RESOURCE_NOT_EXIST, res);
        return success();
    }

    /**
     * 删除图片
     * /profile/df517a3e8788499497ad82031749c4ae.png
     *
     * @param resource 路径
     * @param resource 空间
     * @return
     */
    private boolean deleteFile(String resource, String bucket) {
        //本地资源路径
        String localPath = Global.getProfile() + "/" + bucket;
        // 数据库资源地址
        String filePath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
        return FileUtils.deleteFile(filePath);
    }

    /**
     * 参数匹配
     *
     * @param callbackBody
     * @param request
     * @return
     */
    private MultiValueMap<String, String> splitBodyParm(String callbackBody, HttpServletRequest request) {
        /**
         * request:/restboot/common/upload?x:filename=人头1&x:photoMimeType=图片&x:photoTableType=用户&x:photoRandom=846515616&x:photoUserId=6&x:photoBucket=公有
         * 将request的里面的数据填充到 callbackBody：photoTableId=$(x:photoTableId)&photoTableType=$(x:photoTableType)
         */
        Map<String, String> stringMap = new HashMap<>();
        Enumeration<String> pNames = request.getParameterNames();
        while (pNames.hasMoreElements()) {
            String name = pNames.nextElement();
            String value = request.getParameter(name);
            stringMap.put(name, value);
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap();
        String[] params = callbackBody.split("&");
        for (String parm : params) {
            String[] keyValue = parm.split("=");
            if (keyValue[1].indexOf("$(") >= 0) {
                String zt = stringMap.get(keyValue[1].substring(2, keyValue[1].length() - 1));
                if (StringUtils.isNotEmpty(zt)) {
                    map.set(keyValue[0], zt);
                }
            }
        }
        return map;
    }

}
