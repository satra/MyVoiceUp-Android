package edu.mit.voicesurvey.androidapplication.sinks.datahub.generatedjava;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;

public class DataHubClient {
	DataHub.Client client;
	Connection con;
	final static String SQL_INSERT_FORMAT = "INSERT INTO test.dev_accel_data (data) VALUES ('%s')";
	final static String SQL_CREATE_SCHEMA = "CREATE TABLE test.dev_accel_data (id serial primary key,data json not null);";
	final static String SQL_MOTION_VIEW = "CREATE or replace view motion_view as select to_timestamp((cast (json_array_elements->>'Timestamp' as double precision))/1000) as datetime, " +
										"cast(json_array_elements->>'Timestamp' as bigint) as epoch_datetime, " +
										"json_array_elements->'X' as X, " +
										"json_array_elements->'Y' as Y, " +
										"json_array_elements->'Z' as Z " +
										"from (select * from (select json_array_elements (data) from test.dev_accel_data) as alldata) as temptable;";
	final static String SQL_MOTION_ACTIVITY_VIEW = "CREATE or replace view motion_activity_view as select to_timestamp((cast (json_array_elements->>'Timestamp' as double precision))/1000) as datetime, " +
												"cast(json_array_elements->>'Timestamp' as bigint) as epoch_datetime, " +
												"json_array_elements->'X' as X, " +
												"json_array_elements->'Y' as Y, " +
												"json_array_elements->'Z' as Z, " +
												"json_array_elements->'Activity' as activity " +
												"from (select * from (select json_array_elements (data) from test.dev_accel_data) as alldata) as temptable;";
	final static String SQL_MOTION_ALL = "CREATE or replace view motion_all as select to_timestamp((cast (json_array_elements->>'Timestamp' as double precision))/1000) as datetime, " +
										"cast(json_array_elements->>'Timestamp' as bigint) as epoch_datetime, " +
										"json_array_elements->'DeviceID' as device_id, " +
										"json_array_elements->'X' as X, "+
										"json_array_elements->'Y' as Y, " +
										"json_array_elements->'Z' as Z, " +
										"json_array_elements->'Activity' as activity, "+
										"json_array_elements->'DHand' as dominant_hand, " + 
										"json_array_elements->'Intensity' as intensity " +
										"from (select * from (select json_array_elements (data) from test.dev_accel_data) as alldata) as temptable;";
	final static String SQL_INSERT_USERNAME = "INSERT INTO username (uname,email) VALUES ('%s', '%s');";
	
	public static List<String> retrieveCredential(String fileName) {
		List<String> secret = new ArrayList<String>();
		Scanner scanner = new Scanner(DataHubClient.class.getResourceAsStream("secret.txt"));
		
		while (scanner.hasNext()) {
			secret.add(scanner.nextLine());
		}
		scanner.close();
		return secret;
	}
	
	public DataHubClient(String repo_base) throws DBException, TException {
		TTransport transport = new THttpClient("http://datahub.csail.mit.edu/service");
		TProtocol protocol = new  TBinaryProtocol(transport);
		client = new DataHub.Client(protocol);
		
		// open connection
		ConnectionParams con_params = new ConnectionParams();
		con_params.setRepo_base(repo_base);
		
		List<String> secret = retrieveCredential("secret.txt");
		con_params.setApp_id(secret.get(0));
		con_params.setApp_token(secret.get(1));
		con = client.open_connection(con_params);
		
		try {
			client.execute_sql(con, SQL_CREATE_SCHEMA, null);
		} catch (DBException e) { // some account already have the table created, so just ignore error for those cases;
			e.printStackTrace();
		}
		
		try {
			client.execute_sql(con, SQL_MOTION_VIEW, null);
			client.execute_sql(con, SQL_MOTION_ACTIVITY_VIEW, null);
			client.execute_sql(con, SQL_MOTION_ALL, null);
		} catch (DBException e) {
			e.printStackTrace();
		}
	}
	
	public void pushData(String deviceID, String timeStamp, String activity, int x, int y, int z) throws DBException, TException {
		String sqlStatement = String.format(SQL_INSERT_FORMAT, deviceID, timeStamp, activity, x, y,z);
		
		// 	execute a query
		ResultSet res = client.execute_sql(con, sqlStatement, null);
	}
	
	/**
	 * In order to aggregate the data later, we need to save the username into our own database.
	 * This function can be call whenever a new user is registerred or LOGIN
	 * @param username
	 */
	public static void submitUsernameToDB(String username, String email) {
		try {
			TTransport transport = new THttpClient("http://datahub.csail.mit.edu/service");
			TProtocol protocol = new  TBinaryProtocol(transport);
			DataHub.Client client = new DataHub.Client(protocol);
		
			// 	open connection
			ConnectionParams con_params = new ConnectionParams();
			con_params.setRepo_base("trannguyen");
			
			List<String> secret = retrieveCredential("/ActivityDetection/src/com/superurop/activitydetection/datahub/java/secret.txt");
			con_params.setApp_id(secret.get(0));
			con_params.setApp_token(secret.get(1));
			Connection con = client.open_connection(con_params);
		
			String sqlStatement = String.format(SQL_INSERT_USERNAME, username, email);
			
			client.execute_sql(con, sqlStatement, null);
		} catch (DBException e) { // some account already have the table created, so just ignore error for those cases;
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		} 
	}
	
	public static boolean isValidLogin(String username, String password) {
		try {
			TTransport transport = new THttpClient("http://datahub.csail.mit.edu/service");
			TProtocol protocol = new  TBinaryProtocol(transport);
			DataHub.Client temp_client = new DataHub.Client(protocol);
		
			ConnectionParams con_params = new ConnectionParams();
			con_params.setUser(username);
			con_params.setPassword(password);
		
			Connection con = temp_client.open_connection(con_params);

		} catch (DBException e) {
			e.printStackTrace();
			return false;
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			
		}
		return true;
	}
	
	public void pushData(String blob) throws DBException, TException {
		String sqlStatement = String.format(SQL_INSERT_FORMAT, blob);
		
		// 	execute a query
		ResultSet res = client.execute_sql(con, sqlStatement, null);
	}
	
	public Connection getConnection(){
		return con;
	}
	
	public DataHub.Client getClient() {
		return client;
	}
	
	public static void main(String[] args) throws DBException, TException {
		//DataHubClient client = new DataHubClient();
		//client.pushData("tranDevice", String.valueOf(System.currentTimeMillis()), "Running", 10, 20, 100);
	}
}
