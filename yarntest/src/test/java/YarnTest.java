import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class YarnTest {

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
