import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.junit.Test;

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
  }
}
