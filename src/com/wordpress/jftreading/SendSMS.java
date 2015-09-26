package com.wordpress.jftreading;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.widget.Toast;

public class SendSMS extends MainFragment implements Runnable {
	
	@Override
	public void run() {
		handler = new Handler();
		// Send text message
		try {
            String SENT = "sent";
            String DELIVERED = "delivered";

            Intent sentIntent = new Intent(SENT);
            //Create Pending Intents
            PendingIntent sentPI = PendingIntent.getBroadcast(
                getActivity().getApplicationContext(), 0, sentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

            Intent deliveryIntent = new Intent(DELIVERED);

            PendingIntent deliverPI = PendingIntent.getBroadcast(
                getActivity().getApplicationContext(), 0, deliveryIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
            //Register for SMS send action
            getActivity().registerReceiver(new BroadcastReceiver() {
            	String result = "";

                @Override
                public void onReceive(Context context, Intent intent) {							                    

                    switch (getResultCode()) {

                    case Activity.RESULT_OK:
                        result = "Message sent";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        result = "Sending failed";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        result = "Radio off";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        result = "No PDU defined";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        result = "No service";
                        break;
                    }
                    
                    handler.post(new Runnable() {
						
						@Override
						public void run() {
							if (result != "") {													
								Toast toast = Toast.makeText(getActivity().getApplicationContext(), result,
				                    Toast.LENGTH_LONG);
				                toast.setGravity(Gravity.CENTER, 0, 0);
				                toast.show();
				                result = "";
							}														
		                    dialog.dismiss();														
						}
					});							                    
                }

            }, new IntentFilter(SENT));
            //Register for Delivery event
            getActivity().registerReceiver(new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                	handler.post(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(getActivity().getApplicationContext(), "Delivered",
			                        Toast.LENGTH_LONG).show();														
						}
					});							                    
                }

            }, new IntentFilter(DELIVERED));

            //Send SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(
                getMobileNumber(contactUri),
                null,
                textMessage.getText().toString(),
                sentPI,
                deliverPI);
        } catch (Exception ex) {
        	final String exception = ex.getMessage().toString();
        	handler.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getActivity().getApplicationContext(),
			            exception, Toast.LENGTH_LONG).show();												
				}
			});
        	ex.printStackTrace();
        	dialog.dismiss();
        }
	}
}
