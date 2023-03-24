package com.coden.service;

import com.coden.entity.FileDocument;
import com.coden.entity.dto.BasePageDTO;
import com.coden.entity.dto.DocumentDTO;
import com.coden.enums.DocStateEnum;
import com.coden.task.exception.TaskRunException;
import com.coden.util.BaseApiResult;
import org.apache.http.auth.AuthenticationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;



public interface IFileService {


    /**
     * 保存文件 - 表单
     * @param md5
     * @param file
     * @return
     */
    FileDocument saveFile(String md5, MultipartFile file);


    BaseApiResult documentUpload(MultipartFile file, String userId, String username) throws AuthenticationException;

    /**
     * 保存文件 - js文件流
     * @param fileDocument FileDocument
     * @param inputStream InputStream
     * @return FileDocument
     */
    FileDocument saveFile(FileDocument fileDocument, InputStream inputStream);

    /**
     * update file
     * @Description 重建索引和缩略图的时候专用的
     * @Param fileDocument FileDocument
     **/
    void updateFile(FileDocument fileDocument);

    /**
     * @Description 更新文档状态
     * @Param [fileDocument, state]
     * @return void
     **/
    void updateState(FileDocument fileDocument, DocStateEnum state, String errorMsg) throws TaskRunException;

    /**
     * @Description 删除GridFS系统中的文件
     * @Param [id]
     * @return void
     **/
    void deleteGridFs(String ...id);

    /**
     * 删除文件
     * @param id
     * @param isDeleteFile 是否删除文件
     * @return
     */
    void removeFile(String id, boolean isDeleteFile);

    /**
     * 根据id获取文件
     * @param id
     * @return
     */
    Optional<FileDocument> getById(String id);

    /**
     * 根据id获取文件
     * @param id
     * @return
     */
    Optional<FileDocument> getPreviewById(String id);

    /**
     * 根据md5获取文件对象
     * @param md5
     * @return
     */
    FileDocument getByMd5(String md5);

    /**
     * queryById
     * @param docId String
     * @return result
     */
    FileDocument queryById(String docId);

    /**
     * 分页查询，按上传时间降序
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<FileDocument> listFilesByPage(int pageIndex, int pageSize);

    /**
     * listAndFilterByPage
     * @param pageIndex int
     * @param pageSize int
     * @param ids Collection
     * @return result
     */
    List<FileDocument> listAndFilterByPage(int pageIndex, int pageSize, Collection<String> ids);

    /**
     * listAndFilterByPageNotSort
     * @param pageIndex int
     * @param pageSize int
     * @param ids List
     * @return result
     */
    List<FileDocument> listAndFilterByPageNotSort(int pageIndex, int pageSize, List<String> ids);

    /**
     * 分页检索目前的文档信息
     * @param documentDTO DocumentDTO
     * @return result
     */
    BaseApiResult list(DocumentDTO documentDTO);

    /**
     *根据文档的详情，查询该文档的详细信息
     * @param id ->Long
     * @return ApiResult
     */
    BaseApiResult detail(String id);

    /**
     * 删除掉已经存在的文档
     * @param id -> Long
     * @return ApiResult
     */
    BaseApiResult remove(String id);

    BaseApiResult listWithCategory(DocumentDTO documentDTO);

    /**
     * update file thumb
     * @param inputStream FileDocument
     * @param fileDocument InputStream
     * @throws FileNotFoundException file not found
     */
    void updateFileThumb(InputStream inputStream, FileDocument fileDocument) throws FileNotFoundException;

    /**
     * getFileThumb
     * @Description 查询缩略图信息
     * @param thumbId String
     * @return java.io.InputStream
     **/
    InputStream getFileThumb(String thumbId);

    byte[] getFileBytes(String thumbId);

    /**
     * 保存文件流到dfs系统中
     **/
    String uploadFileToGridFs(String prefix, InputStream in, String contentType);

    /**
     * @Description 查询并删除某个文档
     * @Param [docId]
     * @return FileDocument
     **/
    List<FileDocument> queryAndRemove(String ...docId);

    /**
     * @Description 文档查询并通过文档审批
     * @Param [docId]
     * @return java.util.List<com.coden.entity.FileDocument>
     **/
    List<FileDocument> queryAndUpdate(String ...docId);

    /**
     * @Description 查询是否在评审的文档
     * @Param [pageDTO, reviewing]
     * @return java.util.List<FileDocument>
     **/
    List<FileDocument> queryFileDocument(BasePageDTO pageDTO, boolean reviewing);

    /**
     * @Description 查询文档评审的列表, 实际是查询文档的信息
     * @Param [page, user]
     * @return BaseApiResult
     **/
    BaseApiResult queryFileDocumentResult(BasePageDTO pageDTO, boolean reviewing);


    BaseApiResult getMyUploadFilesList(String userId);
}
