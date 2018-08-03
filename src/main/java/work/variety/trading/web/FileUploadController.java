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
        "成功上传并解析文件： " + file.getOriginalFilename() + "!");
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
        "成功上传并解析文件： " + file.getOriginalFilename() + "!");
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
    String filename = storageService.storeTXT(file);
    txtDataParseService.parse(filename);
    redirectAttributes.addFlashAttribute("message", "成功上传并解析文件： " + file.getOriginalFilename() + "!");
    return "redirect:/upload/uploadTxt";
  }


  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
    return ResponseEntity.notFound().build();
  }

  private String storeFile(MultipartFile file) {
    return storageService.store(file);
  }

}
