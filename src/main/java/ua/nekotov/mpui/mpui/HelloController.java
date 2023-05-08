package ua.nekotov.mpui.mpui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

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

        hboxmain.getChildren().clear();

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        MenuItem item = new MenuItem("Copy");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ObservableList rowList = (ObservableList) table.getSelectionModel().getSelectedItems();

                StringBuilder clipboardString = new StringBuilder();

                // rowList.get(0) Itterator
                Iterator rowIterator = rowList.iterator();
                for (int i = 0; i < rowList.size(); i++) {
                    String cell = rowIterator.next().toString();
                    clipboardString.append(cell);
                    clipboardString.append("\n");

                }
                final ClipboardContent content = new ClipboardContent();

                content.putString(clipboardString.toString());
                Clipboard.getSystemClipboard().setContent(content);
            }
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(item);
        table.setContextMenu(menu);



        if(bottomhbox.getChildren().size() == 0){
            bottomhbox.getChildren().add(searchQ);
            bottomhbox.getChildren().add(search);
            hboxmain.getChildren().remove(searchQ);
            hboxmain.getChildren().remove(search);
        }

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
