package com.persistent.oed.ui.dal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.persistent.oed.ui.localization.Localization;

public class DALHelper {
	private static final Logger logger = LoggerFactory.getLogger(DALHelper.class.getName());	

	private static Boolean isMultitenancyEnabled = null;
	
	public static Boolean isTenantEnabled(String tenantName) throws SQLException{
		String getTenantStatusQuery = Localization.getLocalizedQueries("DALHELPER_SELECT_TENANT_ENTRY");
		ArrayList listOfParams = new ArrayList<HashMap<String,Object>>();
		DataAccessLayer.entryForPstmt(listOfParams, 1, tenantName, "string");
		Map<Integer,Object> result = DataAccessLayer.selectQuery_pstmt(getTenantStatusQuery, listOfParams, false, "default");
		java.util.List<java.util.Map<String, Object>> queryResult = null;
		if (result.get(1) != null) {
			queryResult = (java.util.List<java.util.Map<String, Object>>) result.get(1);
		}
		if(queryResult.isEmpty() || Boolean.parseBoolean(queryResult.get(0).get("enabled").toString()) == false){
			return false;
		}
		
		return true;
	}
	
	public static Boolean isMultitenancyEnabled(){
		if(isMultitenancyEnabled == null){
			try{
				String getTenantStatusQuery = Localization.getLocalizedQueries("DALHELPER_SELECT_MULTITENANCY_PREFERENCE");
				Map<Integer,Object> result = DataAccessLayer.selectQuery(getTenantStatusQuery,false,"default");
				java.util.List<java.util.Map<String, Object>> queryResult = null;
				if (result.get(1) != null) {
					queryResult = (java.util.List<java.util.Map<String, Object>>) result.get(1);
				}
				if(!queryResult.isEmpty() && queryResult.get(0).get("preferences") != null &&
						((String)queryResult.get(0).get("preferences")).trim().equalsIgnoreCase("ENABLED")){
					isMultitenancyEnabled = true;
				}else{
					isMultitenancyEnabled = false;
				}
			} catch(Exception e){
				isMultitenancyEnabled = false;
			}
			
		}
		
		return isMultitenancyEnabled;
	}
}
