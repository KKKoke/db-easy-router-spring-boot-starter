package com.kkkoke.middleware.db.router.annotation;

import java.lang.annotation.*;

/**
 * @author KeyCheung
 * @date 2023/07/15
 * @desc 路由策略，分表标记
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DBRouterStrategy {

    boolean splitTable() default false;
}