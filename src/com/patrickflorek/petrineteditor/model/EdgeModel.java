package com.patrickflorek.petrineteditor.model;

/**
 * EdgeModel {@link com.patrickflorek.petrineteditor.model.PetrinetModel} connects source node to target node.
 *
 * @see PetrinetModel
 * @see PlaceModel
 * @see TransitionModel
 */
public class EdgeModel {
    String id;
    NodeModel source;
    NodeModel target;

    public EdgeModel(String id) {
        this.id = id;
    }

    public EdgeModel(String id, NodeModel source, NodeModel target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NodeModel getSource() {
        return this.source;
    }

    public void setSource(NodeModel source) {
        this.source = source;
    }

    public NodeModel getTarget() {
        return this.target;
    }

    public void setTarget(NodeModel target) {
        this.target = target;
    }
}
