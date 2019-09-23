package com.github.rgmatute;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Autor Ronny Gabriel Matute Granizo 
 * Email: rgmatute91@gmail.com 
 * Whatsapp: +593 981851214
 **/
@SuppressWarnings({"static-access","unused"})
public class InvokeSoap {

	public int code;
	public Object body;
	
	private HashMap<String, String> HEADER = new HashMap<String,String>();

	private static final Log log = LogFactory.getLog(InvokeSoap.class);
	private static Document doc = null;
	private static BigDecimal timeout = null;
	
	public String call(String requestWS, String urlWS) {
		return this.call(requestWS, "POST", urlWS);
	}
	
	public String call(String requestWS, String urlWS, int timeout) {
		this.timeout = new BigDecimal(timeout);
		return this.call(requestWS, "POST", urlWS);
	}

	public String call(String requestWS, String method, String urlWS) {
		log.info("InvokeSoap Start-Time: " + __.currentDate("SSS") + " milliseconds");
		String resposeWS = "";
		try {
			log.info("URL WS: " + urlWS);
			log.info(requestWS);
			URL url = new URL(urlWS);

			HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
			conexion.setDoOutput(true);
			conexion.setReadTimeout(this.getTimeOut().intValue());
            //Conexion de red
            conexion.setConnectTimeout(this.getTimeOut().intValue());
            log.info("timeout milisegundos: "+this.getTimeOut().intValue());
			conexion.setRequestMethod(method.toUpperCase());
			conexion.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
			// Se le agrega mas headers de ser necesario
			for (Map.Entry<String, String> entry : HEADER.entrySet()) {
				conexion.setRequestProperty(entry.getKey(), entry.getValue());
			}
			OutputStream outputStream = conexion.getOutputStream();
			outputStream.write(requestWS.getBytes("utf-8"));
			outputStream.flush();
			this.code = conexion.getResponseCode();

			//if (this.code == conexion.HTTP_OK) {
				InputStream is = conexion.getInputStream();
				BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(is));
				StringBuilder output = new StringBuilder();
				String aux;

				while ((aux = responseBuffer.readLine()) != null) {
					output.append(aux);
				}
				is.close();
				outputStream.close();
				responseBuffer.close();
				log.info("ResponseActivation: " + output.toString());
				resposeWS = output.toString();
				// ADD PARA LA MANIPULACION DEL RESPONSE
				addDoc(resposeWS);
			//}
		} catch (Exception ex) {
			log.info("Exception: " + ex.getMessage());
			return "Exception: " + ex;
		}
		log.info("InvokeSoap End-Time: " + __.currentDate("SSS") + " milliseconds");
		return resposeWS;
	}

	public static void addDoc(String respuest) throws Exception {
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(new StringReader(respuest)));
	}
	
	public InvokeSoap setTimeout(String timeout) {
		this.timeout = (timeout != null) ? new BigDecimal(timeout) : null; 
		return this;
	}
	
	private BigDecimal getTimeOut() {
		if(this.timeout == null){
			try {
				this.timeout = new BigDecimal(InvokeProperties.getProperty("spring.invokesoap.timeoutInMilliseconds"));
			} catch (NullPointerException e) {
				e.getStackTrace();
			}
			this.timeout = (this.timeout == null) ? new BigDecimal("0")  : this.timeout ;
		}
		return this.timeout;
	}
	
	public InvokeSoap addRequestProperty(String key,String value) {
		this.HEADER.put(key, value);
		return this;
	}

	public String getTagByName(String name) {
		return (doc != null) ? doc.getElementsByTagName(name).item(0).getTextContent() : "No service has been called";
	}
	
	public String getTagByName(String xml,String tagName) {
		Document tags = null;
		try {
			tags = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(xml)));
			return  (tags.getElementsByTagName(tagName).item(0) != null)? tags.getElementsByTagName(tagName).item(0).getTextContent(): "Tag not found";
		} catch (Exception e) {
			log.error("Error: " + e.getMessage());
		}
		
		return null;
	}
	


	public List<String> getTagByNames(String name) {
		List<String> l = new ArrayList<String>();
		if (doc != null) {
			if (doc.getElementsByTagName(name).getLength() > 1) {
				for (int i = 0; i < doc.getElementsByTagName(name).getLength(); i++) {
					l.add(doc.getElementsByTagName(name).item(i).getTextContent());
				}
			} else {
				l.add(doc.getElementsByTagName(name).item(0).getTextContent());
			}
		} else {
			try {
				throw new Exception("No service has been called");
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		return l;
	}
	
	
	public List<String> getTagByNamesXml(String tagName) {
		List<String> list = new ArrayList<String>();
		NodeList nodeList = null;
		if(doc != null) {
			nodeList = doc.getElementsByTagName(tagName);
			for(int i=0;i<nodeList.getLength();i++) {
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element elemen = (Element)node;
					String tag = "";
					for(int j=0;j<node.getChildNodes().getLength();j++) {
						Node nodeSub = node.getChildNodes().item(j);
						if(nodeSub.getNodeType() == Node.ELEMENT_NODE) {
							String key = nodeSub.getNodeName();
							String value = elemen.getElementsByTagName(key).item(0).getTextContent();
							tag += "<"+key+">" + value   + "</"+key+">";
						}
					}
					list.add(tag);
				}
			}
		}
		return list;
	}
	
	public List<String> getTagByNamesXml(String xml, String tagName) {
		List<String> list = new ArrayList<String>();
		NodeList nodeList = null;
		Document tags = null;
		try {
			tags = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(xml)));
			//if(doc != null) {
			nodeList = tags.getElementsByTagName(tagName);
			for(int i=0;i<nodeList.getLength();i++) {
				Node node = nodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
					Element elemen = (Element)node;
					String tag = "";
					for(int j=0;j<node.getChildNodes().getLength();j++) {
						Node nodeSub = node.getChildNodes().item(j);
						if(nodeSub.getNodeType() == Node.ELEMENT_NODE) {
							String key = nodeSub.getNodeName();
							String value = elemen.getElementsByTagName(key).item(0).getTextContent();
							tag += "<"+key+">" + value   + "</"+key+">";
						}
					}
					list.add(tag);
				}
			}
		//}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	public String getTagByNamesJson(String tagName) { // PENDIENTE
		String json = "";
		return json;
	}
}