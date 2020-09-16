package com.sam.project.info.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sam.common.utils.ApiUtils;
import com.sam.common.utils.file.AuthFile;
import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.utils.ApiAssert;
import com.sam.project.info.mapper.PhotoMapper;
import com.sam.project.info.model.entity.Photo;
import com.sam.project.info.service.IPhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * <p>
 * 图片表 服务实现类
 * </p>
 *
 * @author Sam
 * @since 2020-08-06
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements IPhotoService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${custom.photoPath}")
    private String photoPath;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void deletePhoto(String photoBucket, String photoUrl) {
        // 静态创建域名数组
        String[] domainNames = new String[]{"http://pho.pub.com", "http://pho.pub.com"};
        for (String domainName : domainNames) {
            // 检索fileName路径字符串是否包含数组中的域名
            if (photoUrl.contains(domainName)) {
                photoUrl.replaceAll(domainName, "");
            }
        }
        String uri = photoPath + "/common/delete";
        String token = AuthFile.generateBucket(AuthFile.CustomAccessKey, AuthFile.CustomSecretKey, photoBucket);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("resource", photoUrl);
        map.set("token", token);
        uri = !map.isEmpty() ? UriComponentsBuilder.fromUriString(uri).queryParams(map).toUriString() : uri;
        try {
            restTemplate.delete(uri);
        } catch (RestClientException e) {
            logger.error("图片删除失败 bucket = " + photoBucket + " url = " + photoUrl, e);
            ApiAssert.failure(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("删除失败"));
        }
    }

    @Override
    public void savePhoto(Integer tableId, String tableType, String randomNum) {
        //不执行图片
        if (StringUtils.isEmpty(randomNum) || StringUtils.isEmpty(tableType)) {
            return;
        }
        List<Photo> list = list(new LambdaQueryWrapper<Photo>().eq(Photo::getPhotoRandom, randomNum)
                .eq(Photo::getPhotoStatus, "临时").eq(Photo::getPhotoTableType, tableType));
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        LambdaUpdateWrapper<Photo> wrapper = Wrappers.<Photo>lambdaUpdate()
                .eq(Photo::getPhotoRandom, randomNum)
                .eq(Photo::getPhotoTableType, tableType)
                .set(Photo::getPhotoTableId, tableId)
                .set(Photo::getPhotoUserId, ApiUtils.currentUid())
                .set(Photo::getPhotoStatus, "正常")
                .set(Photo::getPhotoRandom, null);
        ApiAssert.isTrue(ErrorCodeEnum.INTERNAL_SERVER_ERROR.convert("保存图片失败"), update(wrapper));
    }
}
