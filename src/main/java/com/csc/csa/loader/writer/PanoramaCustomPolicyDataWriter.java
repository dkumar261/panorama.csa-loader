package com.csc.csa.loader.writer;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.csc.csa.loader.domain.Address;
import com.csc.csa.loader.domain.ClientAddress;
import com.csc.csa.loader.domain.JobLogData;
import com.csc.csa.loader.domain.Party;
import com.csc.csa.loader.domain.Portfolio;
import com.csc.csa.loader.util.Constants;
/**
 * 
 * @author dkumar261
 *	This class is for customized csA Loader for panorama , after populating dataobject from extract and 
 *	as per given operation it executes sql query and save/update/delete into database.
 *	 
 */

@Repository(value="metlifepolicyWriter")
@Transactional
public class PanoramaCustomPolicyDataWriter implements ItemWriter<Party>{	
	private boolean partyPresent=false;	
	private List<String>processedPolicyList=new ArrayList<>();
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate jdbcTemplateUnia;
	private String extractType=System.getProperty(Constants.EXTRACT_TYPE);	
	private String policyNumber;
	private String policyNumberforClientDeletes; 
	private List<String>clientListToDelete=new ArrayList<>(); 
	private static final Log logger = LogFactory.getLog(Constants.AUDIT_LOGGER);
	
	public void write(List<? extends Party> items) {
		for (Party party : items) {
			if(Constants.HEADER.equalsIgnoreCase(party.getOperationFlag())
					|| Constants.TRAILER.equalsIgnoreCase(party.getOperationFlag())){
				checkForClientDeletes(); 
				writeJobMetaData(party);
				return;
			}
			processParty(party);
		}	
	}
	@Transactional
	public void processParty(Party party) {
		
		String clientID = null;
		partyPresent = false;
		clientID = checkIfPartyPresent(party);
		if (!isNullOREmpty(clientID)) {
			party.setClientID(clientID);
			partyPresent = true;
		}
		party.updateClientIDInHeirarchy();
		logger.info("Before policy number check: policyNumberforClientDeletes ==> "+policyNumberforClientDeletes);
		logger.info("Before policy number check: party contract id ==> "+party.getPortfolio().getContractID());			
		if(policyNumberforClientDeletes!=null){
			if(!policyNumberforClientDeletes.equals(party.getPortfolio().getContractID())){
				checkForClientDeletes();
				policyNumberforClientDeletes=party.getPortfolio().getContractID();
				logger.info("After deletes: policyNumberforClientDeletes ==> "+policyNumberforClientDeletes);
			}
		}else{
			policyNumberforClientDeletes=party.getPortfolio().getContractID();
		}
		switch(party.getOperationFlag()){
		case "I":
			insertPartyData(party);
			break;
		case "U":				
			if(policyNumber==null || !policyNumber.equals(party.getPortfolio().getContractID())){
				policyNumber=party.getPortfolio().getContractID();
				recheckIfPartyPresent(party);
			}
			party.updateClientIDInHeirarchy();
			insertPartyData(party);
			break;
		case "D":
			deletePartyData(party);
			break;
		default:
			logger.error("Unsupported operation flag");
		}
	}
	public void checkForClientDeletes(){
		logger.info("Start checkForClientDeletes ==> "+clientListToDelete);
		if(clientListToDelete!=null && !clientListToDelete.isEmpty()){
			String whereDeleteClause=buildInClause(clientListToDelete);
			List<String> portList=jdbcTemplate.getJdbcOperations().queryForList(Constants.PORT_RETRIEVE_ON_CLIENTID_SQL
					+whereDeleteClause,String.class);
			clientListToDelete.removeAll(portList);
			if(!CollectionUtils.isEmpty(clientListToDelete)){
				whereDeleteClause=buildInClause(clientListToDelete); 
				logger.info("Before deletes: checkForClientDeletes ==> "+clientListToDelete);
				List<String> addressIDToDelete = jdbcTemplate.getJdbcOperations().queryForList(
						Constants.RETRIEVE_ADDRID_TODELETE_IN_SQL+whereDeleteClause,String.class);
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_CLIENT_ADDR_SQL+whereDeleteClause);
				
				if(!CollectionUtils.isEmpty(addressIDToDelete)){
					String adddeleteWhereClause=buildInClause(addressIDToDelete);
					jdbcTemplate.getJdbcOperations().update(Constants.DELETE_ADDR_SQL+adddeleteWhereClause);
				}	
				jdbcTemplate.getJdbcOperations().update(Constants. DELETE_PRODUCER_PARTY_SQL+whereDeleteClause);
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_CONTACT_INFORMATION_SQL+whereDeleteClause);
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_CONTACT_PARTICIPANT_SQL+whereDeleteClause);						
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_HOST_CLIENT_SQL+whereDeleteClause);	
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_PERSON_DTL_SQL+whereDeleteClause);
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_ORGANIZATION_DTL_SQL+whereDeleteClause);
				String partyDeleteWhereClause=buildInClause(clientListToDelete);
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_PARTY_SQL+partyDeleteWhereClause);
			}
		}	
		clientListToDelete.clear();
	}	
	public void recheckIfPartyPresent(Party party){
		String clientID = checkIfPartyPresent(party);
		if (!isNullOREmpty(clientID)) {
			partyPresent=true;
			party.setClientID(clientID);//CSCOPT-1568
		}else{
			partyPresent=false;
		}
	}
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	public void setdataSourceUnia(DataSource dataSource) {
		this.jdbcTemplateUnia = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public void insertPartyData(Party party){
				
		if(!partyPresent){
			SqlParameterSource params = new BeanPropertySqlParameterSource(party);
			logger.info("Insert Party data - party not present "+party);	
			jdbcTemplate.update(Constants.PARTY_INSERT_SQL, params);
		}
		else{
			SqlParameterSource params = new BeanPropertySqlParameterSource(party);
			logger.info("Insert Party data - party present "+party);	
			jdbcTemplate.update(Constants.PARTY_UPDATE_SQL, params);
		}
		
			String clientId=getClientIdByHostClientId(party);
			if (StringUtils.isEmpty(clientId)){
				insertParty(party);
			}
			insertPortfolio(party.getPortfolio());	
			insertAgent(party);
		
		//CSCOPT-1743 begin - delete any preexisting addresses so only latest one will be on database
		//if(partyPresent && !CollectionUtils.isEmpty(party.getClientAddrList())){//CSCOPT-1842, 1985
		if(partyPresent){ //CSCOPT-1985
			deleteAddressData(party);
		}//CSCOPT-1743 end
		insertAddress(party.getAddrList());
		insertClientAddress(party);
	}
	/**
	 * <PANFRM-1050> 
	 * @param party
	 * @return
	 * Getting client Id from client.host_client table 
	 */
	protected String getClientIdByHostClientId(Party party) {
		String csAclientID = null;
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(party);
		try {
			csAclientID = jdbcTemplate.queryForObject(Constants.RETRIEVE_CLIENT_ID_SQL, namedParameters, String.class);
		} catch (Exception e) {
			//logger.error("In host client table for party is not available " + party);
		}
		return csAclientID;
	}
	//CSCOPT-1985 begin
	@Transactional(propagation = Propagation.REQUIRED)
	protected void deletePolicyData(Party party){ 
		deleteProducerContractData(party.getPortfolio());		
		List<String>clientIDList=retrieveClientIDList(party);	
		//this client id list is needed later to delete client ids no longer on the portfolio table	
		int size=clientIDList.size();		
		for (int i = 0; i < size; i++) {
			clientListToDelete.add(clientIDList.get(i));
		}
		if(clientIDList!=null && !clientIDList.isEmpty()){
			try{
				Portfolio port = party.getPortfolio();
				String whereClause2="WHERE CONTRACT_ID = '"+port.getContractID()+"'";
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_PORT_SQL+whereClause2);
			}catch(Exception ex){
				ex.printStackTrace();
				logger.error("Error occurred while deleting policy data");
			}
		}
	}
	//CSCOPT-1985 end
	@Transactional(propagation = Propagation.REQUIRED)
	protected void deletePartyData(Party party){//CSCOPT-1808
		deleteProducerContractData(party.getPortfolio());		
		List<String>clientIDList=retrieveClientIDList(party);		
		if(clientIDList!=null && !clientIDList.isEmpty()){
			String whereClause=buildInClause(clientIDList);
			SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
					party);
			deleteAllData(namedParameters,whereClause,party.getPortfolio(),clientIDList);//CSCOPT-1808			
		}
	}
	public List<String> retrieveClientIDList(Party party){
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
				party.getPortfolio());
		List<String> val = jdbcTemplate.queryForList(Constants.PORT_RETRIEVE_ON_CONTRACTID_SQL,
				namedParameters, String.class);		
		return val;
	}
	protected String checkIfPartyPresent(Party party) {
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
				party);
		List<String> val = jdbcTemplate.queryForList(Constants.PARTY_RETRIEVE_SQL,
				namedParameters, String.class);
		if(val==null || val.isEmpty()){
			return null;
		}
		return val.get(0);
	}	
	/**
	 *  This method insert party data into host_portfolio table so that in csA , user can search by policy number 
	 */
	public void insertPortfolio(Portfolio port){				
		SqlParameterSource params = new BeanPropertySqlParameterSource(
				port);			
		jdbcTemplate.update(Constants.INSERT_PORT_SQL, params);
	}
	/**
	 *  This method insert party data into host_client table so that in csA Address is visible 
	 * @param party 
	 */
	
	public void insertParty(Party party){
		party.setHostID("7");
		SqlParameterSource partyDataForSqlQuery = new BeanPropertySqlParameterSource(
				party);
		jdbcTemplate.update(Constants.INSERT_HOST_CLIENT_SQL, partyDataForSqlQuery);
		
	}
	public int validateUniaDbForAgentAndContractId(Party party){
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
				party);
		return jdbcTemplateUnia.queryForObject(Constants.FETCH_UNIA_AGENT, namedParameters,Integer.class);
	}
	public void insertAgent(Party party) {
		
			party.setAgentNumber(party.getAgentNumberLong() + party.getContractId());
			SqlParameterSource params = new BeanPropertySqlParameterSource(
					party);
			List<String>agentValue = jdbcTemplate.queryForList(Constants.AGENT_CHECK,params,String.class);
			if(agentValue==null || agentValue.isEmpty()){
				
				jdbcTemplate.update(Constants.INSERT_AGENT_SQL, params);
			}
	}
	public void insertAgentPortfolio(Party party) {

		if (!processedPolicyList.contains(party.getPortfolio().getContractID())) {
			SqlParameterSource portRetrieve = new BeanPropertySqlParameterSource(
					party.getPortfolio());
			List<String> val = jdbcTemplate.queryForList(
					Constants.PORT_RETRIEVE_SQL, portRetrieve, String.class);
			if (val != null && !val.isEmpty()) {
				party.getPortfolio().setClientID(party.getClientID());
				party.getPortfolio().setClientContractID(val.get(0));
				SqlParameterSource prodPortRetrieve = new BeanPropertySqlParameterSource(
						party.getPortfolio());
				int val1 = jdbcTemplate.queryForObject(Constants.RETRIEVE_PRODUCER_CONTRACT_SQL,prodPortRetrieve,Integer.class);
				if (val1 == 0) {
					Portfolio port = new Portfolio();
					Portfolio portOrig = party.getPortfolio();
					port.setClientContractID(val.get(0));
					port.setClientID(party.getClientID());
					port.setParticipationID(portOrig.getParticipationID());
					port.setUpdatedBy(portOrig.getUpdatedBy());
					port.setUpdatedWhen(portOrig.getUpdatedWhen());
					SqlParameterSource portParam = new BeanPropertySqlParameterSource(
							port);
					jdbcTemplate.update(
							Constants.INSERT_PRODUCER_CONTRACT_SQL, portParam);
				}
			}
		} else {
			processedPolicyList.add(party.getPortfolio().getContractID());
		}
	}
	
	public String checkIfAgentPresent(Party party) {
		SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
				party);
		List<String> val = jdbcTemplate.queryForList(Constants.AGENT_RETRIEVE_SQL,
				namedParameters, String.class);
		if(val==null || val.isEmpty()){
			return null;
		}
		return val.get(0);
	}
	
	public void insertAddress(List<Address> addrList) {
		SqlParameterSource[] params = SqlParameterSourceUtils
				.createBatch(addrList.toArray());
		jdbcTemplate.batchUpdate(Constants.INSERT_ADDR_SQL, params);
	}
	
	public void insertClientAddress(Party party) {
		SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(party
				.getClientAddrList().toArray());
		jdbcTemplate.batchUpdate(Constants.INSERT_CLIENT_ADDR_SQL, params);
	}
	@Transactional
	private void deleteAllData(SqlParameterSource params ,String whereClause,
			Portfolio port,List<String> clientIDList){//CSCOPT-1808
		try{
			String whereClause2="WHERE CONTRACT_ID = '"+port.getContractID()+"'";
			//CSCOPT-1743 begin
			jdbcTemplate.getJdbcOperations().update(Constants.DELETE_PORT_SQL+whereClause2);
			List<String> portList=jdbcTemplate.getJdbcOperations().queryForList(Constants.PORT_RETRIEVE_ON_CLIENTID_SQL
					+whereClause,String.class);
			clientIDList.removeAll(portList);
			if(!CollectionUtils.isEmpty(clientIDList)){
				whereClause=buildInClause(clientIDList); //CSCOPT-1985
				List<String> addressIDToDelete = jdbcTemplate.getJdbcOperations().queryForList(
						Constants.RETRIEVE_ADDRID_TODELETE_IN_SQL+whereClause,String.class);
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_CLIENT_ADDR_SQL+whereClause);
				//CSCOPT-1743 begin
				if(!CollectionUtils.isEmpty(addressIDToDelete)){
					String adddeleteWhereClause=buildInClause(addressIDToDelete);
					jdbcTemplate.getJdbcOperations().update(Constants.DELETE_ADDR_SQL+adddeleteWhereClause);
				}//CSCOPT-1743 end
				jdbcTemplate.getJdbcOperations().update(Constants. DELETE_PRODUCER_PARTY_SQL+whereClause);
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_CONTACT_INFORMATION_SQL+whereClause);
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_CONTACT_PARTICIPANT_SQL+whereClause);						
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_HOST_CLIENT_SQL+whereClause);	
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_PERSON_DTL_SQL+whereClause);
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_ORGANIZATION_DTL_SQL+whereClause);
			}
			//CSCOPT-1808 begin
			if(!CollectionUtils.isEmpty(clientIDList)){
				String partyWhereClause=buildInClause(clientIDList);
				jdbcTemplate.getJdbcOperations().update(Constants.DELETE_PARTY_SQL+partyWhereClause);
			}//CSCOPT-1808 end				
			
		}catch(Exception ex){
			logger.error("Error occurred while deleting data",ex);
		}
	}		
	
	public void deleteProducerContractData(Portfolio port) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(port);
		jdbcTemplate.update(Constants.DELETE_PRODUCER_PARTY_CONTRACT_SQL,
				params);
	}	
	private boolean isNullOREmpty(String input){
		boolean flag=false;
		if(input==null || input.isEmpty() ){
			flag=true;
		}		
		return flag;
	}
	private boolean isAgent(int clientTypeID){
		return clientTypeID ==3 || clientTypeID==4;
	}
	private String buildInClause(List<String> cliendIDList){
		StringBuilder sb = new StringBuilder();
		int size=cliendIDList.size();		
		for (int i = 0; i < size; i++) {
		    sb.append("'"+cliendIDList.get(i)+"'");
		    if (i < size - 1) {
		        sb.append(", ");
		    }
		}
		sb.append(") ");
		return sb.toString();
	}
	private void writeJobMetaData(Party party){		
		JobLogData log=party.getLogData();
		if(Constants.HEADER.equalsIgnoreCase(party.getOperationFlag()))				
			logger.info("The Cycle Date is ==> "+log.getCycleDate());
		else if (Constants.TRAILER.equalsIgnoreCase(party.getOperationFlag()))
			logger.info("The total number of records in incoming extract file are ==> "+log.getTotalRecords());
		
	}
	//CSCOPT-1743
	private void deleteAddressData(Party party){
		//String whereClause=buildInClauseForClientAddress(party.getClientAddrList());
		//cscopt-1985 begin
		StringBuilder sb = new StringBuilder();
		sb.append("'"+party.getClientID()+"'"+") ");
		String whereClause=sb.toString();
		//cscopt-1985 end
		List<String> addressIDToDelete = jdbcTemplate.getJdbcOperations().queryForList(
				Constants.RETRIEVE_ADDRID_TODELETE_IN_SQL+whereClause,String.class);
		jdbcTemplate.getJdbcOperations().update(Constants.DELETE_CLIENT_ADDR_SQL+whereClause);
		if(!CollectionUtils.isEmpty(addressIDToDelete)){
			String adddeleteWhereClause=buildInClause(addressIDToDelete);
			jdbcTemplate.getJdbcOperations().update(Constants.DELETE_ADDR_SQL+adddeleteWhereClause);
			
		}
	}
	//CSCOPT-1743
	private String buildInClauseForClientAddress(List<ClientAddress> cliendIDList){
		StringBuilder sb = new StringBuilder();
		int size=cliendIDList.size();		
		for (int i = 0; i < size; i++) {
		    sb.append("'"+cliendIDList.get(i).getClientID()+"'");
		    if (i < size - 1) {
		        sb.append(", ");
		    }
		}
		sb.append(") ");
		return sb.toString();
	}
}