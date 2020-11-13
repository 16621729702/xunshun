package com.wink.livemall.utils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Tools {

  //���ڸ�ʽ
  private static SimpleDateFormat dateFormat1 = new SimpleDateFormat(
      "yyyy-MM-dd");
  private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");

  private static final int nMaxBufferSize = 15 * 1024;
  private static final int MAX_BUFFER_SIZE = 2 * 1024;

  

      /*******************************************************************************
   *Name:toGB
   *
   *Function:Covert String to Unicode Code,and convert null to empty string
   *
   *Author:Jason Shi
   *
   *Create Date:2000/6/6
   *
   *Parameter:
   *        none
   *
   *Relate table:none
   *
   *Return value:
   *        return Unicode Code String  if success
   *        return "" if fail or inStr is null
       ******************************************************************************/
  public static String toGB(String inStr) {
    try {

      if (inStr == null)
        return "";
//Convert String
//        return inStr;
//        return (new String(inStr.getBytes ("8859_1"),"gb2312"));
      //if("Linux".equals( System.getProperty("os.name"))){
//      if(true){
//        return (new String(inStr.getBytes ("ISO8859_1"),"UTF8"));
//      }
//      else{
      return inStr;
//      }
    }
    catch (Exception e) {
      return "";
    }
  }

  public static String fromGB(String inStr) {
    try {

      if (inStr == null)
        return "";
//Convert String
// if OS is NT then return inStr
//        return inStr;
//      return inStr;
      return (new String(inStr.getBytes(StandardCharsets.UTF_8), "8859_1"));
    }
    catch (Exception e) {
      return "";
    }
  }

  public static String fromGB(byte[] inBytes) {
    try {

      if (inBytes == null)
        return "";
//Convert String
// if OS is NT then return inStr

//if OS is Linux then return below
      return (new String(inBytes, "8859_1"));

    }
    catch (Exception e) {
      return "";
    }
  }

  public static long toLong(String inStr) {
    try {
      if (inStr == null) {
        return 0;
      }
      else {
        return Long.valueOf(inStr).longValue();
      }
    }
    catch (Exception e) {
      return 0;
    }
  }

  public static int toInteger(String inStr) {
    try {
      if (inStr == null) {
        return 0;
      }
      else {
//return Integer.valueOf(valueOf(inStr)).intValue();
        return new Integer(inStr).intValue();
      }
    }
    catch (Exception e) {
      return 0;
    }
  }

  public static double toDouble(String inStr) {
    try {
      if (inStr == null) {
        return 0;
      }
      else {
        return Double.valueOf(inStr).doubleValue();
      }
    }
    catch (Exception e) {
      return 0;
    }
  }

  public static float toFloat(String inStr) {
    try {
      if (inStr == null) {
        return 0;
      }
      else {
        return Float.valueOf(inStr).floatValue();
      }
    }
    catch (Exception e) {
      return 0;
    }
  }

  public static String msNull(String inStr) {
    if (inStr == null) {
      return "";
    }
    else {
      return inStr;
    }
  }

  public static Object msNull(Object inStr) {
    if (inStr == null) {
      return "";
    }
    else {
      return inStr;
    }
  }



  private static byte[] blobToBytes(Blob pBlob) {
    byte[] buffer = null;
    try {
      if (pBlob != null) {
        buffer = pBlob.getBytes( (long) 1, (int) pBlob.length());
      }
    }
    catch (Exception e) {
      //e.printStackTrace();
    }
    return buffer;
  }

//��������String����BigDecimal��������String�����ִ�������null
  public static BigDecimal getBigDecimal(String str) {
    BigDecimal bd = null;
    if (str == null)
      return null;
    try {
      bd = new BigDecimal(str);
    }
    catch (Exception e) {
      return null;
    }
    return bd;
  }

//����꣬�£��գ�ת��ΪTimestamp����
  public static Timestamp getTimestamp(String sDate) {
    Timestamp ts = null;
    if (sDate == null || "".equals(sDate))
      return null;
    /*xi.zhu_begin_20111108-̫ƽ�м������Ŀ:����sDate='2011-11-7'�����ַ����ͣ���ȷ��Ӧ��'2011-11-07'*/
    if(sDate.length() < 10){
        int[] iDate = {4, 2, 2};
    	String rDate = "";
    	String[] tDate = sDate.split("-");
    	for(int i=0; i<tDate.length; i++){
    		while(tDate[i].length()<iDate[i]){
    			tDate[i] = "0" + tDate[i];
    		}
    		if(i==0){
    			rDate += tDate[i];
    		}else{
    			rDate = rDate + "-" + tDate[i];
    		}
    	}
    	ts = Timestamp.valueOf(rDate + " 00:00:00.000000000");
    }else{
    	ts = Timestamp.valueOf(sDate + " 00:00:00.000000000");
    }
    /*xi.zhu_end_20111108-̫ƽ�м������Ŀ:����sDate='2011-11-7'�����ַ����ͣ���ȷ��Ӧ��'2011-11-07'*/
    
    return ts;
  }

  public static Timestamp getEndTimestamp(String sDate) {
    Timestamp ts = null;
    if (sDate == null || "".equals(sDate))
      return null;
    ts = Timestamp.valueOf(sDate + " 23:59:59.999999999");
    return ts;
  }

// following date involved static mothods by rock yang, 2001-7
// NOTE: year and month have literal meaning. e.g. 2000-7-1, year = 2001, month = 7, date = 1
  public static String Date2String(Date date) {
    if (date == null)
      return null;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return simpleDateFormat.format(date);
  }

  public static Date toDate(String sDate) throws java.text.
      ParseException {
    Date result = null;
    if (sDate == null)
      result = null;
    else if (sDate.length() == 10 && sDate.indexOf("-") == 4) {
      result = dateFormat1.parse(sDate);
    }
    else if (sDate.length() == 8) {
      result = dateFormat2.parse(sDate);
    }
    else if(sDate.length() < 10 && sDate.indexOf("-") == 4){
    	// ����2014-9-18
        int[] iDate = {4, 2, 2};
    	String rDate = "";
    	String[] tDate = sDate.split("-");
    	for(int i=0; i<tDate.length; i++){
    		while(tDate[i].length()<iDate[i]){
    			tDate[i] = "0" + tDate[i];
    		}
    		if(i==0){
    			rDate += tDate[i];
    		}else{
    			rDate = rDate + "-" + tDate[i];
    		}
    	}
    	result = dateFormat1.parse(rDate);
    }
    return result;
  }

//��URL�������һ����ǣ���֤�û��ύ��URL���ɷ�������ɵ�
//���� �ڿͻ�����ʾһ�����ն����ͼƬ,�� showimage?img_id=123
//��ʽ����URL,�û������޸�img_id���������ÿ�����ͼƬ
//��˶Ը�URL����ǣ���showimage�����м�����Ƿ���URL���
//��صķ�����
//    encryptURL
//    checkEncryptURL

  public static Timestamp addDate(Timestamp oldDate, int addDays) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(oldDate.getTime()));
    calendar.add(Calendar.DATE, addDays);
    return new Timestamp(calendar.getTime().getTime());
  }
  
	public static java.sql.Date addDate(Date oldDate, int addDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(oldDate.getTime()));
		calendar.add(Calendar.DATE, addDays);
	
		String tmpDate = Date2String(calendar.getTime());
	
		return java.sql.Date.valueOf(tmpDate);
	}

  public static Timestamp addMonth(Timestamp oldDate, int addMonths) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(oldDate.getTime()));
    calendar.add(Calendar.MONTH, addMonths);
    return new Timestamp(calendar.getTime().getTime());
  }

  public static Timestamp addYear(Timestamp oldDate, int addYears) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(oldDate.getTime()));
    calendar.add(Calendar.YEAR, addYears);
    return new Timestamp(calendar.getTime().getTime());
  }

  public static Date toDate(int year, int month, int date) {
    return toDate(year, month, date, 0, 0, 0);
  }

  public static Date toDate(int year, int month, int date, int hrs,
                                      int min, int sec) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month - 1, date, hrs, min, sec);
    return calendar.getTime();
  }

  public static int getYear(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.YEAR);
  }

  public static int getYear(long date) {
    return getYear(new Date(date));
  }

  public static int getMonth(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.MONTH) + 1;
  }

  public static int getMonth(long date) {
    return getMonth(new Date(date));
  }

  public static int getDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.DAY_OF_MONTH);
  }

  public static int getDate(long date) {
    return getDate(new Date(date));
  }

  public static int getHours(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.HOUR);
  }

  public static int getMinutes(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.MINUTE);
  }

  public static int getSeconds(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.SECOND);
  }

  public static String getChineseDate(Date d) {
    if (d == null) {
      return " �� �� ��";
    }
    else {
      return "" + getYear(d) + "��" + getMonth(d) + "��" + getDate(d) + "��";
    }
  }

  public static String toString(Object obj) {
    if (obj == null) {
      return "";
    }
    else if (obj instanceof Date) {
      return Date2String( (Date) obj);
    }
    else {
      return obj.toString();
    }
  }

  /*----------------------------------------------------------------------
   Function name:	convertBirthDateToAge(Dabe birthDate,Date nowDate)
   Description: covert birthdate to age
   Input:		birthday, the current date you want to compare
   Output:     age
   Author:		Richard Zhang, Rodolf Li
   Date:		Aug 15,2001
   -----------------------------------------------------------------------*/
  public static int convertBirthDateToAge(Date birthDate, Date nowDate) {
    int nAge = 0;
    nAge = nowDate.getYear() - birthDate.getYear();
    if (nowDate.getMonth() * 100 + nowDate.getDate()
        < birthDate.getMonth() * 100 + birthDate.getDate()) {
      nAge--;
    }
    return nAge;
  }

  /*----------------------------------------------------------------------
   Function name:	getDouble()
   Description: covert double to double
   Input:	    ORIGIN double, count, bDischarge
   Output:     final double
   Author:		Richard Zhang
   Date:		Aug 15,2001
   -----------------------------------------------------------------------*/
  public static double getDoubleDischargeTail(double dOrigin, int nCount) {
    return getDouble(dOrigin, nCount, true);
  }

  public static double getDoubleNotDischargeTail(double dOrigin, int nCount) {
    return getDouble(dOrigin, nCount, false);
  }

  public static double getDouble(double dOrigin, int nCount, boolean bDischarge) {
    long lTemp = (long) Math.pow(10, nCount);
    if (bDischarge == true)
      return (long) (dOrigin * lTemp) / (double) lTemp;
    else
      return Math.round(dOrigin * lTemp) / (double) lTemp;
  }

  /*----------------------------------------------------------------------
   Function name:	getMonthAmount()
   Description: get two date's between month
   Input:	    Date startDate,Date endDate
   Output:     int month_amount
   Author:		Richard Zhang
   Date:		Sept 2,2001
   -----------------------------------------------------------------------*/
  public static int getMonthAmount(Date startDate, Date endDate) {
    int nYear = 0;
    int nMonth = 0;
    int nDay = 0;
    int nMonthAmount = 0;
    Calendar cldStart = Calendar.getInstance();
    Calendar cldEnd = Calendar.getInstance();

    cldStart.setTime(startDate);
    cldEnd.setTime(endDate);

    nYear = cldEnd.get(Calendar.YEAR) - cldStart.get(Calendar.YEAR);
    nMonth = cldEnd.get(Calendar.MONTH) - cldStart.get(Calendar.MONTH);
    nDay = cldEnd.get(Calendar.DATE) - cldStart.get(Calendar.DATE);

    if (nDay < 0)
      nMonthAmount = nYear * 12 + nMonth - 1;
    else
      nMonthAmount = nYear * 12 + nMonth;
    return nMonthAmount;
  }

  public static int getDayAmount(Date startDate, Date endDate) {
    int dayAmount = 0;
//    Calendar cldStart = Calendar.getInstance();
//    Calendar cldEnd   = Calendar.getInstance();
//
//    cldStart.setTime(startDate);
//    cldEnd.setTime(endDate);
//
//    int nStart = cldStart.get(cldStart.DAY_OF_YEAR);
//    int nEnd = cldEnd.get(cldEnd.DAY_OF_YEAR);
//    if (nEnd - nStart > 0)
//      dayAmount = nEnd - nStart;
//    else
//      dayAmount = 365 - (nEnd - nStart);

    return (int) ( (endDate.getTime() - startDate.getTime()) /
                  (1000 * 60 * 60 * 24));
  }

  /*----------------------------------------------------------------------
   Function name:	doubleToStr()
   Description: �����ѧ��������
   Input:	    double
   Output:     ��double��Ӧ���ַ�
   Author:		ernest
   Date:		Sept 2,2001
   ----------------------------------------------------------------------*/
  public static String doubleToStr(double d) {
    String str = NumberFormat.getInstance().format(d);
    String str2 = "";
    int i = 0;
    while ( (i >= 0) && str.length() > 0) {
      i = str.indexOf(",");
      if (i == -1) {
        str2 += str;
        break;
      }
      else {
        str2 += str.substring(0, i);
        str = str.substring(i + 1);
      }
    }
    return str2;
  }

// <Add by=xiayun>
  /**
   * �����ַ�ת��Ϊnull
   */
  static public String emptyStringToNull(String value) {
    if (value == null)
      return null;
    value = value.trim();
    if (value.length() == 0)
      return null;
    return value;
  }

  /**
   * �����ڸ�ʽת��Ϊyyyy-mm-dd��ʽ��String
   * @parameter division�ָ��
   */
  static public String dateToString(Date date, String division) {
    if (date == null)
      return null;
    if (division == null)
      division = "";
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    String result = "" + year;
    result += division;
    if (month < 10)
      result += "0";
    result += month;
    result += division;
    if (day < 10)
      result += "0";
    result += day;
    return result;
  }

  /**
   * �����ڸ�ʽת��Ϊyyyy-mm-dd��ʽ��String
   * @parameter division�ָ��
   */
  static public String dateToString(Timestamp date, String division) {
    if (date == null)
      return null;
    return dateToString(new Date(date.getTime()), division);
  }

  /**
   * �����ڸ�ʽת��Ϊyyyy-mm-dd��ʽ��String
   * @parameter division�ָ��
   */
  static public String dateToString(long date, String division) throws
      Exception {

      return dateToString(new Date(date), division);

  }

// </Add>
  /**
   * collection �����ʽת��ΪXML�ı���ʽ
   * @param list
   * @return
   */
  public static String generateXML(Collection list) {
    StringBuffer xml = new StringBuffer();
    Iterator iter = null;
    if (list != null) {
      iter = list.iterator();
      while (iter.hasNext()) {
        HashMap row = (HashMap) iter.next();
        if (row != null) {
          Set set = row.keySet();
          Iterator cols = set.iterator();
          xml.append("<row>");
          while (cols.hasNext()) {
            String colName = (String) cols.next();
            String colValue = (String) row.get(colName);
            xml.append("<" + colName + ">" + colValue + "</" + colName + ">");
          } // while 2
          xml.append("</row>\n");
        } // if 2
      } // while 1
    } // if 1
    return xml.toString();
  }

  /**
   * ���javascript��Array
   * @param sValue
   */
  static public String generateArray(String[][] sValue) {
    if (sValue == null) {
      sValue = new String[0][0];
    }

    StringBuffer buff = new StringBuffer(4096);
    buff.append("new Array(");
    String[] ss;
    String s;
    for (int i = 0; i < sValue.length; i++) {
      ss = sValue[i];
      if (ss == null) {
        ss = new String[0];
      }
      if (i > 0) {
        buff.append(",");
      }
      buff.append("new Array(");
      for (int j = 0; j < ss.length; j++) {
        if (j > 0) {
          buff.append(",");
        }
        s = ss[j];
        if (s == null) {
          s = "&nbsp;";
        }
        else if ("".equals(s.trim())) {
          s = "&nbsp;";
        }
        buff.append("\"" + convertString(s) + "\"");
      }
      buff.append(")");
    }
    buff.append(")");
    return buff.toString();
  }

  private static String convertString(String sSource) {
    if (sSource == null) {
      return null;
    }
    StringBuffer buff = new StringBuffer(1024);
    int n = sSource.length();
    char c;
    for (int i = 0; i < n; i++) {
      c = sSource.charAt(i);
      if (c == '"') {
        buff.append('\\');
        buff.append(c);
      }
      else if (c == '\\') {
        buff.append('\\');
        buff.append(c);
      }
      else if (c == '\r') {
        buff.append("\\r");
      }
      else if (c == '\n') {
        buff.append("\\n");
      }
      else {
        buff.append(c);
      }
    }
    return buff.toString();
  }

  /**
   * �ж�һ�����Ƿ���0
   * @param value Ҫ�жϵ���
   * @param digits �жϵ�С����λ��
   * @return �Ƿ���0
   */
  public static boolean isZero(double value, int digits) {
    return getDouble(value, digits, false) == 0;
  }

  /**
   * ��ȡobj��String���,��objΪnullʱ������defaultValue
   * @param obj
   * @param defaultValue
   * @return
   */
  public static String getObjectString(Object obj, String defaultValue) {
    if (obj == null) {
      return defaultValue;
    }
    else {
      return obj.toString();
    }
  }

      /*******************************************************************************
   *Name:processStrings
   *
   *Function:to replace all string \r\n to <br>, space to &nbsp, < to &lt, and
   * > to &gt for html displying
   *
   *Author:Minghua wang
   *
   *Create Date:July 5,2000
   *
   *Parameter:
   *    String str
   *
   *Return value:
   *        return replaced string
       ******************************************************************************/
  public static String processStrings(String str) {
    if (str != null) {
      str = replaceString("&", "&amp;", str);
      str = replaceString(" ", "&nbsp;", str);
      str = replaceString("<", "&lt;", str);
      str = replaceString(">", "&gt;", str);
      str = replaceString("\r\n", "<br>", str);
      str = replaceString("\"", "&quot;", str);

      return (str);
    }
    else
      return (str);
  } //end of method processStrings()

  /**
   * �����ڸ�ʽת��Ϊyyyy-mm-dd��ʽ��String
   * @parameter division�ָ��
   */

// this method to replace space to &nbsp  and newline carriage return to <br> ;
      /*******************************************************************************
   *Name:replaceString
   *
   *Function:replace the olsStr to newStr in a string wholeStr
   *
   *Author:Minghua Wang
   *
   *Create Date:July 5, 2000
   *
   *Parameter:
   *        String oldStr , String newStr, String wholeStr
   *
   *Return value:
   *        return the replaced string
       ******************************************************************************/
  public static String replaceString(String oldStr, String newStr,
                                     String wholeStr) {
    if (wholeStr == null)
      return "";

    if (oldStr == null)
      return wholeStr;
    if (newStr == null)
      return wholeStr;
// canceled by rodolf
    /*
//change by jason shi for avoid dead loop
     if(newStr.indexOf(oldStr)>=0){
     return wholeStr;
     }
     */
    int start, end;
    StringBuffer result = new StringBuffer();
    result = result.append(wholeStr);
// updated by rodolf
    start = 0;

    while (wholeStr.indexOf(oldStr, start) > -1) {
      start = wholeStr.indexOf(oldStr, start);
      end = start + oldStr.length();
      result.replace(start, end, newStr);
      wholeStr = result.toString();
      start += newStr.length();
    }
    return wholeStr;
  }

  /**
   * ��������ȡdigitsλС��
   * @param value
   * @param digits С��λ��
   * @return String
   */
  public static String roundString(double value, int digits) {
    String format = "#";
    if (digits > 0) {
      format += ".";
    }
    for (int i = 0; i < digits; i++) {
      format += "#";
    }
    DecimalFormat numberFormatter = new DecimalFormat(format);
    return numberFormatter.format(value);
  }


  /**
   * ��Unicode������ַ�ת��ΪGBK���ַ�
   * @param pGbkString
   * @return
   */
  public static String unicodekToGBK(String pUnicodeString) {
    String sResult = null;
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OutputStreamWriter os = new OutputStreamWriter(baos, "GBK");
      os.write(pUnicodeString);
      os.close();
      sResult = baos.toString("GBK");
    }
    catch (Exception e) {
      //e.printStackTrace();
    }
    return sResult;
  }

  /**
   * ��GBK������ַ�ת��ΪUnicode���ַ�
   * @param pGbkString
   * @return
   */
  public static char[] gbkToUnicode(char[] pGbkString) {
    char[] sResult = null;
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      OutputStreamWriter os = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
      os.write(pGbkString);
      os.close();
      sResult = baos.toString("UTF-8").toCharArray();
    }
    catch (Exception e) {
      //e.printStackTrace();

    }
    return sResult;
  }

  /**
   * ��doubleת��ΪString
   * @param d double���
   * @param limit С��λ��
   * @param hasComma ��ɵ�String�Ƿ�ʹ�ö���(,)�ָ�
   * @param bDischarge �Ƿ񲻲����������롣=true ֱ��ȥΪβ
   * @return ת�����String
   */
  public static String doubleToStr(double d, int limit, boolean hasComma,
                                   boolean bDischarge) {
    d = getDouble(d, limit, bDischarge);
    String sFormat = "#";
    if (hasComma) {
      sFormat += ",";
    }
    sFormat += "##0";
    if (limit > 0) {
      sFormat += ".";
      for (int i = 0; i < limit; i++) {
        sFormat += "0";
      }
    }
    DecimalFormat numberFormatter = new DecimalFormat(sFormat);
    return numberFormatter.format(d);
  }

  /**
   * ���ָ�����ȵ��ַ���Դ�ַ�ǰ
   * �÷���֧�ִ�������
   * @param sourceValue Դ�ִ�
   * @param length ��ʽ����
   * @param c ����ַ�
   */
  public static String beforFilling(String sourceValue, int length, char c) {
    StringBuffer fill = new StringBuffer(1024);
    int currentLength = 0;
    if (sourceValue != null)
      currentLength = getByteLength(sourceValue);
    for (int i = 0; i < length - currentLength; i++)
      fill.append(c);
    return sourceValue == null ? fill.toString() :
        fill.toString() + sourceValue;
  }

  /**
   * ���ָ�����ȵ��ַ���Դ�ַ�β
   * �÷���֧�ִ�������
   * @param sourceValue Դ�ִ�
   * @param length ��ʽ����
   * @param c ����ַ�
   */
  public static String afterFilling(String value, int length, char c) {
    StringBuffer fill = new StringBuffer(1024);
    int currentLength = 0;
    if (value != null)

      //�����ַ�ܵ��ֽڳ���
      //  currentLength = value.length();
      currentLength = getByteLength(value);
    for (int i = 0; i < length - currentLength; i++)
      fill.append(c);
    return value == null ? fill.toString() : value + fill.toString();
  }

  /**
   * �����ַ�ܵ��ֽڳ���
   * @param s Դ�ִ�
   */
  public static int getByteLength(String s) {
    int length = 0;
      byte[] bytes = s.getBytes(StandardCharsets.US_ASCII);
      for (int i = 0; i < bytes.length; i++) {
        length += bytes[i] == 63 ? 2 : 1;
      }

      return length;
  }



  /**
   * �������ַ�תΪ��ָ��lineCharSize(ASCII��׼)�����д�����ַ�,
   * ֧�����ģ����з�'\n'
   *
   * <br>meyer.lu 2004-02-16
   *   fix the bug of lost the last char
   * <br>chris.yang 2004-03-22
   *   change getNewLinedText the process body and fix "double the last char when extend the maxlength"
   * @author meyer.lu 2004-02-12
   * @param originalText
   * @param lineCharSize ÿ���ַ����ASCII��׼��
   * @param indent ���е������ַ�
   * @return
   */
  public static String getNewLinedText(String originalText, int lineCharSize, String indent) {
    //if not ended by \n then add one \n for gen all the text(if no \n then the last line will not gen)

    boolean needAddReturnChar = false;

    if(originalText == null)
      return "";

    if(originalText!=null && originalText.length()>0 && "\n".charAt(0)!=originalText.charAt(originalText.length()-1))
      needAddReturnChar = true;


    if(needAddReturnChar)
      originalText += "\n";


    StringBuffer newLinedText = new StringBuffer(originalText.length() + 10);
      StringBuffer tempLineText = new StringBuffer();
      String currentLineText = "";

      int tempLineSize = 0;
      char ch;
      for (int i = 0; i < originalText.length(); i++) {
        //current char
        ch = originalText.charAt(i);

        //pass the "\r" when read it from page before the "\n"
        if(ch=="\r".charAt(0))
          continue;

        //old text
        currentLineText = tempLineText.toString();

        //new text
        tempLineText.append(ch);

        //new text length
        tempLineSize = getByteLength(tempLineText.toString());

        if (tempLineSize <= lineCharSize) {
          //add current char and in then max length
          if (ch != "\n".charAt(0)) {
            //not "\n" do nothing

          }
          else {
            //when meet '\n', '\n' is before the end of the line

            if (tempLineText.length() > 0) {
              newLinedText.append(indent).append(tempLineText); //new line
            }

            tempLineText.delete(0, tempLineText.length()); //reset tempLineText

          }

        }
        else {
          if (ch != "\n".charAt(0)) {
            //not "\n"
            //use currentLineText to generate new line
            newLinedText.append(indent).append(currentLineText).append("\n"); //new line
            tempLineText.delete(0, tempLineText.length()); //reset tempLineText
            //add current char into next line
            tempLineText.append(ch);
          }
          else {
            //when '\n' is right the end of the line
            newLinedText.append(indent).append(tempLineText); //new line, include "\n"
            tempLineText.delete(0, tempLineText.length()); //reset tempLineText
          }

        }

      }

       //delete the add"\n"
       if(needAddReturnChar)
         newLinedText.delete(newLinedText.length()-1,newLinedText.length());

      return newLinedText.toString();
    }

  /**
   * �����ַ��е������ַ�for oracle
   * @param pOrgString
   * @return
   */
  public static String filterOracleSqlString(String pOrgString) {
    String sResult = pOrgString;
    if (sResult == null) {
      sResult = "";
    }
    sResult = Tools.replaceString("'", "''", sResult);
    return sResult;
  }

  /**
   * ��ZIP��ʽ�ļ���ѹ��ָ���ļ�
   * @param zip
   * @param unzip
   * @throws Exception
   */
  public static void decompresser(File zip, File unzip) throws Exception {
    OutputStream out = null;
    FileInputStream in = new FileInputStream(zip);
    ZipInputStream zis = new ZipInputStream(new BufferedInputStream(in));
    ZipEntry entry;
    byte[] data = new byte[2048];
    int count;
    int BUFFER = 2048;
    FileOutputStream fos = new FileOutputStream(unzip);
    if ( (entry = zis.getNextEntry()) != null) {
      BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
      while ( (count = zis.read(data, 0, BUFFER)) != -1) {
        dest.write(data, 0, count);
      }
      dest.flush();
      dest.close();
    }
    fos.flush();
    fos.close();
    zis.close();
    in.close();
  }

  /**
   * ��ѹ�� ����ZIP��ʽѹ��������������������ؽ�ѹ������������
   * @param ins ���������
   * @return ��ѹ��������������
   * @throws Exception ���ѹ���ļ���û���κ��ļ������ش���
   */
  public static InputStream unzip(InputStream ins) throws Exception {
    ZipInputStream zis = null;

      zis = new ZipInputStream(new BufferedInputStream(ins, MAX_BUFFER_SIZE));
      ZipEntry entry;
      if ( (entry = zis.getNextEntry()) == null) {
        throw new Exception("Not found any file entry in zipped input stream.");
      }

    return zis;
  }
  
    // ���֤У��
	public static boolean validateIDCard(String value) {
		int[] W = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4,
				2, 1 };
		boolean rc = false;
		if (value != null) {
			switch (value.length()) {
			case 15:
				rc = true;
				break;
			case 18:
				int nCount = 0;
				int nIdNum = 0;
				for (int i = 0; i < 18; i++) {
					char c = value.charAt(i);
					if ((c == 'X') || (c == 'x')) {
						nIdNum = 10;
					} else if ((c <= '9') || (c >= '0')) {
						nIdNum = c - '0';
					} else {
						return rc;
					}
					nCount += nIdNum * W[i];
				}
				if ((nCount % 11) == 1) {
					rc = true;
				}
			}
		}
		return rc;
	}
	
	// ��15λ���֤ת��Ϊ18λ
	public static String convertIDCardTo18(String IDCard15) throws Exception {
		if (IDCard15.length() != 15) {
			return IDCard15;
		}
		String perfixPart = IDCard15.substring(0, 6);
		String bithdayPart = IDCard15.substring(6, 12);
		String endPart = IDCard15.substring(12);

		System.out.println("perfixPart=" + perfixPart);
		System.out.println("bithdayPart=" + bithdayPart);
		System.out.println("endPart=" + endPart);

		String IDCard17 = perfixPart + "19" + bithdayPart + endPart;
		return IDCard17 + getId18CheckCode(IDCard17);
	}
	
	//18λ���֤��������
	public static String getId18CheckCode(String sPre17ID) throws Exception {
		int[] nCertiCheckW = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
				4, 2, 1 };
		int nCount = 0;
		int nIdNum = 0;
		if ((sPre17ID == null) || (sPre17ID.length() != 17)) {
			throw new Exception("Invalid certi code length");
		}
		for (int i = 0; i < 17; i++) {
			char c = sPre17ID.charAt(i);
			if ((c <= '9') || (c >= '0')) {
				nIdNum = c - '0';
			} else {
				throw new Exception("Invalid Certi Code char");
			}
			nCount += nIdNum * nCertiCheckW[i];
		}
		nCount = nCount % 11;
		switch (nCount) {
		case 0:
			return "1";
		case 1:
			return "0";
		case 2:
			return "X";
		case 3:
			return "9";
		case 4:
			return "8";
		case 5:
			return "7";
		case 6:
			return "6";
		case 7:
			return "5";
		case 8:
			return "4";
		case 9:
			return "3";
		case 10:
			return "2";
		default:
			return "";
		}
	}
	
	public static void main(String[] args){
		//System.out.println(validateIDCard("321201198211240637"));
		System.out.println(validateIDCard("123456789012ABC"));
	}
  
}
