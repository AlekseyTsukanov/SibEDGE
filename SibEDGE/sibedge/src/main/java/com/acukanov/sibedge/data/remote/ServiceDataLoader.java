package com.acukanov.sibedge.data.remote;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.acukanov.sibedge.BuildConfig;
import com.acukanov.sibedge.data.remote.model.ServiceData;
import com.acukanov.sibedge.utils.LogUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ServiceDataLoader extends AsyncTaskLoader<List<ServiceData>> {
    private static final String LOG_TAG = LogUtils.makeLogTag(ServiceDataLoader.class);
    private List<ServiceData> serviceData;

    public ServiceDataLoader(Context context) {
        super(context);
        serviceData = new ArrayList<>();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<ServiceData> loadInBackground() {
        try {
            URL url = new URL(BuildConfig.SERVER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(inputStream);
                Element element = document.getDocumentElement();
                NodeList nodeList = element.getElementsByTagName("quote");

                if (nodeList.getLength() > 0) {
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Element startElement = (Element) nodeList.item(i);
                        Element idElement = (Element) startElement.getElementsByTagName("id").item(0);
                        Element dateElement = (Element) startElement.getElementsByTagName("date").item(0);
                        Element textElement = (Element) startElement.getElementsByTagName("text").item(0);

                        String id = idElement.getFirstChild().getNodeValue();
                        String date = dateElement.getFirstChild().getNodeValue();
                        String text = textElement.getFirstChild().getNodeValue();

                        ServiceData data = new ServiceData(id, date, text);
                        serviceData.add(data);
                    }
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            LogUtils.error(LOG_TAG, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.error(LOG_TAG, e.getMessage());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            LogUtils.error(LOG_TAG, e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            LogUtils.error(LOG_TAG, e.getMessage());
        }
        return serviceData;
    }
}
