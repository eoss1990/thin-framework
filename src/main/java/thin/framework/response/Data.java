package thin.framework.response;

/**
 * 返回数据对象
 * Created by yangyu on 2017/2/7.
 */
public class Data {

    /**
     * 模型数据
     */
    private Object model;

    public Data(Object model){
        this.model=model;
    }

    public Object getModel() {
        return model;
    }
}
