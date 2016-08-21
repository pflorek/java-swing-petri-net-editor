package com.patrickflorek.petrineteditor.util;

import com.patrickflorek.petrineteditor.vendor.PNMLParser;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.patrickflorek.petrineteditor.model.NodeModel;
import com.patrickflorek.petrineteditor.model.EdgeModel;
import com.patrickflorek.petrineteditor.model.PetrinetModel;
import com.patrickflorek.petrineteditor.model.PlaceModel;
import com.patrickflorek.petrineteditor.model.TransitionModel;

/**
 * @see PNMLParser
 * @see PetrinetModel
 */
public class PetrinetLoader extends PNMLParser {

    private PetrinetModel petrinetModel = null;
    private List<List<String>> edges = new ArrayList<List<String>>();

    public PetrinetLoader(final File pnml) {
        super(pnml);
    }

    /**
     * @see PNMLParser#initParser()
     */
    public final void initLoader() {
        petrinetModel = new PetrinetModel();
        PlaceModel place = new PlaceModel();
        initParser();
    }

    public final PetrinetModel load() {
        parse();
        addEdges();
        return petrinetModel;
    }

    /**
     * @param id
     */
    public void newTransition(final String id) {
        System.out.println("Transition mit id " + id + " wurde gefunden.");
        petrinetModel.addTransition(new TransitionModel(id));
    }

    /**
     * @param id
     */
    public void newPlace(final String id) {
        System.out.println("Stelle mit id " + id + " wurde gefunden.");
        petrinetModel.addPlace(new PlaceModel(id));
    }

    /**
     * @param id
     * @param source
     * @param target
     */
    public void newArc(final String id, final String source, final String target) {
        System.out.println("Kante mit id " + id + " von " + source + " nach "
                + target + " wurde gefunden.");
        edges.add(new ArrayList<String>(Arrays.asList(id, source, target)));
    }

    /**
     * @param id
     * @param x
     * @param y
     */
    public void setPosition(final String id, final String x, final String y) {
        System.out.println("Setze die Position des Elements " + id + " auf ("
                + x + ", " + y + ")");
        NodeModel node = petrinetModel.getNodeById(id);
        node.setPosition(x, y);
    }

    /**
     * @param id
     * @param name
     */
    public void setName(final String id, final String name) {
        System.out.println("Setze den Namen des Elements " + id + " auf "
                + name);
        NodeModel node = petrinetModel.getNodeById(id);
        node.setName(name);
    }

    /**
     * @param id
     * @param marking
     */
    public void setMarking(final String id, final String marking) {
        System.out.println("Setze die Markierung des Elements " + id + " auf "
                + marking);
        PlaceModel place = petrinetModel.getPlaceById(id);
        place.setToken((int) Double.parseDouble(marking));
    }

    private void addEdges() {
        for (List<String> edge : edges) {
            NodeModel source = petrinetModel.getNodeById(edge.get(1));
            NodeModel target = petrinetModel.getNodeById(edge.get(2));
            if (source != null && target != null) {
                petrinetModel.addEdge(new EdgeModel(edge.get(0), source, target));
                System.out.println("Kante mit id " + edge.get(0) + " von " + edge.get(1) + " nach "
                        + edge.get(2) + " wurde gesetzt.");
            }
        }
        edges = new ArrayList<List<String>>();
    }
}
