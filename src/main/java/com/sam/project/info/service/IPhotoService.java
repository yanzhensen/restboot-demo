package com.sam.project.info.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sam.project.info.model.entity.Photo;

/**
 * <p>
 * 图片表 服务类
 * </p>
 *
 * @author Sam
 * @since 2020-08-06
 */
public interface IPhotoService extends IService<Photo> {

    /**
     * 删除图片
     *
     * @param photoBucket
     * @param photoUrl
     */
    void deletePhoto(String photoBucket, String photoUrl);

    /**
     * 保存图片
     *
     * @param tableId
     * @param tableType
     * @param randomNum
     */
    void savePhoto(Integer tableId, String tableType, String randomNum);
}
