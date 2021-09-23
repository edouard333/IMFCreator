package com.phenix.imfcreator;

import java.io.File;
import java.util.UUID;

/**
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class Media {

  public File fichier;

  public int duree;

  /**
   * "48000 1" ou "24 1" par exemple.
   */
  public String edit_rate;

  public int in_media;

  public int out_media;

  public int in_timeline;

  public int duree_cpl;

  public UUID uuid;

  /**
   * Hash en Base 64 avec SHA1.
   */
  public String hash;
}
