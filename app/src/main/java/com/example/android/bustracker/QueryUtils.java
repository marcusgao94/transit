package com.example.android.bustracker;

import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by yishuyan on 9/14/16.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Return a list of {@link BusInfo} objects by parsing out information
     */
    public static ArrayList<BusInfo> extractFeatureFromJson(String requestUrl) {

        ArrayList<BusInfo> busList = new ArrayList<BusInfo>();


        URL url = createUrl(requestUrl);
        String busXML = null;
        try {
            busXML = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        if (TextUtils.isEmpty(busXML)) {
            return null;
        }
        DocumentBuilder newDocumentBuilder = null;
        try {
            newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = newDocumentBuilder.parse(new ByteArrayInputStream(busXML.getBytes()));
            doc.getDocumentElement().normalize();
            Log.i(LOG_TAG, "Root element :" + doc.getDocumentElement().getNodeName());
//            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            // get all child nodes
            NodeList nList = doc.getElementsByTagName("route");
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    String rtnm = eElement.getElementsByTagName("rtnm").item(0).getTextContent();
                    String rt = eElement.getElementsByTagName("rt").item(0).getTextContent();
                    String rtclr = eElement.getElementsByTagName("rtclr").item(0).getTextContent();
                    BusInfo tempBus = new BusInfo(rtnm, rt, rtclr);
                    busList.add(tempBus);
                    Log.i(LOG_TAG, "route name : " + eElement.getElementsByTagName("rtnm").item(0).getTextContent());
//                    System.out.println("route name : " + eElement.getElementsByTagName("rtnm").item(0).getTextContent());
                    Log.i(LOG_TAG, "route number : " + eElement.getElementsByTagName("rt").item(0).getTextContent());
                    Log.i(LOG_TAG, "route color : " + eElement.getElementsByTagName("rtclr").item(0).getTextContent());

                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return busList;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book list JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        //Log.e(LOG_TAG,jsonResponse);
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
