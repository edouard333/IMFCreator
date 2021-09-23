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
public class VolIndex {

  public VolIndex(File fichier_xml) throws ParserConfigurationException, TransformerException {
    Document document = IMF.genererDocumentXML(new GenererXML() {
      @Override
      public void genererXML(Document document) {
        // création de l'Element racine
        Element volume_index = document.createElement("VolumeIndex");
        volume_index.setAttribute("xmlns", "http://www.smpte-ra.org/schemas/429-9/2007/AM");
        document.appendChild(volume_index);

        volume_index.appendChild(IMF.addChild(document, "Index", "1"));
      }
    });

    IMF.genererXMLFromDocument(document, fichier_xml);
  }
}
