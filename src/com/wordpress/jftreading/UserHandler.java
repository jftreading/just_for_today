package com.wordpress.jftreading;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class UserHandler extends DefaultHandler {
	private String urlToParse;
	private String urlToDownload;
	private String versionCode;
	private String tagName;
	private String filename;
	private String[] links;
	private File directory;	
	private Context context;	
	
	public UserHandler(Context context) {
		this.context = context;
		
		links = this.context.getResources().getStringArray(R.array.links);
		urlToParse = links[links.length - 1];
		urlToDownload = links[links.length -2 ];		
				
		directory = Environment.getExternalStorageDirectory();
		filename = this.context.getResources().getString(R.string.app_filename);
	}

	public void processFeed() {
		AsyncTaskParser parser = new AsyncTaskParser();
		parser.execute(this);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("manifest")) {
			versionCode = attributes.getValue("android:versionCode");
			tagName = attributes.getValue("android:versionName");			
		}
	}

	private class AsyncTaskParser extends AsyncTask<DefaultHandler, Void, Void> {

		@Override
		protected Void doInBackground(DefaultHandler... params) {
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				XMLReader reader = parser.getXMLReader();
				reader.setContentHandler(params[0]);
				
				InputStream inputStream = new URL(urlToParse).openStream();
				Log.d("JFT", "Parsing AndroidManifest.xml");
				reader.parse(new InputSource(inputStream));
			} catch (Exception e) {
				Log.e("JFT", "Error: " + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
				if (Integer.parseInt(versionCode) > packageInfo.versionCode) {
					Log.d("JFT", "A new version available!");
					//AsyncTaskDownloader downloader = new AsyncTaskDownloader();
					//downloader.execute();					
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class AsyncTaskDownloader extends AsyncTask<Void, Integer, Void> {
		File file;
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				URL url = new URL(urlToDownload + tagName + "/" + filename);
				URLConnection connection = url.openConnection();
				connection.connect();
				
				int fileLength = connection.getContentLength();
				
				//Download the file
				InputStream input = new BufferedInputStream(url.openStream());				
				file = new File(directory + "/" + filename);
				OutputStream output = new FileOutputStream(file);
				
				byte data[] = new byte[1024];
				long total = 0;
				int count;
				
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
				
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				Log.e("JFT", "Well that didn't work out so well...");
				Log.e("JFT", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Intent i = new Intent();
			i.setAction(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			Log.d("JFT", "About to install new .apk");
			context.startActivity(i);
		}		
	}
}
