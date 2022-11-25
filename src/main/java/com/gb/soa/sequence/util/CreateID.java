package com.gb.soa.sequence.util;

import java.util.UUID;
/***
 * 通过UUID创建数据库表的ID
 * @author 胡永强
 *
 */
public class CreateID {
  public static String getUUID(){
	  return UUID.randomUUID().toString().replace("-", "");
  }

}
