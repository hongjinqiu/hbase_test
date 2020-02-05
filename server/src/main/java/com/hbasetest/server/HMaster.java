package com.hbasetest.server;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * Master 入口类
 */
public class HMaster {
    private static final Logger logger = Logger.getLogger(HMaster.class);
    private static final String ZK_MASTER_PATH = "localhost:2181";

    /**
     * 启动的过程中，启动一个 Zookeeper
     * @throws Exception
     */
    private static void startZookeeper() throws Exception {
        InputStream is = HMaster.class.getClassLoader().getResourceAsStream("zoo.cfg");
        Properties props = new Properties();
        try {
            props.load(is);
        } finally {
            is.close();
        }

        QuorumPeerConfig quorumConfig = new QuorumPeerConfig();
        quorumConfig.parseProperties(props);

        final ZooKeeperServerMain zkServer = new ZooKeeperServerMain();
        final ServerConfig config = new ServerConfig();
        config.readFrom(quorumConfig);
        zkServer.runFromConfig(config);
    }

    /**
     * zookeeper 写一个 /region_info 的目录，若目录不存在，则创建
     * @throws Exception
     */
    private static void writeRegionInfoIfNotExists() throws Exception {
        ZooKeeper zk = new ZooKeeper(ZK_MASTER_PATH, 10000,
                new Watcher() {
                    public void process(WatchedEvent event) {
//                        System.out.println("event: " + event.getType());
                        logger.info("event: " + event.getType());
                    }
                });

        Stat stat = zk.exists("/region_info", true);
        // 节点不存在
        if (stat == null) {
            zk.create("/region_info", "this is region info".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        zk.close();
    }

    public static void main(String[] args) throws Exception{
        startZookeeper();
        writeRegionInfoIfNotExists();
        new CountDownLatch(1).await();
    }
}
