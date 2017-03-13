package thin.framework.request;

import org.apache.commons.collections4.CollectionUtils;
import thin.framework.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyu on 2017/2/6.
 */
public class Param {

    private List<FormParam> formParamList;

    private List<FileParam> fileParamList;

    public Param(List<FormParam> formParamList){
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList,List<FileParam> fileParamList){
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    /**
     * 获取请求参数Map
     * @return
     */
    public Map<String,Object> getFieldMap(){
        Map<String,Object> fieldMap = new HashMap<String, Object>();
        if (CollectionUtils.isNotEmpty(formParamList)){
            for (FormParam formParam : formParamList){
                String fieldName = formParam.getFieldName();
                Object fieldValue = formParam.getFieldValue();
                if (fieldMap.containsKey(fieldName)){
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
                }
                fieldMap.put(fieldName,fieldValue);
            }
        }
        return fieldMap;
    }

    /**
     * 获取上传文件Map
     * @return
     */
    public Map<String,List<FileParam>> getFileMap(){
        Map<String,List<FileParam>> fileMap = new HashMap<String, List<FileParam>>();
        if (CollectionUtils.isNotEmpty(fileParamList)){
            for (FileParam fileParam : fileParamList){
                String fieldName = fileParam.getFieldName();
                List<FileParam> fileList;
                if (fileMap.containsKey(fieldName))
                    fileList = fileMap.get(fieldName);
                else
                    fileList = new ArrayList<FileParam>();
                fileList.add(fileParam);
                fileMap.put(fieldName,fileList);
            }
        }
        return fileMap;
    }

    /**
     * 获取所有上传文件
     * @param fieldName
     * @return
     */
    public List<FileParam> getFileList(String fieldName){
        return getFileMap().get(fieldName);
    }

    public FileParam getFile(String fieldName){
        List<FileParam> fileParamList = getFileMap().get(fieldName);
        if (CollectionUtils.isNotEmpty(fileParamList) && fileParamList.size()==1){
            return fileParamList.get(0);
        }
        return null;
    }

    public boolean isEmpty(){
        return CollectionUtils.isEmpty(formParamList) && CollectionUtils.isEmpty(fileParamList);
    }
}
