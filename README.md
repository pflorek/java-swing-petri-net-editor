# Simple petrinet editor in a week

Written in java while participating at practical course in programming at distance university in Hagen.

## Task

Create a simple java gui application for editing and simulating petri nets. Visualize transitions as boxes, places as
circles and directed arcs as arrows. Let the user editing the petri net by mouse interactions. Implementing loading from
and storing to PNML files by using the given parser and writer. Additional requirement is the resizable GUI usable for
larger petri nets.

## Model-View-Presenter

Identifying two problem cases:
1. loading / storing a data structure
2. and visualize / editing the petri net
got me hooked to the idea leveraging the MVP pattern to solve both cases for each one and wiring them together later on.

## Usage

Compiling sources to bin

```bash
javac -d ../bin com/patrickflorek/petrinetEditor/ThePetriNetEditor.java
```

Packaging jar file

```bash
jar cfve petrineteditor.jar com.patrickflorek.petrineteditor.ThePetrinetEditor com
```

Generating java docs

```bash
javadoc -d ..\docs com.patrickflorek.petrineteditor com.patrickflorek.petrineteditor.model com.patrickflorek.petrineteditor.presenter com.patrickflorek.petrineteditor.test com.patrickflorek.petrineteditor.util com.patrickflorek.petrineteditor.vendor com.patrickflorek.petrineteditor.view
```

Executing editor

```bash
java -jar petrineteditor.jar
```

## Conclusion

Having almost zero time organizing the project, the MVP pattern helped me decoupling model and view in my bottom-to-top
approach. Modifying minor parts of the application was really easy on the examination day. Using swing components
representing the graph led to a lot of debugging and deep digging into the swing api. It would have been easier
implementing a custom canvas on my own.

## Literature

J. Desel, J. Esparza: Free Choice Petri Nets, Cambridge Tracts in Theoretical Computer Science 40, 1995.
