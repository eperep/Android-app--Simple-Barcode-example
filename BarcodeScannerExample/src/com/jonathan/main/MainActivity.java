package com.jonathan.main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private Button scanProductBN;
	private TextView barcodeTV, productTV, priceTV, descriptionTV;
	private static final int REQUEST_BARCODE = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		scanProductBN = (Button) findViewById(R.id.scanProductBN);
		scanProductBN.setOnClickListener(this);

		barcodeTV = (TextView) findViewById(R.id.barcodeTV);
		productTV = (TextView) findViewById(R.id.productTV);
		priceTV = (TextView) findViewById(R.id.priceTV);
		descriptionTV = (TextView) findViewById(R.id.descriptionTV);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.scanProductBN:

			// Launch barcode scanner.
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
			startActivityForResult(intent, REQUEST_BARCODE);
			break;
		}
	}

	@SuppressWarnings("static-access")
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_BARCODE) {
			if (resultCode == RESULT_OK) {

				// Get barcode from result.
				String barcode = intent.getStringExtra("SCAN_RESULT");
				barcodeTV.setText("BARCODE: " + barcode + "\n");

				// Look up barcode information using www.upcdatabase.org <API
				// XML Access>.
				XMLHandler xmlHandler = new XMLHandler();
				String xmlStr = xmlHandler.getXML(barcode);
				Document xmlDoc = xmlHandler.constructXMLFile(xmlStr);

				// Parse XML Document.
				NodeList nodes = xmlDoc.getElementsByTagName("output");

				for (int i = 0; i < nodes.getLength(); i++){

					Element element = (Element) nodes.item(i);
					String valid = XMLHandler.getTagValue(element, "valid");

					// Valid is true if barcode exists at UPCDATABASE.org, false
					// otherwise.
					if ("true".equals(valid)){
						
						productTV.setText("PRODUCT: "
								+ XMLHandler.getTagValue(element, "itemname")
								+ "\n");
						priceTV.setText("PRICE: "
								+ xmlHandler.getTagValue(element, "price")
								+ "\n");
						descriptionTV.setText("DESCRIPTION: \n"
								+ xmlHandler
										.getTagValue(element, "description")
								+ "\n");
					}
					else{ //No product found.
						productTV.setText("No product with this barcode.");
						priceTV.setText("");
						descriptionTV.setText("");
					}
				}
				
			}else if (resultCode == RESULT_CANCELED) {
				finish();
			}
		}
	}

}