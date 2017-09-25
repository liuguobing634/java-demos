package lew.bing.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 刘国兵 on 2017/9/25.
 */
public class LeaderSelectorService extends LeaderSelectorListenerAdapter implements Closeable {


    public static void main(String[] args) {
        System.out.println("模拟10个机器");

        List<LeaderSelectorService> services = new ArrayList<>();
        List<CuratorFramework> works = new ArrayList<>();
        try {
            for (int i = 0;i < 10; i++) {
                CuratorFramework framework = CuratorFrameworkFactory.newClient("localhost:2181",new ExponentialBackoffRetry(1000,3));
                works.add(framework);
                LeaderSelectorService service = new LeaderSelectorService(framework,"/app/leader", "服务器#"+i);
                services.add(service);
                framework.start();
                service.start();
            }
            System.out.println("回车键关闭");
            new BufferedReader(new InputStreamReader(System.in)).readLine();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("结束");
            for (LeaderSelectorService service:services) {
                CloseableUtils.closeQuietly(service);
            }
            for (CuratorFramework curatorFramework: works) {
                CloseableUtils.closeQuietly(curatorFramework);
            }
        }

    }

    private String name;

    private LeaderSelector leaderSelector;

    private AtomicInteger leaderCount = new AtomicInteger();

    public LeaderSelectorService(CuratorFramework client,String path, String name) {
        this.name = name;
        this.leaderSelector = new LeaderSelector(client, path, this);
        leaderSelector.autoRequeue();
    }

    public void start() throws Exception {
        this.leaderSelector.start();
    }

    @Override
    public void close() throws IOException {
        this.leaderSelector.close();
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        // 随机休息几秒
        final int waitSeconds = (int)(5 * Math.random()) + 1;
        System.out.println(name + "现在是领主，等待" + waitSeconds + "秒钟释放领导权。");
        System.out.println(name + "成为领主" + leaderCount.getAndIncrement()+ "次了。");
        try
        {
            Thread.sleep(TimeUnit.SECONDS.toMillis(waitSeconds));
        }
        catch ( InterruptedException e )
        {
            System.err.println(name + "被打断");
            Thread.currentThread().interrupt();
        }
        finally
        {
            System.out.println(name + "交出领导权.\n");
        }
    }
}
