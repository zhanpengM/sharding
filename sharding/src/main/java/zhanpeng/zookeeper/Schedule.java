package zhanpeng.zookeeper;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class Schedule
{
    public static String zkUrl = "192.168.146.33:2181";

    public static void main(String[] args)
        throws IOException, KeeperException, InterruptedException
    {
        final ZooKeeper zk = new ZooKeeper(zkUrl, 5000, new Watcher()
        {
            // 监控所有被触发的事件
            public void process(WatchedEvent event)
            {
                System.out.println("已经触发了" + event.getType() + "事件！" + event.getPath());
            }
        });
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            private String path = "/testRootPath";

            @Override
            public void run()
            {
                try
                {
                    if (zk.exists(path, true) != null)
                    {
                        zk.delete(path, -1);
                    }
                    else
                    {
                        zk.create(path, "testRootData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }
                }
                catch (KeeperException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }, 500, 2000);
    }

}
