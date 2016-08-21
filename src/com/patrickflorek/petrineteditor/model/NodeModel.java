package com.patrickflorek.petrineteditor.model;

import java.awt.Point;

/**
 * Base-Class for all node in the {@link com.patrickflorek.petrineteditor.model.PetrinetModel}.
 * {@link PlaceModel} and {@link com.patrickflorek.petrineteditor.model.TransitionModel} will inherit.
 *
 * @see PetrinetModel
 * @see PlaceModel
 * @see TransitionModel
 */
public class NodeModel {
    String id;
    String name;
    Point position;

    public NodeModel() {
        this.id = "";
    }

    /**
     * @param id
     */
    public NodeModel(String id) {
        this.id = id;
    }

    /**
     * @param id
     * @param name
     * @param position {@link Point}
     */
    public NodeModel(String id, String name, Point position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    /**
     * @return
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param x
     * @param y
     */
    public void setPosition(String x, String y) {
        this.position = new Point((int) Double.parseDouble(x), (int) Double.parseDouble(y));
    }

    /**
     * @return Point
     */
    public Point getPosition() {
        return this.position;
    }

    /**
     * @param position
     */
    public void setPosition(Point position) {
        this.position = position;
    }
}
