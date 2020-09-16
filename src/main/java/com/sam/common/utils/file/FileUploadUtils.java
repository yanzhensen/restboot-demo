package com.sam.common.utils.file;

import com.sam.common.cons.Constants;
import com.sam.common.utils.ApiUtils;
import com.sam.framework.config.Global;
import com.sam.framework.enums.ErrorCodeEnum;
import com.sam.framework.utils.ApiAssert;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 文件上传工具类
 *
 * @author Sam
 */
public class FileUploadUtils {
    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 默认上传的地址
     */
    private static String defaultBaseDir = Global.getProfile();

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    public static final String upload(MultipartFile file) throws IOException {
        return upload(defaultBaseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
    }

    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException {
        return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
    }

    /**
     * 文件上传
     *
     * @param baseDir          相对应用的基目录
     * @param file             上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws IOException 比如读写文件出错时
     */
    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension) throws IOException {
        int fileNamelength = file.getOriginalFilename().length();
        ApiAssert.isFalse(ErrorCodeEnum.PHO_INTERNAL_SERVER_ERROR.convert("文件名过长 最长：" + FileUploadUtils.DEFAULT_FILE_NAME_LENGTH + "，当前：" + fileNamelength),
                fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        assertAllowed(file, allowedExtension);
        String fileName = extractFilename(file);
        File desc = getAbsoluteFile(baseDir, fileName);
        file.transferTo(desc);
        String pathFileName = getPathFileName(baseDir, fileName);
        return pathFileName;
    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
//        fileName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/" + encodingFilename(fileName) + "." + extension;
        fileName = encodingFilename(fileName) + "." + extension;
        return fileName;
    }

    private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
        File desc = new File(uploadDir + File.separator + fileName);
        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;
    }

    private static final String getPathFileName(String uploadDir, String fileName) {
//        int dirLastIndex = Global.getProfile().length() + 1;
//        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
//        String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        String pathFileName = Constants.RESOURCE_PREFIX + "/" + fileName;
        return pathFileName;
    }

    /**
     * 编码文件名
     */
    private static final String encodingFilename(String fileName) {
        fileName = fileName.replace("_", " ");
        //System.nanoTime() 纳秒 1纳秒=0.000001 毫秒
        fileName = ApiUtils.toMD5(fileName + System.nanoTime());
        return fileName;
    }

    /**
     * 文件大小校验
     *
     * @param file             上传的文件
     * @param allowedExtension 上传文件类型
     * @return
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension) {
        long size = file.getSize();
        ApiAssert.isFalse(ErrorCodeEnum.PHO_INTERNAL_SERVER_ERROR.convert("超出最大上传大小" + DEFAULT_MAX_SIZE / 1024 / 1024), DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE);
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        //文件校验异常
        //super("filename : [" + filename + "], extension : [" + extension + "], allowed extension : [" + Arrays.toString(allowedExtension) + "]");
        String msg = "filename : [" + fileName + "], extension : [" + extension + "], allowed extension : [" + Arrays.toString(allowedExtension) + "]";
        ApiAssert.isFalse(ErrorCodeEnum.PHO_INTERNAL_SERVER_ERROR.convert(msg), allowedExtension != null && !isAllowedExtension(extension, allowedExtension));
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension        文件后缀
     * @param allowedExtension 上传文件类型
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }
}
