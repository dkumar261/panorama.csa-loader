package com.csc.csa.loader.util;
/**
 * 
 * @author dkumar261
 *
 */
public class Constants {

	private Constants() {

	}

	public static final String DB_URL="csa.database.url";
	public static final String DB_USERNAME="csa.database.username";
	public static final String DB_PASSWORD="csa.database.password";
	public static final String DB_DRIVER="csa.database.driver";
	
	public static final String COMPANY = "Company";
	public static final String POLICY_NUMBER = "PolicyNumber";
	public static final String POLICY_STATUS = "PolicyStatus";
	public static final String PRODUCT_TYPE = "ProductType";
	public static final String RELATION_ROLE = "RelationRole";
	public static final String CLIENT_ID = "ClientID";
	public static final String CLIENT_TYPE = "ClientType";
	public static final String GENDER_ID = "GenderId";
	public static final String GOVT_ID = "GovID";
	public static final String SEARCH_DOB = "SearchDob";
	public static final String SEARCH_LAST_NAME = "SearchLastName";
	public static final String SUFFIX = "Suffix";
	public static final String SEARCH_FIRST_NAME = "SearchFirstName";
	public static final String SEARCH_MIDDLE_NAME = "SearchMiddleName";
	public static final String FULL_NAME = "FullName";
	public static final String NO_OF_ADDRESS = "NoOfAddress";

	public static final String LN1 = "Ln1";
	public static final String LN2 = "Ln2";
	public static final String LN3 = "Ln3";
	public static final String LN4 = "Ln4";
	public static final String COUNTRY = "Country";
	public static final String STATE = "State";
	public static final String ZIP = "Zip";
	public static final String CITY = "City";
	public static final String ADDR_TYPE = "Type";
	public static final String ADDR_PREFIX = "Add";
	public static final String ADDR1_TYPE = "Add1Type";
	public static final String ERROR_FILE_LOCATION = "c:/optBatchLogs/errorLog.txt";
	public static final String UPDATE_OPERATION = "U";
	public static final String UPDATE_BY = "DBLoader2";
	public static final String PARTY_TYPE = "partyType";
	public static final String AGENT_NUMBER = "agentNumber";
	public static final String AGENT_NUMBER_LONG = "agentNumberLong";
	public static final String OPERATION_FLAG = "OperationFlag";
	public static final String PLAN_NAME = "plan";
	public static final String GROUPBILLING = "GroupBilling";// Added new field
														// GroupBilling
														// Charger1501
	public static final  String PRODUCT_KEY = "ProductKey";// Added new field
													// GroupBilling Charger1501

	public static final String EXTRACT_TYPE = "extractType";
	public static final String AGENT_EXTRACT = "AGENT_EXTRACT";
	public static final String POLICY_EXTRACT = "POLICY_EXTRACT";
	public static final String HEADER = "AAHDR";
	public static final String TRAILER = "ZZTRL";

	public static final String ZLIFE = "ZLIfe";
	public static final String LIFECOMM = "Lifecomm";
	public static final String PANO = "Panorama";
	public static final String SYSTEM = "System";
	public static final String POLDET = "POLDET";
	public static final String CLTROL = "CLTROL";
	public static final String POLAGT = "POLAGT";
	public static final String CLTDET = "CLTDET";
	public static final String CLTADR = "CLTADR";
	public static final String PARTYKEY = "partyKey";
	
	/**
	 * PANFRM-2424 validate with uniA db for agentNumberLong and contractId,
	 * since both are composite key for uniqueness.
	 */
	public static final String CONTRACT_ID = "ContractId";
	public static final String FILE_NAME_POSTFIX = "_PolicyFullExtract";
	public static final String BLANK = "";

	public static final String AGENT_STATUS = "agentStatus";
	public static final String AUDIT_LOGGER = "com.csc.fsg.audit";
	public static final String INSERT_PORT_SQL = "insert into client.portfolio (CLIENT_CONTRACT_ID,CLIENT_ID,HOST_ID,PARTICIPATION_ID,CONTRACT_ID,"
			+ "HOST_CONTRACT_TYPE,CONTRACT_TYPE,STATUS,FACE_VALUE,HOST_CONTRACT_ID,PLAN_NAME,UPDATED_BY,"
			+ "UPDATED_WHEN,GROUP_BILLING) values(:clientContractID,:clientID,:hostID,:participationID,:contractID,"
			+ ":productType,:productType,:policyStatus,:faceAmt,:hostContractID,:plan,:updatedBy,:updatedWhen,:groupBilling)";
	public static final String INSERT_ADDR_SQL = "insert into client.address (ADDRESS_ID,ADDRESS_TYPE_ID,LINE1,LINE2,LINE3,LINE4,CITY,STATE,ZIP,COUNTRY,"
			+ "HOST_ADDRESS_ID,UPDATED_BY,UPDATED_WHEN) values(:addID,:searchAddressType,:line1,:line2,:line3,:line4,:city,:state,:zip,:country,:hostID,"
			+ ":updatedBy,:updatedWhen)";
	public static final String INSERT_AGENT_SQL = "insert into CLIENT.PRODUCER_PARTY (PRODUCER_ID,PARTY_ID,PRODUCER_STATUS,PRODUCER_ID_LONG,"
			+ "UPDATED_BY,UPDATED_WHEN) "
			+ "values(:agentNumber,:clientID,:agentStatus,:agentNumberLong,:updatedBy,:updatedWhen)";

	public static final String INSERT_CLIENT_ADDR_SQL = "insert into client.client_address (ADDRESS_ID,CLIENT_ID,UPDATED_BY,UPDATED_WHEN) "
			+ "values(:addID,:clientID,:updatedBy,:updatedWhen)";

	public static final String INSERT_HOST_CLIENT_SQL = "insert into client.host_client (HOST_CLIENT_ID,CLIENT_ID,HOST_ID,UPDATED_BY,UPDATED_WHEN) "
			+ "values(:partyKey,:clientID,:hostID,:updatedBy,:updatedWhen)";

	public static final String PARTY_INSERT_SQL = "insert into client.party (CLIENT_ID,CLIENT_STATUS,CLIENT_TYPE_ID,FULL_NAME,GOV_ID,GOV_ID_STATUS,"
			+ "GOV_ID_TYPE,SEARCH_FIRST_NAME,SEARCH_LAST_NAME,SEARCH_COMPANY_NAME,SEARCH_ADDRESS_TYPE,SEARCH_DOB,UPDATED_BY,GENDER_ID,PREFERED_COMM_TYPE,"
			+ "UPDATED_WHEN) values(:clientID,:clientStatus,:clientTypeID,:fullName,:govID,:govIdStatus,:govIdType,:searchFirstName,"
			+ ":searchLastName,:searchCompanyName,:searchAddressType,:searchDob,:updatedBy,:genderId,:preffredCommType,:updatedWhen)";
	public static final String PARTY_UPDATE_SQL = "update client.party set CLIENT_STATUS=:clientStatus,CLIENT_TYPE_ID=:clientTypeID,"
			+ "FULL_NAME=:fullName,GOV_ID=:govID,GOV_ID_STATUS=:govIdStatus,GOV_ID_TYPE=:govIdType,SEARCH_FIRST_NAME=:searchFirstName,"
			+ "SEARCH_LAST_NAME=:searchLastName,SEARCH_COMPANY_NAME=:searchCompanyName,SEARCH_ADDRESS_TYPE=:searchAddressType,SEARCH_DOB=:searchDob,"
			+ "UPDATED_BY=:updatedBy,GENDER_ID=:genderId,"
			+ "PREFERED_COMM_TYPE=:preffredCommType,UPDATED_WHEN=:updatedWhen where CLIENT_ID=:clientID";
	public static final String INSERT_PRODUCER_CONTRACT_SQL = "insert into client.producer_contract (PRODUCER_PARTY_ID,PRODUCER_CONTRACT_ID,PRODUCER_PARTICIPATION_ID,UPDATED_BY,UPDATED_WHEN) "
			+ "values(:clientID,:clientContractID,:participationID,:updatedBy,:updatedWhen)";
	public static final String LC_PARTY_UPDATE_SQL = "update client.party set CLIENT_STATUS=:clientStatus,CLIENT_TYPE_ID=:clientTypeID,"
			+ "FULL_NAME=:fullName,GOV_ID=:govID,GOV_ID_STATUS=:govIdStatus,GOV_ID_TYPE=:govIdType,SEARCH_FIRST_NAME=:searchFirstName,"
			+ "SEARCH_LAST_NAME=:searchLastName,SEARCH_COMPANY_NAME=:searchCompanyName,SEARCH_ADDRESS_TYPE=:searchAddressType,SEARCH_DOB=:searchDob,"
			+ "UPDATED_BY=:updatedBy,GENDER_ID=:genderId,"
			+ "PREFERED_COMM_TYPE=:preffredCommType,UPDATED_WHEN=:updatedWhen where CLIENT_ID in (";// CSCOPT-1155
	public static final String PARTY_RETRIEVE_SQL = "select client_id from client.party where GOV_ID = :govID and SEARCH_LAST_NAME = :searchLastName"
			+ " and SEARCH_DOB= :searchDob and CLIENT_TYPE_ID in (1,2)";// CSCOPT-1155
	public static final String PORT_RETRIEVE_SQL = "SELECT CLIENT_CONTRACT_ID FROM CLIENT.PORTFOLIO WHERE CONTRACT_ID=:contractID AND HOST_ID=:hostID and "
			+ "PARTICIPATION_ID in (32,35)";
	public static final String AGENT_RETRIEVE_SQL = "select PARTY_ID from CLIENT.PRODUCER_PARTY where PRODUCER_ID_LONG = :agentNumberLong";
	public static final String RETRIEVE_PRODUCER_CONTRACT_SQL = "SELECT count(*) FROM CLIENT.PRODUCER_CONTRACT WHERE PRODUCER_PARTY_ID=:clientID "
			+ "AND PRODUCER_CONTRACT_ID=:clientContractID";
	public static final String PORT_RETRIEVE_ON_CONTRACTID_SQL = "SELECT distinct CLIENT_ID FROM CLIENT.PORTFOLIO WHERE CONTRACT_ID=:contractID";
	// CSCOPT-1985 public static final String PORT_RETRIEVE_ON_CLIENTID_SQL =
	// "SELECT count(*) FROM CLIENT.PORTFOLIO where client_id in ( ";
	public static final String PORT_RETRIEVE_ON_CLIENTID_SQL = "SELECT client_id FROM CLIENT.PORTFOLIO where client_id in ( ";// CSCOPT-1985

	public static final String DELETE_PORT_SQL = "delete from client.portfolio ";
	public static final String DELETE_PARTY_SQL = "delete from client.party where CLIENT_ID in ( ";
	public static final String DELETE_PRODUCER_PARTY_SQL = "delete from client.PRODUCER_PARTY WHERE PARTY_ID in ( ";
	public static final String DELETE_PRODUCER_PARTY_CONTRACT_SQL = "delete from client.producer_contract where  producer_contract_id "
			+ "in ( select client_contract_id from client.portfolio where CONTRACT_ID=:contractID)";
	public static final String DELETE_CONTACT_PARTICIPANT_SQL = "delete from contact.contact_participant where client_id in ( ";
	public static final String DELETE_CONTACT_INFORMATION_SQL = "delete from client.contact_information where client_id in ( ";
	public static final String DELETE_HOST_CLIENT_SQL = "delete from client.host_client where client_id in ( ";
	public static final String DELETE_PERSON_DTL_SQL = "delete from client.person_detail where client_id in ( ";
	public static final String DELETE_ORGANIZATION_DTL_SQL = "delete from client.organization_detail where client_id in ( ";
	// CSCOPT-1743 begin
	public static final String DELETE_ADDR_SQL = "delete from client.address where address_id in ( ";
	public static final String RETRIEVE_ADDRID_TODELETE_IN_SQL = "select ADDRESS_ID from client.client_address"
			+ " where CLIENT_ID in ( ";
	// CSCOPT-1743 begin end
	public static final String DELETE_CLIENT_ADDR_SQL = "delete from client.client_address where CLIENT_ID in ( ";

	public static final String ZL_PARTY_COUNT_SQL = "select count(*) from client.party where CLIENT_ID=:clientID";
	public static final String ZL_PORT_RETRIEVE_SQL = "SELECT CLIENT_CONTRACT_ID,PARTICIPATION_ID FROM CLIENT.PORTFOLIO WHERE "
			+ "CONTRACT_ID=:contractID AND HOST_ID=:hostID and PARTICIPATION_ID in (32,35)";
	public static final String ZL_AGENT_COUNT_SQL = "select count(*) from CLIENT.PRODUCER_PARTY where PRODUCER_ID_LONG = :agentNumberLong";
	public static final String UPDATE_AGENT_SQL = "update CLIENT.PRODUCER_PARTY set PRODUCER_STATUS=:agentStatus where PRODUCER_ID_LONG = :agentNumberLong";
	public static final String ZL_PORT_COUNT_SQL = "SELECT count(*) FROM CLIENT.PORTFOLIO WHERE "
			+ "CONTRACT_ID=:contractID AND HOST_ID=:hostID and PARTICIPATION_ID=:participationID";
	public static final String UPDATE_PORT_SQL = "update client.portfolio set CONTRACT_TYPE=:productType,HOST_CONTRACT_TYPE=:productType ,STATUS=:policyStatus,"
			+ "PLAN_NAME=:plan  where CONTRACT_ID=:contractID AND HOST_ID=:hostID";// CSCOPT-1478
	public static final String UPDATE_ROLE_SQL = "update client.portfolio set PARTICIPATION_ID=:participationID "
			+ "where CONTRACT_ID=:contractID AND HOST_ID=:hostID and CLIENT_ID=:clientID";
	public static final String ZL_INSERT_ADDR_SQL = "insert into client.address (ADDRESS_ID,ADDRESS_TYPE_ID,LINE1,LINE2,LINE3,LINE4,CITY,STATE,ZIP,COUNTRY,"
			+ "HOST_ADDRESS_ID,UPDATED_BY,UPDATED_WHEN) values(:addID,:searchAddressTypeCode,:line1,:line2,:line3,:line4,:city,:state,:zip,:country,:hostID,"
			+ ":updatedBy,:updatedWhen)";
	public static final String ZL_ADDR_COUNT_SQL = "select count(*) from client.ADDRESS where address_ID in (select"
			+ " address_id from client.client_address where client_id=:clientID)";
	public static final String UPDATE_ADDR_SQL = "update client.address set ADDRESS_TYPE_ID=:searchAddressTypeCode,LINE1=:line1,LINE2=:line2,"
			+ "LINE3=:line3,LINE4=:line4,CITY=:city,STATE=:state,ZIP=:zip,COUNTRY=:country,UPDATED_BY=:updatedBy,"
			+ "UPDATED_WHEN=:updatedWhen where address_ID in (select address_id from client.client_address where "
			+ "client_id=:clientID)";
	public static final String ZL_PORT_COUNT_FOR_POLDET_SQL = "SELECT count(*) FROM CLIENT.PORTFOLIO WHERE "
			+ "CONTRACT_ID=:contractID AND HOST_ID=:hostID";
	public static final String RETRIEVE_CLIENT_ID_SQL = "Select client_id from client.host_client where host_client_id like :partyKey";
	public static final String RETRIEVE_CLIENT_ID_CT_SQL = "Select count(*) from client.host_client where host_client_id like :partyKey";
	public static final String UPDATE_HOST_CLIENT_SQL = "update client.host_client set CLIENT_ID=:clientID,UPDATED_BY=:updatedBy,UPDATED_WHEN=:updatedWhen)"
			+ "where client_id in (";
	public static final String UPDATE_CLIENT_TYPE_ID_SQL = "update client.party set client_type_id = client_type_id+2 where client_id in"
			+ "(select party_id from client.producer_party where party_id=:clientID) and client_type_id<3";
	/**
	 * PANFRM-2424 fetch number of row in uniA Database based on agentNumber and
	 * contractId
	 */
	public static final String FETCH_UNIA_AGENT = "SELECT count(*) FROM CYBERLIFE.AGENT WHERE AGENT_NUMBER=:agentNumberLong AND CONTRACT_ID=:contractId";
	/**
	 * PANFRM-2424
	 */
	public static final String AGENT_CHECK = "select PARTY_ID from CLIENT.PRODUCER_PARTY where PRODUCER_ID = :agentNumber";
}