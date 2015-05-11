package edu.mit.voicesurvey.androidapplication.sinks.datahub;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.nio.ByteBuffer;
import java.util.Map;

import edu.mit.voicesurvey.androidapplication.sinks.datahub.generatedjava.AccountCreationClient;
import edu.mit.voicesurvey.androidapplication.sinks.datahub.generatedjava.Connection;
import edu.mit.voicesurvey.androidapplication.sinks.datahub.generatedjava.ConnectionParams;
import edu.mit.voicesurvey.androidapplication.sinks.datahub.generatedjava.DBException;
import edu.mit.voicesurvey.androidapplication.sinks.datahub.generatedjava.DataHub;
import edu.mit.voicesurvey.androidapplication.sinks.datahub.generatedjava.ResultSet;
import edu.mit.voicesurvey.androidapplication.sinks.datahub.generatedjava.Tuple;

public class DataHubClient {
    private static final String TAG = "DatahubClient";
    private static DataHub.Client client;
    private static Connection con;

    public DataHubClient() {
        new TestAsync().execute("");
    }

    public void initWithURL() {
        Log.d(TAG, "initWithURL");
        try {
            TTransport transport = new THttpClient(datahubConstants.URL);
            TProtocol protocol = new TBinaryProtocol(transport);
            client = new DataHub.Client(protocol);
            // open connection
            ConnectionParams con_params = new ConnectionParams();
            con_params.setUser(datahubConstants.USERNAME);
            con_params.setPassword(datahubConstants.PASSWORD);
            con = client.open_connection(con_params);
        } catch (TTransportException e) {
            e.printStackTrace();
            Log.e(TAG, "TTransportException " + e.toString());
        } catch (DBException e) {
            e.printStackTrace();
            Log.e(TAG, "DBExeception " + e.toString());
        } catch (TException e) {
            e.printStackTrace();
            Log.e(TAG, "TException " + e.toString());
        }
    }

    public void testConnection() {
        Log.d(TAG, "testConnection");
        try {
            // execute a query
            ResultSet res = client.execute_sql(con, "select * from testuser.test.demo2", null);
            // print field names
            for (String field_name : res.getField_names()) {
                Log.d(TAG, field_name + "\t");
            }

            // print tuples
            for (Tuple t : res.getTuples()) {
                for (ByteBuffer cell : t.getCells()) {
                    Log.d(TAG, new String(cell.array()) + "\t");
                }
            }
        } catch (TException e) {
            Log.e(TAG, "Second TException " + e.toString());
            e.printStackTrace();
        }
    }

    public void createAccountExample() {
        try {
            TTransport datahub_transport = new THttpClient("http://ec2-50-112-188-7.us-west-2.compute.amazonaws.com/");
            TProtocol datahub_protocol = new TBinaryProtocol(datahub_transport);
            DataHub.Client datahub_client = new DataHub.Client(datahub_protocol);

            AccountCreationClient acc_client = null;
            acc_client = new AccountCreationClient();
            Map<String, String> acc_creation_result = acc_client.createAccount("trannguyen_abc", "test", "whathurtsthemost1989@gmail.com");

            ConnectionParams params = new ConnectionParams();
            params.setApp_id("activity_collection_pebble");
            params.setApp_token("b895b860-a4db-4e15-ba1b-263d5db0bb27");
            params.setRepo_base("trannguyen_test");
            Connection conn = datahub_client.open_connection(params);
            ResultSet result = datahub_client.list_repos(conn);

            // print field names
            for (String field_name : result.getField_names()) {
                System.out.print(field_name + "\t");
            }

            // print tuples
            for (Tuple t : result.getTuples()) {
                for (ByteBuffer cell : t.getCells()) {
                    System.out.print(new String(cell.array()) + "\t");
                }
                System.out.println();
            }
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (DBException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public void registerUser(String email, String password) {

    }

    public void loginUser(String username, String password) {

    }

    public void uploadFile(String localFile, String user, String repo) {

    }

    public void uploadResponse(String responses, String user, String toTable, String inRepo) {

    }

    public void createRepo(String repo, String shareWithUser) {

    }

    public void createTable(String table, String inRepo, String withSchema) {

    }

    class TestAsync extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            try {
                initWithURL();
                testConnection();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return "executed";
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "done");
        }
    }
}