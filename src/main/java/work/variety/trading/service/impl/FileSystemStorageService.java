package work.variety.trading.service.impl;

/**
 * @author zhangbin
 * @date 2018/7/25 10:53
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import work.variety.trading.exception.ErrorFileException;
import work.variety.trading.exception.StorageException;
import work.variety.trading.exception.StorageFileNotFoundException;
import work.variety.trading.service.StorageProperties;
import work.variety.trading.service.StorageService;

@Service
public class FileSystemStorageService implements StorageService {

  private final Path rootLocation;

  @Autowired
  public FileSystemStorageService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
    init();
  }

  @Override
  public String store(MultipartFile file) {
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    if (!filename.endsWith("xls")) {
      throw new ErrorFileException("只支持excel文件上传");
    }
    saveFile(file, filename);
    return filename;
  }

  @Override
  public String storeTXT(MultipartFile file) {
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    if (!filename.endsWith("txt")) {
      throw new ErrorFileException("只支持txt文件上传");
    }
    saveFile(file, filename);
    return filename;
  }

  @Override
  public String storeZip(MultipartFile file) {
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    if (!filename.endsWith("zip")) {
      throw new ErrorFileException("只支持zip文件上传");
    }
    saveFile(file, filename);
    return filename;
  }

  private void saveFile(MultipartFile file, String filename) {
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + filename);
      }
      if (filename.contains("..")) {
        // This is a security check
        throw new StorageException(
          "Cannot store file with relative path outside current directory "
            + filename);
      }
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, this.rootLocation.resolve(filename),
          StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + filename, e);
    }
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.rootLocation, 1)
        .filter(path -> !path.equals(this.rootLocation))
        .map(this.rootLocation::relativize);
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }

  }

  @Override
  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException(
          "Could not read file: " + filename);

      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(rootLocation.toFile());
  }

  @Override
  public void init() {
    try {
      File f = rootLocation.toFile();
      if (!f.exists()) {
        Files.createDirectories(rootLocation);
      }
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }
}
