package ru.alxant.worker.model;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.alxant.worker.view.StartDayPage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alxan on 02.02.2017.
 */
public class WorkXML {

    private static final Logger log = Logger.getLogger(WorkXML.class);

    public static String[] servicesWork(String xmlName)  {
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();

        }
        Document document = null;
        try {
            document = documentBuilder.parse(xmlName);
        } catch (SAXException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        Node root = document.getDocumentElement();
        NodeList services = root.getChildNodes();
        ArrayList<String> listTmp = new ArrayList<String>();


        for (int i = 0; i < services.getLength(); i++) {
            Node service = services.item(i);
            if (service.getNodeType() != Node.TEXT_NODE) {
                NodeList serviceProps = service.getChildNodes();
                for (int j = 0; j < serviceProps.getLength(); j++) {
                    Node serviceProp = serviceProps.item(j);

                    if (serviceProp.getNodeType() != Node.TEXT_NODE) {
                        listTmp.add(serviceProp.getChildNodes().item(0).getTextContent());
                    }
                }
            }
        }

        String[] result = new String[listTmp.size()];
        for(int i=0;i<listTmp.size();i++){
            result[i] = listTmp.get(i);
        }
        return result;
    }
}
