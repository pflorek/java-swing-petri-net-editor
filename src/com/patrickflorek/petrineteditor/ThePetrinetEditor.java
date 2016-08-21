package com.patrickflorek.petrineteditor;

import com.patrickflorek.petrineteditor.view.EditorView;
import com.patrickflorek.petrineteditor.presenter.EditorPresenter;

/**
 * @see EditorView
 * @see EditorPresenter
 */
public class ThePetrinetEditor {
    public static final Boolean DEBUG = true;

    public static void main(String[] args) {
        EditorView editorView = new EditorView();
        editorView.setEditorPresenter(new EditorPresenter(editorView));
    }
}
