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

          for (int i = 0; i < 10; i++) {
            Element essence_descriptor = document.createElement("EssenceDescriptor");
            essence_descriptor.setAttribute("xmlns:r0", "http://www.smpte-ra.org/reg/395/2014/13/1/aaf");
            essence_descriptor.setAttribute("xmlns:r1", "http://www.smpte-ra.org/reg/335/2012");
            essence_descriptor.setAttribute("xmlns:r2", "http://www.smpte-ra.org/reg/2003/2012");

            essence_descriptor.appendChild(IMF.addChild(document, "Id", "urn:uuid:" + UUID.randomUUID().toString()));

            Element rgba_descriptor = document.createElement("RGBADescriptor");

            rgba_descriptor.appendChild(IMF.addChild(document, "r1:InstanceID", "urn:uuid:" + UUID.randomUUID().toString()));

            Element sub_descriptors = document.createElement("r1:SubDescriptors");

            Element jpeg2000_sub_descriptor = document.createElement("r0:JPEG2000SubDescriptor");
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:InstanceID", "urn:uuid:" + UUID.randomUUID().toString()));
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:Rsiz", "1334"));
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:Xsiz", "3840"));
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:Ysiz", "2160"));
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:XOsiz", "0"));
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:YOsiz", "0"));
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:XTsiz", "3840"));
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:YTsiz", "2160"));
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:XTOsiz", "0"));
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:YTOsiz", "0"));
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:Csiz", "3"));

            Element picture_component_sizing = document.createElement("r1:PictureComponentSizing");

            // r2:J2KComponentSizing :
            for (int j = 0; j < 3; j++) {
              Element j2k_component_sizing = document.createElement("r2:J2KComponentSizing");

              j2k_component_sizing.appendChild(IMF.addChild(document, "r2:Ssiz", "11"));
              j2k_component_sizing.appendChild(IMF.addChild(document, "r2:XRSiz", "1"));
              j2k_component_sizing.appendChild(IMF.addChild(document, "r2:YRSiz", "1"));

              picture_component_sizing.appendChild(j2k_component_sizing);
            }

            jpeg2000_sub_descriptor.appendChild(picture_component_sizing);

            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:CodingStyleDefault", "0104000101060303000077888888888888"));
            jpeg2000_sub_descriptor.appendChild(IMF.addChild(document, "r1:QuantizationDefault", "229f1e9eea9eea9ebc96ea96ea96bc8f008f008ee2874c874c876470037003704577d277d27761"));

            Element j2c_layout = document.createElement("r1:J2CLayout");

            String[] code = new String[]{"CompRed", "CompGreen", "CompBlue", "CompNull", "CompNull", "CompNull", "CompNull", "CompNull"};
            String[] component_size = new String[]{"12", "12", "12", "0", "0", "0", "0", "0"};

            for (int j = 0; j < code.length; j++) {

              Element rba_component = document.createElement("r2:RGBAComponent");

              rba_component.appendChild(IMF.addChild(document, "r2:Code", code[j]));
              rba_component.appendChild(IMF.addChild(document, "r2:ComponentSize", component_size[j]));

              j2c_layout.appendChild(rba_component);
            }

            jpeg2000_sub_descriptor.appendChild(j2c_layout);

            rgba_descriptor.appendChild(jpeg2000_sub_descriptor);

            {
              Element phdr_metadata_track_sub_descriptor = document.createElement("r0:PHDRMetadataTrackSubDescriptor");

              phdr_metadata_track_sub_descriptor.appendChild(IMF.addChild(document, "r1:InstanceID", "urn:uuid:" + UUID.randomUUID().toString()));
              phdr_metadata_track_sub_descriptor.appendChild(IMF.addChild(document, "r1:PHDRMetadataTrackSubDescriptor_DataDefinition", "urn:smpte:ul:060e2b34.04010105.0e090607.01010101"));
              phdr_metadata_track_sub_descriptor.appendChild(IMF.addChild(document, "r1:PHDRMetadataTrackSubDescriptor_SourceTrackID", "3"));
              phdr_metadata_track_sub_descriptor.appendChild(IMF.addChild(document, "r1:PHDRMetadataTrackSubDescriptor_SimplePayloadSID", "2"));

              rgba_descriptor.appendChild(phdr_metadata_track_sub_descriptor);
            }

            rgba_descriptor.appendChild(sub_descriptors);

            rgba_descriptor.appendChild(IMF.addChild(document, "r1:LinkedTrackID", "2"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:SampleRate", "24/1"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:EssenceLength", "72544"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:ContainerFormat", "urn:smpte:ul:060e2b34.0401010d.0d010301.020c0600<!--MXFGCP1FrameWrappedPictureElement-->"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:FrameLayout", "FullFrame"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:StoredWidth", "3840"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:StoredHeight", "2160"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:SampledWidth", "3840"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:SampledHeight", "2160"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:SampledXOffset", "0"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:SampledYOffset", "0"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:DisplayHeight", "2160"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:DisplayWidth", "3840"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:DisplayXOffset", "0"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:DisplayYOffset", "0"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:DisplayF2Offset", "0"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:ImageAspectRatio", "3840/2160"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:TransferCharacteristic", "urn:smpte:ul:060e2b34.0401010d.04010101.010a0000<!--TransferCharacteristic_SMPTEST2084-->"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:PictureCompression", "urn:smpte:ul:060e2b34.0401010d.04010202.03010312"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:ColorPrimaries", "urn:smpte:ul:060e2b34.0401010d.04010101.03060000<!--ColorPrimaries_P3D65-->"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:ActiveWidth", "3840"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:ActiveHeight", "2160"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:ActiveXOffset", "0"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:ActiveYOffset", "0"));

            Element video_line_map = document.createElement("r1:VideoLineMap");

            video_line_map.appendChild(IMF.addChild(document, "r2:Int32", "42"));
            video_line_map.appendChild(IMF.addChild(document, "r2:Int32", "0"));

            rgba_descriptor.appendChild(video_line_map);

            Element mastering_display_primaries = document.createElement("r1:MasteringDisplayPrimaries");

            String[] x = new String[]{"34000", "13250", "7500"};
            String[] y = new String[]{"16000", "34500", "3000"};

            for (int j = 0; j < x.length; j++) {
              Element color_primary = document.createElement("r2:ColorPrimary");

              color_primary.appendChild(IMF.addChild(document, "r2:X", x[j]));
              color_primary.appendChild(IMF.addChild(document, "r2:Y", x[j]));

              mastering_display_primaries.appendChild(color_primary);
            }

            rgba_descriptor.appendChild(mastering_display_primaries);

            Element mastering_display_white_point_chromaticity = document.createElement("r1:MasteringDisplayWhitePointChromaticity");

            mastering_display_white_point_chromaticity.appendChild(IMF.addChild(document, "r2:X", "15635"));
            mastering_display_white_point_chromaticity.appendChild(IMF.addChild(document, "r2:Y", "16450"));

            rgba_descriptor.appendChild(mastering_display_white_point_chromaticity);

            rgba_descriptor.appendChild(IMF.addChild(document, "r1:MasteringDisplayMaximumLuminance", "10000000"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:MasteringDisplayMinimumLuminance", "1"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:ComponentMaxRef", "4095"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:ComponentMinRef", "0"));
            rgba_descriptor.appendChild(IMF.addChild(document, "r1:ScanningDirection", "ScanningDirection_LeftToRightTopToBottom"));

            Element pixel_layout = document.createElement("r1:PixelLayout");

            for (int j = 0; j < code.length; j++) {
              Element rgba_component = document.createElement("r2:RGBAComponent");

              rgba_component.appendChild(IMF.addChild(document, "r2:Code", code[j]));
              rgba_component.appendChild(IMF.addChild(document, "r2:ComponentSize", component_size[j]));

              pixel_layout.appendChild(rgba_component);
            }
            rgba_descriptor.appendChild(pixel_layout);

            essence_descriptor.appendChild(rgba_descriptor);

            essence_descriptor_list.appendChild(essence_descriptor);
          }

          for (int i = 0; i < 2; i++) {
            Element essence_descriptor = document.createElement("EssenceDescriptor");

            essence_descriptor.setAttribute("xmlns:r0", "http://www.smpte-ra.org/reg/395/2014/13/1/aaf");
            essence_descriptor.setAttribute("xmlns:r1", "http://www.smpte-ra.org/reg/335/2012");
            essence_descriptor.setAttribute("xmlns:r2", "http://www.smpte-ra.org/reg/2003/2012");

            essence_descriptor.appendChild(IMF.addChild(document, "Id", "urn:uuid:" + UUID.randomUUID().toString()));

            Element wave_pcm_descriptor = document.createElement("r0:WAVEPCMDescriptor");

            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:InstanceID", "urn:uuid:" + UUID.randomUUID().toString()));

            // r1:SubDescriptors : @TODO
            {
              // @TODO
            }

            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:LinkedTrackID", "2"));
            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:SampleRate", "48000/1"));
            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:EssenceLength", "145088000"));
            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:ContainerFormat", "urn:smpte:ul:060e2b34.04010101.0d010301.02060200<!--MXFGCClipWrappedBroadcastWaveAudioData-->"));
            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:AudioSampleRate", "48000/1"));
            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:Locked", "True"));
            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:ChannelCount", "2"));
            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:QuantizationBits", "24"));
            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:BlockAlign", "6"));
            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:AverageBytesPerSecond", "288000"));
            wave_pcm_descriptor.appendChild(IMF.addChild(document, "r1:ChannelAssignment", "urn:smpte:ul:060e2b34.0401010d.04020210.04010000"));

            essence_descriptor.appendChild(wave_pcm_descriptor);

            essence_descriptor_list.appendChild(essence_descriptor);
          }

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
