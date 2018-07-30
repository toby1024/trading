CREATE TABLE `client_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `futures_capital_number` varchar(50) DEFAULT NULL COMMENT '客户期货期权内部资金账户	',
  `stock_capital_number` varchar(50) DEFAULT NULL COMMENT '客户证券现货内部资金账户	',
  `company_name` varchar(100) DEFAULT NULL COMMENT '期货公司名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


CREATE TABLE `deal_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `deal_date` datetime DEFAULT NULL COMMENT '交易日期',
  `client_info_id` int(11) DEFAULT NULL COMMENT '关联的客户id',
  `contract` varchar(50) DEFAULT NULL COMMENT '合约',
  `deal_number` varchar(50) DEFAULT NULL COMMENT '成交序号',
  `deal_type` varchar(20) DEFAULT NULL COMMENT '买/卖',
  `deal_price` decimal(8,2) DEFAULT NULL COMMENT '成交价',
  `board_lot` int(11) DEFAULT NULL COMMENT '手数',
  `deal_fee` decimal(8,2) DEFAULT NULL COMMENT '成交额',
  `open_close` varchar(20) DEFAULT NULL COMMENT '开/平',
  `commission` decimal(8,2) DEFAULT NULL COMMENT '手续费',
  `close_profit` decimal(8,2) DEFAULT NULL COMMENT '平仓盈亏',
  `speculate_hedging` varchar(20) DEFAULT NULL COMMENT '投机/套保',
  `real_deal_date` date DEFAULT NULL COMMENT '实际成交日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=338 DEFAULT CHARSET=utf8;

CREATE TABLE `position_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `contract` varchar(50) DEFAULT NULL COMMENT '合约',
  `deal_number` varchar(50) DEFAULT NULL COMMENT '成交序号',
  `buy_board_lot` int(11) DEFAULT NULL COMMENT '买持仓',
  `buy_price` decimal(8,2) DEFAULT NULL COMMENT '买入价',
  `sell_board_lot` int(11) DEFAULT NULL COMMENT '卖持仓',
  `sell_price` decimal(8,2) DEFAULT NULL COMMENT '卖出价',
  `yesterday_price` decimal(8,2) DEFAULT NULL COMMENT '昨结算价',
  `today_price` int(11) DEFAULT NULL COMMENT '今结算价',
  `profit` decimal(8,2) DEFAULT NULL COMMENT '浮动盈亏',
  `speculate_hedging` varchar(20) DEFAULT NULL COMMENT '投机/套保',
  `transaction_number` varchar(30) DEFAULT NULL COMMENT '交易编码',
  `real_deal_date` date DEFAULT NULL COMMENT '实际成交日期',
  `client_info_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;