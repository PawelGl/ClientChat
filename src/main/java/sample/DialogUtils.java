package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class DialogUtils {
    public static String createNickDIalog(String message){
        TextInputDialog dialog =new TextInputDialog("Twoj nick");
        dialog.setTitle("Podaj swoj nick");
        if(message==null) {
            dialog.setHeaderText("Ustawianie nick");

        }else {
            dialog.setHeaderText(message);
        }
        dialog.setContentText("Twoj nick : ");

        Optional<String> result=dialog.showAndWait();

        if(result.isPresent()) {
            return result.get();
        }
        return null;
    }

    public static void showDialog(String title,String header,String msg){
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);

        alert.show();
    }
}
