package com.kkkoke.middleware.db.router;

/**
 * @author KeyCheung
 * @date 2023/07/15
 * @desc 数据源基础配置
 */
public class DBRouterBase {

    private String tbIdx;

    public String getTbIdx() {
        return DBContextHolder.getTBKey();
    }
}