package gui;

import java.util.Optional;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main class of the program. Launches and builds the gui Implementation of the
 * game "Dizzle" from "Schmidt Spiele"
 * https://www.schmidtspiele.de/details/produkt/dizzle.html
 *
 * @author Mario da Graca (cgt103579)
 */
public class Grp04_daGraca_Dizzle extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent main = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene mainScene = new Scene(main);

        stage.setTitle("Dizzle");
        stage.setWidth(1300);
        stage.setHeight(800);
        stage.setMinWidth(1300);
        stage.setMinHeight(800);

        stage.setScene(mainScene);
        stage.show();

        // 1. Anweisung in Application.start():
       // Anwendung beenden wenn eine Ausnahme nicht abgefangen worden ist.
       Thread.currentThread().setUncaughtExceptionHandler((Thread th, Throwable ex)-> {
           ex.printStackTrace(); 
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Unerwarteter Fehler");
           alert.setContentText("Entschuldigung, das hätte nicht passieren dürfen!");
           alert.showAndWait(); 
       });
        
        stage.setOnCloseRequest(handleCloseDialogue);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

    /**
     * EventHandler to confirm the closure of the application
     */
    private final EventHandler<WindowEvent> handleCloseDialogue = (WindowEvent event) -> {
        //Create an alert
        Alert closeDialogue = new Alert(Alert.AlertType.CONFIRMATION);
        closeDialogue.setTitle("Confirmation");
        closeDialogue.setHeaderText(null);
        closeDialogue.setContentText("Are you sure you want to exit?\nIf you didn't save, your progress will be lost.");
        
        Optional<ButtonType> result = closeDialogue.showAndWait();
        if (result.get() != ButtonType.OK) {
            event.consume();
        }
    };

}
