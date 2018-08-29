package work.variety.trading.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangbin
 * @date 2018/8/28 17:19
 */
public class TestMatchs {

  public static void main(String[] args) {
    Pattern pattern = Pattern.compile("^\\|\\d{8}\\|");

    Matcher matcher = pattern.matcher("|20180808|大商所  |豆油 ");
    matcher.find();
    System.out.println(matcher.start());
    matcher = pattern.matcher("|成交日期| 交易所 | ");
    System.out.println(matcher.find());
  }
}
