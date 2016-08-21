package com.patrickflorek.petrineteditor.model;

import java.awt.Point;
import java.util.ArrayList;

import com.patrickflorek.petrineteditor.model.PlaceModel;

/**
 * The transistion in the {@link com.patrickflorek.petrineteditor.model.PetrinetModel}.
 * Inherits from {@link com.patrickflorek.petrineteditor.model.NodeModel}.
 *
 * @see PetrinetModel
 * @see NodeModel
 * @see PlaceModel
 */
public class TransitionModel extends NodeModel {
    private ArrayList<PlaceModel> incoming = new ArrayList<PlaceModel>();
    private ArrayList<PlaceModel> outgoing = new ArrayList<PlaceModel>();

    public TransitionModel() {
        super();
    }

    public TransitionModel(String id) {
        super(id);
    }

    public TransitionModel(String id, String name, Point position) {
        super(id, name, position);
    }

    public void addIncoming(PlaceModel place) {
        incoming.add(place);
    }

    public void removeIncoming(PlaceModel place) {
        incoming.remove(place);
    }

    public PlaceModel[] getIncoming() {
        return incoming.toArray(new PlaceModel[incoming.size()]);
    }

    public void addOutgoing(PlaceModel place) {
        outgoing.add(place);
    }

    public void removeOutgoing(PlaceModel place) {
        outgoing.remove(place);
    }

    public PlaceModel[] getOutgoing() {
        return outgoing.toArray(new PlaceModel[outgoing.size()]);
    }

    public Boolean isActive() {
        Boolean active = true;
        for (PlaceModel place : incoming) {
            if (place.getToken() == 0) {
                active = false;
            }
        }
        return active;
    }

    /**
     * @see #isActive()
     */
    public void operate() {
        if (isActive()) {
            for (PlaceModel place : incoming) {
                place.setToken(place.getToken() - 1);
            }
            for (PlaceModel place : outgoing) {
                place.setToken(place.getToken() + 1);
            }
        }
    }
}
