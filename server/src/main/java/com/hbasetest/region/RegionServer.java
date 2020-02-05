package com.hbasetest.region;

import com.hbasetest.util.DateUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * region server
 * 需要连接到 HMaster，写入或更新 /region_info/regionServer1 的相关信息
 */
public class RegionServer {
    private static String ZK_REGISTER_PATH = "/region_info/regionServer1";
    private static final String ZK_MASTER_PATH = "localhost:2181";

    /**
     * 取得 RegionServer 的信息
     * thisHostName is:DESKTOP-DPEORFV
     * thisHostAddress is:192.168.1.165
     * @return
     */
    private static String getZkRegionServerInfo() throws UnknownHostException {
        String thisHostName = InetAddress.getLocalHost().getHostName();
        String thisHostAddress = InetAddress.getLocalHost().getHostAddress();
        String port = "2005";
        String createTime = DateUtil.getDateShow(new Date(), "yyyy-MM-dd HH:mm:ss");
        String updateTime = createTime;

        JSONObject result = new JSONObject();
        result.put("ip", thisHostAddress);
        result.put("port", port);
        result.put("host_name", thisHostName);
        result.put("create_time", createTime);
        result.put("update_time", updateTime);

        return result.toString();
    }

    /**
     * 取得 RegionServer 的更新信息,只更新 update_time 字段
     * @param inputInfo
     * @return
     * @throws UnknownHostException
     */
    private static String getZkRegionServerUpdateInfo(String inputInfo) throws UnknownHostException {
        String updateTime = DateUtil.getDateShow(new Date(), "yyyy-MM-dd HH:mm:ss");

        JSONObject result = new JSONObject(inputInfo);
        result.put("update_time", updateTime);
        return result.toString();
    }

    /**
     * region server 启动后,注册信息到 RegionServer.REGISTER_PATH 下
     * @throws Exception
     */
    private static void registerToMaster() throws Exception {
        ZooKeeper zk = new ZooKeeper(ZK_MASTER_PATH, 10000,
                new Watcher() {
                    public void process(WatchedEvent event) {
                        System.out.println("event: " + event.getType());
                    }
                });

        Stat stat = zk.exists(ZK_REGISTER_PATH, true);
        if (stat == null) {
            // 不存在则创建
            zk.create(ZK_REGISTER_PATH, getZkRegionServerInfo().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } else {
            // 存在则更新
            byte[] registerData = zk.getData(ZK_REGISTER_PATH, true, null);

        }

        zk.close();
    }

    public static void main(String[] args) throws Exception {
//        registerToMaster();
        String thisHostName = InetAddress.getLocalHost().getHostName();
        String thisHostAddress = InetAddress.getLocalHost().getHostAddress();
        System.out.println("thisHostName is:" + thisHostName);
        System.out.println("thisHostAddress is:" + thisHostAddress);
    }
}
