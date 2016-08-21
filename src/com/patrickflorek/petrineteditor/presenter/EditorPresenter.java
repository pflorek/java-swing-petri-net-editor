package com.patrickflorek.petrineteditor.presenter;

import com.patrickflorek.petrineteditor.view.EditorView;
import com.patrickflorek.petrineteditor.view.PetrinetView;
import com.patrickflorek.petrineteditor.model.PetrinetModel;
import com.patrickflorek.petrineteditor.presenter.PetrinetPresenter;

import java.io.File;
import java.awt.Dimension;

public class EditorPresenter {
    private EditorView view;

    public EditorPresenter(EditorView view) {
        this.view = view;
        newPetrinet();
    }

    public void newPetrinet() {
        PetrinetView petrinetView = new PetrinetView();
        PetrinetPresenter petrinetPresenter = new PetrinetPresenter(petrinetView, new PetrinetModel());
        petrinetView.setPresenter(petrinetPresenter);
        view.addPetrinetView(petrinetView);
    }

    /**
     * @param file
     */
    public void openPetrinet(File file) {
        PetrinetView petrinetView = new PetrinetView();
        PetrinetPresenter petrinetPresenter = new PetrinetPresenter(petrinetView, new PetrinetModel());
        petrinetView.setPresenter(petrinetPresenter);
        petrinetPresenter.loadPetrinet(file);
        petrinetPresenter.drawPetrinet();
        petrinetView._resize(petrinetView.calculateSize());
        view.addPetrinetView(petrinetView);
    }

    /**
     * @param file
     * @petrinetView
     */
    public void savePetrinet(PetrinetView petrinetView, File file) {
        petrinetView.save(file);
    }

    public void refreshPetrinet(PetrinetView petrinetView) {
        PetrinetView freshPetrinetView = new PetrinetView();
        PetrinetPresenter petrinetPresenter = petrinetView.getPresenter();
        petrinetPresenter.setPetrinetView(freshPetrinetView);
        freshPetrinetView._resize(petrinetView._getSize());
        freshPetrinetView.setPresenter(petrinetPresenter);
        petrinetPresenter.drawPetrinet();
        view.addPetrinetView(freshPetrinetView);
    }

    public void resizePetrinet(PetrinetView petrinetView, Dimension newSize) {
        PetrinetPresenter petrinetPresenter = petrinetView.getPresenter();
        petrinetPresenter.resize(newSize);
    }
}
