package thin.framework.response;

import java.util.Map;

/**
 * 返回视图对象
 * Created by yangyu on 2017/2/7.
 */
public class View {
    /**
     * 视图路径
     */
    private String path;

    /**
     * 模型数据
     */
    private Map<String,Object> model;

    public View(String path){
        this.path=path;
    }

    public View addModel(String key,Object value){
        this.model.put(key,value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
