package com.phenix.imfcreator;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
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

  /**
   *
   * @param dossier_destination Où doit être l'IMF.
   * @param package_name Nom du package IMF.
   * @param liste_fichier_image Liste des fichiers images dnas l'IMF.
   * @param liste_fichier_audio Listes des fichiers audios dans l'IMF.
   */
  public IMF(File dossier_destination, String package_name, ArrayList<String> liste_fichier_image, ArrayList<String> liste_fichier_audio) {

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
        //Files.copy(new File(liste_fichier_image.get(i)).toPath(), dossier_imf + File.separator + new File(liste_fichier_image.get(i)).getName(), null);
      }

      for (int i = 0; i < liste_fichier_audio.size(); i++) {
        //Files.copy(new File(liste_fichier_audio.get(i)).toPath(), dossier_imf + File.separator + new File(liste_fichier_audio.get(i)).getName(), null);
      }

      String nom_asset_map = "ASSETMAP.xml";
      String nom_cpl = "CPL_XXX.xml";
      String nom_pkl = "PKL_XXX.xml";
      String nom_opl = "OPL_XXX.xml";
      String nom_vol_index = "VOLINDEX.xml";

      AssetMap asset_map = new AssetMap(new File(dossier_imf + nom_asset_map));

      CPL cpl = new CPL(
              new File(dossier_imf + nom_cpl),
              liste_fichier_image,
              liste_fichier_audio
      );

      PKL pkl = new PKL(new File(dossier_imf + nom_pkl));

      OPL opl = new OPL(new File(dossier_imf + nom_opl));

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
    transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

    //formatage
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

    //sortie
    transformer.transform(new DOMSource(document), new StreamResult(fichier_xml));
  }
}
