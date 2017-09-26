package com.csc.csa.loader.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

import com.csc.csa.loader.domain.Address;
import com.csc.csa.loader.domain.ClientAddress;
import com.csc.csa.loader.domain.Party;
import com.csc.csa.loader.domain.Portfolio;
import com.csc.csa.loader.util.Constants;
import com.csc.csa.loader.util.UtilHelper;

import com.csc.fs.sa.security.DataEncryptor;
import com.csc.fs.util.GUIDFactory;
/**
 * 
 *	This class is doing mapping between *PolicyFullExtract.dat (extract file) and dataobject Customize csA Loader 
 *	for panorama
 */

@Component(value="metLifePolicyDataMapper")
public class PanoramaPolicyDataMapper implements FieldSetMapper<Party> {
	private static final Log logger = LogFactory.getLog(PanoramaPolicyDataMapper.class);
	private static final Date updatedWhen=new Date();	
	private String extractType=System.getProperty(Constants.EXTRACT_TYPE);	
	public Party mapFieldSet(FieldSet fs) {
		
		if (fs == null) {
			return null;
		}
		Party partyObj = new Party();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");	
		partyObj.setOperationFlag(fs.readString(Constants.OPERATION_FLAG));
		if(Constants.HEADER.equalsIgnoreCase(partyObj.getOperationFlag())){				
			partyObj.getLogData().setCycleDate(fs.readString("cycleDate"));
			return partyObj;
		}else if(Constants.TRAILER.equalsIgnoreCase(partyObj.getOperationFlag())){
			partyObj.getLogData().setTotalRecords(fs.readString("totalRecs"));
			return partyObj;
		}
		partyObj.setClientID(GUIDFactory.getIdHexString());
		partyObj.setOperationFlag(fs.readString(Constants.OPERATION_FLAG));
		partyObj.setClientStatus(1);	
		if(!UtilHelper.isNullOREmpty(fs.readString(Constants.govID))){
		// <PANFRM-472> Encrypt GovId and save into DB to Organization search criteria
			String EncryptGovId=DataEncryptor.encrypt(fs.readString(Constants.govID));
			partyObj.setGovID(EncryptGovId);
		}
		partyObj.setContractId(fs.readString(Constants.CONTRACT_ID));
		partyObj.setGovIdStatus("VALID");
		partyObj.setGovIdType(1);
		partyObj.setSearchFirstName(fs.readString(Constants.searchFirstName));
		partyObj.setPartyType(fs.readString(Constants.PARTY_TYPE));
		partyObj.setAgentNumberLong(fs.readString(Constants.AGENT_NUMBER));	
		
		if("I".equals(partyObj.getPartyType())){
			partyObj.setPartyKey(fs.readString(Constants.PARTYKEY));
			partyObj.setSearchLastName(fs.readString(Constants.searchLastName));
			partyObj.setClientTypeID(1);
		}else if("C".equals(partyObj.getPartyType())){
			partyObj.setPartyKey(fs.readString(Constants.PARTYKEY));
			partyObj.setSearchCompanyName(fs.readString(Constants.searchLastName));
			partyObj.setClientTypeID(2);
		}
		/* commented out this below code for searching customer as value set only 1 and 2.
		 *	
		 if(UtilHelper.isNullOREmpty(partyObj.getAgentNumberLong())){
				if("I".equals(partyObj.getPartyType())){
					partyObj.setPartyKey(fs.readString(Constants.PARTYKEY));
					partyObj.setSearchLastName(fs.readString(Constants.searchLastName));
					partyObj.setClientTypeID(1);
				}else if("C".equals(partyObj.getPartyType())){
					partyObj.setPartyKey(fs.readString(Constants.PARTYKEY));
					partyObj.setSearchCompanyName(fs.readString(Constants.searchLastName));
					partyObj.setClientTypeID(2);
				}
			}else{
				if("I".equals(partyObj.getPartyType())){
					partyObj.setPartyKey(fs.readString(Constants.PARTYKEY));
					partyObj.setSearchLastName(fs.readString(Constants.searchLastName));
					partyObj.setClientTypeID(3);
				}else if("C".equals(partyObj.getPartyType())){
					partyObj.setPartyKey(fs.readString(Constants.PARTYKEY));
					partyObj.setSearchCompanyName(fs.readString(Constants.searchLastName));
					partyObj.setClientTypeID(4);
				}else{
					partyObj.setClientTypeID(3);
				}
			}*/
			if(UtilHelper.isNullOREmpty(fs.readString(Constants.searchLastName))){
				partyObj.setSearchCompanyName(null);
				partyObj.setSearchLastName(null);
			}
		String addType = "1";
		String company=fs.readString(Constants.company);
		if (UtilHelper.isInteger(fs.readString(Constants.ADDR1_TYPE)))
			partyObj.setSearchAddressType(fs.readString(Constants.ADDR1_TYPE));
		else
			partyObj.setSearchAddressType(addType);
		try{			
			StringBuffer sb= new StringBuffer(fs.readString(Constants.searchDob));
			if(sb!=null && sb.length()==8)
				// PANFRM-PANTST-1517 correcting the date format(YYYY-MM-DD)
				partyObj.setSearchDob(df.parse(sb.substring(0,4).concat("-").concat(sb.substring(4,6).concat("-").concat(sb.substring(6,8)))));
		}catch(Exception e){
			logger.error("Error Occured while parsing date String "+fs.readString(Constants.searchDob), e);
		}
		if(!"".equals(fs.readString(Constants.genderId).trim()))
			partyObj.setGenderId(UtilHelper.getGender(fs.readChar(Constants.genderId)));
		partyObj.setPreffredCommType(2);
		partyObj.setUpdatedBy(Constants.UPDATE_BY);
		partyObj.setUpdatedWhen(updatedWhen);
		Portfolio portObj = new Portfolio();	
		
		portObj.setUpdatedBy(Constants.UPDATE_BY);
		portObj.setUpdatedWhen(updatedWhen);
		portObj.setClientContractID(GUIDFactory.getIdHexString());
		portObj.setHostID(UtilHelper.getSystemCode(fs.readString(Constants.SYSTEM)));
		
		if(!UtilHelper.isNullOREmpty(fs.readString(Constants.relationRole))){
			if(UtilHelper.isRelationRole(fs.readInt(Constants.relationRole))){
				portObj.setParticipationID(UtilHelper.getRoleCode(fs.readInt(Constants.relationRole)));
			}
			else{
				portObj.setParticipationID(fs.readInt(Constants.relationRole));
			}
		}
		portObj.setContractID(fs.readString(Constants.policyNumber));
		if(fs.readString(Constants.productKey).equalsIgnoreCase("T1")){
			portObj.setProductType(UtilHelper.getPolicyProductCode("T1"));
		}else if(fs.readString(Constants.productKey).equalsIgnoreCase("A1")){
			portObj.setProductType(UtilHelper.getPolicyProductCode("A1"));
		}
		else if(fs.readString(Constants.productKey).equalsIgnoreCase("A2")){
			portObj.setProductType(UtilHelper.getPolicyProductCode("A2"));
		}
		else if(fs.readString(Constants.productKey).equalsIgnoreCase("U1")){
			portObj.setProductType(UtilHelper.getPolicyProductCode("U1"));
		}
		else if(fs.readString(Constants.productKey).equalsIgnoreCase("U2")){
			portObj.setProductType(UtilHelper.getPolicyProductCode("U2"));
		}
		else if(fs.readString(Constants.productKey).equalsIgnoreCase("1")){
			portObj.setProductType(UtilHelper.getPolicyProductCode("1"));
		}
		else
			portObj.setProductType(UtilHelper.getPolicyProductCode(fs.readString(Constants.productType)));
		portObj.setPolicyStatus(fs.readString(Constants.policyStatus));
		if(Constants.AGENT_EXTRACT.equals(extractType)){
			partyObj.setAgentStatus(UtilHelper.getConvertedAgentStatus(fs.readString(Constants.policyStatus)));
		}
		portObj.setPlan(fs.readString(Constants.PLAN_NAME));
		String hostContractID=portObj.getContractID().concat(":").concat(portObj.getHostID())
				.concat(":").concat("NL:").concat(company);
		portObj.setHostContractID(hostContractID);
		portObj.setGroupBilling(fs.readString(Constants.groupBilling)); //Charger - Group Billing: Start
		int index=0; 
		if(!"".equals(fs.readString(Constants.noOfAddress))){
				index=fs.readInt(Constants.noOfAddress);
		}
		List<ClientAddress>clientAddrList=new ArrayList<>();
		List<Address>addrList=new ArrayList<>();
		for(int i=0;i<index;i++){
			String addrePrefix=Constants.ADDR_PREFIX+(i+1);
			Address addressObj = new Address();
			if (UtilHelper.isInteger(fs.readString(Constants.ADDR1_TYPE).toString()))
				addressObj.setSearchAddressType(fs.readString(Constants.ADDR1_TYPE).toString());
			else
				addressObj.setSearchAddressType(addType);
	
			addressObj.setUpdatedBy(Constants.UPDATE_BY);
			addressObj.setUpdatedWhen(updatedWhen);
			addressObj.setHostID(UtilHelper.getSystemCode(fs.readString(Constants.SYSTEM)));
			addressObj.setAddID(GUIDFactory.getIdHexString());
			addressObj.setLine1(fs.readString(addrePrefix.concat(Constants.LN1)));			
			addressObj.setLine2(fs.readString(addrePrefix.concat(Constants.LN2)));
			addressObj.setLine3(fs.readString(addrePrefix.concat(Constants.LN3)));
			addressObj.setLine4(fs.readString(addrePrefix.concat(Constants.LN4)));
			addressObj.setCity(fs.readString(addrePrefix.concat(Constants.CITY)));
			addressObj.setState(UtilHelper.getStateCode(fs.readString(addrePrefix.concat(Constants.STATE)))); //corrected state translation
			addressObj.setCountry(UtilHelper.getNation(fs.readString(addrePrefix.concat(Constants.COUNTRY)))); //corrected country translation
			addressObj.setZip(fs.readString(addrePrefix.concat(Constants.ZIP)));
			
			ClientAddress clientAddrObj = new ClientAddress();							
			clientAddrObj.setUpdatedBy(Constants.UPDATE_BY);
			clientAddrObj.setUpdatedWhen(updatedWhen);		
			clientAddrObj.setAddID(addressObj.getAddID());
			clientAddrList.add(clientAddrObj);
			addrList.add(addressObj);
		}
		partyObj.setPortfolio(portObj);
		partyObj.setClientAddrList(clientAddrList);
		partyObj.setAddrList(addrList);
		return partyObj;
	}		
}