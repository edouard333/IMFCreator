package com.phenix.imfcreator;

import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class PKL {

  public PKL(File fichier_xml) throws ParserConfigurationException, TransformerException {
    Document document = IMF.genererDocumentXML(new GenererXML() {
      @Override
      public void genererXML(Document document) {
        // création de l'Element racine
        Element racine = document.createElement("fichier");
        document.appendChild(racine);

        Element documents = document.createElement("document");
      }
    });

    IMF.genererXMLFromDocument(document, fichier_xml);
  }
}
