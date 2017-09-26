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
 * This class is doing mapping between *PolicyFullExtract.dat (extract file) and
 * dataobject Customize csA Loader for panorama
 */

@Component(value = "metLifePolicyDataMapper")
public class PanoramaPolicyDataMapper implements FieldSetMapper<Party> {
	private static final Log logger = LogFactory.getLog(PanoramaPolicyDataMapper.class);
	private static final Date UPDATEDWHEN = new Date();
	private String extractType = System.getProperty(Constants.EXTRACT_TYPE);

	public Party mapFieldSet(FieldSet fs) {

		if (fs == null) {
			return null;
		}
		Party partyObj = new Party();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		partyObj.setOperationFlag(fs.readString(Constants.OPERATION_FLAG));
		if (Constants.HEADER.equalsIgnoreCase(partyObj.getOperationFlag())) {
			partyObj.getLogData().setCycleDate(fs.readString("cycleDate"));
			return partyObj;
		} else if (Constants.TRAILER.equalsIgnoreCase(partyObj.getOperationFlag())) {
			partyObj.getLogData().setTotalRecords(fs.readString("totalRecs"));
			return partyObj;
		}
		partyObj.setClientID(GUIDFactory.getIdHexString());
		partyObj.setOperationFlag(fs.readString(Constants.OPERATION_FLAG));
		partyObj.setClientStatus(1);
		if (!UtilHelper.isNullOREmpty(fs.readString(Constants.GOVT_ID))) {
			// <PANFRM-472> Encrypt GovId and save into DB to Organization
			// search criteria
			String encryptGovtId = DataEncryptor.encrypt(fs.readString(Constants.GOVT_ID));
			partyObj.setGovID(encryptGovtId);
		}
		partyObj.setContractId(fs.readString(Constants.CONTRACT_ID));
		partyObj.setGovIdStatus("VALID");
		partyObj.setGovIdType(1);
		partyObj.setSearchFirstName(fs.readString(Constants.SEARCH_FIRST_NAME));
		partyObj.setPartyType(fs.readString(Constants.PARTY_TYPE));
		partyObj.setAgentNumberLong(fs.readString(Constants.AGENT_NUMBER));

		if ("I".equals(partyObj.getPartyType())) {
			partyObj.setPartyKey(fs.readString(Constants.PARTYKEY));
			partyObj.setSearchLastName(fs.readString(Constants.SEARCH_LAST_NAME));
			partyObj.setClientTypeID(1);
		} else if ("C".equals(partyObj.getPartyType())) {
			partyObj.setPartyKey(fs.readString(Constants.PARTYKEY));
			partyObj.setSearchCompanyName(fs.readString(Constants.SEARCH_LAST_NAME));
			partyObj.setClientTypeID(2);
		}

		if (UtilHelper.isNullOREmpty(fs.readString(Constants.SEARCH_LAST_NAME))) {
			partyObj.setSearchCompanyName(null);
			partyObj.setSearchLastName(null);
		}
		String addType = "1";
		String company = fs.readString(Constants.COMPANY);
		if (UtilHelper.isInteger(fs.readString(Constants.ADDR1_TYPE)))
			partyObj.setSearchAddressType(fs.readString(Constants.ADDR1_TYPE));
		else
			partyObj.setSearchAddressType(addType);
		try {
			StringBuilder sb = new StringBuilder(fs.readString(Constants.SEARCH_DOB));
			if (sb != null && sb.length() == 8)
				// PANFRM-PANTST-1517 correcting the date format(YYYY-MM-DD)
				partyObj.setSearchDob(df.parse(sb.substring(0, 4).concat("-")
						.concat(sb.substring(4, 6).concat("-").concat(sb.substring(6, 8)))));
		} catch (Exception e) {
			logger.error("Error Occured while parsing date String " + fs.readString(Constants.SEARCH_DOB), e);
		}
		if (!"".equals(fs.readString(Constants.GENDER_ID).trim()))
			partyObj.setGenderId(UtilHelper.getGender(fs.readChar(Constants.GENDER_ID)));
		partyObj.setPreffredCommType(2);
		partyObj.setUpdatedBy(Constants.UPDATE_BY);
		partyObj.setUpdatedWhen(UPDATEDWHEN);
		Portfolio portObj = new Portfolio();

		portObj.setUpdatedBy(Constants.UPDATE_BY);
		portObj.setUpdatedWhen(UPDATEDWHEN);
		portObj.setClientContractID(GUIDFactory.getIdHexString());
		portObj.setHostID(UtilHelper.getSystemCode(fs.readString(Constants.SYSTEM)));

		if (!UtilHelper.isNullOREmpty(fs.readString(Constants.RELATION_ROLE))) {
			if (UtilHelper.isRelationRole(fs.readInt(Constants.RELATION_ROLE))) {
				portObj.setParticipationID(UtilHelper.getRoleCode(fs.readInt(Constants.RELATION_ROLE)));
			} else {
				portObj.setParticipationID(fs.readInt(Constants.RELATION_ROLE));
			}
		}
		portObj.setContractID(fs.readString(Constants.POLICY_NUMBER));
		if (fs.readString(Constants.PRODUCT_KEY).equalsIgnoreCase("T1")) {
			portObj.setProductType(UtilHelper.getPolicyProductCode("T1"));
		} else if (fs.readString(Constants.PRODUCT_KEY).equalsIgnoreCase("A1")) {
			portObj.setProductType(UtilHelper.getPolicyProductCode("A1"));
		} else if (fs.readString(Constants.PRODUCT_KEY).equalsIgnoreCase("A2")) {
			portObj.setProductType(UtilHelper.getPolicyProductCode("A2"));
		} else if (fs.readString(Constants.PRODUCT_KEY).equalsIgnoreCase("U1")) {
			portObj.setProductType(UtilHelper.getPolicyProductCode("U1"));
		} else if (fs.readString(Constants.PRODUCT_KEY).equalsIgnoreCase("U2")) {
			portObj.setProductType(UtilHelper.getPolicyProductCode("U2"));
		} else if (fs.readString(Constants.PRODUCT_KEY).equalsIgnoreCase("1")) {
			portObj.setProductType(UtilHelper.getPolicyProductCode("1"));
		} else
			portObj.setProductType(UtilHelper.getPolicyProductCode(fs.readString(Constants.PRODUCT_TYPE)));
		portObj.setPolicyStatus(fs.readString(Constants.POLICY_STATUS));
		if (Constants.AGENT_EXTRACT.equals(extractType)) {
			partyObj.setAgentStatus(UtilHelper.getConvertedAgentStatus(fs.readString(Constants.POLICY_STATUS)));
		}
		portObj.setPlan(fs.readString(Constants.PLAN_NAME));
		String hostContractID = portObj.getContractID().concat(":").concat(portObj.getHostID()).concat(":")
				.concat("NL:").concat(company);
		portObj.setHostContractID(hostContractID);
		portObj.setGroupBilling(fs.readString(Constants.GROUPBILLING)); 

		int index = 0;
		
		if (!Constants.BLANK.equals(fs.readString(Constants.NO_OF_ADDRESS))) {
			index = fs.readInt(Constants.NO_OF_ADDRESS);
		}
		List<ClientAddress> clientAddrList = new ArrayList<>();
		List<Address> addrList = new ArrayList<>();
		for (int i = 0; i < index; i++) {
			String addrePrefix = Constants.ADDR_PREFIX + (i + 1);
			Address addressObj = new Address();
			if (UtilHelper.isInteger(fs.readString(Constants.ADDR1_TYPE)))
				addressObj.setSearchAddressType(fs.readString(Constants.ADDR1_TYPE));
			else
				addressObj.setSearchAddressType(addType);

			addressObj.setUpdatedBy(Constants.UPDATE_BY);
			addressObj.setUpdatedWhen(UPDATEDWHEN);
			addressObj.setHostID(UtilHelper.getSystemCode(fs.readString(Constants.SYSTEM)));
			addressObj.setAddID(GUIDFactory.getIdHexString());
			addressObj.setLine1(fs.readString(addrePrefix.concat(Constants.LN1)));
			addressObj.setLine2(fs.readString(addrePrefix.concat(Constants.LN2)));
			addressObj.setLine3(fs.readString(addrePrefix.concat(Constants.LN3)));
			addressObj.setLine4(fs.readString(addrePrefix.concat(Constants.LN4)));
			addressObj.setCity(fs.readString(addrePrefix.concat(Constants.CITY)));
			addressObj.setState(UtilHelper.getStateCode(fs.readString(addrePrefix.concat(Constants.STATE)))); 
			addressObj.setCountry(UtilHelper.getNation(fs.readString(addrePrefix.concat(Constants.COUNTRY)))); 																									// country
			addressObj.setZip(fs.readString(addrePrefix.concat(Constants.ZIP)));

			ClientAddress clientAddrObj = new ClientAddress();
			clientAddrObj.setUpdatedBy(Constants.UPDATE_BY);
			clientAddrObj.setUpdatedWhen(UPDATEDWHEN);
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