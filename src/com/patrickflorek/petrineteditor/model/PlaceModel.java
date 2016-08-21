package com.patrickflorek.petrineteditor.model;

import com.patrickflorek.petrineteditor.model.NodeModel;

import java.awt.Point;

import com.patrickflorek.petrineteditor.model.TransitionModel;

import java.util.ArrayList;

/**
 * The place in the {@link com.patrickflorek.petrineteditor.model.PetrinetModel}.
 * Inherits from {@link com.patrickflorek.petrineteditor.model.NodeModel}
 *
 * @see PetrinetModel
 * @see NodeModel
 * @see TransitionModel
 */
public class PlaceModel extends NodeModel {
    Integer token;
    private ArrayList<TransitionModel> incoming = new ArrayList<TransitionModel>();
    private ArrayList<TransitionModel> outgoing = new ArrayList<TransitionModel>();

    public PlaceModel() {
        super();
    }

    public PlaceModel(String id) {
        super(id);
    }

    public PlaceModel(String id, String name, Point position) {
        super(id, name, position);
    }

    public PlaceModel(String id, String name, Point position, Integer token) {
        super(id, name, position);
        this.token = token;
    }

    /**
     * @param trans
     */
    public void addIncoming(TransitionModel trans) {
        incoming.add(trans);
    }

    /**
     * @param trans
     */
    public void removeIncoming(TransitionModel trans) {
        incoming.remove(trans);
    }

    /**
     * @return
     */
    public TransitionModel[] getIncoming() {
        return incoming.toArray(new TransitionModel[incoming.size()]);
    }


    /**
     * @param trans
     */
    public void addOutgoing(TransitionModel trans) {
        outgoing.add(trans);
    }


    /**
     * @param trans
     */
    public void removeOutgoing(TransitionModel trans) {
        outgoing.remove(trans);
    }

    /**
     * @return
     */
    public TransitionModel[] getOutgoing() {
        return outgoing.toArray(new TransitionModel[outgoing.size()]);
    }

    /**
     * @return
     */
    public Integer getToken() {
        return this.token;
    }

    /**
     * @param token
     */
    public void setToken(Integer token) {
        this.token = token;
    }
}