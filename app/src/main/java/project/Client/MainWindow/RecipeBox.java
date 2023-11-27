package project.Client.MainWindow;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import project.PantryPal;
import project.Client.ViewWindow.ViewWindow;
import project.Database.MongoDBClient;
import project.Database.UserSession;
import project.Server.Recipe;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import org.json.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.*;

/**
 * A Recipe in RecipeList (App display)
 */
class RecipeBox extends HBox {
    private Button title;
    private String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";
    private Button deleteButton;

    

    RecipeBox(String title, UserSession userSession) {
        // this.setPrefSize(1140, 50);

        // Set recipe appearance
        this.title = new Button();
        this.title.setPrefSize(800, 50);
        this.title.setText(title);
       
        this.title.setStyle(defaultButtonStyle);
        this.getChildren().add(this.title);

        // Set up delete button apperance
        this.deleteButton = new Button("delete");
        this.deleteButton.setPrefSize(350, 50);

        // deletes the recipe from the list
        this.deleteButton.setOnAction(e -> {
            Recipe Rp = getRecipeByTitle(this.title.getText());
            PantryPal.recipeStorage.remove(Rp);
            try {
                RecipeList List = (RecipeList) getParent();
                List.getChildren().remove(this);
            } catch (NullPointerException error) {
            }

            MongoDBClient mongoClient = new MongoDBClient(userSession.getUsername());
            mongoClient.deleteRecipe(title);
        });
        // adds the delete button
        this.getChildren().add(this.deleteButton);

        // Creates a new RecipeDetailsView window using selected recipe
        this.title.setOnAction(e -> {
            Recipe recipe = getRecipeByTitle(this.title.getText());
            if (recipe != null) {
                ViewWindow recipeDetailsView = new ViewWindow(recipe, this.title.getScene(), userSession);
                Scene recipeDetailsScene = new Scene(recipeDetailsView, 500, 400);
                Stage stage = (Stage) this.getScene().getWindow();
                double height = stage.getHeight();
                double width = stage.getWidth();
                boolean fullscreen = stage.isFullScreen();
                stage.setScene(recipeDetailsScene);
                if (fullscreen == true) {
                    stage.setFullScreen(fullscreen);
                } else {
                    stage.setHeight(height);
                    stage.setWidth(width);
                }

            } else {
                System.out.println("no recipe");
            }
        });
    }
    private Recipe getRecipeByTitle(String title) {
        for (Recipe recipe : PantryPal.recipeStorage) {
            if (recipe.getTitle().equals(title)) {
                return recipe;
            }
        }
        return null;
    }
     public void replaceRecipe(String title){
            this.title.setText(title);
    }
   
}