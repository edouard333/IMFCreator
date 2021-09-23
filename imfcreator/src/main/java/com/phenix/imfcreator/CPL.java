package com.phenix.imfcreator;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class CPL {

  private File fichier_xml;

  private UUID uuid;

  public CPL(IMF imf, File fichier_xml, ArrayList<Image> liste_image, ArrayList<Audio> liste_audio, UUID uuid, String content_originator) throws TransformerConfigurationException, TransformerException, ParserConfigurationException {
    this.fichier_xml = fichier_xml;
    this.uuid = uuid;
    Document document = IMF.genererDocumentXML(new GenererXML() {
      @Override
      public void genererXML(Document document) {
        // création de l'Element racine
        Element cpl = document.createElement("CompositionPlaylist");
        document.appendChild(cpl);

        cpl.appendChild(IMF.addChild(document, "Id", "urn:uuid:" + uuid.toString()));
        cpl.appendChild(IMF.addChild(document, "Annotation", imf.package_name));
        cpl.appendChild(IMF.addChild(document, "IssueDate", imf.issue_date));
        cpl.appendChild(IMF.addChild(document, "Issuer", imf.issuer));
        cpl.appendChild(IMF.addChild(document, "Creator", imf.creator));
        cpl.appendChild(IMF.addChild(document, "ContentOriginator", content_originator));
        cpl.appendChild(IMF.addChild(document, "ContentTitle", imf.package_name));
        cpl.appendChild(IMF.addChild(document, "ContentKind", imf.getContentKing()));

        Element content_version_list = document.createElement("ContentVersionList");

        Element content_version = document.createElement("ContentVersion");

        content_version.appendChild(IMF.addChild(document, "Id", "urn:uuid:" + UUID.randomUUID().toString()));
        content_version.appendChild(IMF.addChild(document, "LabelText", imf.package_name));

        content_version_list.appendChild(content_version);

        cpl.appendChild(content_version_list);

        // EssenceDescriptorList
        {
          Element essence_descriptor_list = document.createElement("EssenceDescriptorList");

          cpl.appendChild(essence_descriptor_list);
        }

        // CompositionTimecode
        {
          Element composition_timecode = document.createElement("CompositionTimecode");

          composition_timecode.appendChild(IMF.addChild(document, "TimecodeDropFrame", "0"));
          composition_timecode.appendChild(IMF.addChild(document, "TimecodeRate", "24"));
          composition_timecode.appendChild(IMF.addChild(document, "TimecodeStartAddress", "00:00:00:00"));

          cpl.appendChild(composition_timecode);
        }

        cpl.appendChild(IMF.addChild(document, "EditRate", "24 1"));

        // LocaleList :
        {
          Element locale_list = document.createElement("LocaleList");

          Element locale = document.createElement("Locale");

          {
            Element language_list = document.createElement("LanguageList");

            language_list.appendChild(IMF.addChild(document, "Language", "fr"));

            locale.appendChild(language_list);
          }

          {
            Element region_list = document.createElement("RegionList");

            region_list.appendChild(IMF.addChild(document, "Region", "xg"));

            locale.appendChild(region_list);
          }

          {
            Element content_maturity_rating_list = document.createElement("ContentMaturityRatingList");

            Element content_maturity_rating = document.createElement("ContentMaturityRating");

            content_maturity_rating.appendChild(IMF.addChild(document, "Agency", "http://www.mpaa.org/2003-ratings"));
            content_maturity_rating.appendChild(IMF.addChild(document, "Rating", "NR"));

            content_maturity_rating_list.appendChild(content_maturity_rating);

            locale.appendChild(content_maturity_rating_list);
          }

          locale_list.appendChild(locale);

          cpl.appendChild(locale_list);

        }

        // ExtensionProperties :
        {
          Element extension_properties = document.createElement("ExtensionProperties");

          extension_properties.appendChild(IMF.addChildAndAttribute(
                  document,
                  "cc:ApplicationIdentification",
                  "http://www.smpte-ra.org/ns/2067-21/2020",
                  "xmlns:cc",
                  "http://www.smpte-ra.org/ns/2067-2/2020"
          ));

          cpl.appendChild(extension_properties);
        }

        // SegmentList :
        {
          Element segment_list = document.createElement("SegmentList");

          Element segment = document.createElement("Segment");

          segment.appendChild(IMF.addChild(document, "Id", "urn:uuid:" + UUID.randomUUID().toString()));

          Element sequence_list = document.createElement("SequenceList");

          // cc:MainImageSequence :
          {
            Element main_image_sequence = document.createElement("cc:MainImageSequence");
            main_image_sequence.setAttribute("xmlns:cc", "http://www.smpte-ra.org/ns/2067-2/2020");

            main_image_sequence.appendChild(IMF.addChild(document, "Id", "urn:uuid:" + UUID.randomUUID().toString()));
            main_image_sequence.appendChild(IMF.addChild(document, "TrackId", "urn:uuid:" + UUID.randomUUID().toString()));

            Element resource_list = document.createElement("ResourceList");

            for (int i = 0; i < liste_image.size(); i++) {
              resource_list.appendChild(addResource(document, liste_image.get(i)));
            }

            main_image_sequence.appendChild(resource_list);

            sequence_list.appendChild(main_image_sequence);
          }

          // cc:MainAudioSequence :
          for (int i = 0; i < liste_audio.size(); i++) {
            Element main_audio_sequence = document.createElement("cc:MainAudioSequence");
            main_audio_sequence.setAttribute("xmlns:cc", "http://www.smpte-ra.org/ns/2067-2/2020");

            main_audio_sequence.appendChild(IMF.addChild(document, "Id", "urn:uuid:" + UUID.randomUUID().toString()));
            main_audio_sequence.appendChild(IMF.addChild(document, "TrackId", "urn:uuid:" + UUID.randomUUID().toString()));

            Element resource_list = document.createElement("ResourceList");

            resource_list.appendChild(addResource(document, liste_audio.get(i)));

            main_audio_sequence.appendChild(resource_list);

            sequence_list.appendChild(main_audio_sequence);
          }

          segment.appendChild(sequence_list);

          segment_list.appendChild(segment);

          cpl.appendChild(segment_list);
        }

      }
    });

    IMF.genererXMLFromDocument(document, fichier_xml);
  }

  private Element addResource(Document document, Media media) {
    Element resource = document.createElement("Resource");
    resource.setAttribute("xsi:type", "TrackFileResourceType");

    resource.appendChild(IMF.addChild(document, "Id", "urn:uuid:" + UUID.randomUUID().toString()));
    resource.appendChild(IMF.addChild(document, "Annotation", media.fichier.getName()));
    resource.appendChild(IMF.addChild(document, "EditRate", media.edit_rate));
    resource.appendChild(IMF.addChild(document, "IntrinsicDuration", "" + media.duree));
    resource.appendChild(IMF.addChild(document, "EntryPoint", "" + media.in_media));
    resource.appendChild(IMF.addChild(document, "SourceDuration", "" + media.duree_cpl));
    resource.appendChild(IMF.addChild(document, "SourceEncoding", "urn:uuid:"));
    resource.appendChild(IMF.addChild(document, "TrackFileId", "urn:uuid:" + media.uuid.toString()));;
    resource.appendChild(IMF.addChild(document, "Hash", media.hash));
    resource.appendChild(IMF.addChildWithAttribut(document, "HashAlgorithm", "Algorithm", "http://www.w3.org/2000/09/xmldsig#sha1"));
    return resource;
  }

  /**
   * Retourne le fichier xml de la CPL.
   *
   * @return Fichier xml.
   */
  public File getFile() {
    return this.fichier_xml;
  }

  /**
   * Récupère le UUID.
   *
   * @return
   */
  public UUID getUUID() {
    return this.uuid;
  }
}
