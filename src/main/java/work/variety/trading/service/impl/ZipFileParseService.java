package work.variety.trading.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.variety.trading.service.FileParseService;
import work.variety.trading.service.StorageProperties;
import work.variety.trading.util.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * @author zhangbin
 * @date 2018/8/29 11:06
 */
@Slf4j
@Service("zipFileParseService")
public class ZipFileParseService implements FileParseService {
  private final Path rootLocation;

  @Autowired
  public ZipFileParseService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
  }


  @Override
  public void parse(String filename) {
    log.info("----开始解析zip文件----");
    try {
      ZipUtil.unZip(rootLocation + "/" + filename, rootLocation.toString());
      String[] txt = {"txt"};
      Collection<File> files = FileUtils.listFiles(new File(rootLocation + "/" + filename.substring(0, filename.lastIndexOf("."))), txt, false);
      files.parallelStream().forEach(file -> {
        log.info("----解析：" + file.getAbsolutePath());
        txtDataParseService.parse(filename.substring(0, filename.lastIndexOf(".")) + "/" + file.getName());
      });
    } catch (IOException e) {
      log.error("----解压文件失败----");
      log.error(e.getMessage(), e);
      e.printStackTrace();
    }


  }

  @Autowired
  private FileParseService txtDataParseService;
}
