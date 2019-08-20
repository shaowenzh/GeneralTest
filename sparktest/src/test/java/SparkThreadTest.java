import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.spark.util.UninterruptibleThread;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SparkThreadTest {

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
    threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool(threadFactory);


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

}
