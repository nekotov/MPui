package ua.nekotov.mpui.mpui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class HelloController {
    @FXML
    private Button search;
    @FXML
    private TextField searchQ;
    @FXML
    private TableView table;
    @FXML
    private HBox hboxmain;
    @FXML
    private HBox bottomhbox;

    public void searchClicked(){
        System.out.println("search clicked");
        Effect effect = new DropShadow();

        // deletate all ckeckboxes from hboxmain
        hboxmain.getChildren().clear();

        search.setEffect(effect);
        table.getItems().clear();
        table.getColumns().clear();
        //table.getColumns().add(new TableColumn("Price"));
        ArrayList<MP.Products> prod = new ArrayList<>();
        try {
            prod = MP.Products.get(searchQ.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }


        Field[] fields = MP.Products.class.getFields();
        for (Field f : fields) {
            System.out.println(f.getName());

            TableColumn<MP.Products, String> column = new TableColumn<>(f.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(f.getName()));
            VBox vb = new VBox();
            CheckBox cb = new CheckBox(f.getName());
            cb.setSelected(true);
            // onclick hide column
            cb.setOnAction(event -> {
                if (cb.isSelected()) {
                    column.setVisible(true);
                } else {
                    column.setVisible(false);
                }
            });

            TextField tf = new TextField();
            tf.setPromptText(f.getName());

            // tf on edit act as filter for column
            ArrayList<MP.Products> finalProd = prod;

            tf.textProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("textfield "+f.getName()+" changed from " + oldValue + " to " + newValue);
                column.setVisible(true);
                table.getItems().clear();
                    for (MP.Products p : finalProd) {
                        if(newValue.equals("")){
                            table.getItems().add(p);
                            continue;
                        }
                        try {
                            if (f.get(p).toString().contains(newValue)) {
                                System.out.println("found "+p.title);
                                table.getItems().add(p);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }



            });
            vb.getChildren().add(tf);
            vb.getChildren().add(cb);
            hboxmain.getChildren().add(vb);
            table.getColumns().add(column);
        }

        for (MP.Products p : prod) {
            table.getItems().add(p);
        }



    }

}
