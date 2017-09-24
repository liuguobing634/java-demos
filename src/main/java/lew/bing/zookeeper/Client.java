package lew.bing.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by 刘国兵 on 2017/9/24.
 */
public class Client implements Watcher {

    private ZooKeeper zooKeeper;


    public Client() throws IOException {
        this.zooKeeper = new ZooKeeper("localhost:2181", 30000, this);

    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        Client client = new Client();
        System.out.println("回车键关闭");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            client.close();
        }

    }


    @Override
    public void process(WatchedEvent event) {
        // 判断port的
        System.out.println(event);
        if (event.getPath() == null && event.getType().equals(Event.EventType.None)) {
            System.out.println("启动");
            try {
                if (this.zooKeeper.exists("/port", this) != null) {
                    // 获取数据
                    try {
                        byte[] data = this.zooKeeper.getData("/port", this, null);
                        System.out.println("端口是:" + new String(data));
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (event.getPath().equals("/port")) {
            switch (event.getType()) {
                case NodeCreated:
                    // 节点创建获取数据
                    System.out.println("服务恢复，获取端口,并重新监听");
                    try {
                        byte[] data = this.zooKeeper.getData("/port", this, null);
                        System.out.println("端口是:" + new String(data));
                        this.zooKeeper.exists("/port", this);
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case NodeDeleted:
                    // 节点删除
                    System.out.println("服务暂时不可用,重新监听");
                    try {
                        this.zooKeeper.exists("/port", this);
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case NodeDataChanged:
                    // 端口改变了就重新获取端口
                    try {
                        byte[] data = this.zooKeeper.getData("/port", this, null);
                        System.out.println("端口修改，新端口是:" + new String(data));
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    }

    public void close() {
        try {
            this.zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
