package com.patrickflorek.petrineteditor.test;

import com.patrickflorek.petrineteditor.presenter.PetrinetPresenter;
import com.patrickflorek.petrineteditor.view.PetrinetView;
import com.patrickflorek.petrineteditor.model.PetrinetModel;

import java.io.File;

public class PetrinetPresenterTest {
    public static void main(String[] args) {


        PetrinetView petrinetView = new PetrinetView();
        PetrinetPresenter petrinetPresenter = new PetrinetPresenter(petrinetView, new PetrinetModel());
        petrinetView.setPresenter(petrinetPresenter);

        petrinetPresenter.loadPetrinet(new File("path\\to\\sample1.pnml"));

        petrinetView.save(new File("path\\to\\sample1.pnml_"));

    }
}
