package org.apache.android.xmppClient;

import java.util.concurrent.ExecutionException;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChatWithDialog extends Dialog {

	private XMPPClient client;
	
	public ChatWithDialog(XMPPClient client) {
		super(client);
		this.client=client;
	}

	@Override
	protected void onStart() {
		super.onStart();
	
		setContentView(R.layout.chat_with);
	    getWindow().setFlags(4, 4);
	        
	    setTitle("XMPP Settings");
	    
	    Button ok = (Button) findViewById(R.id.selectUser);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            EditText chatwithUser = (EditText)findViewById(R.id.chatwithUser);
            String user = chatwithUser.getText().toString();
//            client.setReceipient(user);
            
/*			try {
				String value =	new XmppValidUserTask().execute(client ).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	*/	
            if(user.length()>0) {
            client.setReceipient(user);
            dismiss();
            } else {
            	client.raiseToast("enter userid !!");
            }

			
            }            
	});
}
}