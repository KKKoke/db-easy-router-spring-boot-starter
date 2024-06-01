package com.kkkoke.middleware.db.router.dynamic;

import com.kkkoke.middleware.db.router.util.DBContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author KeyCheung
 * @date 2023/07/15
 * @desc 动态数据源获取，每当切换数据源，都要从这个里面进行获取
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.info("switch to db" + DBContextHolder.getDBKey());
        return "db" + DBContextHolder.getDBKey();
    }
}