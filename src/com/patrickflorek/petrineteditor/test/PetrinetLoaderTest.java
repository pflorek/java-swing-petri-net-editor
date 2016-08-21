package com.patrickflorek.petrineteditor.test;

import com.patrickflorek.petrineteditor.util.PetrinetLoader;
import com.patrickflorek.petrineteditor.util.PetrinetSaver;
import com.patrickflorek.petrineteditor.model.PetrinetModel;

import java.io.File;
import java.util.Arrays;

public class PetrinetLoaderTest {
    public static void main(final String[] args) {
        if (args.length > 0) {
            File source = new File(args[0]);
            if (source.exists()) {
                PetrinetLoader loader = new PetrinetLoader(source);
                loader.initLoader();
                PetrinetModel petrinetModel = loader.load();

                File target = new File(source.getPath() + "_");

                PetrinetSaver saver = new PetrinetSaver(target);
                saver.save(petrinetModel);

            } else {
                System.err.println("Die Datei " + source.getAbsolutePath()
                        + " wurde nicht gefunden!");
            }
        } else {
            System.out.println("Bitte eine Datei als Parameter angeben!");
        }
    }
}
