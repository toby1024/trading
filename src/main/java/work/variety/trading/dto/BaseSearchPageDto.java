package work.variety.trading.dto;

import lombok.Data;

/**
 * @author zhangbin
 * @date 2018/7/29 07:59
 */
@Data
public class BaseSearchPageDto {
  private int pageNum = 1;
  private int pageSize = 10;
  private String orderBy;
  private String orderDesc;

  public int getStartPage() {
    if (pageNum > 0) {
      return (pageNum - 1) * pageSize;
    }
    return 0;
  }
}
