package org.apache.android.xmppClient;

import java.util.Iterator;

import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class XmppValidUserTask extends AsyncTask<XMPPClient,String,String> {

	@Override
	protected String doInBackground(XMPPClient... params) {
		findUserValidity(params[0]);
		return "";
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		
		XMPPClient client = new XMPPClient();		
		client.raiseToast(values[0]);
		
		super.onProgressUpdate(values);
	}

	
	
	private void findUserValidity(XMPPClient client){
		
		XMPPConnection connection = client.getConnection();
		if(connection!=null)
            Log.i("XMPPClient", " conn not null  " + connection.getServiceName());
            SmackAndroid.init(client);
            UserSearchManager search = new UserSearchManager(connection);
            
            Log.i("XMPPClient", "6 ");
			try {
				Log.i("XMPPClient", "search." +connection.getServiceName() );
				Form searchForm = search.getSearchForm("search."+connection.getServiceName());
				Log.i("XMPPClient", "5 ");
	            Form answerForm = searchForm.createAnswerForm();  
	            Log.i("XMPPClient", "4 ");
	            answerForm.setAnswer("Username", true);  
//	            Toast.makeText(client,"1",Toast.LENGTH_SHORT).show();
	            Log.i("XMPPClient", "1 ");
	            answerForm.setAnswer("search", client.getReceipient());  
	            Log.i("XMPPClient", "2 ");
//	            Toast.makeText(client,"2",Toast.LENGTH_SHORT).show();
	            org.jivesoftware.smackx.ReportedData data = search.getSearchResults(answerForm,"search."+connection.getServiceName());
	            Log.i("XMPPClient", "3 ");
	            Toast.makeText(client,"3",Toast.LENGTH_SHORT).show();
	            if(data.getRows() != null)
	            {
	                Iterator<Row> it = data.getRows();
	                while(it.hasNext())
	                {
	                    Row row = it.next();
	                    Iterator iterator = row.getValues("jid");
	                    if(iterator.hasNext())
	                    {
	                        String value = iterator.next().toString();
	                        Log.i("Iteartor values......"," "+value);
	                    }
	                    //Log.i("Iteartor values......"," "+value);
	                }
//	                 Toast.makeText(client,"Username Exists",Toast.LENGTH_SHORT).show();
	                onProgressUpdate("Username Exists");
	            }
			} catch (XMPPException e) {
//                Toast.makeText(client,"Exception !!",Toast.LENGTH_SHORT).show();
				onProgressUpdate("Exception !");
				e.printStackTrace();
			}  
			
          }

 
	}
	
	

