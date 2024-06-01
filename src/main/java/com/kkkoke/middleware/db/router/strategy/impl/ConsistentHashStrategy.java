package com.kkkoke.middleware.db.router.strategy.impl;

import com.kkkoke.middleware.db.router.config.DBRouterConfig;
import com.kkkoke.middleware.db.router.strategy.IDBRouterStrategy;
import com.kkkoke.middleware.db.router.util.DBContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author KeyCheung
 * @date 2024/06/01
 * @desc
 */
@Slf4j
public class ConsistentHashStrategy implements IDBRouterStrategy {

    private DBRouterConfig dbRouterConfig;

    public ConsistentHashStrategy(DBRouterConfig dbRouterConfig) {
        this.dbRouterConfig = dbRouterConfig;
    }

    /**
     * 虚拟节点的数目，一个真实结点对应5个虚拟节点
     */
    private static final int VIRTUAL_NODES = 5;

    /**
     * 真实结点列表，考虑到服务器上线、下线的场景，即添加、删除的场景会比较频繁，这里使用LinkedList会更好
     */
    private static List<String> realDbNodes = new LinkedList<>();

    private static List<String> realTbNodes = new LinkedList<>();

    /**
     * 虚拟节点，key表示虚拟节点的hash值，value表示虚拟节点的名称
     */
    private static SortedMap<Integer, String> virtualDbNodes = new TreeMap<>();

    private static SortedMap<Integer, String> virtualTbNodes = new TreeMap<>();

    @Override
    public void doRouter(String dbKeyAttr) {
        initNodeList();

        // 得到带路由的结点的Hash值
        int hash = getHash(dbKeyAttr);
        // 得到大于该Hash值的所有Map
        SortedMap<Integer, String> subDbMap = virtualDbNodes.tailMap(hash);
        SortedMap<Integer, String> subTbMap = virtualTbNodes.tailMap(hash);
        // 第一个Key就是顺时针过去离node最近的那个结点
        Integer idx = subDbMap.firstKey();
        String dbIdx = subDbMap.get(idx);
        idx = subTbMap.firstKey();
        String tbIdx = subTbMap.get(idx);
        DBContextHolder.setDBKey(dbIdx);
        log.info("DBContextHolder.setDBKey: " + DBContextHolder.getDBKey());
        DBContextHolder.setTBKey(tbIdx);

        clearNodeList();
    }

    private void initNodeList() {
        // 将数据库列表添加到真实结点列表中
        for (int i = 1; i <= 4; i++) {
            realDbNodes.add(String.format("%02d", i));
        }

        for (int i = 0; i < 4; i++) {
            realTbNodes.add(String.format("%03d", i));
        }

        // 添加虚拟节点
        for (String str : realDbNodes) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                int hash = getHash(str);
                virtualDbNodes.put(hash, str);
            }
        }

        for (String str : realTbNodes) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                int hash = getHash(str);
                virtualTbNodes.put(hash, str);
            }
        }
    }

    private void clearNodeList() {
        realTbNodes.clear();
        realDbNodes.clear();
        virtualTbNodes.clear();
        virtualDbNodes.clear();
    }

    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值
     */
    private int getHash(String str) {
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }

        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

    @Override
    public void setDBKey(int dbIdx) {
        DBContextHolder.setDBKey(String.format("%02d", dbIdx));
    }

    @Override
    public void setTBKey(int tbIdx) {
        DBContextHolder.setTBKey(String.format("%03d", tbIdx));
    }

    @Override
    public int dbCount() {
        return dbRouterConfig.getDbCount();
    }

    @Override
    public int tbCount() {
        return dbRouterConfig.getTbCount();
    }

    @Override
    public void clear() {
        DBContextHolder.clearDBKey();
        DBContextHolder.clearTBKey();
    }
}
