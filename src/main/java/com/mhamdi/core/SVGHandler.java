package com.mhamdi.core;

// Import the necessary classes and packages
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.crypto.dsig.TransformException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.apache.batik.apps.rasterizer.SVGConverterException;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SVGHandler {
    public void convertToSVG(String svgFilePath) throws SVGConverterException{
      // create a new SVGConverter object
      SVGConverter converter = new SVGConverter ();
      // set the source file name
      converter.setSources (new String [] {svgFilePath});
      // set the destination file name
      converter.setDst (new File ("output.svg"));
      // execute the conversion
      converter.execute ();
    }

    public Vector<String> listSvgLayers(String svgFilePath) {
        Vector<String> result = new Vector<String>();
        try {
          // Create a document factory
          SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
          
          // Parse the SVG file into a document
          Document doc = factory.createDocument(new File(svgFilePath).toURI().toString());
          
          // Get the root element of the document
          Element root = doc.getDocumentElement();
          
          // Get all the elements with tag name "g", which are groups of elements that can be considered as layers
          NodeList layers = root.getElementsByTagName("g");
          
          // Loop through the layers and print their id attributes, which are their names
          for (int i = 0; i < layers.getLength(); i++) {
            Element layer = (Element) layers.item(i);
            String layerName = layer.getAttribute("id");
            System.out.println("Layer " + (i + 1) + ": " + layerName);
            result.add(layerName);
          }
        } catch (IOException e) {
          // Handle the exception
          e.printStackTrace();
        }

        return result;
      }
    
    // Define a function that takes an SVG file path, a text layer id, and a new text as parameters
    public void changeTextLayer(String svgFilePath, String outFilePath, String textLayerId, String newText) {
        try {
            // Create a document factory
            SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
            
            // Parse the SVG file into a document
            Document doc = factory.createDocument(new File(svgFilePath).toURI().toString());
            
            // Get the root element of the document
            Element root = doc.getDocumentElement();
            
            // Get the element with the given id attribute, which is the text layer
            Element textLayer = doc.getElementById(textLayerId);
            NodeList texts = textLayer.getElementsByTagName("text");
            for (int i = 0; i < texts.getLength(); i++) {
              Element txtItm = (Element) texts.item(i);
              // Check if the element exists and is a text element
              if (txtItm != null && txtItm.getTagName().equals("text")) {
                  // Set the text content of the element to the new text
                  txtItm.setTextContent(newText);                  
              }
            }  
            
            
          // Save the modified document to the same file
          // You can use other methods to save the document, such as using an SVG generator or a transformer
        //   this.saveDocument(doc, "C:\\Users\\mahmo\\OneDrive\\Documents\\Desktop\\saved.svg");
          this.saveDocument(doc, outFilePath);
        } catch (IOException e) {
        // Handle the exception
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (TransformerException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }

    public void saveDocument(Document doc, String filePath) throws TransformerConfigurationException, TransformerException{
      // create a transformer object
      Transformer transformer = TransformerFactory.newInstance ().newTransformer ();

      // set the output properties for the transformer
      transformer.setOutputProperty (OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty (OutputKeys.INDENT, "yes");
      transformer.setOutputProperty ("{http://xml.apache.org/xslt}indent-amount", "2");

      // create a DOM source object with the document
      DOMSource source = new DOMSource (doc);

      // create a stream result object with the file
      StreamResult result = new StreamResult (new File (filePath));

      // transform the source to the result
      transformer.transform (source, result);
    }

}
