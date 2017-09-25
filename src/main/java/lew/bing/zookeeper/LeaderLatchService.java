package lew.bing.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 刘国兵 on 2017/9/25.
 */
public class LeaderLatchService implements LeaderLatchListener , Closeable{

    private String name;

    private LeaderLatch leaderLatch;


    public LeaderLatchService(String name, String path, CuratorFramework curatorFramework) {
        this.name = name;
        this.leaderLatch = new LeaderLatch(curatorFramework,path);
        this.leaderLatch.addListener(this);
    }

    @Override
    public void isLeader() {
        System.out.println(name + "是领导");
    }

    @Override
    public void notLeader() {
        System.out.println(name + "不是领导");
    }

    public void start() throws Exception {
        this.leaderLatch.start();
    }

    @Override
    public void close() throws IOException {
        this.leaderLatch.close();
    }

    public static void main(String[] args) {
        // 随机创建一个数字作为主机名
        int num = 1024 + (int)(1000 * (Math.random()));
        CuratorFramework framework = CuratorFrameworkFactory.newClient("localhost:2181",new ExponentialBackoffRetry(1000,3));

        LeaderLatchService m = new LeaderLatchService("机器#"+num, "/app/master", framework);
        try {
            framework.start();
            m.start();
            System.out.println("回车键关闭");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(m);
            CloseableUtils.closeQuietly(framework);
        }
    }
}
