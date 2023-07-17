package com.kkkoke.middleware.db.router.dynamic;

import com.kkkoke.middleware.db.router.DBContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author KeyCheung
 * @date 2023/07/15
 * @desc 动态数据源获取，每当切换数据源，都要从这个里面进行获取
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return "db" + DBContextHolder.getDBKey();
    }
}