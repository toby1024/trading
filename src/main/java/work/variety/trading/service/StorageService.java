package work.variety.trading.service;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;
/**
 * @author zhangbin
 * @date 2018/7/25 10:50
 */
public interface StorageService {
  void init();

  String store(MultipartFile file);

  String storeTXT(MultipartFile file);

  String storeZip(MultipartFile file);

  Stream<Path> loadAll();

  Path load(String filename);

  Resource loadAsResource(String filename);

  void deleteAll();
}
