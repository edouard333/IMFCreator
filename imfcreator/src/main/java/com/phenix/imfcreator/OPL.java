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
public class OPL {

  private File fichier_xml;

  public OPL(IMF imf, File fichier_xml, UUID uuid, CPL cpl) throws ParserConfigurationException, TransformerException {
    this.fichier_xml = fichier_xml;
    Document document = IMF.genererDocumentXML(new GenererXML() {
      @Override
      public void genererXML(Document document) {
        // création de l'Element racine
        Element output_profile_list = document.createElement("OutputProfileList");
        output_profile_list.setAttribute("xmlns", "http://www.smpte-ra.org/schemas/2067-100/2014");
        document.appendChild(output_profile_list);

        output_profile_list.appendChild(IMF.addChild(document, "Id", "urn:uuid:" + uuid.toString()));
        output_profile_list.appendChild(IMF.addChild(document, "Annotation", "urn:uuid:" + imf.package_name));
        output_profile_list.appendChild(IMF.addChild(document, "IssueDate", "urn:uuid:" + imf.issue_date));
        output_profile_list.appendChild(IMF.addChild(document, "Issuer", "urn:uuid:" + imf.issuer));
        output_profile_list.appendChild(IMF.addChild(document, "Creator", "urn:uuid:" + imf.creator));
        output_profile_list.appendChild(IMF.addChild(document, "CompositionPlaylistId", "urn:uuid:" + cpl.getUUID().toString()));
        output_profile_list.appendChild(IMF.addChild(document, "AliasList", ""));

        Element macro_list = document.createElement("MacroList");

        Element macro = document.createElement("Macro");
        macro.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        macro.setAttribute("xsi:type", "PresetMacroType");

        macro.appendChild(IMF.addChild(document, "Name", "empty"));
        macro.appendChild(IMF.addChild(document, "Annotation", "empty"));
        macro.appendChild(IMF.addChild(document, "Preset", "http://uri"));

        macro_list.appendChild(macro);

        output_profile_list.appendChild(macro_list);
      }
    });

    IMF.genererXMLFromDocument(document, fichier_xml);
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
