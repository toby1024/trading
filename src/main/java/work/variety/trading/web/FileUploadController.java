package work.variety.trading.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import work.variety.trading.exception.ErrorFileException;
import work.variety.trading.exception.StorageFileNotFoundException;
import work.variety.trading.service.FileParseService;
import work.variety.trading.service.StorageService;
import work.variety.trading.service.impl.ZipFileParseService;
import work.variety.trading.util.ZipUtil;

/**
 * @author zhangbin
 * @date 2018/7/25 10:44
 */
@Controller
@RequestMapping("/upload")
public class FileUploadController {
  @Autowired
  private StorageService storageService;
  @Autowired
  private FileParseService monthExcelParseService;
  @Autowired
  private FileParseService dayExcelParseService;
  @Autowired
  private FileParseService txtDataParseService;
  @Autowired
  private ZipFileParseService zipFileParseService;

  @GetMapping("uploadMonth")
  public String uploadMonth() {
    return "upload/uploadMonth";
  }

  @PostMapping("month")
  public String monthData(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

    try {
      String filename = storeFile(file);
      monthExcelParseService.parse(filename);

      redirectAttributes.addFlashAttribute("message",
        "成功上传并解析月汇总文件： " + file.getOriginalFilename() + "!");

    } catch (ErrorFileException e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }
    return "redirect:/upload/uploadMonth";
  }

  @GetMapping("uploadDay")
  public String uploadDay() {
    return "upload/uploadDay";
  }

  @PostMapping("day")
  public String dayData(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    try {
      String filename = storeFile(file);
      dayExcelParseService.parse(filename);
      redirectAttributes.addFlashAttribute("message",
        "成功上传并解析日文件： " + file.getOriginalFilename() + "!");
    } catch (ErrorFileException e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }
    return "redirect:/upload/uploadDay";
  }

  @GetMapping("uploadTxt")
  public String uploadTxt() {
    return "upload/uploadTxt";
  }

  @PostMapping("txt")
  public String txt(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

    try {
      String filename = storageService.storeTXT(file);
      txtDataParseService.parse(filename);
      redirectAttributes.addFlashAttribute("message", "成功上传并解析txt文件： " + file.getOriginalFilename() + "!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", "解析文件失败");
    }
    return "redirect:/upload/uploadTxt";
  }

  @GetMapping("uploadZip")
  public String uploadZip() {
    return "upload/uploadZip";
  }

  @PostMapping("zip")
  public String zip(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    try {
      String filename = storageService.storeZip(file);
      zipFileParseService.parse(filename);
      redirectAttributes.addFlashAttribute("message", "成功上传并解析zip文件： " + file.getOriginalFilename() + "!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", "解析文件失败");
    }
    return "redirect:/upload/uploadZip";
  }


  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
    return ResponseEntity.notFound().build();
  }

  private String storeFile(MultipartFile file) {
    return storageService.store(file);
  }

}
