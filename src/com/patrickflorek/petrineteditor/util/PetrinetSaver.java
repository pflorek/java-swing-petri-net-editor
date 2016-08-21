package com.patrickflorek.petrineteditor.util;

import com.patrickflorek.petrineteditor.vendor.PNMLWriter;
import com.patrickflorek.petrineteditor.model.NodeModel;
import com.patrickflorek.petrineteditor.model.EdgeModel;
import com.patrickflorek.petrineteditor.model.PetrinetModel;
import com.patrickflorek.petrineteditor.model.PlaceModel;
import com.patrickflorek.petrineteditor.model.TransitionModel;


import java.io.File;

/**
 * @see PNMLWirter
 * @see PetrinetModel
 */
public class PetrinetSaver {

    private File file;

    public PetrinetSaver(File file) {
        this.file = file;
    }

    public final void save(PetrinetModel petrinetModel) {
        PNMLWriter pnmlWriter = new PNMLWriter(file);

        pnmlWriter.startXMLDocument();

        for (TransitionModel transition : petrinetModel.getTransitions()) {
            pnmlWriter.addTransition(transition.getId(), transition.getName(), String.valueOf(transition.getPosition().getX()), String.valueOf(transition.getPosition().getY()));

            System.out.println("Transition mit id " + transition.getId() + ", name " + transition.getName() +
                    ", x " + String.valueOf(transition.getPosition().getX()) + ", y " + String.valueOf(transition.getPosition().getY()) + " wird geschrieben.");
        }

        for (PlaceModel place : petrinetModel.getPlaces()) {
            pnmlWriter.addPlace(place.getId(), place.getName(), String.valueOf(place.getPosition().getX()), String.valueOf(place.getPosition().getY()), "0");

            System.out.println("Place mit id " + place.getId() + ", name " + place.getName() +
                    ", x " + String.valueOf(place.getPosition().getX()) + ", y " + String.valueOf(place.getPosition().getY()) + " wird geschrieben.");
        }

        for (EdgeModel edge : petrinetModel.getEdges()) {
            pnmlWriter.addArc(edge.getId(), edge.getSource().getId(), edge.getTarget().getId());

            System.out.println("Edge mit id " + edge.getId() + ", source " + edge.getSource().getId() + ", target " + edge.getTarget().getId());
        }

        pnmlWriter.finishXMLDocument();
    }
}
