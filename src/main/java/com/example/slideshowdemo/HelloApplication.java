// Author: MKC


package com.example.slideshowdemo;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HelloApplication extends Application {

    private final int FRAME_DURATION = 2500; // ms

    private ArrayList<Path> pokemonList;
    private ImageView[] imageView;
    private Label[] labels;
    private int numberOfImages;
    private int index;

    /**
     * This initializes the image list and number of images
     * @throws IOException
     */
    public void init() throws IOException {
        // ASSUMPTION: The currect directory is the project root
        final Path dir = Paths.get("src/main/resources/images/");
        pokemonList = Files.list(dir).collect(Collectors.toCollection(ArrayList::new));
        //    System.out.println(pokemonList.get(0).toFile().getName()); // Debug
        numberOfImages = pokemonList.size();
        imageView = new ImageView[numberOfImages];
        labels = new Label[numberOfImages];
    }

    /**
     * Scene creation and window handling
     * @param stage
     */
    public void start(Stage stage) {
        Scene scene = new Scene(createContent(), 660, 680);
        stage.setScene(scene);
        stage.setTitle("Pokemon Slide Show");
        stage.show();
    }

    /**
     * Create flashcards composed of images and captions
     */
    public void initFlashCards() {
        for (int i = 0; i < numberOfImages; i++) {
            String filename = pokemonList.get(i).toFile().getName();
            imageView[i] = new ImageView(new Image("file:src/main/resources/images/" + filename));
            labels[i] = new Label(filename.replace(".gif", ""));
            labels[i].setFont(new Font("TimesRoman", 28));
        }
    }

    /**
     * Add animation to the flashcards
     * @param pane
     * @return
     */
    private Timeline getTimeline(Pane pane) {
        EventHandler<ActionEvent> eventHandler = e -> {
            if (index < numberOfImages - 1) {
                pane.getChildren().remove(imageView[index]);
                pane.getChildren().remove(labels[index]);
                pane.getChildren().add(imageView[++index]);
            } else { // Reset Slideshow
                index = 0; //
                pane.getChildren().remove(imageView[numberOfImages - 1]);
                pane.getChildren().remove(labels[numberOfImages - 1]);
                pane.getChildren().add(imageView[index]);
            }
            pane.getChildren().add(labels[index]);
            fade(imageView[index]);
        };

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(FRAME_DURATION), eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        return animation;
    }

    /**
     * Add fading animation between flashcards
     * @param imageView
     */
    private void fade(ImageView imageView) {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), imageView);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    /**
     * Creates the overall scene graph with animation
     * @return
     */
    public Parent createContent() {
        initFlashCards();
        // Initial layout
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        index = 0;
        box.getChildren().add(imageView[index]);
        box.getChildren().add(labels[index]);

        Timeline animation = getTimeline(box);
        animation.play();

        // Add pause functionality via mouse click
        box.setOnMouseClicked(e -> {
            if (animation.getStatus() == Animation.Status.PAUSED) {
                animation.play();
                System.out.println("STATUS: ACTIVE!");
            } else {
                animation.pause();
                System.out.println("STATUS: PAUSED!");
            }
        });
        return box;
    }


    public static void main(String[] args) {
        launch();
    }
}