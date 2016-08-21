rmdir bin /s /q
mkdir bin
cd src
javac -d ..\bin com\patrickflorek\petrinetEditor\ThePetriNetEditor.java
cd ..\bin
jar cfve petrieneteditor.jar com.patrickflorek.petrineteditor.ThePetrinetEditor com
cd ..
rmdir docs /s /q
mkdir docs
cd src
javadoc -d ..\docs com.patrickflorek.petrineteditor com.patrickflorek.petrineteditor.model com.patrickflorek.petrineteditor.presenter com.patrickflorek.petrineteditor.test com.patrickflorek.petrineteditor.util com.patrickflorek.petrineteditor.vendor com.patrickflorek.petrineteditor.view
cd ..
java -jar bin\petrieneteditor.jar
