package lew.bing.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Scanner;


/**
 * Created by 刘国兵 on 2017/9/24.
 */
public class Service implements Watcher{

    private int port;
    private ZooKeeper zooKeeper;

    private boolean isMaster = false;

    public Service(int port) throws IOException {
        this.port = port;
        this.zooKeeper = new ZooKeeper("localhost:2181",30000,this);

    }

    public static void main(String[] args) throws Exception {

        // 创建随机端口
        int port = 1024 + (int)(1000 * (Math.random()));
        Service service = new Service(port);
        System.out.println("端口是:"+port);
        System.out.println("回车键关闭");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            service.close();
        }
    }



    @Override
    public void process(WatchedEvent event) {
        if (event.getPath() == null && event.getType().equals(Event.EventType.None)) {
            System.out.println("服务启动");
            // 检查端口是否存在
            try {
                if (zooKeeper.exists("/port", this) != null) {
                    System.out.println(port + ": 已经有master了，等会再去竞选");
                    isMaster = false;
                } else {
                    // 竞选master
                    try {
                        zooKeeper.create("/port", (""+port).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                        isMaster = true;
                    } catch (KeeperException e) {
                        System.out.println(port + ": 被别人抢跑了，等会再去竞选");
                        isMaster = false;
                        zooKeeper.exists("/port", this);
                    }
                }
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (event.getPath().equals("/port")) {
            switch (event.getType()) {
                case NodeDeleted:
                    // 节点被删除时去竞选
                    try {
                        try {
                            // 如果自己就是master，那么就立刻去抢
                            if (this.isMaster) {
                                System.out.println(port + "因为网络问题被断，重新成为master");
                                zooKeeper.create("/port", (""+port).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                                this.isMaster = true;
                            } else {
                                Thread.sleep(3000);
                                System.out.println(port + ": 原master关闭了，去竞争master");
                                zooKeeper.create("/port", (""+port).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                                this.isMaster = true;
                            }

                        } catch (KeeperException e) {
                            System.out.println(port + ": 被别人抢跑了，等会再去竞选");
                            zooKeeper.exists("/port", this);
                            this.isMaster = false;
                        }
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                  // 不管
                  break;

            }
        }
    }

    public void close() throws InterruptedException {
        this.zooKeeper.close();
    }

}
