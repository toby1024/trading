package work.variety.trading.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.*;

/**
 * @author zhangbin
 * @date 2018/8/29 10:32
 */
public class ZipUtil {

  public static void unZip(String fileName, String zipDir) throws IOException {
    ZipFile zfile = new ZipFile(fileName);
    Enumeration zList = zfile.getEntries();
    ZipEntry ze = null;
    byte[] buf = new byte[1024];
    while (zList.hasMoreElements()) {
      ze = (ZipEntry) zList.nextElement();
      if (ze.isDirectory()) {
        File f = new File(zipDir + ze.getName());
        f.mkdir();
        continue;
      }
      OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(zipDir, ze.getName().replaceAll("\\.\\.","_"))));
      InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
      int readLen = 0;
      while ((readLen = is.read(buf, 0, 1024)) != -1) {
        os.write(buf, 0, readLen);
      }
      is.close();
      os.close();
    }
    zfile.close();

  }

  /**
   * 给定根目录，返回一个相对路径所对应的实际文件名.
   *
   * @param baseDir     指定根目录
   * @param absFileName 相对路径名，来自于ZipEntry中的name
   * @return java.io.File 实际的文件
   */
  public static File getRealFileName(String baseDir, String absFileName) {
    String[] dirs = absFileName.split("/");
    File ret = new File(baseDir);
    if (dirs.length > 1) {
      for (int i = 0; i < dirs.length - 1; i++) {
        ret = new File(ret, dirs[i]);
      }
      if (!ret.exists()) {
        ret.mkdirs();
      }
    }
    return new File(ret, dirs[dirs.length - 1]);
  }
}
