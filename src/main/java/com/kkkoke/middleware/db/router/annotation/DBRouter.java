package com.kkkoke.middleware.db.router.annotation;

import java.lang.annotation.*;

/**
 * @author KeyCheung
 * @date 2023/07/15
 * @desc 路由注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DBRouter {

    /**
     * 分库分表字段
     */
    String key() default "";
}