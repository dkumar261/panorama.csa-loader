package com.csc.csa.loader.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UtilHelper {
	private static Map<Integer, Integer> roleCodeMap=new HashMap<>();
	private static Map<Integer, Integer> agentStatusMap=new HashMap<>();
	private static Map<String, Integer> addressMap=new HashMap<>();
	private static Map<String, Integer> roleCodeMapZLife=new HashMap<>();
	private static Properties nationProps;
	private static Properties stateProps;
	private static Properties policyStatusProps;
	private static Properties policyProductCodeProps;
	private static Properties systemsProps;
	
	private UtilHelper(){}
	static{
		populateRoleCodeMap();
		populateAgentStatusMap();
		populateAddressMap();	
	}
	public static boolean isInteger(String str) {
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException nfe) {}
	    return false;
	}
	private static void populateAddressMap(){
		addressMap.put("R", 1);
		addressMap.put("C", 17);
		addressMap.put("U", 0);
	}	
	private static void populateRoleCodeMap(){
		roleCodeMap.put(110, 1000702003);
		roleCodeMap.put(111, 1000702001);
		roleCodeMap.put(112, 36);
		roleCodeMap.put(113,1000702004);
		roleCodeMap.put(114,1000702005);
		roleCodeMap.put(115,1000702006);
		roleCodeMap.put(116,1000702002);
		roleCodeMapZLife.put("OW", 8);
		roleCodeMapZLife.put("LF", 32);
		roleCodeMapZLife.put("BN", 34);
		roleCodeMapZLife.put("JO", 184);
	}
	private static void populateAgentStatusMap(){
		agentStatusMap.put(0, 1);
		agentStatusMap.put(20, 2);
		agentStatusMap.put(21, 1000702001);
		agentStatusMap.put(22, 1000702002);
		agentStatusMap.put(40, 1000702003);
		agentStatusMap.put(50,1000702004);
		agentStatusMap.put(51,1000702005);		
	}
//	public static int getRoleCode(int role){
//		return roleCodeMap.containsKey(role)?roleCodeMap.get(role):-1;
//	}
	public static int getRoleCode(int role){
		if(!"".equals(role)){
			if(roleCodeMap.containsKey(role)){
				return roleCodeMap.get(role);
			}
			else{
				return role;
			}
		}
		return role;
	}
	public static String getPolicyStatus(String polStatus){
		return policyStatusProps.containsKey(polStatus)?policyStatusProps.get(polStatus).toString():"-1";
	}
	public static String getNation(String nationStr){
		return nationProps.containsKey(nationStr)?nationProps.get(nationStr).toString():"-1";
	}
	public static int getRoleCodeForZLife(String role){
		return roleCodeMapZLife.containsKey(role)?roleCodeMapZLife.get(role):-1;
	}
	public static int getStateCode(String state){
		return stateProps.containsKey(state.trim())?Integer.parseInt(stateProps.get(state).toString()):-1;
	}
	public static String getSystemCode(String system){
		return (String) (systemsProps.containsKey(system.trim())?systemsProps.get(system):-1);
	}
	public static int getAddressCode(String address){
		return addressMap.containsKey(address)?addressMap.get(address):-1;
	}
	public static String getPolicyProductCode(String policyProductCode){
		return policyProductCodeProps.containsKey(policyProductCode)?policyProductCodeProps.get(policyProductCode).toString():"-1";
	}
	
	public static int getConvertedAgentStatus(String agentStatusStr){
			//CSCOPT-1153 begin
			if(!"".equals(agentStatusStr)){
				if(agentStatusMap.containsKey(Integer.parseInt(agentStatusStr))){
						return agentStatusMap.get(Integer.parseInt(agentStatusStr));
				}else{
					return Integer.parseInt(agentStatusStr);
				}
			}
			return -1;
	}//CSCOPT-1153 end
	
	public static boolean isRelationRole(int relationRole){
		
			if(roleCodeMap.containsKey(relationRole)){
					return true;
							}
			else 
				return false;
			
	}
	public static int getGender(char gender){
		int ret=0;
		switch(gender){
		case 'M':
			ret=1;
			break;
		case 'F':
			ret=2;
			break;
		case 'U':
			ret=3;
			break;
		default:
			ret=0;
		}
		return ret;
	}
	
	public static boolean isNullOREmpty(String input){
		boolean flag=false;
		if(input==null || input.isEmpty() ){
			flag=true;
		}		
		return flag;
	}	
	public static void setNationProps(Properties nationProps) {
		UtilHelper.nationProps = nationProps;
	}
	public static void setStateProps(Properties stateProps) {
		UtilHelper.stateProps = stateProps;
	}
	public static void setPolicyStatusProps(Properties policyStatusProps) {
		UtilHelper.policyStatusProps = policyStatusProps;
	}
	public static Properties getPolicyProductCodeProps() {
		return policyProductCodeProps;
	}
	public static void setPolicyProductCodeProps(Properties policyProductCodeProps) {
		UtilHelper.policyProductCodeProps = policyProductCodeProps;
	}
	public static void setSystemsProps(Properties systemsProps) {
		UtilHelper.systemsProps = systemsProps;
	}
   //Charger
	public static Properties getSystemsProps() {
		return UtilHelper.systemsProps;
	}	

}
