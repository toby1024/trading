package work.variety.trading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 查询分页的参数
 *
 * @author zhangbin
 * @date 2018/3/27 15:50
 */
@Data
public class PageDto<T> implements Serializable {

  private static final Integer DEFAULT_PAGE_SIZE = 10;

  /**
   * 当前页码
   */
  private Integer pageNum;
  /**
   * 每页条数
   */
  private Integer pageSize;
  /**
   * 排序字段
   */
  private String orderBy;
  /**
   * 是否倒序
   */
  private Boolean desc;
  /**
   * 当前页的数量
   */
  private Integer size;
  /**
   * 总条数
   */
  private Integer total;
  /**
   * 总页数
   */
  private Integer pages;

  /**
   * 前一页
   */
  private Integer prePage;
  /**
   * 下一页
   */
  private Integer nextPage;

  private List<T> list;

  public PageDto(List<T> list, Integer total) {
    new PageDto<T>(list, 1, total, DEFAULT_PAGE_SIZE);
  }

  public PageDto(List<T> list, Integer pageNum, Integer total, Integer pageSize) {
    this.list = list;
    this.total = total;
    this.pageSize = pageSize;
    this.pageNum = pageNum;
    this.pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    this.nextPage = pages > pageNum ? pageNum + 1 : null;
    this.prePage = null;
  }

  public Integer getNextPage() {
    if (pageNum < pages) {
      return pageNum + 1;
    }
    return pageNum;
  }

  public Integer getPrePage() {
    if (pageNum > 1) {
      return pageNum - 1;
    }
    return 1;
  }

  /**
   * 默认第1页
   *
   * @return pageNum
   */
  public Integer getPageNum() {
    if (pageNum == null) {
      return 1;
    }
    return pageNum;
  }

  /**
   * 默认每页10条
   *
   * @return pageSize
   */
  public Integer getPageSize() {
    if (pageSize == null) {
      return DEFAULT_PAGE_SIZE;
    }
    return pageSize;
  }

  /**
   * 默认按id排序
   *
   * @return orderBy
   */
  public String getOrderBy() {
    if (StringUtils.isBlank(orderBy)) {
      return "id";
    }
    return orderBy;
  }

  /**
   * 默认倒序排序
   *
   * @return desc
   */
  public Boolean isDesc() {
    return desc == null || desc;
  }
}
