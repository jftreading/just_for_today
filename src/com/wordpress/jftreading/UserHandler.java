package com.wordpress.jftreading;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.os.AsyncTask;
import android.util.Log;

public class UserHandler extends DefaultHandler {
	private String url = "https://raw.githubusercontent.com/jftreading/just_for_today/master/AndroidManifest.xml";
	
	public void processFeed() {
		AsyncTaskRunner runner = new AsyncTaskRunner();
		runner.execute(this);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("manifest")) {
			String versionCode = attributes.getValue("android:versionCode");
			Log.d("SAXPARSER", "VERSION CODE: " + versionCode);
		}
	}
	
	private class AsyncTaskRunner extends AsyncTask<DefaultHandler, Void, Void> {

		@Override
		protected Void doInBackground(DefaultHandler... params) {
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				XMLReader reader = parser.getXMLReader();
				reader.setContentHandler(params[0]);
				
				InputStream inputStream = new URL(url).openStream();
				Log.d("SAXPARSER", "Parsing AndroidManifest.xml");
				reader.parse(new InputSource(inputStream));
			} catch (Exception e) {
				Log.d("SAXPARSER", "Error: " + e.toString());
			}
			return null;
		}		
	}
}
