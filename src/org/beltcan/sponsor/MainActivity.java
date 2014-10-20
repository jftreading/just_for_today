package org.beltcan.sponsor;

import java.io.InputStream;

import org.beltcan.sponsor.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
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


public class MainActivity extends Activity
{
	private static final int PICK_CONTACT_REQUEST = 0;
	private DBHelper databaseHelper;
    private EditText textMessage;    
    private Uri contactUri;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        databaseHelper = new DBHelper(this);
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
        textMessage = (EditText) findViewById(R.id.etId);                
    }   

    private void renderContact(Uri uri) {
        TextView contactNameView = (TextView) findViewById(R.id.contact_name);
        TextView contactPhoneView = (TextView) findViewById(R.id.contact_phone);
        ImageView contactPhotoView = (ImageView) findViewById(R.id.portrait);

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

    public void onUpdateContact(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
            PICK_CONTACT_REQUEST
        );
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {            	
                contactUri = intent.getData();
                String s = contactUri.toString();
                databaseHelper.saveSponsorData(s);
                renderContact(intent.getData());
                ImageButton callBut = (ImageButton) findViewById(R.id.call_btn);
                callBut.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						/* Dial mobile number */
						Intent callIntent = new Intent(Intent.ACTION_CALL);
				        callIntent.setData(Uri.parse("tel:" + getMobileNumber(contactUri)));
				        startActivity(callIntent);
					}
				});
                ImageButton sendBut = (ImageButton) findViewById(R.id.send_btn);
                textMessage = (EditText) findViewById(R.id.etId);                
                sendBut.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String stextMessage = textMessage.getText().toString();
						if (stextMessage.matches("")){
							
						}
						else {							
							final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Sending", "Sending text message");
							Thread th = new Thread() {
							    @Override
							    public void run() {
							    	// TODO Auto-generated method stub
							    	/* Send text message */
									try {
							            String SENT = "sent";
							            String DELIVERED = "delivered";

							            Intent sentIntent = new Intent(SENT);
							            /*Create Pending Intents*/
							            PendingIntent sentPI = PendingIntent.getBroadcast(
							                getApplicationContext(), 0, sentIntent,
							                PendingIntent.FLAG_UPDATE_CURRENT);

							            Intent deliveryIntent = new Intent(DELIVERED);

							            PendingIntent deliverPI = PendingIntent.getBroadcast(
							                getApplicationContext(), 0, deliveryIntent,
							                PendingIntent.FLAG_UPDATE_CURRENT);
							            /* Register for SMS send action */
							            registerReceiver(new BroadcastReceiver() {

							                @Override
							                public void onReceive(Context context, Intent intent) {
							                    String result = "";

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

							                        Toast toast = Toast.makeText(getApplicationContext(), result,
							                            Toast.LENGTH_LONG);
							                        toast.setGravity(Gravity.CENTER, 0, 0);
							                        toast.show();
							                        dialog.dismiss();
							                }

							            }, new IntentFilter(SENT));
							            /* Register for Delivery event */
							            registerReceiver(new BroadcastReceiver() {

							                @Override
							                public void onReceive(Context context, Intent intent) {
							                    Toast.makeText(getApplicationContext(), "Delivered",
							                        Toast.LENGTH_LONG).show();
							                }

							            }, new IntentFilter(DELIVERED));

							            /*Send SMS*/
							            SmsManager smsManager = SmsManager.getDefault();
							            smsManager.sendTextMessage(
							                getMobileNumber(contactUri),
							                null,
							                textMessage.getText().toString(),
							                sentPI,
							                deliverPI);
							        } catch (Exception ex) {
							              Toast.makeText(getApplicationContext(),
							                  ex.getMessage().toString(), Toast.LENGTH_LONG)
							                  .show();
							              ex.printStackTrace();
							        }								
							    }	
							};
							th.start();
						}						
						
					}
				});                
            }
        }
    }

    private String getDisplayName(Uri uri) { 
        String displayName = null;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
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

        Cursor contactCursor = getContentResolver().query(
            uri, new String[]{ContactsContract.Contacts._ID},
            null, null, null);

            String id = null;
            if (contactCursor.moveToFirst()){
                id = contactCursor.getString(
                    contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
            }
            contactCursor.close();

        Cursor phoneCursor = getContentResolver().query(
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
        Cursor contactCursor = getContentResolver().query(
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
                        getContentResolver(),
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
		Intent steps_intent = null;
		Intent meeting_intent = null;
		
        switch (item.getItemId()) 
        {
            case R.id.steps:
                steps_intent = new Intent(this, TwelveStepsActivity.class);
                startActivity(steps_intent);
                return true;

            case R.id.meeting:
                meeting_intent = new Intent(this, MeetingActivity.class);
                startActivity(meeting_intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

