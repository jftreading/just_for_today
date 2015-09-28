package com.wordpress.jftreading;

import java.io.InputStream;

import com.wordpress.jftreading.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

@SuppressLint("NewApi")
public class MainFragment extends Fragment implements OnClickListener
{
	private static final int PICK_CONTACT_REQUEST = 0;	
    private View fragmentView;
    private TextView contactNameView;
    private TextView contactPhoneView;
    private ImageView contactPhotoView;
    private EditText textMessage;    
    private DBHelper databaseHelper;
    private Uri contactUri;
	private Handler handler;
	private ProgressDialog dialog;
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {    	
    	fragmentView = inflater.inflate(R.layout.main_fragment, container, false);
    	
    	contactNameView = (TextView) fragmentView.findViewById(R.id.contact_name);
        contactPhoneView = (TextView) fragmentView.findViewById(R.id.contact_phone);
        contactPhotoView = (ImageView) fragmentView.findViewById(R.id.portrait);
        textMessage = (EditText) fragmentView.findViewById(R.id.etId);
        
        ImageButton updateContact = (ImageButton) fragmentView.findViewById(R.id.update_contact);
        ImageButton callBut = (ImageButton) fragmentView.findViewById(R.id.call_btn);
        ImageButton sendBut = (ImageButton) fragmentView.findViewById(R.id.send_btn);
        
        updateContact.setOnClickListener(this);
        callBut.setOnClickListener(this);        
        sendBut.setOnClickListener(this);
        
        searchDatabase();
        renderContact(contactUri);
        handler = new Handler();
        
		return fragmentView;
	}
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {            	
                contactUri = intent.getData();
                String s = contactUri.toString();
                databaseHelper.saveContactData(s);
                renderContact(intent.getData());                
            }
        }
    }
    
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update_contact:
			startActivityForResult(new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI),
					PICK_CONTACT_REQUEST);
			break;
		case R.id.call_btn:
			// Dial mobile number
			if (contactUri != null) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
		        callIntent.setData(Uri.parse("tel:" + getMobileNumber(contactUri)));
		        startActivity(callIntent);					
			}
			break;
		case R.id.send_btn:
			String stextMessage = textMessage.getText().toString();
			if (stextMessage.matches("")){
				
			} else if (contactUri != null) {
				
				 dialog = ProgressDialog.show(getActivity(),
						"Sending", "Sending text message");
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
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
			});
			th.start();
			}
			break;
		default:
			break;
		}
	}
    
    protected String getMobileNumber(Uri uri) {
        String phoneNumber = null;
        String id = null;

        Cursor contactCursor = getActivity().getContentResolver().query(
        		uri, new String[]{ContactsContract.Contacts._ID},null, null, null);
        
        if (contactCursor.moveToFirst()){
        	id = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
        }
        contactCursor.close();

        Cursor phoneCursor = getActivity().getContentResolver().query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
                + ContactsContract.CommonDataKinds.Phone.TYPE + " = "
                + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
            new String[] { id },null);
        
        if (phoneCursor.moveToFirst()){
            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        phoneCursor.close();
        return phoneNumber;
    }
    
    private void searchDatabase() {
        databaseHelper = new DBHelper(getActivity());        
        Cursor cursor = databaseHelper.getAllContactData();        
        if (cursor.moveToLast()) {
			do { String s = cursor.getString(1);
                 contactUri = Uri.parse(s);
			} while (cursor.moveToNext());
		} else {
			contactUri = null;
		}
        if (!cursor.isClosed()) {
			cursor.close();
		}
    }

	private void renderContact(Uri uri) {
        if (uri != null) {
        	contactNameView.setText(getDisplayName(uri));
            contactPhoneView.setText(getMobileNumber(uri));
            contactPhotoView.setImageBitmap(getPhoto(uri));
        }
    }

    private String getDisplayName(Uri uri) { 
        String displayName = null;

        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if(cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            }
        cursor.close();
        return displayName;
    }

    @SuppressLint("UseValueOf")
	private Bitmap getPhoto(Uri uri) {
        Bitmap photo = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_report_image);
        String id = null;
        Uri photoUri = null;
        Cursor contactCursor = getActivity().getContentResolver().query(
            uri, new String[]{ContactsContract.Contacts._ID},
            null, null, null);

        if (contactCursor.moveToFirst()){
            id = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
            photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
            		new Long(id).longValue());
        }
        contactCursor.close();        
        if (photoUri != null) {
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(),
        		ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id).longValue()));
            if (input != null) {
                photo = BitmapFactory.decodeStream(input);
            }
        }
        return photo;
    }	
}