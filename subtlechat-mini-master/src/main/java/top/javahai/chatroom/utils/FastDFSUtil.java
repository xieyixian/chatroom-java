package top.javahai.chatroom.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class FastDFSUtil {
  private static StorageClient1 client1;

  static{
    try{
      ClientGlobal.initByProperties("fastdfs-client.properties");
      TrackerClient trackerClient=new TrackerClient();
      TrackerServer trackerServer=trackerClient.getConnection();
      client1=new StorageClient1(trackerServer,null);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (MyException e) {
      e.printStackTrace();
    }
  }

  /**
   * uploadfile
   * @param file
   * @return
   * @throws IOException
   * @throws MyException
   */
  public static String upload(MultipartFile file) throws IOException, MyException {
    // filename
    String oldName=file.getOriginalFilename();
    // server path
    // oldName.substring(oldName.lastIndexOf(".")+1)
    return client1.upload_file1(file.getBytes(),oldName.substring(oldName.lastIndexOf(".")+1),null);
  }

  /**
   * Get a token to access a file
   * @throws UnsupportedEncodingException
   * @throws NoSuchAlgorithmException
   * @throws MyException
   * @return
   */
//  public static StringBuilder getToken(String fileId) throws UnsupportedEncodingException, NoSuchAlgorithmException, MyException {
//    int ts = (int) Instant.now().getEpochSecond();
//    fileId=fileId.substring(7);
//    String token = ProtoCommon.getToken(fileId, ts, "FastDFS1234567890");
//    StringBuilder sb = new StringBuilder();
//    sb.append("?token=").append(token);
//    sb.append("&ts=").append(ts);
//    return sb;
//  }
}
