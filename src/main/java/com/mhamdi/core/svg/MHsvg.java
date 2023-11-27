package com.mhamdi.core.svg;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.mhamdi.core.svg.models.Layer;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class MHsvg {

    private final File file;
    private Document doc;
    private Element root;
    private SAXSVGDocumentFactory factory;

    public MHsvg(File file) {
        this.file = file;
        try {
            this.factory = new SAXSVGDocumentFactory(
                    XMLResourceDescriptor.getXMLParserClassName());
            // Parse the SVG file into a document
            this.doc = factory.createDocument(this.file.toURI().toString());
            // Get the root element of the document
            this.root = doc.getDocumentElement();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public Vector<Layer> listSvgLayers() {
        Vector<Layer> result = new Vector<Layer>();
        try {
            // Get all the elements with tag name "g", which are groups of elements that can
            // be considered as layers
            NodeList layers = this.root.getElementsByTagName("text");
            // Loop through the layers and print their id attributes, which are their names
            for (int i = 0; i < layers.getLength(); i++) {
                Layer layer = new Layer();
                Element ele = (Element) layers.item(i);
                String layerName = ele.getAttribute("id");
                layer.setId(layerName);
                String currVal = ele.getTextContent();
                layer.setVal(currVal);
                System.out.println("Layer " + (i + 1) + ": " + layerName);

                // Get the attributes of the ele element
                NamedNodeMap attrs = ele.getAttributes();
                // Loop through the attributes
                for (int j = 0; j < attrs.getLength(); j++) {
                    // Get the attribute node
                    var attr = attrs.item(j);
                    // Print the attribute name and value
                    System.out.println(attr.getNodeName() + " = " + attr.getNodeValue());
                    layer.getAttrs().put(attr.getNodeName(), attr.getNodeValue());
                }
                result.add(layer);
                System.out.println("---------------------");
            }
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        }

        return result;
    }

    public void changeTextLayer(String textLayerId, String newText) {
        try {
            Element textLayer = this.doc.getElementById(textLayerId);
            textLayer.setTextContent(newText);

            // NodeList texts = textLayer.getElementsByTagName("text");
            // for (int i = 0; i < texts.getLength(); i++) {
            // Element txtItm = (Element) texts.item(i);
            // // Check if the element exists and is a text element
            // if (txtItm != null && txtItm.getTagName().equals("text")) {
            // // Set the text content of the element to the new text
            // txtItm.setTextContent(newText);
            // }
            // }

            this.saveDocument(doc, this.file.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveDocument(File outFile)
            throws TransformerConfigurationException, TransformerException {
        // create a transformer object
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        // set the output properties for the transformer
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount",
                "2");
        // create a DOM source object with the document
        DOMSource source = new DOMSource(doc);
        // create a stream result object with the file
        StreamResult result;
        if (outFile == null) {
            result = new StreamResult(this.file);
        } else {
            result = new StreamResult(outFile);
        }
        // transform the source to the result
        transformer.transform(source, result);
    }

    public void saveDocument(Document doc, String filePath)
            throws TransformerConfigurationException, TransformerException {
        // create a transformer object
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        // set the output properties for the transformer
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // create a DOM source object with the document
        DOMSource source = new DOMSource(doc);

        // create a stream result object with the file
        StreamResult result = new StreamResult(new File(filePath));

        // transform the source to the result
        transformer.transform(source, result);
    }

}
