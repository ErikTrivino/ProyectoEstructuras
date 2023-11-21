package uniquindio.controllers;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uniquindio.exceptions.PermisoDenegadoException;

import java.io.IOException;
import java.security.SecureRandom;


public class LoginViewController {
    ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();

    public void setModelFactoryController(ModelFactoryController modelFactoryController) {
        this.modelFactoryController = modelFactoryController;
    }

    @FXML
    private TextField txtUsuario_loginView;
    @FXML
    private TextField txtContraseña_loginView;

    @FXML
    private Button btnCancelar;

    @FXML
    void btnCancelar_loginView(ActionEvent event) {
        salir();
    }

    public void salir() {

        Stage stage = (Stage) this.btnCancelar.getScene().getWindow();
        stage.close();
    }

    public void mostrarPestaniaPrincipal(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/uniquindio/viewPrincipal.fxml"));


        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    @FXML
    void btnIngresar_loginView(ActionEvent actionEvent) throws Exception {
        String usuario=txtUsuario_loginView.getText();
        String contrasenia=txtContraseña_loginView.getText();


        if (!(usuario.equals("") && contrasenia.equals(""))) {

            modelFactoryController.verificarLoginAdmin(usuario, contrasenia);


            if (modelFactoryController.isLoginAdmin()) {

                mostrarPestaniaPrincipal(actionEvent);

            }else if(modelFactoryController.verificarLoginUsuarioNormal(usuario, contrasenia)){
                mostrarPestaniaPrincipal(actionEvent);
            } else{
                mostrarMensaje("Datos de acceso incorreectos", null, "Asegurese de introducir  los datos correctos ",
                        Alert.AlertType.ERROR);
                try {
                    throw new PermisoDenegadoException("El acceso no fue autorizado");
                }catch (PermisoDenegadoException e){
                    e.printStackTrace();

                }
            }
        }
        else {
            mostrarMensaje("Datos de acceso incompletos", null, "Asegurese de introducir todos los datos ",
                    Alert.AlertType.ERROR);
            try {
                throw new PermisoDenegadoException("El acceso no fue autorizado");
            }catch (PermisoDenegadoException e){
                e.printStackTrace();

            }

        }


    }

    private void mostrarMensaje(String titulo, String head, String content, Alert.AlertType tipo) {
        Alert alerta = new Alert(null);
        alerta.setTitle(titulo);
        alerta.setHeaderText(head);
        alerta.setContentText(content);
        alerta.setAlertType(tipo);
        alerta.show();
    }






    }
