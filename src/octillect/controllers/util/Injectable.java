package octillect.controllers.util;

import java.lang.reflect.Method;

/**
 * Inject a Parent Controller in the controller that implements this interface.
 *
 * @param <ParentController> the controller that needs to be injected.
 */
public interface Injectable<ParentController> {

    void inject(ParentController parentController);

    /**
     * Invoke all methods with the {@code @PostLoad} annotation.
     */
    default void init() {
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostLoad.class)) {
                try {
                    method.invoke(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
