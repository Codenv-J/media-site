package com.coden.controller;

import cn.hutool.core.io.IoUtil;
import com.coden.auth.Permission;
import com.coden.auth.PermissionEnum;
import com.coden.common.MessageConstant;
import com.coden.config.SystemConfig;
import com.coden.intercepter.SensitiveFilter;
import com.coden.intercepter.SensitiveWordInit;
import com.coden.util.BaseApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.coden.controller.FileController.extracted;


@Api("系统设置的配置模块")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/system")
public class SystemConfigController {

    public static final String STATIC_CENSOR_WORD_TXT = "static" + File.separator + "censorword.txt";

    @Resource
    SystemConfig systemConfig;

    @Permission(PermissionEnum.ADMIN)
    @GetMapping("getConfig")
    @ApiOperation(value = "管理员获取系统设置", notes = "只有管理员有权限修改系统的设置信息")
    public BaseApiResult getSystemConfig() {
        return BaseApiResult.success(systemConfig);
    }

    @Permission({PermissionEnum.ADMIN})
    @ApiOperation(value = "管理员修改系统设置", notes = "只有管理员有权限修改系统的设置信息")
    @PutMapping("updateConfig")
    public BaseApiResult systemConfig(@RequestBody SystemConfig userSetting) {
        if (userSetting.getUserUpload() == null || userSetting.getUserRegistry() == null
                || userSetting.getAdminReview() == null || userSetting.getProhibitedWord() == null) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
        systemConfig.setUserUpload(userSetting.getUserUpload());
        systemConfig.setProhibitedWord(userSetting.getProhibitedWord());
        systemConfig.setUserRegistry(userSetting.getUserRegistry());
        systemConfig.setAdminReview(userSetting.getAdminReview());
        return BaseApiResult.success(userSetting);
    }

    @ApiOperation(value = "管理员下载最新的违禁词")
    @GetMapping(value = "getProhibitedWord", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public void downloadTxt(HttpServletResponse response) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(STATIC_CENSOR_WORD_TXT);
            InputStream inputStream = classPathResource.getInputStream();
            byte[] buffer = IoUtil.readBytes(inputStream);
            extracted(response, buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @ApiOperation(value = "管理员更新违禁词")
    @PostMapping(value = "updateProhibitedWord")
    public BaseApiResult updateProhibitedWord(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty() || file.getSize() > 20000) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
        String originFileName = file.getOriginalFilename();
        originFileName = Optional.ofNullable(originFileName).orElse("");
        String suffix = originFileName.substring(originFileName.lastIndexOf(".") + 1).toLowerCase(Locale.ROOT);
        if (!ObjectUtils.nullSafeEquals(suffix, "txt")) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }


        try {
            String fileCode = codeString(file.getInputStream());
            Set<String> strings = SensitiveWordInit.getStrings(file.getInputStream(), Charset.forName(fileCode));
            writeToFile(strings);
            SensitiveFilter filter = SensitiveFilter.getInstance();
            filter.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE, MessageConstant.OPERATE_FAILED);
        }

        return BaseApiResult.success();
    }

    private void writeToFile(Set<String> strSet) throws IOException {
        String txt = strSet.stream().limit(10000).collect(Collectors.joining("\n"));
        ClassPathResource classPathResource = new ClassPathResource(STATIC_CENSOR_WORD_TXT);
        String replacedTxt = txt.replace(" ", "");

        String filePath= this.getClass().getClassLoader().getResource(STATIC_CENSOR_WORD_TXT).getFile();
        File file= new File(filePath);
        // 通过流文件复制到file中
        FileUtils.copyToFile(classPathResource.getInputStream(), file);

        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        try (OutputStreamWriter out = new OutputStreamWriter(fileOutputStream, "UTF-8")) {
            out.write(replacedTxt);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断文件的编码格式
     * @param inputStream :file
     * @return 文件编码格式
     * @throws Exception
     */
    public static String codeString(InputStream inputStream) throws IOException{
        BufferedInputStream bin = new BufferedInputStream(inputStream);
        int p = (bin.read() << 8) + bin.read();
        String code = null;

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        IOUtils.closeQuietly(bin);
        return code;
    }

}
