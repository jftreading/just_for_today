package com.pythonanywhere.healingpathfoundationhp;

import java.io.InputStream;
import com.pythonanywhere.healingpathfoundationhp.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

@SuppressLint("NewApi")
public class MainFragment extends Fragment
{
	private static final int PICK_CONTACT_REQUEST = 0;
	private DBHelper databaseHelper;
    private EditText textMessage;    
    private Uri contactUri;
	private Handler handler;
    private View fragmentView;
    private TextView contactNameView;
    private TextView contactPhoneView;
    private ImageView contactPhotoView;
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	fragmentView = inflater.inflate(R.layout.main_fragment, container, false);
    	
    	contactNameView = (TextView) fragmentView.findViewById(R.id.contact_name);
        contactPhoneView = (TextView) fragmentView.findViewById(R.id.contact_phone);
        contactPhotoView = (ImageView) fragmentView.findViewById(R.id.portrait);
    	
        handler = new Handler();
        databaseHelper = new DBHelper(getActivity());
        Cursor cursor = databaseHelper.getAllSponsorData();
        
        if (cursor.moveToLast()) {
			do { String s = cursor.getString(1);
                 Uri mUri = Uri.parse(s);
                 contactUri = mUri;
                 renderContact(mUri);
				 Log.d("DB Value", s);
			} while (cursor.moveToNext());
		}
		
		else { renderContact(null); }
        
        if (!cursor.isClosed()) {
			cursor.close();
		}
				        
        textMessage = (EditText) fragmentView.findViewById(R.id.etId);
        ImageButton updateContact = (ImageButton) fragmentView.findViewById(R.id.update_contact);
        ImageButton callBut = (ImageButton) fragmentView.findViewById(R.id.call_btn);
        ImageButton sendBut = (ImageButton) fragmentView.findViewById(R.id.send_btn);        
        
        updateContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
			            PICK_CONTACT_REQUEST
			        );				
			}
		});
        
        callBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Dial mobile number
				if (contactUri != null) {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
			        callIntent.setData(Uri.parse("tel:" + getMobileNumber(contactUri)));
			        startActivity(callIntent);					
				}				
			}
		});        
                      
        sendBut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String stextMessage = textMessage.getText().toString();
				if (stextMessage.matches("")){
					
				} else if (contactUri != null) {
					
					final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Sending", "Sending text message");
					Thread th = new Thread() {
						
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
					};
					th.start();
				}										
			}
		});
        
		return fragmentView;
	}

	private void renderContact(Uri uri) {        

        if (uri == null) {
            contactNameView.setText("Select sponsor");
            contactPhoneView.setText("");
            contactPhotoView.setImageBitmap(null);            
        } else {
            contactNameView.setText(getDisplayName(uri));
            contactPhoneView.setText(getMobileNumber(uri));
            contactPhotoView.setImageBitmap(getPhoto(uri));
        }
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {            	
                contactUri = intent.getData();
                String s = contactUri.toString();
                databaseHelper.saveSponsorData(s);
                renderContact(intent.getData());                
            }
        }
    }

    private String getDisplayName(Uri uri) { 
        String displayName = null;

        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if(cursor.moveToFirst()) {
                displayName = cursor.getString(
                    cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                );
            }
        cursor.close();

        return displayName;
    }

    private String getMobileNumber(Uri uri) {
        String phoneNumber = null;

        Cursor contactCursor = getActivity().getContentResolver().query(
            uri, new String[]{ContactsContract.Contacts._ID},
            null, null, null);

            String id = null;
            if (contactCursor.moveToFirst()){
                id = contactCursor.getString(
                    contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
            }
            contactCursor.close();

        Cursor phoneCursor = getActivity().getContentResolver().query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
                + ContactsContract.CommonDataKinds.Phone.TYPE + " = "
                + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
            new String[] { id },
            null
        );
        if (phoneCursor.moveToFirst()){
            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER)
            );
        }
        phoneCursor.close();
        return phoneNumber;
    }

    private Bitmap getPhoto(Uri uri) {
        Bitmap photo = null;
        String id = null;
        Uri photoUri = null;
        Cursor contactCursor = getActivity().getContentResolver().query(
            uri, new String[]{ContactsContract.Contacts._ID},
            null, null, null);

        if (contactCursor.moveToFirst()){
            id = contactCursor.getString(
                contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
            photoUri = ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI,
                            new Long(id).longValue());
        }
        else {
            Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_report_image);
            return defaultPhoto;
        }
        contactCursor.close();        
        if (photoUri != null) {
        InputStream input =
                    ContactsContract.Contacts.openContactPhotoInputStream(
                    		getActivity().getContentResolver(),
                        ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI,
                            new Long(id).longValue())
                    );
            if (input != null) {
                return BitmapFactory.decodeStream(input);
            }
        } else {
            Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_report_image);
            return defaultPhoto;
        }
        Bitmap defaultPhoto = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_report_image);
        return defaultPhoto;
    }
}