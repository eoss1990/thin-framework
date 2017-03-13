package thin.framework.request;

/**
 * Created by yangyu on 2017/2/13.
 */
public class FormParam {

    private String fieldName;

    private Object fieldValue;

    public FormParam(String fieldName,Object fieldValue){
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
