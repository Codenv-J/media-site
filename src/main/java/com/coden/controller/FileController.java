package com.coden.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.coden.auth.Permission;
import com.coden.auth.PermissionEnum;
import com.coden.util.UserHolder;
import com.google.common.collect.Lists;
import com.coden.common.MessageConstant;
import com.coden.entity.FileDocument;
import com.coden.entity.ResponseModel;
import com.coden.enums.DocStateEnum;
import com.coden.service.IFileService;
import com.coden.service.TaskExecuteService;
import com.coden.util.BaseApiResult;
import com.coden.util.FileContentTypeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Api(tags = "文档上传、下载模块")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("files")
public class FileController {

    private static final String DOT = ".";

    @Autowired
    private IFileService fileService;

    @Autowired
    private TaskExecuteService taskExecuteService;


    /**
     * 列表数据
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public List<FileDocument> list(int pageIndex, int pageSize) {
        return fileService.listFilesByPage(pageIndex, pageSize);
    }

    /**
     * 在线显示文件
     * @param id 文件id
     * @return
     */
    @ApiOperation(value = "在线显示文件", notes = "")
    @GetMapping("/view/{id}")
    public ResponseEntity<Object> serveFileOnline(@PathVariable String id) throws UnsupportedEncodingException {
        Optional<FileDocument> file = fileService.getById(id);
        if (file.isPresent()) {
            return ResponseEntity.ok()
                    // 这里需要进行中文编码
                    .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=" + URLEncoder.encode(file.get().getName(), "utf-8"))
                    .header(HttpHeaders.CONTENT_TYPE, file.get().getContentType())
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "").header("Connection", "close")
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "")
                    .body(file.get().getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageConstant.FILE_NOT_FOUND);
        }
    }

    /**
     * 在线显示文件
     * @param id 文件id
     * @return
     */
    @GetMapping("/view2/{id}")
    public ResponseEntity<Object> previewFileOnline(@PathVariable String id) throws UnsupportedEncodingException {
        Optional<FileDocument> file = fileService.getPreviewById(id);
        if (file.isPresent()) {
            return ResponseEntity.ok()
                    // 这里需要进行中文编码
                    .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=" + URLEncoder.encode(file.get().getName(), "utf-8") + ".pdf")
                    .header(HttpHeaders.CONTENT_TYPE, FileContentTypeUtils.getContentType("pdf"))
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "").header("Connection", "close")
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "")
                    .body(file.get().getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageConstant.FILE_NOT_FOUND);
        }
    }

    /**
     * 下载附件
     * @param id
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> downloadFileById(@PathVariable String id) throws UnsupportedEncodingException {
        Optional<FileDocument> file = fileService.getById(id);
        if (file.isPresent()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=" + URLEncoder.encode(file.get().getName(), "utf-8"))
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "").header("Connection", "close")
                    .header(HttpHeaders.CONTENT_LENGTH, file.get().getSize() + "")
                    .body(file.get().getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageConstant.FILE_NOT_FOUND);
        }
    }

    /**
     * 表单上传文件（当前使用）
     * 当数据库中存在该md5值时，可以实现秒传功能
     * @param file 文件
     * @return
     */
    @ApiOperation(value = "文档上传", notes = "")
    @PostMapping("auth/upload")
    public BaseApiResult documentUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request)
            throws AuthenticationException {
        String username = (String) request.getAttribute("username");
        String userId = (String) request.getAttribute("id");
        return fileService.documentUpload(file, userId, username);
    }



    /**
     * 删除附件
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseModel deleteFile(@PathVariable String id) {
        ResponseModel model = ResponseModel.getInstance();
        if (!StrUtil.isEmpty(id)) {
            fileService.removeFile(id, true);
            model.setCode(ResponseModel.SUCCESS);
            model.setMessage("删除成功");
        } else {
            model.setMessage("请传入文件id");
        }
        return model;
    }


    /**
     * 删除附件
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public ResponseModel deleteFileByGetMethod(@PathVariable String id) {
        ResponseModel model = ResponseModel.getInstance();
        if (!StrUtil.isEmpty(id)) {
            fileService.removeFile(id, true);
            model.setCode(ResponseModel.SUCCESS);
            model.setMessage("删除成功");
        } else {
            model.setMessage("请传入文件id");
        }
        return model;
    }

    /**
     * @return byte[]
     * @Description previewThumb
     * @Param [thumbId]
     **/
    @GetMapping(value = "/image/{thumbId}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] previewThumb(@PathVariable String thumbId) throws Exception {
        InputStream inputStream = fileService.getFileThumb(thumbId);
        FileInputStream fileInputStream = (FileInputStream) (inputStream);
        if (inputStream == null) {
            return new byte[0];
        }
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes, 0, fileInputStream.available());
        return bytes;
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] test() {

        File file = new File("thumbnail20220724194018003.png");
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;
        } catch (Exception e) {
            log.error("预览文档图片报错 ==> ", e);
            return new byte[0];
        }
    }


    @GetMapping("/thumb/{id}")
    public ResponseEntity<Object> previewThumb1(@PathVariable String id) {

        if (StringUtils.hasText(id)) {
            InputStream inputStream = fileService.getFileThumb(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=" + id)
                    .header(HttpHeaders.CONTENT_TYPE, "image/png")
                    .header(HttpHeaders.CONTENT_LENGTH, "123")
                    .body(IoUtil.readBytes(inputStream));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageConstant.FILE_NOT_FOUND);
        }
    }

    @GetMapping(value = "/image2/{thumbId}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] previewThumb2(@PathVariable String thumbId) {
        return fileService.getFileBytes(thumbId);
    }

    @GetMapping(value = "/text2/{txtId}", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public byte[] previewTxt(@PathVariable String txtId) {
        return fileService.getFileBytes(txtId);
    }

    @GetMapping(value = "/text/{txtId}", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public void downloadTxt(@PathVariable String txtId, HttpServletResponse response) {
        try {
            byte[] buffer = fileService.getFileBytes(txtId);
            extracted(response, buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void extracted(HttpServletResponse response, byte[] buffer) throws IOException {
        // 清空response
        response.reset();
        // 设置response的Header
        response.setCharacterEncoding("UTF-8");
        // 解决跨域问题，这句话是关键，对任意的域都可以，如果需要安全，可以设置成安前的域名
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("字符文件", "UTF-8") + ".txt");
        // 告知浏览器文件的大小
        response.addHeader("Content-Length", "" + buffer.length);
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        outputStream.write(buffer);
        outputStream.flush();
    }

    /**
     * @Description 重建文档索引，继续加入到列表中
     * @Param [docId]
     * @return BaseApiResult
     **/
    @GetMapping("/rebuildIndex")
    public BaseApiResult rebuildIndex(@RequestParam("docId") String docId) {
        if (!StringUtils.hasText(docId)) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_IS_NOT_NULL);
        }
        FileDocument fileDocument = fileService.queryById(docId);
        if (fileDocument != null && fileDocument.getDocState() != DocStateEnum.ON_PROCESS) {
            taskExecuteService.execute(fileDocument);
            return BaseApiResult.success(MessageConstant.SUCCESS);
        } else {
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE, MessageConstant.OPERATE_FAILED);
        }
    }


    @Permission({PermissionEnum.USER,PermissionEnum.ADMIN})
    @GetMapping("/myUploadFile")
    public BaseApiResult getMyUploadFile(){
        String userId = UserHolder.getUser().getId();
        if (userId == null){
            return BaseApiResult.error(403,"请确认登录有无错误！");
        }
        return  fileService.getMyUploadFilesList(userId);
    }

}
