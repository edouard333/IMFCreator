package com.phenix.imfcreator;

import java.io.File;
import java.util.ArrayList;
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

  public AssetMap(IMF imf, File fichier_xml, UUID uuid, ArrayList<File> liste_fichier) throws ParserConfigurationException, TransformerException {
    Document document = IMF.genererDocumentXML(new GenererXML() {
      @Override
      public void genererXML(Document document) {
        // création de l'Element racine
        Element asset_map = document.createElement("AssetMap");
        asset_map.setAttribute("xmlns", "http://www.smpte-ra.org/schemas/429-9/2007/AM");
        document.appendChild(asset_map);

        asset_map.appendChild(IMF.addChild(document, "Id", "urn:uuid:" + uuid.toString()));

        asset_map.appendChild(IMF.addChild(document, "AnnotationText", imf.package_name));

        asset_map.appendChild(IMF.addChild(document, "Creator", imf.creator));

        asset_map.appendChild(IMF.addChild(document, "VolumeCount", "1"));

        asset_map.appendChild(IMF.addChild(document, "IssueDate", imf.issue_date));

        asset_map.appendChild(IMF.addChild(document, "Issuer", imf.issuer));

        Element asset_list = document.createElement("AssetList");

        Element asset;
        Element chunk_list;
        Element chunk;

        // Parcoure tous les éléments que dois avoir l'AssetMap.
        for (int i = 0; i < liste_fichier.size(); i++) {
          asset = document.createElement("Asset");

          chunk_list = document.createElement("ChunkList");

          chunk = document.createElement("Chunk");

          // Si c'est une PKL on le signal :
          if (liste_fichier.get(i).getName().startsWith("PKL_")) {
            chunk.appendChild(IMF.addChild(document, "PackingList", "true"));
          }

          chunk.appendChild(IMF.addChild(document, "Path", liste_fichier.get(i).getName()));
          chunk.appendChild(IMF.addChild(document, "VolumeIndex", "1"));
          chunk.appendChild(IMF.addChild(document, "Offset", "0"));
          chunk.appendChild(IMF.addChild(document, "Length", "" + liste_fichier.get(i).length()));

          chunk_list.appendChild(chunk);

          asset.appendChild(chunk_list);

          asset_list.appendChild(asset);
        }

        asset_map.appendChild(asset_list);
      }
    });

    IMF.genererXMLFromDocument(document, fichier_xml);
  }
}
