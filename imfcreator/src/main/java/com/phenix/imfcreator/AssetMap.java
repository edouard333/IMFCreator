package com.phenix.imfcreator;

import java.io.File;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class AssetMap {

  public AssetMap(IMF imf, File fichier_xml, UUID uuid) throws ParserConfigurationException, TransformerException {
    Document document = IMF.genererDocumentXML(new GenererXML() {
      @Override
      public void genererXML(Document document) {
        // création de l'Element racine
        Element asset_map = document.createElement("AssetMap");
        asset_map.setAttribute("xmlns", "http://www.smpte-ra.org/schemas/429-9/2007/AM");
        document.appendChild(asset_map);

        asset_map.appendChild(IMF.addChild(document, "Id", "urn:uuid:"+uuid.toString()));
        
        asset_map.appendChild(IMF.addChild(document, "AnnotationText", imf.package_name));
        
        asset_map.appendChild(IMF.addChild(document, "Creator", imf.creator));
        
        asset_map.appendChild(IMF.addChild(document, "VolumeCount", "1"));
        
        asset_map.appendChild(IMF.addChild(document, "IssueDate", imf.issue_data));
        
        asset_map.appendChild(IMF.addChild(document, "Issuer", imf.issuer));
      }
    });

    IMF.genererXMLFromDocument(document, fichier_xml);
  }
}
