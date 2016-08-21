package com.patrickflorek.petrineteditor.presenter;

import com.patrickflorek.petrineteditor.ThePetrinetEditor;

import com.patrickflorek.petrineteditor.model.PetrinetModel;
import com.patrickflorek.petrineteditor.model.TransitionModel;
import com.patrickflorek.petrineteditor.model.PlaceModel;
import com.patrickflorek.petrineteditor.model.EdgeModel;
import com.patrickflorek.petrineteditor.model.NodeModel;
import com.patrickflorek.petrineteditor.view.PetrinetView;
import com.patrickflorek.petrineteditor.util.PetrinetLoader;
import com.patrickflorek.petrineteditor.util.PetrinetSaver;

import java.io.File;
import java.awt.Point;
import java.awt.Dimension;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PetrinetPresenter {
    private PetrinetModel model;
    private PetrinetView view;

    public PetrinetPresenter(PetrinetView view, PetrinetModel model) {
        this.view = view;
        this.model = model;
    }

    public PetrinetView getPetrinetView() {
        return this.view;
    }

    public void setPetrinetView(PetrinetView petrinetView) {
        this.view = petrinetView;
    }

    public void loadPetrinet(File file) {
        PetrinetLoader loader = new PetrinetLoader(file);
        loader.initLoader();
        model = loader.load();

        //Besonderheit, der Loader verknüpft nicht die Kanten, er fügt nur das EdgeModel-Objekt ein.
        for (EdgeModel edge : model.getEdges()) {
            String source = edge.getSource().getId();
            String target = edge.getTarget().getId();
            NodeModel sourceModel = model.getNodeById(source);
            NodeModel targetModel = model.getNodeById(target);
            //Füge Kante von Stelle zu Transition ein
            if (sourceModel instanceof PlaceModel) {
                PlaceModel place = (PlaceModel) sourceModel;
                TransitionModel transition = (TransitionModel) targetModel;
                place.addOutgoing(transition);
                transition.addIncoming(place);

                if (ThePetrinetEditor.DEBUG)
                    System.out.println("Verknüpfe Place: " + place.getId() + " mit Anzahl token: " + place.getToken() + " mit der Transition: " + transition.getId());
                //Füge Kante von Transition zu Stelle ein
            } else {
                TransitionModel transition = (TransitionModel) sourceModel;
                PlaceModel place = (PlaceModel) targetModel;
                transition.addOutgoing(place);
                place.addIncoming(transition);
            }
        }
    }

    public void drawPetrinet() {
        for (PlaceModel place : model.getPlaces()) {
            view.addPlace(place.getId(), place.getName(), place.getPosition(), place.getToken());
        }

        for (TransitionModel transition : model.getTransitions()) {
            view.addTransition(transition.getId(), transition.getName(), transition.getPosition());
        }

        //Besonderheit, der Loader verknüpft nicht die Kanten, er fügt nur das EdgeModel-Objekt ein.
        for (EdgeModel edge : model.getEdges()) {
            String source = edge.getSource().getId();
            String target = edge.getTarget().getId();

            view.addEdge(edge.getId(), source, target);
        }


        for (TransitionModel transition : model.getTransitions()) {
            if (transition.isActive()) {
                view.activateTransition(transition.getId());
                if (ThePetrinetEditor.DEBUG)
                    System.out.println("Setze Transition: " + transition.getId() + " auf aktiv");
            } else {
                view.deactivateTransition(transition.getId());
                if (ThePetrinetEditor.DEBUG)
                    System.out.println("Setze Transitioen: " + transition.getId() + " auf inaktiv");
            }
        }
    }

    public void savePetrinet(File file) {
        System.out.println("PetrinetPresenter Saving: " + file.getName() + ".\n");
        PetrinetSaver saver = new PetrinetSaver(file);
        saver.save(model);
    }

    public void addNewTransition(String name, Point position) {
        String id = model.generateTransitionId();
        TransitionModel transition = new TransitionModel(id, name, position);
        model.addTransition(transition);
        view.addTransition(id, name, position);
        if (transition.isActive()) {
            view.activateTransition(transition.getId());
        } else {
            view.deactivateTransition(transition.getId());
        }
    }

    public void addNewPlace(String name, Point position, Integer token) {
        String id = model.generatePlaceId();
        model.addPlace(new PlaceModel(id, name, position, token));
        view.addPlace(id, name, position, token);
    }

    public void addNewEdge(String source, String target) {
        String id = model.generateEdgeId();
        NodeModel sourceModel = model.getNodeById(source);
        NodeModel targetModel = model.getNodeById(target);
        if (sourceModel instanceof PlaceModel) {
            PlaceModel place = (PlaceModel) sourceModel;
            TransitionModel transition = (TransitionModel) targetModel;
            place.addOutgoing(transition);
            transition.addIncoming(place);
            if (transition.isActive()) {
                view.activateTransition(transition.getId());
            } else {
                view.deactivateTransition(transition.getId());
            }
        } else {
            TransitionModel transition = (TransitionModel) sourceModel;
            PlaceModel place = (PlaceModel) targetModel;
            transition.addOutgoing(place);
            place.addIncoming(transition);
        }
        model.addEdge(new EdgeModel(id, sourceModel, targetModel));
        view.addEdge(id, source, target);
        if (ThePetrinetEditor.DEBUG)
            System.out.println("New Edge from " + source + " to " + target);
    }

    public void removeEdge(String id) {
        if (ThePetrinetEditor.DEBUG)
            System.out.println("Removing edge with id: " + id);
        EdgeModel toRemove = model.getEdgeById(id);
        NodeModel sourceModel = toRemove.getSource();
        NodeModel targetModel = toRemove.getTarget();
        if (sourceModel instanceof PlaceModel) {
            PlaceModel place = (PlaceModel) sourceModel;
            TransitionModel transition = (TransitionModel) targetModel;
            place.removeOutgoing(transition);
            transition.removeIncoming(place);
            if (transition.isActive()) {
                view.activateTransition(transition.getId());
            } else {
                view.deactivateTransition(transition.getId());
            }
        } else {
            TransitionModel transition = (TransitionModel) sourceModel;
            PlaceModel place = (PlaceModel) targetModel;
            transition.removeOutgoing(place);
            place.removeIncoming(transition);
        }
        model.removeEdge(toRemove);
        view.removeEdge(id);
    }

    /**
     * @see #removeEdge(String id)
     */
    public void removePlace(String id) {
        if (ThePetrinetEditor.DEBUG)
            System.out.println("Removing place with id: " + id);
        PlaceModel toRemove = model.getPlaceById(id);
        for (EdgeModel edge : model.getEdges()) {
            if (toRemove.equals(edge.getSource())) {
                removeEdge(edge.getId());
            } else if (toRemove.equals(edge.getTarget())) {
                removeEdge(edge.getId());
            }
        }
        model.removePlace(toRemove);
        view.removePlace(id);

    }

    /**
     */
    public void removeTransition(String id) {
        TransitionModel toRemove = model.getTransitionById(id);
        for (EdgeModel edge : model.getEdges()) {
            if (toRemove.equals(edge.getSource())) {
                removeEdge(edge.getId());
            } else if (toRemove.equals(edge.getTarget())) {
                removeEdge(edge.getId());
            }
        }
        model.removeTransition(model.getTransitionById(id));
        view.removeTransition(id);
    }

    public void renameTransition(String id, String name) {
        TransitionModel transition = model.getTransitionById(id);
        transition.setName(name);
        view.renameTransition(id, name);
    }

    public void renamePlace(String id, String name) {
        PlaceModel place = model.getPlaceById(id);
        place.setName(name);

        view.renamePlace(id, name);
    }

    public void setToken(String id, Integer token) {
        PlaceModel place = model.getPlaceById(id);
        place.setToken(token);
        for (TransitionModel transition : place.getOutgoing()) {
            if (transition.isActive()) {
                view.activateTransition(transition.getId());
            } else {
                view.deactivateTransition(transition.getId());
            }
        }
        view.setToken(id, token);
    }

    private void increaseToken(String id) {
        PlaceModel place = model.getPlaceById(id);
        Integer token = place.getToken() + 1;
        place.setToken(token);
        view.setToken(id, token);
    }

    private void decreaseToken(String id) {
        PlaceModel place = model.getPlaceById(id);
        Integer token = Math.max(0, place.getToken() + 1);
        place.setToken(token);
        view.setToken(id, token);
    }

    public void moveTransition(String id, Point position) {
        if (ThePetrinetEditor.DEBUG)
            System.out.println("Move Transition " + id + "to " + position);
        TransitionModel transition = model.getTransitionById(id);
        transition.setPosition(position);
        view.moveTransition(id, position);
    }

    public void movePlace(String id, Point position) {
        if (ThePetrinetEditor.DEBUG)
            System.out.println("Move Place " + id + "to " + position);
        PlaceModel place = model.getPlaceById(id);
        place.setPosition(position);
        view.movePlace(id, position);
    }

    /**
     * @see TransitionModel#operate()
     */
    public void operate(String id) {
        TransitionModel transition = model.getTransitionById(id);
        if (transition.isActive()) {
            transition.operate();
            for (PlaceModel incoming : transition.getIncoming()) {
                view.setToken(incoming.getId(), incoming.getToken());
                for (TransitionModel outgoing : incoming.getOutgoing()) {
                    if (outgoing.isActive()) {
                        view.activateTransition(outgoing.getId());
                    } else {
                        view.deactivateTransition(outgoing.getId());
                    }
                }
            }
            for (PlaceModel outgoing : transition.getOutgoing()) {
                view.setToken(outgoing.getId(), outgoing.getToken());
                for (TransitionModel theNextOutgoing : outgoing.getOutgoing()) {
                    if (theNextOutgoing.isActive()) {
                        view.activateTransition(theNextOutgoing.getId());
                    } else {
                        view.deactivateTransition(theNextOutgoing.getId());
                    }

                }
            }
        }

    }

    public void resize(Dimension newSize) {
        view._resize(newSize);
    }

    /**
     * Switch mulitple transitions in a step
     */
    public void transitionStep(ArrayList<String> selectedTransitions) {

        Boolean stepable = true;
        //Check if all transitions are active and if we have enough tokens!!!

        Map<String, Integer> tokens = new HashMap<String, Integer>();
        ;

        for (String id : selectedTransitions) {
            TransitionModel transition = model.getTransitionById(id);
            if (transition.isActive()) {
                for (PlaceModel incoming : transition.getIncoming()) {
                    if (tokens.containsKey(incoming.getId())) { //
                        tokens.put(incoming.getId(), tokens.get(incoming.getId()) - 1);
                    } else {
                        tokens.put(incoming.getId(), incoming.getToken() - 1);
                    }
                }
            } else {
                stepable = false;
            }
        }

        for (Entry<String, Integer> entry : tokens.entrySet()) {
            stepable = stepable && (entry.getValue() >= 0);
        }


        System.out.println("stepable " + stepable);
        if (stepable) {

            for (String id : selectedTransitions) {
                operate(id);
            }


        }
    }
}
