package com.patrickflorek.petrineteditor.model;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @see EdgeModel
 * @see PlaceModel
 * @see TransitionModel
 */
public class PetrinetModel {
    Collection<PlaceModel> places;
    Collection<TransitionModel> transitions;
    Collection<EdgeModel> edges;

    public PetrinetModel() {
        this.places = new ArrayList<PlaceModel>();
        this.transitions = new ArrayList<TransitionModel>();
        this.edges = new ArrayList<EdgeModel>();
    }

    public void addPlace(PlaceModel place) {
        places.add(place);
    }

    public void addTransition(TransitionModel transition) {
        transitions.add(transition);
    }

    public void addEdge(EdgeModel edge) {
        edges.add(edge);
    }

    public NodeModel getNodeById(String id) {
        NodeModel node = getPlaceById(id);
        if (node == null) {
            node = getTransitionById(id);
        }
        return node;
    }

    public PlaceModel getPlaceById(String id) {
        PlaceModel place = null;
        Iterator<PlaceModel> iterator = places.iterator();
        Boolean found = false;
        while (!found && iterator.hasNext()) {
            place = iterator.next();
            if (place.getId().equals(id)) {
                found = true;
            } else {
                place = null;
            }
        }
        return place;
    }

    public TransitionModel getTransitionById(String id) {
        TransitionModel transition = null;
        Iterator<TransitionModel> iterator = transitions.iterator();
        Boolean found = false;
        while (!found && iterator.hasNext()) {
            transition = iterator.next();
            if (transition.getId().equals(id)) {
                found = true;
            } else {
                transition = null;
            }
        }
        return transition;

    }

    public EdgeModel getEdgeById(String id) {
        EdgeModel edge = null;
        Iterator<EdgeModel> iterator = edges.iterator();
        Boolean found = false;
        while (!found && iterator.hasNext()) {
            edge = iterator.next();
            if (edge.getId().equals(id)) {
                found = true;
            } else {
                edge = null;
            }
        }
        return edge;

    }

    public PlaceModel[] getPlaces() {
        return places.toArray(new PlaceModel[places.size()]);
    }

    public TransitionModel[] getTransitions() {
        return transitions.toArray(new TransitionModel[transitions.size()]);
    }

    public EdgeModel[] getEdges() {
        return edges.toArray(new EdgeModel[edges.size()]);
    }

    public void removePlace(PlaceModel place) {
        places.remove(place);
    }

    public void removeTransition(TransitionModel transition) {
        transitions.remove(transition);
    }

    public void removeEdge(EdgeModel edge) {
        edges.remove(edge);
    }

    public String generatePlaceId() {
        Integer counter = places.size();
        while (!isUniqueId("place" + counter) && counter < 10) {
            counter = counter + 1;
            System.out.println("place" + counter);
        }
        return "place" + counter;
    }

    public String generateTransitionId() {
        Integer counter = transitions.size();
        while (!isUniqueId("transition" + counter)) {
            counter = counter + 1;
        }
        return "transition" + counter;
    }

    public String generateEdgeId() {
        Integer counter = edges.size();
        while (!isUniqueId("edge" + counter)) {
            counter = counter + 1;
        }
        return "edge" + counter;
    }

    public Boolean isUniqueId(String id) {
        TransitionModel tm = getTransitionById(id);
        PlaceModel pm = getPlaceById(id);
        EdgeModel em = getEdgeById(id);
        return ((em == null) && (tm == null) && (pm == null));
    }
}
