package com.patrickflorek.petrineteditor.view;

import com.patrickflorek.petrineteditor.presenter.PetrinetPresenter;
import com.patrickflorek.petrineteditor.view.TransitionView;
import com.patrickflorek.petrineteditor.view.PlaceView;
import com.patrickflorek.petrineteditor.view.EdgeView;
import com.patrickflorek.petrineteditor.ThePetrinetEditor;


import java.io.File;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.LayerUI;
import javax.swing.JLayer;
import javax.swing.BorderFactory;

import java.awt.Graphics;
import java.awt.AWTEvent;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.AlphaComposite;
import java.awt.Rectangle;

import java.util.ArrayList;

/**
 * @see JPanel
 */
public class PetrinetView extends JPanel {

    private PetrinetView self = this;
    private PetrinetPresenter presenter;
    private Map<String, TransitionView> transitions;
    private Map<String, PlaceView> places;
    private Map<String, EdgeView> edges;
    private JPanel jpanel = new JPanel();
    private LayerUI<JPanel> emptyLayerUI = new LayerUI() {
    };
    private SelectionLayerUI<JPanel> selectionLayerUI = new SelectionLayerUI();
    private ClickLayerUI<JPanel> clickLayerUI = new ClickLayerUI();
    private JLayer<JPanel> jlayer = new JLayer<JPanel>(jpanel, emptyLayerUI);
    private String edgeSource = "";
    private String edgeTarget = "";

    /**
     * @Deprecated
     */
    private MouseAdapter transitionListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent me) {
            if (ThePetrinetEditor.DEBUG)
                System.out.println("Clicked new transition: " + me.getX() + " " + me.getY());
            presenter.addNewTransition("transition", me.getPoint());
        }
    };

    /**
     * @Deprecated
     */
    private MouseAdapter placeListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent me) {
            if (ThePetrinetEditor.DEBUG)
                System.out.println("Clicked new place: " + me.getX() + " " + me.getY());
            presenter.addNewPlace("place", me.getPoint(), 0);
        }
    };

    /**
     * @see edgeSourceListener
     */
    private MouseAdapter edgeTargetListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent me) {
            if (me.getSource() instanceof TransitionView) {
                for (Entry<String, TransitionView> entry : transitions.entrySet()) {
                    String id = entry.getKey();
                    TransitionView transitionView = entry.getValue();
                    transitionView.removeMouseListener(edgeTargetListener);
                    if (transitionView == (TransitionView) me.getSource()) {
                        edgeTarget = id;
                    }
                }
            } else {
                for (Entry<String, PlaceView> entry : places.entrySet()) {
                    String id = entry.getKey();
                    PlaceView placeView = entry.getValue();
                    placeView.removeMouseListener(edgeTargetListener);
                    if (placeView == (PlaceView) me.getSource()) {
                        edgeTarget = id;
                    }
                }
            }
            if (ThePetrinetEditor.DEBUG)
                System.out.println("Target clicked: " + edgeTarget);
            presenter.addNewEdge(edgeSource, edgeTarget);
        }
    };

    /**
     * @see edgeTargetListener
     */
    private MouseAdapter edgeSourceListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent me) {
            if (me.getSource() instanceof TransitionView) {
                for (Entry<String, TransitionView> entry : transitions.entrySet()) {
                    String id = entry.getKey();
                    TransitionView transitionView = entry.getValue();
                    transitionView.removeMouseListener(edgeSourceListener);
                    if (transitionView == (TransitionView) me.getSource()) {
                        edgeSource = id;
                    }
                }
                for (Entry<String, PlaceView> entry : places.entrySet()) {
                    String id = entry.getKey();
                    PlaceView placeView = entry.getValue();
                    placeView.removeMouseListener(edgeSourceListener);
                    placeView.addMouseListener(edgeTargetListener);
                }
            } else {
                for (Entry<String, PlaceView> entry : places.entrySet()) {
                    String id = entry.getKey();
                    PlaceView placeView = entry.getValue();
                    placeView.removeMouseListener(edgeSourceListener);
                    if (placeView == (PlaceView) me.getSource()) {
                        edgeSource = id;
                    }
                }
                for (Entry<String, TransitionView> entry : transitions.entrySet()) {
                    String id = entry.getKey();
                    TransitionView transitionView = entry.getValue();
                    transitionView.removeMouseListener(edgeSourceListener);
                    transitionView.addMouseListener(edgeTargetListener);
                }
            }
            if (ThePetrinetEditor.DEBUG)
                System.out.println("Source clicked: " + edgeSource);

        }
    };

    public PetrinetView() {
        super();
        jpanel.setLayout(null);
        jpanel.setPreferredSize(new Dimension(600, 400));
        jpanel.setBorder(BorderFactory.createLineBorder(Color.black));
        //this.setBackground(Color.BLACK);
        //this.setMaximumSize(this.getPreferredSize());
        transitions = new HashMap<String, TransitionView>();
        places = new HashMap<String, PlaceView>();
        edges = new HashMap<String, EdgeView>();

        this.add(jlayer);
    }

    public PetrinetPresenter getPresenter() {
        return this.presenter;
    }

    /**
     * @see PetrinetPresenter
     */
    public void setPresenter(PetrinetPresenter presenter) {
        this.presenter = presenter;
    }

    public void save(File file) {
        if (ThePetrinetEditor.DEBUG)
            System.out.println("PetrinetView Saving: " + file.getName() + ".\n");
        presenter.savePetrinet(file);
    }

    /**
     * @see MouseAdapter
     */
    protected void addTransition() {
        Selected add = new PetrinetView.Selected() {
            public void onSelection(Rectangle dest) {
                jlayer.setUI(emptyLayerUI);
                presenter.addNewTransition("transition", new Point(dest.x, dest.y));
            }
        };
        selectionLayerUI.registerSelectionCallback(add);
        jlayer.setUI(selectionLayerUI);
    }

    /**
     * @see addPlace(String id, String name, Point position, Integer token)
     */
    public void addTransition(final String id, String name, Point position) {
        TransitionView transition = new TransitionView(name);
        //Absolutes Positionieren!!
        Dimension size = transition.getPreferredSize();
        Insets insets = jpanel.getInsets();
        transition.setBounds((int) position.getX() + insets.left, (int) position.getY() + insets.top,
                size.width, size.height);
        jpanel.add(transition);
        this.transitions.put(id, transition);
        jpanel.removeMouseListener(transitionListener);

        JPopupMenu popupMenu = new JPopupMenu();


        JMenuItem menuItem = new JMenuItem("Delete");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Delete Node");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                presenter.removeTransition(id);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Move");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Move Node");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Selected move = new PetrinetView.Selected() {
                    public void onSelection(Rectangle dest) {
                        jlayer.setUI(emptyLayerUI);
                        presenter.moveTransition(id, new Point(dest.x, dest.y));
                    }
                };
                selectionLayerUI.registerSelectionCallback(move);
                jlayer.setUI(selectionLayerUI);

            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Rename");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Rename Node");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JTextField nameField = new JTextField();
                Object[] request = {"Rename transition: ", nameField};
                JFrame frame = (JFrame) SwingUtilities.getRoot(self);
                int response = JOptionPane.showConfirmDialog(frame, request, "Enter a new name for this transition", JOptionPane.OK_CANCEL_OPTION);
                if (response == JOptionPane.OK_OPTION) {
                    presenter.renameTransition(id, nameField.getText());
                }
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Switch/operate");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Do a single switch on this transition");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                presenter.operate(id);
            }
        });
        popupMenu.add(menuItem);

        transition.addPopupMenu(popupMenu);


        jpanel.revalidate();
        jpanel.repaint();
    }

    /**
     * @see MouseAdapter
     */
    protected void addPlace() {
        Selected add = new PetrinetView.Selected() {
            public void onSelection(Rectangle dest) {
                jlayer.setUI(emptyLayerUI);
                presenter.addNewPlace("place", new Point(dest.x, dest.y), 0);
            }
        };
        selectionLayerUI.registerSelectionCallback(add);
        jlayer.setUI(selectionLayerUI);
    }

    public void addPlace(final String id, String name, Point position, Integer token) {
        Insets insets = jpanel.getInsets();
        PlaceView place = new PlaceView(name);
        place.setToken(token);
        Dimension size = place.getPreferredSize();
        place.setBounds((int) position.getX() + insets.left, (int) position.getY() + insets.top,
                size.width, size.height);
        jpanel.add(place);
        this.places.put(id, place);
        jpanel.removeMouseListener(placeListener);


        JPopupMenu popupMenu = new JPopupMenu();


        JMenuItem menuItem = new JMenuItem("Delete");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Delete Node");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                presenter.removePlace(id);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Move");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Move Node");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Selected move = new PetrinetView.Selected() {
                    public void onSelection(Rectangle dest) {
                        jlayer.setUI(emptyLayerUI);
                        presenter.movePlace(id, new Point(dest.x, dest.y));
                    }
                };
                selectionLayerUI.registerSelectionCallback(move);
                jlayer.setUI(selectionLayerUI);
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Rename");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Rename Node");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JTextField nameField = new JTextField();
                Object[] request = {"Rename place to: ", nameField};
                JFrame frame = (JFrame) SwingUtilities.getRoot(self);
                int response = JOptionPane.showConfirmDialog(frame, request, "Enter a new name for this place", JOptionPane.OK_CANCEL_OPTION);
                if (response == JOptionPane.OK_OPTION) {
                    presenter.renamePlace(id, nameField.getText());
                }
            }
        });
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Set token");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Set token");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Integer newToken;
                JTextField nameField = new JTextField();
                Object[] request = {"Please insert token: ", nameField};
                JFrame frame = (JFrame) SwingUtilities.getRoot(self);
                int response = JOptionPane.showConfirmDialog(frame, request, "Set the new marking", JOptionPane.OK_CANCEL_OPTION);
                if (response == JOptionPane.OK_OPTION) {
                    try {
                        newToken = Integer.parseInt(nameField.getText());
                    } catch (Exception e) {
                        newToken = 0;
                    }
                    newToken = newToken >= 0 ? newToken : 0;
                    presenter.setToken(id, newToken);
                }
            }
        });
        popupMenu.add(menuItem);

        place.addPopupMenu(popupMenu);


        jpanel.revalidate();
        jpanel.repaint();

    }

    /**
     * @see MouseAdapter
     * @see edgeSourceListener
     * @see edgeTargetListener
     */
    protected void addEdge() {
        for (Entry<String, TransitionView> entry : transitions.entrySet()) {
            String id = entry.getKey();
            TransitionView transitionView = entry.getValue();
            transitionView.addMouseListener(edgeSourceListener);
        }
        for (Entry<String, PlaceView> entry : places.entrySet()) {
            String id = entry.getKey();
            PlaceView placeView = entry.getValue();
            placeView.addMouseListener(edgeSourceListener);
        }
    }

    public void addEdge(final String id, String sourceId, String targetId) {
        JComponent source, target;
        if (places.containsKey(sourceId)) {
            source = places.get(sourceId);
            target = transitions.get(targetId);
        } else {
            source = transitions.get(sourceId);
            target = places.get(targetId);
        }
        Insets insets = jpanel.getInsets();
        EdgeView edge = new EdgeView(source, target);
        Point position = edge.getPreferredPosition();
        Dimension size = edge.getPreferredSize();
        edge.setBounds((int) position.getX() + insets.left, (int) position.getY() + insets.top,
                size.width, size.height);
        jpanel.add(edge);
        if (ThePetrinetEditor.DEBUG)
            System.out.println("Adding Edge: " + edge);
        this.edges.put(id, edge);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Delete");
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Delete Node");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                presenter.removeEdge(id);
            }
        });
        popupMenu.add(menuItem);
        edge.addPopupMenu(popupMenu);
        jpanel.revalidate();
        jpanel.repaint();
    }

    public void removeTransition(String id) {
        TransitionView toRemove = getTransitionView(id);
        this.transitions.remove(id);
        jpanel.repaint();
        jpanel.remove(toRemove);
    }

    public void removePlace(String id) {
        PlaceView toRemove = getPlaceView(id);
        this.places.remove(id);
        jpanel.repaint();
        jpanel.remove(toRemove);
    }

    public void removeEdge(String id) {
        EdgeView toRemove = getEdgeView(id);
        this.edges.remove(id);
        jpanel.repaint();
        jpanel.remove(toRemove);
    }

    public void moveTransition(String id, Point position) {
        TransitionView toMoveTo = getTransitionView(id);
        Insets insets = jpanel.getInsets();
        Dimension size = toMoveTo.getPreferredSize();
        toMoveTo.setBounds((int) position.getX() + insets.left, (int) position.getY() + insets.top,
                size.width, size.height);
        jpanel.revalidate();
        jpanel.repaint();
    }

    /**
     * http://docs.oracle.com/javase/tutorial/uiswing/layout/none.html
     */
    public void movePlace(String id, Point position) {
        PlaceView toMoveTo = getPlaceView(id);
        Insets insets = jpanel.getInsets();
        Dimension size = toMoveTo.getPreferredSize();
        toMoveTo.setBounds((int) position.getX() + insets.left, (int) position.getY() + insets.top,
                size.width, size.height);
        jpanel.revalidate();
        jpanel.repaint();
    }

    public void renameTransition(String id, String name) {
        TransitionView toRename = getTransitionView(id);
        toRename.setText(name);

        jpanel.removeAll();
        presenter.drawPetrinet();
    }

    public void renamePlace(String id, String name) {
        PlaceView toRename = getPlaceView(id);

        toRename.setText(name);

        jpanel.removeAll();
        presenter.drawPetrinet();
    }

    public void setToken(String id, Integer token) {
        PlaceView placeView = getPlaceView(id);
        placeView.setToken(token);

        if (ThePetrinetEditor.DEBUG)
            System.out.println("Token in Stelle: " + id + "auf " + token + " gesetzt.");
    }

    protected TransitionView getTransitionView(String id) {
        TransitionView out = null;
        for (Entry<String, TransitionView> entry : transitions.entrySet()) {
            if (id.equals(entry.getKey())) {
                out = entry.getValue();
            }
        }
        return out;
    }

    protected PlaceView getPlaceView(String id) {
        PlaceView out = null;
        for (Entry<String, PlaceView> entry : places.entrySet()) {
            if (id.equals(entry.getKey())) {
                out = entry.getValue();
            }
        }
        return out;
    }

    protected EdgeView getEdgeView(String id) {
        EdgeView out = null;
        for (Entry<String, EdgeView> entry : edges.entrySet()) {
            if (id.equals(entry.getKey())) {
                out = entry.getValue();
            }
        }
        return out;
    }

    public void activateTransition(String id) {
        TransitionView toActivate = getTransitionView(id);
        toActivate.activate();
    }

    public void deactivateTransition(String id) {
        TransitionView toDeactivate = getTransitionView(id);
        toDeactivate.deactivate();
    }

    public Dimension calculateSize() {
        Dimension newSize = new Dimension();
        Insets insets = jpanel.getInsets();
        for (Component component : jpanel.getComponents()) {
            Point location = component.getLocation();
            Dimension size = component.getPreferredSize();
            Double x = location.getX() + size.getWidth();
            Double y = location.getY() + size.getHeight();
            newSize.setSize(Math.max(x, newSize.getWidth()), Math.max(y, newSize.getHeight()));
        }
        return newSize;
    }

    public void _resize(Dimension newSize) {
        jpanel.setPreferredSize(newSize);
        if (ThePetrinetEditor.DEBUG)
            System.out.println("Resize: " + newSize);
    }

    public Dimension _getSize() {
        return jpanel.getSize();
    }

    /**
     * @see _resize
     */
    public void _setPreferredSize(Dimension newSize) {
        _resize(newSize);
    }

    public void selectionDelete() {
        Selected deleteOnSelection = new PetrinetView.Selected() {
            public void onSelection(Rectangle rect) {
                jlayer.setUI(emptyLayerUI);
                if (ThePetrinetEditor.DEBUG)
                    System.out.println("selectedArea " + rect);
                ArrayList<String> toRemove = new ArrayList<String>();
                for (Entry<String, TransitionView> entry : transitions.entrySet()) {
                    String id = entry.getKey();
                    TransitionView transitionView = entry.getValue();
                    Point hisLocation = transitionView.getLocation();
                    if ((rect.x <= hisLocation.x && hisLocation.x <= (rect.x + rect.width)) &&
                            (rect.y <= hisLocation.y && hisLocation.y <= (rect.y + rect.height))) {
                        toRemove.add(id);
                    }
                }
                for (String id : toRemove) {
                    presenter.removeTransition(id);
                }
                toRemove.clear();
                for (Entry<String, PlaceView> entry : places.entrySet()) {
                    String id = entry.getKey();
                    PlaceView placeView = entry.getValue();
                    Point hisLocation = placeView.getLocation();
                    if ((rect.x <= hisLocation.x && hisLocation.x <= (rect.x + rect.width)) &&
                            (rect.y <= hisLocation.y && hisLocation.y <= (rect.y + rect.height))) {
                        toRemove.add(id);
                    }
                }
                for (String id : toRemove) {
                    presenter.removePlace(id);
                }
            }
        };
        selectionLayerUI.registerSelectionCallback(deleteOnSelection);
        jlayer.setUI(selectionLayerUI);
    }

    public void selectionMove() {
        Selected moveOnSelection = new PetrinetView.Selected() {
            public void onSelection(final Rectangle rect) {
                jlayer.setUI(clickLayerUI);
                if (ThePetrinetEditor.DEBUG)
                    System.out.println("selectedArea " + rect);

                Selected destination = new PetrinetView.Selected() {
                    public void onSelection(Rectangle dest) {
                        jlayer.setUI(emptyLayerUI);
                        if (ThePetrinetEditor.DEBUG)
                            System.out.println("destination " + dest);

                        Point src = null;

                        ArrayList<String> transitionsToMoveTo = new ArrayList<String>();
                        ArrayList<String> placesToMoveTo = new ArrayList<String>();
                        for (Entry<String, TransitionView> entry : transitions.entrySet()) {
                            String id = entry.getKey();
                            TransitionView transitionView = entry.getValue();
                            Point hisLocation = transitionView.getLocation();
                            if ((rect.x <= hisLocation.x && hisLocation.x <= (rect.x + rect.width)) &&
                                    (rect.y <= hisLocation.y && hisLocation.y <= (rect.y + rect.height))) {
                                transitionsToMoveTo.add(id);
                                if (src == null) {
                                    src = new Point(hisLocation.x, hisLocation.y);
                                }
                                src.x = Math.min(src.x, hisLocation.x);
                                src.y = Math.min(src.y, hisLocation.y);
                            }
                        }
                        placesToMoveTo.clear();
                        for (Entry<String, PlaceView> entry : places.entrySet()) {
                            String id = entry.getKey();
                            PlaceView placeView = entry.getValue();
                            Point hisLocation = placeView.getLocation();
                            if ((rect.x <= hisLocation.x && hisLocation.x <= (rect.x + rect.width)) &&
                                    (rect.y <= hisLocation.y && hisLocation.y <= (rect.y + rect.height))) {
                                placesToMoveTo.add(id);

                                if (src == null) {
                                    src = new Point(hisLocation.x, hisLocation.y);
                                }
                                src.x = Math.min(src.x, hisLocation.x);
                                src.y = Math.min(src.y, hisLocation.y);
                            }
                        }
                        Integer dx, dy;
                        dx = dest.x - src.x;
                        dy = dest.y - src.y;

                        for (String id : transitionsToMoveTo) {
                            TransitionView toMove = getTransitionView(id);
                            presenter.moveTransition(id, new Point(toMove.getLocation().x + dx, toMove.getLocation().y + dy));
                        }
                        for (String id : placesToMoveTo) {
                            PlaceView toMove = getPlaceView(id);
                            presenter.movePlace(id, new Point(toMove.getLocation().x + dx, toMove.getLocation().y + dy));
                        }


                    }
                };
                clickLayerUI.registerSelectionCallback(destination);
            }
        };
        selectionLayerUI.registerSelectionCallback(moveOnSelection);
        jlayer.setUI(selectionLayerUI);
    }

    public void transitionStep() {
        ArrayList<String> selectedTransitions = new ArrayList<String>();


        for (Entry<String, TransitionView> entry : transitions.entrySet()) {
            String id = entry.getKey();
            TransitionView transitionView = entry.getValue();
            if (transitionView.isSelected()) {
                selectedTransitions.add(id);
            }
        }

        System.out.println("transitionStep ");
        presenter.transitionStep(selectedTransitions);
    }

    private interface Selected {
        void onSelection(Rectangle selectedArea);
    }

    private interface Selector {
        void registerSelectionCallback(Selected callback);
    }

    private class SelectionLayerUI<JPanel> extends LayerUI implements Selector {
        private boolean mActive;
        private int mX, mY;

        private Point from, to;

        private Selected callback;

        public void registerSelectionCallback(Selected callback) {
            this.callback = callback;
            if (ThePetrinetEditor.DEBUG == true)
                System.out.println("callback " + this.callback);
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            JLayer jlayer = (JLayer) c;
            jlayer.setLayerEventMask(
                    AWTEvent.MOUSE_EVENT_MASK |
                            AWTEvent.MOUSE_MOTION_EVENT_MASK
            );
        }

        @Override
        public void uninstallUI(JComponent c) {
            JLayer jlayer = (JLayer) c;
            jlayer.setLayerEventMask(0);
            super.uninstallUI(c);
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();

            // Paint the view.
            super.paint(g2, c);

            if (mActive) {
                g2.setColor(Color.BLACK);
                g2.drawRect(from.x, from.y, Math.max(0, to.x - from.x), Math.max(0, to.y - from.y));
            }

            g2.dispose();
        }

        @Override
        protected void processMouseEvent(MouseEvent e, JLayer l) {
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                mActive = true;
                from = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), l);
                if (false && ThePetrinetEditor.DEBUG == true)
                    System.out.println("from:  " + from);
                to = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), l);
                if (false && ThePetrinetEditor.DEBUG == true)
                    System.out.println("to:  " + to);
            }
            if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                mActive = false;
                if (ThePetrinetEditor.DEBUG == true)
                    System.out.println("callback " + this.callback);
                if (callback != null) {
                    callback.onSelection(new Rectangle(from.x, from.y, Math.max(0, to.x - from.x), Math.max(0, to.y - from.y)));
                }
            }
            l.repaint();
        }

        @Override
        protected void processMouseMotionEvent(MouseEvent e, JLayer l) {
            Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), l);
            mX = p.x;
            mY = p.y;
            to = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), l);
            if (false && ThePetrinetEditor.DEBUG == true)
                System.out.println("dragged to:  " + to);
            l.repaint();
        }
    }

    private class ClickLayerUI<JPanel> extends LayerUI implements Selector {

        private Selected callback;

        public void registerSelectionCallback(Selected callback) {
            this.callback = callback;
            if (ThePetrinetEditor.DEBUG == true)
                System.out.println("callback " + this.callback);
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            JLayer jlayer = (JLayer) c;
            jlayer.setLayerEventMask(
                    AWTEvent.MOUSE_EVENT_MASK |
                            AWTEvent.MOUSE_MOTION_EVENT_MASK
            );
        }

        @Override
        public void uninstallUI(JComponent c) {
            JLayer jlayer = (JLayer) c;
            jlayer.setLayerEventMask(0);
            super.uninstallUI(c);
        }

        @Override
        protected void processMouseEvent(MouseEvent e, JLayer l) {
            if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                Point on = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), l);
                if (ThePetrinetEditor.DEBUG == true)
                    System.out.println("callback " + this.callback);
                if (callback != null) {
                    callback.onSelection(new Rectangle(on.x, on.y, 0, 0));
                }
            }
            l.repaint();
        }
    }
}
