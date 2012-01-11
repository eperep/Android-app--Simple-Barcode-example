package com.jonathan.main;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

//Simple XML handler in order to query UPCDATABASE.ORG.
public class XMLHandler {
	
	private static final String API_KEY = ""; //Use your api key obtained from upcdatabase.org

	public XMLHandler() {

	}

	/**
	 * Get XML content from a web service.
	 * @param barcode
	 * @return XML content as a string representation.
	 */
	public static String getXML(String barcode) {
		String line = null;
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(
					"http://www.upcdatabase.org/api/xml/" + API_KEY + "/" + barcode);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			line = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			line = "Can't connect to server";
		} catch (MalformedURLException e) {
			line = "Can't connect to server";
		} catch (IOException e) {
			line = "Can't connect to server";
		}

		return line;

	}

	/**
	 * @param element
	 * @param tag
	 * @return String containing the tag value.
	 */
	public final static String getTagValue(Element element, String tag) {
		NodeList list = element.getElementsByTagName(tag).item(0)
				.getChildNodes();
		Node node = (Node) list.item(0);

		return node.getNodeValue();
	}

	/**
	 * Construct XML file from a string.
	 * @param xml
	 * @return
	 */
	public Document constructXMLFile(String xml) {

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}

		return doc;
	}
}
