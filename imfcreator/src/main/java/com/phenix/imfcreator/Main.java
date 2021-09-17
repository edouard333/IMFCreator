package com.phenix.imfcreator;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class Main {

  public static void main(String[] args) {
    ArrayList<String> liste_fichier_image = new ArrayList<String>();
    ArrayList<String> liste_fichier_audio = new ArrayList<String>();

    IMF imf = new IMF(new File("/Users/mp-dailies/Desktop/genereIMF/"), "package_name", liste_fichier_image, liste_fichier_audio);

  }
}
