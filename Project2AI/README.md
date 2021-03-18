# Kakuro
Instructions on how to run our simulation in Eclipse.

1) Open the extracted .zip folder in eclipse as a workspace.
2) Open the project by "File > Open Projects from File System.
3) Import the "Project2AI" folder as a eclipse project using the "import source" from directory at the top. And verify that you can see our code.


Import JavaFX errors.
1) Download JavaFX JDK: https://gluonhq.com/download/javafx-14-0-1-sdk-windows/ (if you don't already have it)
2) Extract the files anywhere and write down the file path.
3) In eclipse go to Window > Preferences > Java> Build Path> User Libraries
4) Click "New..." and name it "JavaFX" and click ok
5) Click on "Add External JARs"
6) Navigate to where you saved those extracted files
7) Go into the "lib" folder. Select all of the JAR files and the src.zip and click open.
8) Click "Apply and Close"
9) Right click on your eclipse project and navigate to Build Path> Add Libraries 
10) Click "User Library" and Click "Next" and Select "JavaFX" (which we made before) and Click "Finish". 
11) Try to run "Kakuro.java". If everything runs, stop here, but if you get JavaFX runtime errors, continue.
12) In eclipse, nagivate to Run > Run configurations
13) If you ran "Kakuro.java", under the java application section there should be a section for Kakuro.
14) Select the arguments tab.
15) In the VM Arguments box type:

 --module-path "the file path of your JavaFX jars to the lib level, the file path between quotation marks --add-modules javafx.controls,javafx.fxml
 
17) Click Apply and then Run.
18) Our simulation should run and should see a filled in Kakuro board and output in the console.

A couple things to note about our simulation.
1) We have 5 different runs using different algorithms and they are all labeled in the console.
2) The booleans below the lables is the board was solved.
3) The numbers belows the booleans is the time in milliseconds.
4) The JavaFX popup is the last solved kakuro board of the last algorithm (Back Tracking with Forwarding Checking AC3).




