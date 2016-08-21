package com.patrickflorek.petrineteditor.view;

import com.patrickflorek.petrineteditor.view.PetrinetView;
import com.patrickflorek.petrineteditor.presenter.EditorPresenter;
import com.patrickflorek.petrineteditor.ThePetrinetEditor;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JToolBar;
import javax.swing.JComboBox;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
//import javax.swing.Box;
import java.awt.Dimension;

/**
 * @see PetrinetView
 */
public class EditorView extends JFrame {

    EditorView self = this;

    EditorPresenter presenter;
    String title;
    JFrame frame;


    JMenuBar menuBar;
    JMenu menu;
    JMenuItem menuItem;
    JScrollPane scrollPane;
    JToolBar toolBar;
    JButton saveButton, transitionButton, placeButton, edgeButton, resizeButton, selectionDeleteButton, selectionMoveButton, transitionStepButton;
    JComboBox<String> scaler;

    JPanel contentPane, plottingBoard;
    PetrinetView petrinetView;

    JButton place;

    Scale globalSize;

    File currentDirectory = null;

    public EditorView() {
        globalSize = Scale.getScale();
        globalSize.setSize(100.0);
        buildUI();
    }

    public void buildUI() {

        this.title = "Simple petrinet editor with swing and model-view-presenter pattern";
        this.frame = new JFrame(this.title);

        //Create the menu bar.
        menuBar = new JMenuBar();
        //Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        //File menu items.
        menuItem = new JMenuItem("New", KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "New Petrinet");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                presenter.newPetrinet();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Open", KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Open Petrinet");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser filechooser = new JFileChooser();
                if (currentDirectory != null) {
                    filechooser.setCurrentDirectory(currentDirectory);
                }
                int returnVal = filechooser.showOpenDialog(EditorView.this);
                if (filechooser.getCurrentDirectory() != null) {
                    currentDirectory = filechooser.getCurrentDirectory();
                }

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    if (ThePetrinetEditor.DEBUG == true)
                        System.out.println("Loading: " + file.getName() + ".\n");
                    presenter.openPetrinet(file);
                } else {
                    if (ThePetrinetEditor.DEBUG == true)
                        System.out.println("Open command cancelled by user.\n");
                }
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Save", KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Save Petrinet");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser filechooser = new JFileChooser();
                if (currentDirectory != null) {
                    filechooser.setCurrentDirectory(currentDirectory);
                }
                int returnVal = filechooser.showSaveDialog(EditorView.this);
                if (filechooser.getCurrentDirectory() != null) {
                    currentDirectory = filechooser.getCurrentDirectory();
                }
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    if (ThePetrinetEditor.DEBUG == true)
                        System.out.println("Saving: " + file.getName() + ".\n");
                    presenter.savePetrinet(petrinetView, file);
                } else {
                    if (ThePetrinetEditor.DEBUG == true)
                        System.out.println("Open command cancelled by user.\n");
                }
            }
        });
        menu.add(menuItem);
        //Add the menu bar.
        frame.setJMenuBar(menuBar);

        //Build the editor pane.
        contentPane = new JPanel(new BorderLayout());
        frame.setContentPane(contentPane);

        //Build the Toolbar.
        toolBar = new JToolBar();
        transitionButton = new JButton("Add transition");
        transitionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                petrinetView.addTransition();
            }
        });
        toolBar.add(transitionButton);
        placeButton = new JButton("Add place");
        placeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                petrinetView.addPlace();
            }
        });
        toolBar.add(placeButton);
        edgeButton = new JButton("Add edge");
        edgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                petrinetView.addEdge();
            }
        });
        toolBar.add(edgeButton);
        selectionDeleteButton = new JButton("Selection Delete");
        selectionDeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                petrinetView.selectionDelete();
            }
        });
        toolBar.add(selectionDeleteButton);
        selectionMoveButton = new JButton("Selection Move");
        selectionMoveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                petrinetView.selectionMove();
            }
        });
        toolBar.add(selectionMoveButton);
        transitionStepButton = new JButton("Transition Step");
        transitionStepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                petrinetView.transitionStep();
            }
        });
        toolBar.add(transitionStepButton);
        resizeButton = new JButton("Resize");
        resizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JTextField xTextField = new JTextField(4);
                xTextField.setText(petrinetView._getSize().width + "");
                JTextField yTextField = new JTextField(4);
                yTextField.setText(petrinetView._getSize().height + "");

                JPanel jp = new JPanel();
                jp.add(new JLabel("x:"));
                jp.add(xTextField);
                jp.add(new JLabel("y:"));
                jp.add(yTextField);

                int response = JOptionPane.showConfirmDialog(frame, jp, "Set the new size", JOptionPane.OK_CANCEL_OPTION);
                if (response == JOptionPane.OK_OPTION) {
                    if (ThePetrinetEditor.DEBUG)
                        System.out.println("x value: " + xTextField.getText());
                    if (ThePetrinetEditor.DEBUG)
                        System.out.println("y value: " + yTextField.getText());
                    try {
                        Integer x = Math.max(0, Integer.parseInt(xTextField.getText()));
                        Integer y = Math.max(0, Integer.parseInt(yTextField.getText()));

                        presenter.resizePetrinet(petrinetView, new Dimension(x, y));
                        contentPane.remove(scrollPane);
                        scrollPane = new JScrollPane(petrinetView);
                        contentPane.add(scrollPane, BorderLayout.CENTER);
                        contentPane.validate();
                    } catch (Exception e) {

                    }
                }

            }
        });
        toolBar.add(resizeButton);
        scaler = new JComboBox<String>(new String[]{"5%", "10%", "25%", "33%", "50%", "66%", "75%", "100%", "125%", "150%", "200%", "400%"});
        scaler.setSelectedIndex(7); //100%
        scaler.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JComboBox cb = (JComboBox) ae.getSource();
                Double selectedSize = Double.parseDouble(((String) cb.getSelectedItem()).replace("%", ""));
                globalSize.setSize(selectedSize);
                if (ThePetrinetEditor.DEBUG == true)
                    System.out.println("Selected size for our components: " + globalSize.getSize());
                presenter.refreshPetrinet(petrinetView);
            }
        });
        toolBar.add(scaler);

        //Adding the Toolbar
        contentPane.add(toolBar, BorderLayout.NORTH);
        //Build the working area.
        scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);


        Insets insets = frame.getInsets();
        frame.setSize(600 + insets.left + insets.right,
                400 + insets.top + insets.bottom);
        //frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Set the presenter
     *
     * @param presenter presenter corresponding to this view
     */
    public void setEditorPresenter(EditorPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Adding a new petrinet to this editor.
     * in this stage, it just will ovveride the old one
     *
     * @param petriinetView the new one
     */
    public void addPetrinetView(PetrinetView petrinetView) {
        this.petrinetView = petrinetView;
        contentPane.remove(scrollPane);
        scrollPane = new JScrollPane(petrinetView);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.validate();

    }
}
