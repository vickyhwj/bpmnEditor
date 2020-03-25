package com.activiti6.util;

public class JdbcUtils {
	public static String beanName2LineName(String beanName) {
		StringBuffer sb = new StringBuffer(beanName.substring(0, 1).toLowerCase());
		boolean isStart = true;

		for (int i = 1; i < beanName.length(); ++i) {
			if (beanName.charAt(i) >= 'A' && beanName.charAt(i) <= 'Z') {
				sb.append("_" + Character.toLowerCase(beanName.charAt(i)));
			} else {
				sb.append(beanName.charAt(i));
			}

		}
		return sb.toString();

	}
	
	public static String lineName2BeanName(String lineName) {
		String lowLineName=lineName.toLowerCase();
		StringBuffer sb = new StringBuffer("");
		boolean before=false;
		for(int i=0;i<lowLineName.length();++i){
			if(lowLineName.charAt(i)=='_'){
				before=true;
			}else{
				if(before){
					sb.append(Character.toUpperCase(lowLineName.charAt(i)));
					before=false;
				}else{
					sb.append(lowLineName.charAt(i));
				}
			}
		}
		return sb.toString();



	}
	public static void main(String[] args) {
		System.out.println(beanName2LineName("qaBe213"));
		System.out.println(lineName2BeanName("abc_oo_pp"));
	}

}
