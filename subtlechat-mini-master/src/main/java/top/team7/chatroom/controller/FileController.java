package top.team7.chatroom.controller;

import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.team7.chatroom.utils.AliyunOssUtil;
import top.team7.chatroom.utils.FastDFSUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@RestController
public class FileController {

  @Value("${fastdfs.nginx.host}")
  String nginxHost;

  @Autowired
  AliyunOssUtil aliyunOssUtil;

  @PostMapping("/file")
  public String uploadFlie(MultipartFile file) throws IOException, MyException {
    String fileId= FastDFSUtil.upload(file);
    return nginxHost+fileId;
  }

  @PostMapping("/ossFileUpload")
  public String ossFileUpload(@RequestParam("file")MultipartFile file,@RequestParam("module") String module) throws IOException, MyException {
    return aliyunOssUtil.upload(file.getInputStream(),module,file.getOriginalFilename());
  }

  @PostMapping("/base64ossFileUpload")
  public String base64ossFileUpload(@RequestParam("dataStr")String dataStr,@RequestParam("filename") String filename,
                                    @RequestParam("module") String module) throws IOException {
    String s = dataStr.substring(dataStr.indexOf(',')+1);
    String dataUri = dataStr.substring(0,dataStr.indexOf(','));
    Base64ToMultipartFile file = new Base64ToMultipartFile(s,dataUri);
    return aliyunOssUtil.upload(file.getInputStream(),module,filename);
  }


  class Base64ToMultipartFile implements MultipartFile {
    private final byte[] fileContent;

    private final String extension;
    private final String contentType;



    public Base64ToMultipartFile(String base64, String dataUri) {
      this.fileContent = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
      this.extension = dataUri.split(";")[0].split("/")[1];
      this.contentType = dataUri.split(";")[0].split(":")[1];
    }

    public Base64ToMultipartFile(String base64, String extension, String contentType) {
      this.fileContent = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
      this.extension = extension;
      this.contentType = contentType;
    }

    @Override
    public String getName() {
      return "param_" + System.currentTimeMillis();
    }

    @Override
    public String getOriginalFilename() {
      return "file_" + System.currentTimeMillis() + "." + extension;
    }

    @Override
    public String getContentType() {
      return contentType;
    }

    @Override
    public boolean isEmpty() {
      return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
      return fileContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
      return fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
      return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
      try (FileOutputStream fos = new FileOutputStream(file)) {
        fos.write(fileContent);
      }
    }

  }



}
