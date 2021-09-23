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
public class PKL {

  private File fichier_xml;

  public PKL(IMF imf, File fichier_xml, UUID uuid, OPL opl, CPL cpl) throws ParserConfigurationException, TransformerException {
    this.fichier_xml = fichier_xml;
    Document document = IMF.genererDocumentXML(new GenererXML() {
      @Override
      public void genererXML(Document document) {
        // création de l'Element racine
        Element packing_list = document.createElement("PackingList");
        packing_list.setAttribute("xmlns", "http://www.smpte-ra.org/schemas/2067-2/2016/PKL");
        document.appendChild(packing_list);

        packing_list.appendChild(IMF.addChild(document, "Id", uuid.toString()));
        packing_list.appendChild(IMF.addChild(document, "AnnotationText", imf.package_name));
        packing_list.appendChild(IMF.addChild(document, "IssueDate", imf.issue_date));
        packing_list.appendChild(IMF.addChild(document, "Issuer", imf.issuer));
        packing_list.appendChild(IMF.addChild(document, "Creator", imf.creator));

        Element asset_list = document.createElement("AssetList");

        // Liste d'Asset :
        // OPL + CPL
        asset_list.appendChild(getAsset(document, opl.getFile()));
        asset_list.appendChild(getAsset(document, cpl.getFile()));

        packing_list.appendChild(asset_list);
      }
    });

    IMF.genererXMLFromDocument(document, fichier_xml);
  }

  /**
   *
   * @param document
   * @param fichier
   * @return
   */
  private Element getAsset(Document document, File fichier) {
    Element asset = document.createElement("AssetList");

    asset.appendChild(IMF.addChild(document, "Id", "urn:uuid:"));
    asset.appendChild(IMF.addChild(document, "AnnotationText", fichier.getName()));
    try {
      asset.appendChild(IMF.addChild(document, "Hash", IMF.hashSha1Base64(fichier)));
    } catch (Exception exception) {
      asset.appendChild(IMF.addChild(document, "Hash", "error"));
      exception.printStackTrace();
    }
    asset.appendChild(IMF.addChild(document, "Size", "" + fichier.length()));
    asset.appendChild(IMF.addChild(document, "Type", "text/xml"));
    asset.appendChild(IMF.addChild(document, "OriginalFileName", fichier.getName()));
    asset.appendChild(IMF.addChildWithAttribut(document, "HashAlgorithm", "Algorithm", "http://www.w3.org/2000/09/xmldsig#sha1"));
    return asset;
  }

  /**
   * Retourne le fichier xml de l'OPL.
   *
   * @return Fichier xml.
   */
  public File getFile() {
    return this.fichier_xml;
  }
}
