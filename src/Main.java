/**
 *  Author - Thomas Bowidowicz
 *
 *  CS351 Project 1
 *
 *  This project is a visualization of the multiplication circle video done
 *  by Mathologer here https://www.youtube.com/watch?v=qhbuKbxJsk8&vl=en. The
 *  program has one class (Main) which uses JavaFX and the borderPane,
 *  gridPane, and pane nodes to organize the various elements on the screen.
 *  The visualization occurs by taking a given number of points on a circle,
 *  which are determined by using the radian 2*PI value to represent 360
 *  degrees and then dividing it by said number to determine the location
 *  on the circle. This point value is multiplied by whatever the given times
 *  table value is to find the second coordinate and a line drawn between the
 *  two points before the point number increments up.
 *
 *  There is a small control panel at the bottom which allows the user to run
 *  the visualization. They can choose a specific number of points and times
 *  table to jump to, cycle through 10 of my personal favorites, or start the
 *  visualization which will use the given number of points and start to
 *  increase the times table by a given increment. This increment can be
 *  adjusted up or down based on a slider. There is also a slider used to
 *  adjust the speed of the visualization. The visualization can be paused
 *  and started back up at any point. The color also randomly changes every
 *  time the image is drawn.
 */
package sample;

import javafx.application.Application;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.util.Random;

/**
 * Main class
 * This is the only class in the program. Global variables for the circle
 * coordinates, radius, a looping counter, the number of points, multiplication
 * table, nanosecond to second conversion, and time/multiplication increments
 * are declared before the start method is overridden. Inside of the start
 * method are all of the button, label, slider, textfield, pane, borderPane,
 * and gridPane declarations and assignments. There are six button event
 * listeners and one animation timer which contains the drawing logic of the
 * circle.
 *
 */
public class Main extends Application {

    // Global variable declarations for circle values
    double circleX = 400;
    double circleY = 320;
    double radius = 250;

    //  Counter for looping over favorites
    int counter = 0;

    // Initial number of points and multiplication
    double numberOfPoints = 150;
    double multiplication = 0;

    double oneSecond = 1_000_000_000;
    double timeIncrement = oneSecond;
    double multIncrement = 1;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Multiplication Circle Visualization");

        //Buttons
        Button btnStart = new Button("Start");
        Button btnPause = new Button("Pause");
        Button btnJumpTo = new Button("Jump to");
        Button btnFavorites = new Button("Tour my favorites");

        //Labels
        Label numPoints = new Label("Number of points: ");
        Label numMultiplier = new Label("Multiplier");
        Label increment = new Label("Multiplication increment");
        Label timeScale = new Label("Time scale increment");

        //Text fields
        TextField points = new TextField("10");
        TextField multiplier = new TextField("0");

        //Sliders
        Slider incrementStep = new Slider(0, 5, 1.0);
        Slider incrementTime = new Slider(0, 2, 1.0);

        incrementStep.setMajorTickUnit(0.1f);
        incrementStep.setShowTickMarks(true);
        incrementStep.setShowTickLabels(true);

        incrementTime.setMajorTickUnit(0.1f);
        incrementTime.setShowTickMarks(true);
        incrementTime.setShowTickLabels(true);

        // Multiplication slider event listener
        incrementStep.valueProperty().addListener(
                new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                multIncrement = (double) newValue;
            }
        });

        // Time increment slider event listener
        incrementTime.valueProperty().addListener(
                new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                timeIncrement = ( (double) newValue * 1_000_000_000);
            }
        });

        // Creation of the grid pane
        GridPane gridPane = new GridPane();

        //Creation of pane
        Pane pane = new Pane();

        // Circle creation
        Circle circle = new Circle(circleX, circleY, radius);

        // Circle initial values set
        circle.setStroke(Color.BLACK);
        circle.setFill(null);
        circle.setStrokeWidth(1);
        pane.getChildren().addAll(circle);

        /**
         * Main animation timer
         * This timer is called by the start and pause button and contains
         * all of the logic needed to draw the lines in their correct
         * positions. The main for loop iterates from 0 to 359 and
         * draws/appends a new line after each loop. It uses the trigonometric
         * relationships of the unit circle ot find each coordinate by using
         * the center of the circle and adding the radius to it reach the edge
         * of the circle and then multiplying it by the cos or sin of 360
         * degrees (2PI) times the numbered point that it is on (i) divided
         * by the number of points. This will give one x or y coordinate. Once
         * there is a starting point of the graph, the process repeats with the
         * only change being the multiplication by given value. A random color
         * is generated by three random values being generated and each being
         * placed into the R, G, B locations. This is from Professor Haugh's
         * lecture example. The multiplication increment occurs at the end.
         */
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= timeIncrement) {
                    numberOfPoints = Double.parseDouble(points.getText());

                    // Random color generator based on Professor Joseph Haugh's
                    // lecture example from 9/1
                    Random rand = new Random();
                    double r = rand.nextDouble();
                    double g = rand.nextDouble();
                    double b = rand.nextDouble();
                    Color color = new Color(r,g,b,1);

                    pane.getChildren().clear();

                    // Loop to draw lines
                    // Uses 2PI to represent 360 degrees
                    for (int i = 0; i < numberOfPoints; i++) {
                        Line line = new Line((circleX + radius * Math.cos(
                                              2 * Math.PI * i / numberOfPoints)),
                                             (circleY + radius * Math.sin(
                                              2 * Math.PI * i / numberOfPoints)),
                                             (circleX + radius * Math.cos(
                                              2 * Math.PI * i * multiplication
                                              / numberOfPoints)),
                                             (circleY + radius * Math.sin(
                                              2 * Math.PI * i * multiplication
                                              / numberOfPoints)));
                        line.setStroke(color);
                        pane.getChildren().addAll(line);
                    }

                    circle.setStroke(Color.BLACK);
                    circle.setFill(null);
                    circle.setStrokeWidth(1);
                    pane.getChildren().addAll(circle);

                    multiplication += multIncrement;

                    lastUpdate = now;
                }
            }
        };

        // Start button
        btnStart.setOnAction(event -> {
            timer.start();
        });

        // Pause button
        btnPause.setOnAction(event -> {
            timer.stop();
        });

        // Jump to button
        // Uses the same for loop and math to generate the lines
        btnJumpTo.setOnAction(event -> {
            numberOfPoints = Double.parseDouble(points.getText());
            multiplication = Double.parseDouble(multiplier.getText());

            // Random color generator based on Professor Joseph Haugh's
            // lecture example from 9/1
            Random rand = new Random();
            double r = rand.nextDouble();
            double g = rand.nextDouble();
            double b = rand.nextDouble();
            Color color = new Color(r,g,b,1);

            //Circle circle = new Circle(circleX, circleY, radius);

            pane.getChildren().clear();

            // Loop to draw lines
            // Uses the same for loop as the animation timer for the math and
            // to draw the lines
            for (int i = 0; i < numberOfPoints; i++) {
                Line line = new Line((circleX + radius * Math.cos(
                                      2 * Math.PI * i / numberOfPoints)),
                                     (circleY + radius * Math.sin(
                                      2 * Math.PI * i / numberOfPoints)),
                                     (circleX + radius * Math.cos(
                                      2 * Math.PI * i * multiplication
                                      / numberOfPoints)),
                                     (circleY + radius * Math.sin(
                                      2 * Math.PI * i * multiplication
                                      / numberOfPoints)));
                line.setStroke(color);
                pane.getChildren().addAll(line);
            }

            circle.setStroke(Color.BLACK);
            circle.setFill(null);
            circle.setStrokeWidth(1);
            pane.getChildren().addAll(circle);
        });

        // Favorite button event handler
        btnFavorites.setOnAction(event -> {
            int[] pointsArray = new int[] {360, 360, 15, 360, 100,
                                           100, 360, 100, 100, 100};
            int[] multiplicationArray = new int[] {3, 8, 25, 26, 26,
                                                   35, 40, 49, 53, 68};

            if (counter == 10) {
                counter = 0;
            }

            double numberOfPoints = pointsArray[counter];
            double multiplication = multiplicationArray[counter];

            // Random color generator based on Professor Joseph Haugh's
            // lecture example from 9/1/2020
            Random rand = new Random();
            double r = rand.nextDouble();
            double g = rand.nextDouble();
            double b = rand.nextDouble();
            Color color = new Color(r,g,b,1);

            pane.getChildren().clear();

            // Loop to draw lines
            // Uses the same for loop and math as the animation timer
            for (int i = 0; i < numberOfPoints; i++) {
                Line line = new Line((circleX + radius * Math.cos(
                                      2 * Math.PI * i / numberOfPoints)),
                                     (circleY + radius * Math.sin(
                                      2 * Math.PI * i / numberOfPoints)),
                                     (circleX + radius * Math.cos(
                                      2 * Math.PI * i * multiplication
                                      / numberOfPoints)),
                                     (circleY + radius * Math.sin(
                                      2 * Math.PI * i * multiplication
                                      / numberOfPoints)));
                line.setStroke(color);
                pane.getChildren().addAll(line);
            }

            circle.setStroke(Color.BLACK);
            circle.setFill(null);
            circle.setStrokeWidth(1);
            pane.getChildren().addAll(circle);

            counter++;
        });

        // Gridpane addition and placement
        gridPane.add(numPoints, 0,0);
        gridPane.add(points, 1,0);
        gridPane.add(numMultiplier, 2,0);
        gridPane.add(multiplier, 3,0);

        gridPane.add(btnJumpTo,0,1);
        gridPane.add(btnStart,1,1);
        gridPane.add(btnPause,2,1);
        gridPane.add(btnFavorites, 3,1);

        gridPane.add(increment, 0,3);
        gridPane.add(incrementStep, 1, 3);
        gridPane.add(timeScale, 2,3);
        gridPane.add(incrementTime, 3,3);

        gridPane.setVgap(4);
        gridPane.setHgap(4);

        gridPane.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();

        // Borderpane placement
        borderPane.setBottom(gridPane);
        borderPane.setCenter(pane);

        primaryStage.setScene(new Scene(borderPane, 800, 800));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
