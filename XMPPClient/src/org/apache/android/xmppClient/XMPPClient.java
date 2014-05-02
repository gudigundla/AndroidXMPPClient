package org.apache.android.xmppClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class XMPPClient extends ActionBarActivity {

    private List<String> messages = new ArrayList<String>();
    private Handler mHandler = new Handler();
    private Handler cwHandler = new Handler();
    private SettingsDialog mDialog;
    private ChatWithDialog cDialog;
    private EditText mRecipient;
    private EditText mSendText;
    private ListView mList;
    private XMPPConnection connection;
    private XMPPDetails xmpp;
    private String receipient=""; 
    
    public String getReceipient() {
		return receipient;
	}

	public void setReceipient(String receipient) {
		this.receipient = receipient;
	}

	public XMPPConnection getConnection() {
		return connection;
	}
    
    public XMPPDetails getXmpp() {
		return xmpp;
	}

	public void setXmpp(XMPPDetails xmpp) {
		this.xmpp = xmpp;
	}

	//    private String xmppServerName = "xmpp-server";
	/**
     * Called with the activity is first created.
     */
   @Override
    public void onCreate(Bundle state) {
     	super.onCreate(state);
     	
     	SharedPreferences xmppPreferences = getPreferences(0);
     	String host = xmppPreferences.getString("host", "");
     	String port = xmppPreferences.getString("port", "");
     	String service = xmppPreferences.getString("service", "");
     	String user = xmppPreferences.getString("user", "");
     	String password = xmppPreferences.getString("password", "");

     	xmpp = new XMPPDetails(host, port, service, user, password);
     	
     	helperMethod();
     	//auto login
     	try {
			connection =	new XmppConnectionTask().execute(host, port, service, user, password ).get();
		} catch (InterruptedException e) {
			if (connection ==null) {
				raiseToast("Failed to login as " + user + "!");
			}
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

   }


private void helperMethod() {
	   setContentView(R.layout.main);
/*        mRecipient = (EditText) this.findViewById(R.id.recipient);
        Log.i("XMPPClient", "mRecipient = " + mRecipient);
*/      
        mSendText = (EditText) this.findViewById(R.id.sendText);
        Log.i("XMPPClient", "mSendText = " + mSendText);
        mList = (ListView) this.findViewById(R.id.listMessages);
        Log.i("XMPPClient", "mList = " + mList);
        setListAdapter();

        // Set a listener to send a chat text message
        Button send = (Button) this.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            if (connection!=null) {
//            	String to = mRecipient.getText().toString();
            	String to =receipient;
//            			+ "@" + xmppServerName;
				String text = mSendText.getText().toString();
				if (to.length()>0) {
	                if (text.length()>0) {
	    				Log.i("XMPPClient", "Sending text [" + text + "] to [" + to + "]");
	                    Message msg = new Message(to, Message.Type.chat);
	                    msg.setBody(text);
	                    connection.sendPacket(msg);
	                    mSendText.setText("");
	                    
	                    messages.add(connection.getUser() + ":");
	                    messages.add(text);
	                    setListAdapter();
					} else {
						raiseToast("Enter message");
					}
					
				} else {
					raiseToast("Enter receipient");
				}
                
			} else {
				raiseToast("Establish connection !!");
			}	   
         }
        });
	}

   // to populate action bar items
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.actionbar, menu);
    return super.onCreateOptionsMenu(menu);
   }
   public void raiseToast(String message){
		Toast.makeText(XMPPDemosApplication.getContext(), message, Toast.LENGTH_SHORT).show();
	}
   
   public static void setActionBarTitle(String message){
	
   }
   
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
	if(item.getItemId() == R.id.action_settings) {
		// Dialog for getting the xmpp settings
        mDialog = new SettingsDialog(this);		
        mHandler.post(new Runnable() {
            public void run() {
                mDialog.show();
            } });
		return true;
	} else
		if(item.getItemId() == R.id.action_newChat){
			cDialog = new ChatWithDialog(this);
			cwHandler.post(new Runnable() {
				
				@Override
				public void run() {
					cDialog.show();
				}
			});
	
//			raiseToast("Will be added in next version");
			return true;
		}
		else
		return super.onOptionsItemSelected(item);
	}
	      
    /**
     * Called by Settings dialog when a connection is established with the XMPP server
     *
     * @param connection
     */
   public void setConnection(XMPPConnection connection) {
        this.connection = connection;
        if (connection != null) {
            // Add a packet listener to get messages sent to us
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            connection.addPacketListener(new PacketListener() {
                public void processPacket(Packet packet) {
                    Message message = (Message) packet;
                    if (message.getBody() != null) {
                        String fromName = StringUtils.parseBareAddress(message.getFrom());
                        Log.i("XMPPClient", "Got text [" + message.getBody() + "] from [" + fromName + "]");
                        messages.add(fromName + ":");
                        messages.add(message.getBody());
                        // Add the incoming message to the list view
                        mHandler.post(new Runnable() {
                            public void run() {
                                setListAdapter();
                            }
                        });
                    }
                }
            }, filter);
   
            //set preferences
            SharedPreferences preferences = getPreferences(0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("host", xmpp.getHost());
            editor.putString("port", xmpp.getPort());
            editor.putString("service", xmpp.getService());
            editor.putString("password", xmpp.getPassword());
            editor.putString("user", xmpp.getUser());
            
            editor.commit();
            
            
        }
      }
    
   private void setListAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.multi_line_list_item,
                messages);
        mList.setAdapter(adapter);
    }
	
}