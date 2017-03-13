package thin.framework.request;

import java.io.InputStream;

/**
 * Created by yangyu on 2017/2/13.
 */
public class FileParam {

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 上传文件的文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件字节输入流
     */
    private InputStream inputStream;

    public FileParam(String fieldName,String fileName,long fileSize,String contentType,InputStream inputStream){
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
