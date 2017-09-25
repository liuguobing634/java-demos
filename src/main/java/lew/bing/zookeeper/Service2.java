package lew.bing.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;

import java.util.Scanner;

/**
 * Created by 刘国兵 on 2017/9/24.
 */
public class Service2 {

    public static void main(String[] args) throws Exception {
        // 采用curator
        Service2 client = new Service2();
//        client.testNormal();
//        client.testListener();
//        client.testNodeCache();
        client.testTreeCache();
        System.out.println("回车键关闭");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            client.close();
        }
    }

    private CuratorFramework client;

    public Service2() {
        this.client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000,3));
        client.start();
    }



    public void close() {
        CloseableUtils.closeQuietly(this.client);
    }

    // 测试普通的方式
    public void testNormal() throws Exception {
        // 测试增删改查节点
        // 创建节点，永久节点
        client.create().withMode(CreateMode.PERSISTENT).forPath("/hello", "hello".getBytes());
        client.checkExists().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                if (event.getStat() != null) {
                    System.out.println("节点/hello存在");
                } else {
                    System.out.println("节点/hello不存在");
                }
            }
        }).forPath("/hello");
        // 由于节点/hello存在，所以这里直接写getData
        byte[] data = client.getData().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent event) throws Exception {
                System.out.println("节点/hello发生了数据变化");
            }
        }).forPath("/hello");
        System.out.println("节点/hello的数据是:" + new String(data));

        client.setData().forPath("/hello", "no".getBytes());
        // 继续监测节点变化
        client.checkExists().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println(event);
            }
        }).forPath("/hello");
        // 创建子节点,临时节点
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/hello/sub", "sub".getBytes());
        // 获取子节点数据
        data = client.getData().forPath("/hello/sub");
        System.out.println("节点/hello/sub的数据是:"+ new String(data));
        client.delete().forPath("/hello/sub");
        // 删除父节点
        client.delete().forPath("/hello");
    }

    public void testListener() throws Exception {

        // curator提供了一个监听接口，但这个接口只是用来接收client异步操作的结果，Zookeeper的数据变化与之无关

        CuratorListener listener = new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println(event);
                if (event.getType() != null) {
                    System.out.println(event.getPath() + "被"+event.getType());
                } else {
                    System.out.println("未知");
                }
            }
        };
        client.getCuratorListenable().addListener(listener);
        // 先创建一个节点
        client.create().forPath("/hello", "hello".getBytes());
        // 只有添加inBackground，才能够被监听
        client.setData().inBackground().forPath("/hello", "abv".getBytes());
        client.getData().inBackground().forPath("/hello");
        client.delete().inBackground().forPath("/hello");

    }

    public void testNodeCache() throws Exception{
        // 测试node cache
        NodeCacheListener listener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("节点/hello发生了变化");
                if (client.checkExists().forPath("/hello") != null) {
                    System.out.println("当前节点/hello值为:" + new String(client.getData().forPath("/hello")));
                }
            }
        };
        NodeCache nodeCache = new NodeCache(client, "/hello");
        nodeCache.getListenable().addListener(listener);
        nodeCache.start();
        // 先休息2s
        // 创建节点会发生变化
        Thread.sleep(2000);
        client.create().forPath("/hello", "hello".getBytes());
        // 再休息1s
        Thread.sleep(1000);
        client.setData().forPath("/hello","abc".getBytes());
        // 再休息1s
        // 删除节点会发生反应
        Thread.sleep(1000);
        client.delete().forPath("/hello");
    }

    public void testPathChildrenCache() throws Exception {

        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event.getData() != null) {
                    System.out.println("节点:" + event.getData().getPath() + "发生了变化,类型是" + event.getType());
                    if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)
                            || event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                        System.out.println("节点:"+event.getData().getPath() + "现在的值是" + new String(event.getData().getData()));
                    }
                }
            }
        };

        // 监听的是子节点
        PathChildrenCache cache = new PathChildrenCache(client, "/hello",true);
        cache.getListenable().addListener(pathChildrenCacheListener);
        cache.start();
        // 先休息2s
        // 创建节点会发生变化
        Thread.sleep(2000);
        client.create().forPath("/hello/abc", "hello".getBytes());
        // 再休息1s
        Thread.sleep(1000);
        client.setData().forPath("/hello/abc","abc".getBytes());
//        // 再休息1s
//        // 删除节点会发生反应
        Thread.sleep(1000);
        client.delete().forPath("/hello/abc");
//        client.delete().forPath("/hello");
    }

    public void testTreeCache() throws Exception {
        TreeCacheListener treeCacheListener = new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                if (event.getData() != null) {
                    System.out.println("节点:" + event.getData().getPath() + "发生了变化,类型是" + event.getType());
                    if (event.getType().equals(TreeCacheEvent.Type.NODE_ADDED)
                            || event.getType().equals(TreeCacheEvent.Type.NODE_UPDATED)) {
                        System.out.println("节点:"+event.getData().getPath() + "现在的值是" + new String(event.getData().getData()));
                    }
                }
            }
        };

        TreeCache treeCache = new TreeCache(client, "/hello");
        treeCache.getListenable().addListener(treeCacheListener);
        treeCache.start();
        // 先休息2s
        // 创建节点会发生变化
        Thread.sleep(2000);
        client.create().forPath("/hello", "hello".getBytes());
        // 再休息1s
        Thread.sleep(1000);
        client.setData().forPath("/hello","abc".getBytes());
        // 休息2s, 创建子节点
        Thread.sleep(2000);
        client.create().forPath("/hello/abc", "hello".getBytes());
        // 再休息1s， 修改子节点
        Thread.sleep(1000);
        client.setData().forPath("/hello/abc","abc".getBytes());
//        // 再休息1s
//        // 删除子节点会发生反应
        Thread.sleep(1000);
        client.delete().forPath("/hello/abc");
        // 再休息1s
        // 删除节点会发生反应
        Thread.sleep(1000);
        client.delete().forPath("/hello");
    }


}
