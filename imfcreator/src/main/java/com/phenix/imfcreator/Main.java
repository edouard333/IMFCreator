package com.phenix.imfcreator;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class Main {

  public static void main(String[] args) {
    ArrayList<Image> liste_fichier_image = new ArrayList<Image>();
    ArrayList<Audio> liste_fichier_audio = new ArrayList<Audio>();

    String pc= "C:\\Users\\Edouard\\Desktop\\Nouveau dossier";
    String mac= "/Users/mp-dailies/Desktop/genereIMF/";
    
    IMF imf = new IMF(new File(pc), "package_name", liste_fichier_image, liste_fichier_audio);

  }
}
