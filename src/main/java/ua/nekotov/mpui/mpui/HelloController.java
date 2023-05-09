package ua.nekotov.mpui.mpui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

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
        Effect effect = new DropShadow();

        hboxmain.getChildren().clear();

        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        MenuItem item = new MenuItem("Copy as TEXT");
        MenuItem json = new MenuItem("Copy as JSON");
        MenuItem open = new MenuItem("Open in browser");
        MenuItem ui = new MenuItem("Open in UI");

        ui.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // create window
                Stage stage = new Stage();
                stage.setTitle("MPUI");
                VBox root = new VBox();
                Scene scene = new Scene(root, 640, 480);
                stage.setScene(scene);
                stage.show();

                HBox main = new HBox();

                VBox images = new VBox();

                MP.Products product = (MP.Products) table.getSelectionModel().getSelectedItem();
                for (String imageUrl : product.getImageUrls()) {
                     javafx.scene.image.Image image = new javafx.scene.image.Image("https:"+imageUrl);
                     javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
                    images.getChildren().add(imageView);
                }

                VBox info = new VBox();
                HBox bmenu = new HBox();

                Field[] fields = product.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        if(field.getName().equals("imageUrls")){
                            continue;
                        }

                        if(field.getName().equals("title")){
                            stage.setTitle(product.getTitle());
                            continue;
                        }

                        if(field.getName().equals("description")){
                            TextArea textArea = new TextArea();
                            textArea.setText(product.getDescription());
                            textArea.setPrefRowCount(10);
                            textArea.setPrefColumnCount(50);
                            textArea.setWrapText(true);
                            textArea.setEditable(false);
                            info.getChildren().add(textArea);
                            continue;
                        }

                        if(field.getName().equals("priceCents")){

                            Label label = new Label("Price: " + product.getPriceCents()/100 + " EUR");
                            info.getChildren().add(label);
                            continue;
                        }

                        if(field.getName().equals("latitude")){
                            Button button = new Button("Check Location");
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    try {
                                        //java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://google.com/maps/place/"+product.getLatitude()+","+product.getLongitude()));
                                        // use https://www.openstreetmap.org/directions?from=&to=52.09305%2C5.12348#map=15/52.09301/5.12315
                                        java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://www.openstreetmap.org/directions?from=&to="+product.getLatitude()+"%2C"+product.getLongitude()+"#map=15/"+product.getLatitude()+"/"+product.getLongitude()));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            bmenu.getChildren().add(button);
                            continue;
                        }

                        if(field.getName().equals("longitude")){
                            continue;
                        }

                        if(field.getName().equals("itemId")){
                            // create button open in browser
                            Button button = new Button("Open in browser");
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    try {
                                        java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://marktplaats.nl/"+product.getItemId()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            bmenu.getChildren().add(button);
                            continue;
                        }

                        info.getChildren().add(new Label(field.getName() + ": " + field.get(product)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                main.getChildren().add(images);
                info.getChildren().add(bmenu);
                main.getChildren().add(info);
                root.getChildren().add(main);

            }
        });

        open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList rowList = table.getSelectionModel().getSelectedItems();

                Iterator rowIterator = rowList.iterator();
                for (int i = 0; i < rowList.size(); i++) {
                    MP.Products product = (MP.Products) rowIterator.next();
                    try {
                        java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://marktplaats.nl/"+product.getItemId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        json.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            ObservableList rowList = table.getSelectionModel().getSelectedItems();

                StringBuilder clipboardString = new StringBuilder();

                Iterator rowIterator = rowList.iterator();
                for (int i = 0; i < rowList.size(); i++) {
                    MP.Products product = (MP.Products) rowIterator.next();
                    clipboardString.append(product.toJSON());
                    clipboardString.append("\n");

                }
                final ClipboardContent content = new ClipboardContent();

                content.putString(clipboardString.toString());
                Clipboard.getSystemClipboard().setContent(content);

            }
        });

        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ObservableList rowList = table.getSelectionModel().getSelectedItems();

                StringBuilder clipboardString = new StringBuilder();

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
        menu.getItems().add(json);
        menu.getItems().add(open);
        menu.getItems().add(ui);
        table.setContextMenu(menu);

        if(bottomhbox.getChildren().size() == 0){
            bottomhbox.getChildren().add(searchQ);

            TextField Pages = new TextField();
            Pages.setPromptText("Pages");
            Pages.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    Pages.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
            bottomhbox.getChildren().add(Pages);
            bottomhbox.getChildren().add(search);
            hboxmain.getChildren().remove(searchQ);
            hboxmain.getChildren().remove(search);
        }

        search.setEffect(effect);
        table.getItems().clear();
        table.getColumns().clear();
        ArrayList<MP.Products> prod = new ArrayList<>();
        try {
            AtomicBoolean bool = new AtomicBoolean(false);
            bottomhbox.getChildren().forEach(node -> {
                if (node instanceof TextField) {
                    if (((TextField) node).getPromptText().equals("Pages")) {
                        bool.set(true);
                    }
                }
            });

            int i = 0;
            if(bool.get()){
                TextField Pages = (TextField) bottomhbox.getChildren().get(1);
                if(Pages.getText().length() == 0){
                    i = 0;
                }else{
                    i = Integer.parseInt(Pages.getText());
                }
            }

            if(bool.get() && i > 0){
                prod.addAll(MP.Products.get(searchQ.getText(),0, i));
            }else{
                prod = MP.Products.get(searchQ.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Field[] fields = MP.Products.class.getFields();
        for (Field f : fields) {
            TableColumn<MP.Products, String> column = new TableColumn<>(f.getName());
            column.setCellValueFactory(new PropertyValueFactory<>(f.getName()));
            // add support for String array
            if (f.getType().equals(String[].class)) {
                continue;
            }
            VBox vb = new VBox();
            CheckBox cb = new CheckBox(f.getName());
            cb.setSelected(true);
            cb.setOnAction(event -> {
                if (cb.isSelected()) {
                    column.setVisible(true);
                } else {
                    column.setVisible(false);
                }
            });

            TextField tf = new TextField();
            tf.setPromptText(f.getName());

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
                            if(f.get(p) == null){
                                continue;
                            }
                            if (f.get(p).toString().contains(newValue)) {
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

        Arrays.stream(prod.toArray()).forEach(p -> table.getItems().add(p));



    }

}
