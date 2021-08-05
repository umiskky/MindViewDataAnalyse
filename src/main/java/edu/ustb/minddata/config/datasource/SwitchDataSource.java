package edu.ustb.minddata.config.datasource;

import java.lang.annotation.*;

/**
 * @author UmiSkky
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SwitchDataSource {
}
