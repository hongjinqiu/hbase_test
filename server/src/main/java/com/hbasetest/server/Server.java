package com.hbasetest.server;

import org.apache.commons.io.FileUtils;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    private static void startZookeeper() throws Exception {
        InputStream is = Server.class.getClassLoader().getResourceAsStream("zoo.cfg");
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

    public static void main(String[] args) throws Exception{
        startZookeeper();
        new CountDownLatch(1).await();
    }
}
