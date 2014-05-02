package org.apache.android.xmppClient;

import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.XMPPConnection;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Gather the xmpp settings and create an XMPPConnection 
 */
public class SettingsDialog extends Dialog implements android.view.View.OnClickListener {
    private XMPPClient xmppClient;
   
    public SettingsDialog(XMPPClient xmppClient) {
        super(xmppClient);
        this.xmppClient = xmppClient;
    }

    protected void onStart() {
        super.onStart();
        setContentView(R.layout.settings);
        getWindow().setFlags(4, 4);
        
        setTitle("XMPP Settings");
        
        EditText host = (EditText)findViewById(R.id.host);
        host.setText(xmppClient.getXmpp().getHost());
        
        EditText port = (EditText)findViewById(R.id.port);
        port.setText(xmppClient.getXmpp().getPort());

        EditText service = (EditText)findViewById(R.id.service);
        service.setText(xmppClient.getXmpp().getService());

        EditText user = (EditText)findViewById(R.id.userid);
        user.setText(xmppClient.getXmpp().getUser());

        EditText password = (EditText)findViewById(R.id.password);
        password.setText(xmppClient.getXmpp().getPassword());

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            	String host = getText(R.id.host);
        		String port = getText(R.id.port);
        		String service = getText(R.id.service);
        		String username = getText(R.id.userid);
        		String password = getText(R.id.password);
        		int portInt = 5222; 
        		try {
        			portInt = Integer.parseInt(port);
        		} catch (Exception e) {
//        					Toast.makeText(xmppClient, "invalid port", Toast.LENGTH_SHORT).show();
        			}
        		
        		if (host.length() > 0 && port.length() > 0
        				&& service.length() > 0 && username.length() > 0
        				&& password.length() > 0 && portInt > 0)	{
        		
				try {
				XMPPConnection connection =	new XmppConnectionTask().execute(host, port, service, username, password ).get();
				if (connection!=null) {
					xmppClient.getXmpp().setValues(host, port, service, username, password);
			        xmppClient.setConnection(connection);
			        
	        		dismiss();        		
				} else {
					xmppClient.raiseToast("connection is null");
					xmppClient.setConnection(null);
				}
				
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
    		} else {
    			xmppClient.raiseToast("Enter all values");
    		       }
            }
        });
    }

    private String getText(int id) {
        EditText widget = (EditText) this.findViewById(id);
        return widget.getText().toString();
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	}
