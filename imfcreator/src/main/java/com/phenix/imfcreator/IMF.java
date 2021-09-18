package com.phenix.imfcreator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class IMF {

    public String issue_data;
    
    public String creator;
    
    public String issuer;
    
    public String package_name;
    
  /**
   *
   * @param dossier_destination Où doit être l'IMF.
   * @param package_name Nom du package IMF.
   * @param liste_fichier_image Liste des fichiers images dnas l'IMF.
   * @param liste_fichier_audio Listes des fichiers audios dans l'IMF.
   */
  public IMF(File dossier_destination, String package_name, ArrayList<Image> liste_fichier_image, ArrayList<Audio> liste_fichier_audio) {
    this.package_name= package_name;
    try {
      // 1) Du montage, in/out des éléments images, on transcode en JPEG2000 (ffmpeg ...) !
      // 2) On met les fichiers dans un dossier temporaire :
      // 3) On récupère les métadonnées des fichiers (via MediaInfo ...) :
      // 4) On fait le hash des fichiers :
      // 5) On finalise l'IMF :
      String dossier_imf = dossier_destination.getAbsolutePath() + File.separator + package_name + File.separator;

      // On créé le dossier IMF :
      if (!new File(dossier_imf).mkdir()) {
        System.out.println("Dossier a créer : " + dossier_imf);
        throw new Exception("Le dossier de l'IMF n'a pas pu être créé.");
      }

      // On copie les fichiers image et audio transcodé :
      for (int i = 0; i < liste_fichier_image.size(); i++) {
        Files.copy(liste_fichier_image.get(i).fichier.toPath(), new File(dossier_imf + File.separator + liste_fichier_image.get(i).fichier.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
      }

      for (int i = 0; i < liste_fichier_audio.size(); i++) {
        Files.copy(liste_fichier_audio.get(i).fichier.toPath(), new File(dossier_imf + File.separator + liste_fichier_audio.get(i).fichier.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
      }

      UUID uuid_asset_map = UUID.randomUUID();
      UUID uuid_cpl = UUID.randomUUID();
      UUID uuid_opl = UUID.randomUUID();
      UUID uuid_pkl = UUID.randomUUID();
        
      String nom_asset_map = "ASSETMAP.xml";
      String nom_cpl = "CPL_"+uuid_cpl+".xml";
      String nom_pkl = "PKL_"+uuid_pkl+".xml";
      String nom_opl = "OPL_"+uuid_opl+".xml";
      String nom_vol_index = "VOLINDEX.xml";

      this.issue_data= Date.from(Instant.now()).toGMTString();
      this.creator= "IMF Jeanjean 1.0.0";
      this.issuer= "IMF Jeanjean";
      
      AssetMap asset_map = new AssetMap(this, new File(dossier_imf + nom_asset_map), uuid_asset_map);

      CPL cpl = new CPL(
              new File(dossier_imf + nom_cpl),
              liste_fichier_image,
              liste_fichier_audio,
              uuid_cpl
      );

      OPL opl = new OPL(new File(dossier_imf + nom_opl), uuid_opl);
      
      PKL pkl = new PKL(new File(dossier_imf + nom_pkl), uuid_pkl);

      VolIndex vol_index = new VolIndex(new File(dossier_imf + nom_vol_index));

    } catch (Exception exception) {
      exception.printStackTrace();
    }

  }

  public static Document genererDocumentXML(GenererXML xml) throws ParserConfigurationException {
    Element racine;

    // création d'un Document
    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

    xml.genererXML(document);

    return document;
  }

  public static void genererXMLFromDocument(Document document, File fichier_xml) throws TransformerException {
    // Ecrit l'XML.
    Transformer transformer = TransformerFactory.newInstance().newTransformer();

    //prologue
    transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty(OutputKeys.STANDALONE,  "no");

    //formatage
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

    //sortie
    document.setXmlStandalone(true);
    transformer.transform(new DOMSource(document), new StreamResult(fichier_xml));
  }
  
  public static Element addChild(Document document, String nom_node, String valeur){
    Element child = document.createElement(nom_node);
    child.setTextContent(valeur);
    
    return child;
  }
}
