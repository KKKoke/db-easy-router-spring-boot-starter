package com.kkkoke.middleware.db.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KeyCheung
 * @date 2023/07/15
 * @desc 数据库路由配置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DBRouterConfig {

    /**
     * 分库数量
     */
    private int dbCount;

    /**
     * 分表数量
     */
    private int tbCount;

    /**
     * 路由字段
     */
    private String routerKey;
}