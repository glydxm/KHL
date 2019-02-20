package com.glyfly.khl.app.util;

/**
 * 正则校验
 * 
 * @author lianjia-01
 *
 */
public final class RegularUtil {
	public static String regex1to1000Msg = "请输入1000以内的整数";
	public static String regex1to1000 = "^([1-9]\\d{0,2}|1000)$";

	public static String regex1to100Msg = "请输入0~100（含）之间的整数";
	public static String regex1to100 = "^([1-9]\\d?|100)$";

	public static String regex1to10Msg = "请输入1-10以内的整数";
	public static String regex1to10 = "^([1-9]|10)$";

	public static String regex_Numeric3to10Msg = "请输入3~10（含）之间的数（小数点后一位）";
	public static String regex3to10 = "^([3-9]|10)$";

	public static String regex1to50Msg = "请输入0~50（含）之间的整数";
	public static String regex1to50 = "^([1-9]|[1-4][0-9]|50)$";

	public static String regexGt0Msg = "请输入大于0的整数";
	public static String regexGt0 = "^\\+?[1-9]\\d{0,9}$";

	public static String regex_clientMsg = "请输入50个以内的字符，只可以为汉字、英文、数字和顿号";
	public static String regex_client = "^[\u4e00-\u9fa5、a-zA-Z0-9]{0,50}$";

	public static String regex_comm_10Msg = "请输入10个以内的字符，只可以为汉字、英文或数字";
	public static String regex_comm_10 = "^[\u4e00-\u9fa5a-zA-Z0-9]{0,10}$";

	public static String regex_nameMsg = "请输入5个以内的字符，只可以为汉字、英文";
	public static String regex_name = "^[\u4e00-\u9fa5a-zA-Z0-9]{0,5}$";

	public static String regex_telMsg = "请检查后重新输入";
	public static String regex_tel = "(^(\\d{3,4}-?)?\\d{7,8})$|(1[0-9]{10})";

	public static String regex_NumericMsg = "请输入大于0的数（小数点后一位）";
	public static String regex_Numeric = "^[1-9]{0,8}(.[0-9]{1})?$";

	public static String regex_Numeric1to100Msg = "请输入0~100的数（小数点后一位）";
	public static String regex_Numeric1to100 = "^([1-9]\\d?(\\.\\d{1})?|100)$";

	public static String regex_EngAndNumberMsg = "请输入100个以内的字符~";
	public static String regex_EngAndNumber = "^[_a-zA-Z0-9]{0,100}$";

	public static String regex_funMsg = "请输入100个以内的字符~";
	public static String regex_fun = "^[a-zA-Z0-9]+(-[a-zA-Z0-9]+)?$";


	public static String regex_string_length8to20 = "^[\u4e00-\u9fa5a-zA-Z0-9]{8,20}$";
	public static String regex_string_length1to20 = "^[\u4e00-\u9fa5a-zA-Z0-9]{1,20}$";
	public static String regex_string_length1to10 = "^[\u4e00-\u9fa5a-zA-Z0-9]{1,10}$";
	public static String regex_string_length1to100 = "^[\u4e00-\u9fa5a-zA-Z0-9]{1,100}$";

	public static String regex_number_range1to10000 = "^([1-9]\\d{0,3}|10000)$";
	public static String regex_number_range1to10 = "^([1-9]|10)$";
	public static String regex_number_range1to50 = "^([1-9]|[1-4][0-9]|50)$";
	public static String regex_number_range1to40 = "^([1-9]|[1-3][0-9]|40)$";
	public static String regex_number_integer2double4 = "^((([1-9]\\d)|\\d)(\\.\\d{1,4})?)$";//整数最多2位，小数最多4位

	public static String regex_intmax7dotmax2 = "^([1-9]([0-9]{1,6})?|0)(\\.\\d{1,2})?$";//整数最多7位，小数最多2位

}
