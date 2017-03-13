package thin.framework.helper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import thin.framework.request.FileParam;
import thin.framework.request.FormParam;
import thin.framework.request.Param;
import thin.framework.util.FileUtil;
import thin.framework.util.StreamUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyu on 2017/2/13.
 */
public final class UploadHelper {

    private static ServletFileUpload servletFileUpload;

    /**
     * 初始化
     * @param servletContext
     */
    public static void init(ServletContext servletContext){
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tmpdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,repository));
        int uploadLimit = ConfigHelper.getAppUploadLimit();
        if (uploadLimit != 0)
            servletFileUpload.setFileSizeMax(uploadLimit*1024*1024);
    }

    /**
     * 判断请求是否为multipart类型
     * @param request
     * @return
     */
    public static boolean isMultipart(HttpServletRequest request){
        return servletFileUpload.isMultipartContent(request);
    }

    public static Param createParam(HttpServletRequest request){
        List<FormParam> formParamList = new ArrayList<FormParam>();
        List<FileParam> fileParamList = new ArrayList<FileParam>();
        try {
            Map<String,List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
            if(MapUtils.isNotEmpty(fileItemListMap)){
                for (Map.Entry<String,List<FileItem>> fileItemListEntry: fileItemListMap.entrySet()){
                    String fieldName = fileItemListEntry.getKey();
                    List<FileItem> fileItemList = fileItemListEntry.getValue();
                    if (CollectionUtils.isEmpty(fileItemList)){
                        for (FileItem fileItem : fileItemList){
                            if (fileItem.isFormField()){
                                String fieldValue = fileItem.getString("UTF-8");
                                formParamList.add(new FormParam(fieldName,fieldValue));
                            }else {
                                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(),"UTF-8"));
                                if(StringUtils.isNotEmpty(fileName)){
                                    long fileSize = fileItem.getSize();
                                    String contentType = fileItem.getContentType();
                                    InputStream inputStream = fileItem.getInputStream();
                                    fileParamList.add(new FileParam(fieldName,fileName,fileSize,contentType,inputStream));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Param(formParamList,fileParamList);
    }

    /**
     * 上传文件
     * @param basePath
     * @param fileParam
     */
    public static void uploadFile(String basePath,FileParam fileParam){
        if (fileParam!=null){
            String filePath = basePath+fileParam.getFileName();
            FileUtil.createFile(filePath);
            try {
                StreamUtil.copyStream(fileParam.getInputStream(),new FileOutputStream(filePath));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 批量上传文件
     * @param basePath
     * @param fileParamList
     */
    public static void uploadFiles(String basePath,List<FileParam> fileParamList){
        if (CollectionUtils.isNotEmpty(fileParamList)){
            for (FileParam fileParam : fileParamList){
                uploadFile(basePath,fileParam);
            }
        }
    }

}
