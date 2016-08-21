package com.patrickflorek.petrineteditor.vendor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Diese Klasse implementiert eine einfache XML Ausgabe für
 * PNML Dateien.
 */
public final class PNMLWriter {

    /**
     * Dies ist eine Referenz zum Java Datei Objekt.
     */
    private File pnmlDatei;
    /**
     * Dies ist eine Referenz zum XML Writer. Diese Referenz wird durch die
     * Methode startXMLDocument() initialisiert.
     */
    private XMLStreamWriter writer = null;

    /**
     * Dieser Konstruktor erstellt einen neuen Writer für PNML Dateien,
     * dem die PNML Datei als Java {@link File} übergeben wird.
     *
     * @param pnml Java {@link File} Objekt der PNML Datei
     */
    public PNMLWriter(final File pnml) {
        super();

        pnmlDatei = pnml;
    }

    /**
     * Mit dieser Main Methode kann der PNMLWriter zum Testen
     * aufgerufen werden. Als erster und einziger Paramter muss
     * dazu der Pfad der zu erstellenden PNML Datei angegeben
     * werden.
     *
     * @param args Die Konsolen Parameter, mit denen das Programm aufgerufen wird.
     */
    public static void main(final String[] args) {
        if (args.length > 0) {
            File pnmlDatei = new File(args[0]);
            PNMLWriter pnmlWriter = new PNMLWriter(pnmlDatei);
            pnmlWriter.startXMLDocument();

            pnmlWriter
                    .addTransition("transition1", "Transition A", "200", "200");
            pnmlWriter
                    .addTransition("transition2", "Transition B", "200", "400");

            pnmlWriter.addPlace("place1", "Stelle 1", "100", "300", "1");
            pnmlWriter.addPlace("place2", "Stelle 2", "300", "300", "0");

            pnmlWriter.addArc("arc1", "transition1", "place1");
            pnmlWriter.addArc("arc2", "place1", "transition2");
            pnmlWriter.addArc("arc3", "transition2", "place2");
            pnmlWriter.addArc("arc4", "place2", "transition1");

            pnmlWriter.finishXMLDocument();
        } else {
            System.out.println("Bitte eine Datei als Parameter angeben!");
        }
    }

    /**
     * Diese Methode beginnt ein neues XML Dokument und initialisiert den
     * XML Writer für diese Datei.
     */
    public void startXMLDocument() {
        try {
            FileOutputStream fos = new FileOutputStream(pnmlDatei);
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            writer = factory.createXMLStreamWriter(fos, "UTF-8");
            // XML Dokument mit Version 1.0 und Kodierung UTF-8 beginnen
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("pnml");
            writer.writeStartElement("net");
        } catch (FileNotFoundException e) {
            System.err.println("Die Datei " + pnmlDatei.getAbsolutePath()
                    + " kann nicht geschrieben werden! " + e.getMessage());
            e.printStackTrace();
        } catch (XMLStreamException e) {
            System.err.println("XML Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Diese Methode beendet das Schreiben eines Petrinetzes als XML Datei.
     */
    public void finishXMLDocument() {
        if (writer != null) {
            try {
                writer.writeEndElement();
                writer.writeEndElement();
                writer.writeEndDocument();
                writer.close();
            } catch (XMLStreamException e) {
                System.err.println("XML Fehler: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Das Dokument wurde noch nicht gestartet!");
        }
    }

    /**
     * Diese Methode fügt eine neue Transition zum XML Dokument hinzu. Vor dieser Methode muss
     * startXMLDocument() aufgerufen worden sein.
     *
     * @param id        Indentifikationstext der Transition
     * @param label     Beschriftung der Transition
     * @param xPosition x Position der Transition
     * @param yPosition y Position der Transition
     */
    public void addTransition(final String id, final String label,
                              final String xPosition, final String yPosition) {
        if (writer != null) {
            try {
                writer.writeStartElement("", "transition", "");
                writer.writeAttribute("id", id);

                writer.writeStartElement("", "name", "");
                writer.writeStartElement("", "value", "");
                writer.writeCharacters(label);
                writer.writeEndElement();
                writer.writeEndElement();

                writer.writeStartElement("", "graphics", "");
                writer.writeStartElement("", "position", "");
                writer.writeAttribute("x", xPosition);
                writer.writeAttribute("y", yPosition);
                writer.writeEndElement();
                writer.writeEndElement();

                writer.writeEndElement();
            } catch (XMLStreamException e) {
                System.err
                        .println("Transition " + id
                                + " konnte nicht geschrieben werden! "
                                + e.getMessage());
                e.printStackTrace();
            }

        } else {
            System.err.println("Das Dokument muss zuerst gestartet werden!");
        }
    }

    /**
     * Diese Methode fügt eine neue Stelle zum XML Dokument hinzu. Vor dieser Methode muss
     * startXMLDocument() aufgerufen worden sein.
     *
     * @param id             Indentifikationstext der Stelle
     * @param label          Beschriftung der Stelle
     * @param xPosition      x Position der Stelle
     * @param yPosition      y Position der Stelle
     * @param initialMarking Anfangsmarkierung der Stelle
     */
    public void addPlace(final String id, final String label,
                         final String xPosition, final String yPosition,
                         final String initialMarking) {
        if (writer != null) {
            try {
                writer.writeStartElement("", "place", "");
                writer.writeAttribute("id", id);

                writer.writeStartElement("", "name", "");
                writer.writeStartElement("", "value", "");
                writer.writeCharacters(label);
                writer.writeEndElement();
                writer.writeEndElement();

                writer.writeStartElement("", "initialMarking", "");
                writer.writeStartElement("", "token", "");
                writer.writeStartElement("", "value", "");
                writer.writeCharacters(initialMarking);
                writer.writeEndElement();
                writer.writeEndElement();
                writer.writeEndElement();

                writer.writeStartElement("", "graphics", "");
                writer.writeStartElement("", "position", "");
                writer.writeAttribute("x", xPosition);
                writer.writeAttribute("y", yPosition);
                writer.writeEndElement();
                writer.writeEndElement();

                writer.writeEndElement();
            } catch (XMLStreamException e) {
                System.err
                        .println("Stelle " + id
                                + " konnte nicht geschrieben werden! "
                                + e.getMessage());
                e.printStackTrace();
            }

        } else {
            System.err.println("Das Dokument muss zuerst gestartet werden!");
        }
    }

    /**
     * Diese Methode fügt eine neue Kante zum XML Dokument hinzu. Vor dieser Methode muss
     * startXMLDocument() aufgerufen worden sein.
     *
     * @param id     Indentifikationstext der Stelle
     * @param source Indentifikationstext des Startelements der Kante
     * @param target Indentifikationstext der Endelements der Kante
     */
    public void addArc(final String id, final String source, final String target) {
        if (writer != null) {
            try {
                writer.writeStartElement("", "arc", "");
                writer.writeAttribute("id", id);
                writer.writeAttribute("source", source);
                writer.writeAttribute("target", target);
                writer.writeEndElement();
            } catch (XMLStreamException e) {
                System.err
                        .println("Kante " + id
                                + " konnte nicht geschrieben werden! "
                                + e.getMessage());
                e.printStackTrace();
            }

        } else {
            System.err.println("Das Dokument muss zuerst gestartet werden!");
        }
    }
}
