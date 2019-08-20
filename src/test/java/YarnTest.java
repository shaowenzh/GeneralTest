import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.spark.util.UninterruptibleThread;
import org.junit.Test;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class YarnTest {
  private ThreadPoolExecutor threadPool = null;

  @Test
  public void threadFactoryTest() {
    ThreadFactory tf = new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
        return new UninterruptibleThread(r, "unused") ;
      }
    };
    ThreadFactory threadFactory = new ThreadFactoryBuilder()
    .setDaemon(true)
    .setNameFormat("Executor task launch worker-%d")
    .setThreadFactory(tf).build();
    threadPool = (ThreadPoolExecutor)Executors.newCachedThreadPool(threadFactory);


    Runnable tr = new Runnable() {
      @Override
      public void run() {
        System.out.println(Thread.currentThread().getId());
      }
    };

    //for (int i = 0)
    threadPool.execute(tr);
    threadPool.execute(tr);
    threadPool.execute(tr);
    threadPool.execute(tr);
    threadPool.execute(tr);
    threadPool.execute(tr);
    threadPool.execute(tr);

    try {
      threadPool.awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void codeTest() {
    //String yarnResource = Thread.currentThread().getContextClassLoader().getResource("yarn-site.xml").toString();
    Configuration hadoopConf = new Configuration();
    //hadoopConf.addResource(Thread.currentThread().getContextClassLoader().getResource("yarn-site.xml"));
    YarnConfiguration yarnConf = new YarnConfiguration(hadoopConf);
    String hostname = yarnConf.get("yarn.resourcemanager.hostname");
    YarnClient yarnClient = YarnClient.createYarnClient();
    yarnClient.init(yarnConf);
    yarnClient.start();
    System.out.println(3 * 0.01 == 0.03);

    ArrayList<List> testList = new ArrayList<List>();
    ArrayList strList = getArrayList(String.class);
    strList.add("test");
    ArrayList longList = getArrayList(Long.class);
    longList.add(123L);
    testList.add(new ArrayList<String>());
    testList.add(strList);
    testList.add(longList);

    for(List arr: testList) {
      System.out.println(arr.size());
    }

    int a = 0;
  }

  private <T> ArrayList<T> getArrayList(Class<T> type) {
    ArrayList<T> arrayList = new ArrayList<T>();
    return arrayList;
  }
}
