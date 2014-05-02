package org.apache.android.xmppClient;

import java.io.File;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class XmppConnectionTask extends AsyncTask<String, String, XMPPConnection> {

	@Override
	protected XMPPConnection doInBackground(String... params) {
		
		return establishConnection(params[0], params[1], params[2],params[3],params[4]);
		
	}

	@Override
	protected void onPostExecute(XMPPConnection result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(String... values) {
		
		XMPPClient client = new XMPPClient();		
		client.raiseToast(values[0]);
		
		super.onProgressUpdate(values);
	}
	
	private XMPPConnection establishConnection(String host, String port, String service , String username, String password) {
	
		int portInt = 5222; 
		try {
			portInt = Integer.parseInt(port);
		} catch (Exception e) {
			}
		
		    // Create a connection
		    ConnectionConfiguration connConfig =
		            new ConnectionConfiguration(host, portInt, service);
		    connConfig.setSASLAuthenticationEnabled(true);
		    connConfig.setCompressionEnabled(true);
		    connConfig.setSecurityMode(SecurityMode.enabled);
		   
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
		        connConfig.setTruststoreType("AndroidCAStore");
		        connConfig.setTruststorePassword(null);
		        connConfig.setTruststorePath(null);
		        Log.i("XMPPClient", "[XmppConnectionTask] Build Icecream");

		    } else {
		        connConfig.setTruststoreType("BKS");
		        String path = System.getProperty("javax.net.ssl.trustStore");
		        if (path == null)
		            path = System.getProperty("java.home") + File.separator + "etc"
		                + File.separator + "security" + File.separator
		                + "cacerts.bks";
		        connConfig.setTruststorePath(path);
		        Log.i("XMPPClient", "[XmppConnectionTask] Build less than Icecream ");

		    }
		    connConfig.setDebuggerEnabled(true);
		    XMPPConnection.DEBUG_ENABLED = true;
		    XMPPConnection connection = new XMPPConnection(connConfig);

		    try {
		        connection.connect();
		        Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
		        publishProgress("Connected to host " + host); 
		    } catch (XMPPException ex) {
		        Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
		        Log.e("XMPPClient", ex.toString());
		        publishProgress("Failed to connect to " + host); 
//		        xmppClient.setConnection(null);
		    }
		    
		    try {
		        connection.login(username, password);
		        Log.i("XMPPClient", "Logged in as " + connection.getUser());
		        publishProgress("Logged in as " + username);

		        // Set the status to available
		        Presence presence = new Presence(Presence.Type.available);
		        connection.sendPacket(presence);
		        // set connection
		        return connection;
		    }
		    // if not connected to the server, or already logged in to the server
		    catch(IllegalStateException ex) {
		    	Log.e("XMPPClient", "[SettingsDialog] Not connected to server");
		    	publishProgress(" Couldn't connect to " + service);
		        Log.e("XMPPClient", ex.toString());
		        return null;
		    }
		    // failed to login as user
		    catch (XMPPException ex) {
		        Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + username);
		        publishProgress("Failed to log in as " + username);
		        Log.e("XMPPClient", ex.toString());
		        return null;
	    }
	}

	

}
