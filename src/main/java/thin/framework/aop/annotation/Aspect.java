package thin.framework.aop.annotation;

import java.lang.annotation.*;

/**
 * Created by yangyu on 2017/2/10.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    Class<? extends Annotation> value();
}
