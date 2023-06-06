package app.helpers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;

/**
 * Affichage de popups.
 *
 * @author P.-A. Mettraux / J.-C. Stritt
 */
public class JfxPopup {

    /**
     * Méthode privée. Prépare une boite de dialogue pour l'affichage. Utilise
     * éventuellement du CSS dans "/resources/css/MyDialogs.css" pour
     * personnaliser la chose.
     *
     * @param dlg une boite de dialogue préalablement créée
     * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     */
    private static void preparePopup(Dialog<?> dlg, Window owner, String title, String header, String msg) {
        dlg.initOwner(owner);
        dlg.setTitle(title);
        dlg.setHeaderText(header);
        dlg.setContentText(msg);
        URL url = JfxPopup.class.getResource("/resources/css/MyDialogs.css");
        if (url != null) {
            DialogPane pane = dlg.getDialogPane();
            pane.getStylesheets().add(JfxPopup.class.getResource("/resources/css/MyDialogs.css").toExternalForm());
            pane.getStyleClass().add("myDialog");
        }
    }

    /**
     * Affiche une popup de type "error" centrée sur son propriétaire.
     *
     * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     */
    public static void displayError(Window owner, String title, String header, String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        preparePopup(alert, owner, title, header, msg);
        alert.showAndWait();
    }

    /**
     * Affiche une popup de type "error" centrée sur l'écran.
     *
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     */
    public static void displayError(String title, String header, String msg) {
        displayError(null, title, header, msg);
    }

    /**
     * Affiche une popup de type "information" centrée sur son propriétaire.
     *
     * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     */
    public static void displayInformation(Window owner, String title, String header, String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        preparePopup(alert, owner, title, header, msg);
        alert.showAndWait();
    }

    /**
     * Affiche une popup de type "information" centrée sur l'écran.
     *
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     */
    public static void displayInformation(String title, String header, String msg) {
        displayInformation(null, title, header, msg);
    }

    /**
     * Affiche une popup de type "avertissement" centrée sur son propriétaire.
     *
     * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     */
    public static void displayWarning(Window owner, String title, String header, String msg) {
        Alert alert = new Alert(AlertType.WARNING);
        preparePopup(alert, owner, title, header, msg);
        alert.showAndWait();
    }

    /**
     * Affiche une popup de type "avertissement" centrée sur l'écran.
     *
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     */
    public static void displayWarning(String title, String header, String msg) {
        displayWarning(null, title, header, msg);
    }

    /**
     * Affiche une popup de type "confirmation" (avec OK ou Annuler) centrée sur
     * son propriétaire.
     *
     * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     *
     * @return true si OK a été sélectionné
     */
    public static boolean askConfirmation(Window owner, String title, String header, String msg) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        preparePopup(alert, owner, title, header, msg);
        Optional<ButtonType> rep = alert.showAndWait();
        return rep.get() == ButtonType.OK;
    }

    /**
     * Affiche une popup de type "confirmation" (avec OK ou Annuler) centrée sur
     * l'écran.
     *
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     *
     * @return true si OK a été sélectionné
     */
    public static boolean askConfirmation(String title, String header, String msg) {
        return askConfirmation(null, title, header, msg);
    }

    /**
     * Affiche une popup de type "confirmation" (avec OUI/NON) centrée sur son
     * propriétaire.
     *
     * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     * @param defaultNo true le bouton par défaut est le "non"
     *
     * @return true si OUI a été sélectionné
     */
    public static boolean askYesNo(Window owner, String title, String header, String msg, boolean defaultNo) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        preparePopup(alert, owner, title, header, msg);

        // messages dans les boutons suivant la "Locale" par défaut
        String msgYes = "Yes";
        String msgNo = "No";
        switch (Locale.getDefault().getLanguage()) {
            case "fr":
                msgYes = "Oui";
                msgNo = "Non";
                break;
            case "de":
                msgYes = "Ja";
                msgNo = "Nein";
                break;
            case "it":
                msgYes = "Si";
                msgNo = "No";
                break;
        }

        // création des boutons
        ButtonType btnYes = new ButtonType(msgYes);
        ButtonType btnNo = new ButtonType(msgNo);
        alert.getButtonTypes().setAll(btnYes, btnNo);

        // si le bouton NON est choisi par défaut
        if (defaultNo) {
            // déactive le bouton OUI par défaut
            Button yesButton = (Button) alert.getDialogPane().lookupButton(btnYes);
            yesButton.setDefaultButton(false);

            // active le bouton NON par défaut
            Button noButton = (Button) alert.getDialogPane().lookupButton(btnNo);
            noButton.setDefaultButton(true);
        }

        // affiche la popup et attent la réponse
        Optional<ButtonType> rep = alert.showAndWait();
        return rep.get() == btnYes;
    }

    /**
     * Affiche une popup de type "confirmation" (avec OUI/NON) centrée sur
     * l'écran.
     *
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     * @param defaultNo true le bouton par défaut est le "non"
     *
     * @return true si OUI a été sélectionné
     */
    public static boolean askYesNo(String title, String header, String msg, boolean defaultNo) {
        return askYesNo(null, title, header, msg, defaultNo);
    }

    /**
     * Affiche une popup de type "demande d'info" centrée sur son propriétaire.
     *
     * @param owner la fenêtre propriétaire de la popup (pour centrage) ou null
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     *
     * @return une valeur de type string or null
     */
    public static String askInfo(Window owner, String title, String header, String msg) {
        TextInputDialog textInputDialog = new TextInputDialog();
        preparePopup(textInputDialog, owner, title, header, msg);
        Optional<String> text = textInputDialog.showAndWait();
        return text.isPresent() ? text.get() : null;
    }

    /**
     * Affiche une popup de type "demande d'info" centrée sur l'écran.
     *
     * @param title un titre pour la popup
     * @param header un entête (si null = pas d'entête)
     * @param msg normalement le message principal
     *
     * @return une valeur de type string or null
     */
    public static String askInfo(String title, String header, String msg) {
        return askInfo(null, title, header, msg);
    }

    public static Pane afficherInformation(Pane pnRoot, String titre, ArrayList<String> listeTxtBtnBleus, ArrayList<String> listeTxtBtnRouges) {
        Pane pn = new Pane();
        pn.setPrefSize(400, 200);
        pn.setLayoutX(116);
        pn.setLayoutY(250);
        pn.setStyle("-fx-background-color: #23C0FF; -fx-background-radius: 15");

        Label lbl = new Label(titre);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 24));
        lbl.setTextFill(Color.WHITE);
        lbl.setAlignment(Pos.CENTER);
        lbl.setPrefSize(378, 124);
        lbl.setWrapText(true);
        lbl.setTextAlignment(TextAlignment.CENTER);
        lbl.setLayoutX(22);
        lbl.setLayoutY(0);
        lbl.setWrapText(true);

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.setPrefSize(326, 76);
        hb.setLayoutX(24);
        hb.setLayoutY(110);
        hb.setSpacing(15);
        int totalBoutons = listeTxtBtnBleus.size() + listeTxtBtnRouges.size();

        for (String lblBtnBleus : listeTxtBtnBleus) {
            Button btn = new Button(lblBtnBleus);
            btn.setStyle("-fx-background-color: #23A5FF; -fx-background-radius: 15; -fx-border-color: white; -fx-border-radius: 15; -fx-border-width: 5");
            btn.setTextFill(Color.WHITE);
            btn.setFont(Font.font("System", FontWeight.BOLD, 16));
            btn.setPrefSize(hb.getPrefWidth() / totalBoutons, 68);
            btn.setCursor(Cursor.HAND);
            hb.getChildren().add(btn);
        }
        for (String lblBtnRouges : listeTxtBtnRouges) {
            Button btn = new Button(lblBtnRouges);
            btn.setStyle("-fx-background-color: #FF4823; -fx-background-radius: 15; -fx-border-color: white; -fx-border-radius: 15; -fx-border-width: 5");
            btn.setTextFill(Color.WHITE);
            btn.setFont(Font.font("System", FontWeight.BOLD, 16));
            btn.setPrefSize(hb.getPrefWidth() / totalBoutons, 68);
            btn.setCursor(Cursor.HAND);
            hb.getChildren().add(btn);
        }

        pn.getChildren().add(lbl);
        pn.getChildren().add(hb);

        return pn;
    }

    public static Pane afficherInput(Pane pnRoot, String titre, String promptText) {
        Pane pn = new Pane();
        pn.setPrefSize(369, 200);
        pn.setLayoutX(116);
        pn.setLayoutY(250);
        pn.setStyle("-fx-background-color: #23C0FF; -fx-background-radius: 15");

        Label lbl = new Label(titre);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 24));
        lbl.setTextFill(Color.WHITE);
        lbl.setAlignment(Pos.CENTER);
        lbl.setWrapText(true);
        lbl.setTextAlignment(TextAlignment.CENTER);
        lbl.setLayoutX(22);
        lbl.setLayoutY(16);

        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER);
        hb.setPrefSize(326, 76);
        hb.setLayoutX(24);
        hb.setLayoutY(110);
        hb.setSpacing(15);
        
        TextField txf = new TextField();
        txf.setPromptText(promptText);
        txf.setPrefSize(170, 57);
        txf.setStyle("-fx-background-color: #23A5FF; -fx-background-radius: 15; -fx-border-color: white; -fx-border-radius: 15; -fx-border-width: 5");
        txf.setFont(Font.font("System", FontWeight.BOLD, 15));
        txf.setOnKeyTyped(e -> {
            if(txf.getText().length() > 12) {
                txf.setText(txf.getText().substring(0, 12));
            }
        });
        hb.getChildren().add(txf);
        
        Button btn = new Button("OK");
        btn.setStyle("-fx-background-color: #23A5FF; -fx-background-radius: 15; -fx-border-color: white; -fx-border-radius: 15; -fx-border-width: 5");
        btn.setTextFill(Color.WHITE);
        btn.setFont(Font.font("System", FontWeight.BOLD, 16));
        btn.setPrefSize(66, 68);
        btn.setCursor(Cursor.HAND);
        hb.getChildren().add(btn);

        pn.getChildren().add(lbl);
        pn.getChildren().add(hb);

        return pn;
    }

}
