import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.server.MiniYARNCluster;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

  String appName = "test";

  @Test
  public void miniYarnTest() {
    Configuration conf = new YarnConfiguration();
    int numNodeManagers = 1;
    int numLocalDirs = 1;
    int numLogDirs = 1;
    boolean enableAHS;

    /*
     * Timeline service should not start if TIMELINE_SERVICE_ENABLED == false
     * and enableAHS flag == false
     */
    conf.setBoolean(YarnConfiguration.TIMELINE_SERVICE_ENABLED, false);
    enableAHS = false;
    MiniYARNCluster cluster = null;
    try {
      cluster = new MiniYARNCluster(TestMiniYarnCluster.class.getSimpleName(),
        numNodeManagers, numLocalDirs, numLogDirs, numLogDirs, enableAHS);
      cluster.init(conf);
      cluster.start();

      YarnClient yarnClient = YarnClient.createYarnClient();
      yarnClient.init(conf);
      yarnClient.start();

      YarnClientApplication app = yarnClient.createApplication();
      GetNewApplicationResponse appResponse = app.getNewApplicationResponse();

      ApplicationSubmissionContext appContext = app.getApplicationSubmissionContext();
      ApplicationId appId = appContext.getApplicationId();

      appContext.setKeepContainersAcrossApplicationAttempts(false);
      appContext.setApplicationName(appName);

      // set local resources for the application master
      // local files or archives as needed
      // In this scenario, the jar file for the application master is part of the local resources
      Map<String, LocalResource> localResources = new HashMap<String, LocalResource>();
      // Copy the application master jar to the filesystem
      // Create a local resource to point to the destination jar path
      FileSystem fs = FileSystem.get(conf);
      addToLocalResources(fs, null, null, appId.toString(),
        localResources, null);

      //verify that the timeline service is not started.
      Assert.assertNull("Timeline Service should not have been started",
        cluster.getApplicationHistoryServer());
    } catch (Exception e) {

    }
    finally {
      if(cluster != null) {
        cluster.stop();
      }
    }
  }

  private void addToLocalResources(FileSystem fs, String fileSrcPath,
    String fileDstPath, String appId, Map<String, LocalResource> localResources,
    String resources) throws IOException {
    String suffix =
      appName + "/" + appId + "/" + fileDstPath;
    Path dst =
      new Path(fs.getHomeDirectory(), suffix);
    if (fileSrcPath == null) {
      FSDataOutputStream ostream = null;
      try {
        ostream = FileSystem
          .create(fs, dst, new FsPermission((short) 0710));
        ostream.writeUTF(resources);
      } finally {
        IOUtils.closeQuietly(ostream);
      }
    } else {
      fs.copyFromLocalFile(new Path(fileSrcPath), dst);
    }
    FileStatus scFileStatus = fs.getFileStatus(dst);
    LocalResource scRsrc =
      LocalResource.newInstance(
        ConverterUtils.getYarnUrlFromURI(dst.toUri()),
        LocalResourceType.FILE, LocalResourceVisibility.APPLICATION,
        scFileStatus.getLen(), scFileStatus.getModificationTime());
    localResources.put(fileDstPath, scRsrc);
  }

  private <T> ArrayList<T> getArrayList(Class<T> type) {
    ArrayList<T> arrayList = new ArrayList<T>();
    return arrayList;
  }
}
