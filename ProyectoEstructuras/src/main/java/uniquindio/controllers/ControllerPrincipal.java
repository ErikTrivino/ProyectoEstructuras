package uniquindio.controllers;


import com.sun.javafx.scene.shape.ArcHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uniquindio.exceptions.ActividadException;
import uniquindio.modelo.Proceso;
import uniquindio.modelo.Actividad;
import uniquindio.modelo.Tarea;
import uniquindio.modelo.UsuarioApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ControllerPrincipal {

    public TextField txtNombreActividadPrecedera;
    public ToggleGroup radioButtonActividadObligatoria;
    public ToggleGroup radioBtnObligatoriaTarea;
    public ToggleGroup agregarTarea;
    public TextField txtPosicionTarea;
    public TableColumn<Actividad, String> tblColumActividades;
    public TableColumn<Tarea, String> tblColumnTareaDescripcion;

    public TabPane tabPrincipal;
    public Tab tabProcesos;
    public Tab tabActivdades;
    public Tab tabTareas;
    public Tab tabUsuarios;
    public Tab tabBuscador;
    public ToggleGroup toggleBuscador;
    public Button btnMostrarActividades;
    public Button btnMostrarTareas;
    public Button btnMostrarPorcesos;
    public Label labelTiempoMax;
    public Label labelTiempoMin;
    public Button btnIntercambiar_tabDetalle;
    public RadioButton rbtProceso_tabDetalle;
    public RadioButton rbtActividad_tabDetalle;
    public RadioButton rbtTarea_tabDetalle;
    public VBox vBoxProcesosUsuario;
    public ComboBox<String> comboBoxUsuario;
    ModelFactoryController modelFactoryController = ModelFactoryController.getInstance();

    @FXML
    private ComboBox<String> cmbVincularActividadAproceso;

    @FXML
    private ComboBox<String> cmbVincularTareaAactividad;

    @FXML
    private TableView<Actividad> tablaActividades;

    @FXML
    private TableView<Proceso> tablaProcesos;

    @FXML
    private TableColumn<Proceso, String> tblColumProcesoNombre;

    @FXML
    private TableView<Tarea> tablaTareas;

    @FXML
    private TextArea txtAreaDescripcionActividad;

    @FXML
    private TextArea txtAreaDescripcionTarea;

    @FXML
    private TextArea txtArea_detalle_tabDetalle;

    @FXML
    private TextField txtBuscarActividad;

    @FXML
    private TextField txtBuscarProcesos;

    @FXML
    private TextField txtBuscarTarea;

    @FXML
    private TextField txtBuscar_tabDetalle;

    @FXML
    private TextField txtDuracionMinutosTarea;

    @FXML
    private TextField txtIdproceso;

    @FXML
    private TextField txtNombreActividad;

    @FXML
    private TextField txtNombreProceso;


    @FXML
    public ToggleGroup agregarActividad;
    Proceso procesoSeleccionado;
    Proceso procesoBuscarDetalle;
    Actividad actividadSeleccionada;
    Actividad actividadBuscarDetalle;
    Tarea tareaSeleccionada;
    Tarea tareaBuscarDetalle;
    ObservableList<Proceso> listadoProceso = FXCollections.observableArrayList();
    ObservableList<Actividad> listadoActividad = FXCollections.observableArrayList();
    ObservableList<Tarea> listadoTarea = FXCollections.observableArrayList();

    ObservableList<String> listadoComboBoxProcesos = FXCollections.observableArrayList();
    ObservableList<String> listadoComboBoxTareas = FXCollections.observableArrayList();
    ObservableList<String> listadoComboUsuarios = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        btnIntercambiar_tabDetalle.setDisable(true);
        btnMostrarPorcesos.setDisable(true);
        btnMostrarActividades.setDisable(true);
        btnMostrarTareas.setDisable(true);

        rbtProceso_tabDetalle.setOnAction(event -> {
            if (rbtProceso_tabDetalle.isSelected()) {
                btnIntercambiar_tabDetalle.setDisable(false);
                btnMostrarPorcesos.setDisable(false);
                btnMostrarActividades.setDisable(true);
                btnMostrarTareas.setDisable(false);
            }
        });
        rbtActividad_tabDetalle.setOnAction(event -> {
            if (rbtActividad_tabDetalle.isSelected()) {
                btnMostrarPorcesos.setDisable(false);
                btnMostrarTareas.setDisable(false);
                btnMostrarActividades.setDisable(true);
                btnIntercambiar_tabDetalle.setDisable(true);
            }
        });
        rbtTarea_tabDetalle.setOnAction(event -> {
            if (rbtTarea_tabDetalle.isSelected()) {
                btnMostrarPorcesos.setDisable(false);
                btnMostrarActividades.setDisable(false);
                btnIntercambiar_tabDetalle.setDisable(true);
                btnMostrarTareas.setDisable(true);

            }
        });


        if (modelFactoryController.loginEmpleado) {
            tabActivdades.setDisable(true);
            tabBuscador.setDisable(true);
            tabProcesos.setDisable(true);
            tabTareas.setDisable(true);
            tabPrincipal.getSelectionModel().select(4);
            modelFactoryController.generarCorreoNotificacion();
        }


        this.tblColumProcesoNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tablaProcesos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            //mostraInformacionvendedor(vendedorSeleccionado, usuarioSeleccionado);
            procesoSeleccionado = newSelection;
            mostraInformacionProceso(procesoSeleccionado);

        });
        this.tblColumActividades.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tablaActividades.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            //mostraInformacionvendedor(vendedorSeleccionado, usuarioSeleccionado);
            actividadSeleccionada = newSelection;
            mostraInformacionActividad(actividadSeleccionada);

        });
        this.tblColumnTareaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tablaTareas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            //mostraInformacionvendedor(vendedorSeleccionado, usuarioSeleccionado);
            tareaSeleccionada = newSelection;
            mostraInformacionTarea(tareaSeleccionada);

        });
        cargarListas();


    }

    private void cargarListas() {
        tablaProcesos.getItems().clear();
        tablaProcesos.setItems(obtenerProcesos());
        tablaActividades.getItems().clear();
        tablaActividades.setItems(obtenerActividades());
        tablaTareas.getItems().clear();
        tablaTareas.setItems(obtenerTareas());
    }

    private ObservableList<Tarea> obtenerTareas() {
        listadoTarea.addAll(modelFactoryController.obtenerTareas());
        return listadoTarea;
    }

    private ObservableList<Actividad> obtenerActividades() {
        listadoActividad.addAll(modelFactoryController.obtenerActividades());
        return listadoActividad;
    }

    private ObservableList<Proceso> obtenerProcesos() {
        listadoProceso.addAll(modelFactoryController.obtenerProcesos());
        return listadoProceso;
    }

    private void mostraInformacionTarea(Tarea tareaSeleccionada) {
        listadoComboBoxTareas.clear();
        if (tareaSeleccionada != null) {
            txtAreaDescripcionTarea.setText(tareaSeleccionada.getDescripcion());
            txtDuracionMinutosTarea.setText(String.valueOf(tareaSeleccionada.getDuracionMinutos()));
            if (tareaSeleccionada.isObligatoria()) {
                Toggle toggle = radioBtnObligatoriaTarea.getToggles().get(0);
                radioBtnObligatoriaTarea.selectToggle(toggle);
            } else {
                Toggle toggle = radioBtnObligatoriaTarea.getToggles().get(1);
                radioBtnObligatoriaTarea.selectToggle(toggle);
            }
            cmbVincularTareaAactividad.setItems(null);
            listadoComboBoxTareas.addAll(modelFactoryController.obtenerActividadesDeTarea(tareaSeleccionada));
            cmbVincularTareaAactividad.setItems(listadoComboBoxTareas);

        }
    }

    private void mostraInformacionActividad(Actividad actividadSeleccionada) {
        listadoComboBoxProcesos.clear();
        if (actividadSeleccionada != null) {
            txtNombreActividad.setText(actividadSeleccionada.getNombre());
            txtAreaDescripcionActividad.setText(actividadSeleccionada.getDescripcion());
            //radioButtonActividadObligatoria.selectToggle();
            if (actividadSeleccionada.isObligatoria()) {
                Toggle toggle = radioButtonActividadObligatoria.getToggles().get(0);
                radioButtonActividadObligatoria.selectToggle(toggle);
            } else {
                Toggle toggle = radioButtonActividadObligatoria.getToggles().get(1);
                radioButtonActividadObligatoria.selectToggle(toggle);
            }
            cmbVincularActividadAproceso.setItems(null);
            listadoComboBoxProcesos.addAll(modelFactoryController.obtenerProcesosDeActividad(actividadSeleccionada));
            cmbVincularActividadAproceso.setItems(listadoComboBoxProcesos);


        }
    }

    private void mostraInformacionProceso(Proceso procesoSeleccionado) {
        listadoComboUsuarios.clear();
        if (procesoSeleccionado != null) {
            txtIdproceso.setText(procesoSeleccionado.getId());
            txtNombreProceso.setText(procesoSeleccionado.getNombre());
            comboBoxUsuario.setItems(null);
            listadoComboUsuarios.addAll(modelFactoryController.obtenerUsuarioConProceso(procesoSeleccionado));

            comboBoxUsuario.setItems(listadoComboUsuarios);
        }
    }

    //Metodos de CRUD proceso
    @FXML
    void actionAgregarProceso(ActionEvent event) {

        String id = txtIdproceso.getText();
        String nombre = txtNombreProceso.getText();

        //Verificacion de null
        modelFactoryController.agregarProceso(id, nombre, listadoComboUsuarios);
        limpiarCamposTexto();
        cargarListas();


    }

    @FXML
    void actionBuscarProceso(ActionEvent event) {

    }

    @FXML
    void actionEliminarProceso(ActionEvent event) {

        if (procesoSeleccionado != null) {
            modelFactoryController.eliminarProceso(procesoSeleccionado);
            limpiarCamposTexto();
            cargarListas();
        }


    }

    @FXML
    void actionModificarProceso(ActionEvent event) {
        try {
            String id = txtIdproceso.getText();
            String nombre = txtNombreProceso.getText();
            modelFactoryController.modificarProceso(id, nombre, listadoComboUsuarios);
            limpiarCamposTexto();
            cargarListas();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limpiarCamposTexto() {
        txtIdproceso.setText(" ");
        txtNombreProceso.setText(" ");
        txtNombreActividad.setText("");
        txtDuracionMinutosTarea.setText("");
        txtAreaDescripcionTarea.setText("");
        radioBtnObligatoriaTarea.selectToggle(null);
        radioButtonActividadObligatoria.selectToggle(null);


    }

    //Metodos crud actividades
    @FXML
    void actionActualizarActividad(ActionEvent event) {

        if (actividadSeleccionada != null) {
            Toggle seleccionado = radioButtonActividadObligatoria.getSelectedToggle();
            RadioButton radioButtonSeleccionado = (RadioButton) seleccionado;
            String valor = radioButtonSeleccionado.getText();

            String nombre = txtNombreActividad.getText();
            String descripcion = txtAreaDescripcionActividad.getText();
            String radioObligatoria = valor;
            String nombreProceso = cmbVincularActividadAproceso.getValue();


            modelFactoryController.modificarActividad(actividadSeleccionada.getNombre(), descripcion, radioObligatoria, nombreProceso);
            cargarListas();
            limpiarCamposTexto();
        }
    }

    @FXML
    void actionAgregarActividad(ActionEvent event) {

        Toggle seleccionado = radioButtonActividadObligatoria.getSelectedToggle();
        RadioButton radioButtonSeleccionado = (RadioButton) seleccionado;
        String valor = radioButtonSeleccionado.getText();

        String nombre = txtNombreActividad.getText();
        String descripcion = txtAreaDescripcionActividad.getText();
        String radioObligatoria = valor;
        ObservableList<String> nombreProcesos = listadoComboBoxProcesos;

        String nombreActividadPrecedera = txtNombreActividadPrecedera.getText();


        Toggle seleccionadoAgregarActividad = agregarActividad.getSelectedToggle();
        RadioButton radioButtonSeleccionadoActividad = (RadioButton) seleccionadoAgregarActividad;
        String valorSeleccionActividad = radioButtonSeleccionadoActividad.getText();


        try {
            modelFactoryController.agregarActividad(nombre, descripcion, radioObligatoria, nombreProcesos, valorSeleccionActividad, nombreActividadPrecedera);
        } catch (ActividadException e) {
            throw new RuntimeException(e);
        }

        limpiarCamposTexto();
        cargarListas();


    }

    @FXML
    void actionBuscarActividad(ActionEvent event) {

        String actividad = txtBuscarActividad.getText();

    }

    @FXML
    void actionEliminarActividad(ActionEvent event) {

        if (actividadSeleccionada != null) {
            String nombreProceso = cmbVincularActividadAproceso.getValue();
            try {
                modelFactoryController.eliminarActividad(actividadSeleccionada, nombreProceso);
            } catch (ActividadException e) {
                throw new RuntimeException(e);
            }
            cargarListas();
            limpiarCamposTexto();
        }


    }


    //metodos para tareas
    @FXML
    void actionModificarTarea(ActionEvent event) {
        if (tareaSeleccionada != null) {

            String nombreActividad = cmbVincularTareaAactividad.getValue();
            Toggle seleccionado = radioBtnObligatoriaTarea.getSelectedToggle();
            RadioButton radioButtonSeleccionado = (RadioButton) seleccionado;

            String seleccionTareaObligatoria = radioButtonSeleccionado.getText();
            String descripcion = txtAreaDescripcionTarea.getText();
            int minutos = Integer.parseInt(txtDuracionMinutosTarea.getText());
            if (seleccionTareaObligatoria.equals("Si")) {
                tareaSeleccionada.setObligatoria(true);
            } else {
                tareaSeleccionada.setObligatoria(false);
            }
            tareaSeleccionada.setDescripcion(descripcion);
            tareaSeleccionada.setDuracionMinutos(minutos);

            modelFactoryController.modificarTarea(tareaSeleccionada, nombreActividad);
            cargarListas();
            limpiarCamposTexto();

        }

    }

    @FXML
    void actionEliminarTarea(ActionEvent event) {

        if (tareaSeleccionada != null) {
            String nombreActividad = cmbVincularTareaAactividad.getValue();

            modelFactoryController.eliminarTarea(tareaSeleccionada, nombreActividad);
            cargarListas();
            limpiarCamposTexto();
        }
    }

    @FXML
    void actionAgregarTarea(ActionEvent event) {
        Toggle seleccionado = radioBtnObligatoriaTarea.getSelectedToggle();
        RadioButton radioButtonSeleccionado = (RadioButton) seleccionado;
        String seleccionTareaObligatoria = radioButtonSeleccionado.getText();

        ObservableList<String> nombreActividad = listadoComboBoxTareas;

        String descripcion = txtAreaDescripcionTarea.getText();
        int minutos = Integer.parseInt(txtDuracionMinutosTarea.getText());

        Toggle seleccionAgregar = agregarTarea.getSelectedToggle();
        RadioButton radioButtonSeleccionAgregar = (RadioButton) seleccionAgregar;
        String seleccionAgregarTarea = radioButtonSeleccionAgregar.getText();

        int posicionTarea = Integer.parseInt(txtPosicionTarea.getText());


        modelFactoryController.agregarTarea(descripcion, seleccionTareaObligatoria, minutos, nombreActividad, seleccionAgregarTarea, posicionTarea);
        cargarListas();
        limpiarCamposTexto();

    }

    public boolean datosValidos(String id, String nombre) {

        String mensaje = " ";
        if (id == null || id.equalsIgnoreCase(" ")) {
            mensaje += "El código es invalido  \n ";
        }
        if (nombre == null || nombre.equalsIgnoreCase(" ")) {
            mensaje += "El nombre es invalido  \n ";
        }


        if (mensaje.equalsIgnoreCase(" ")) {
            return true;
        } else {
            mostrarMensaje("Notificación Producto", "Datos invalidos", mensaje, Alert.AlertType.WARNING);
            return false;
        }
    }

    public void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {
        Alert alerta = new Alert(alertType);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    @FXML
    private void agregarProcesoActividad(ActionEvent event) {
        List<Proceso> listaDeProcesos = modelFactoryController.obtenerProcesos(); // Obtener la lista de procesos

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Detalles del Proceso");
        alerta.setHeaderText(null);
        alerta.initStyle(StageStyle.UTILITY);
        alerta.initModality(Modality.APPLICATION_MODAL);

        GridPane cuadroProcesos = new GridPane();
        cuadroProcesos.setHgap(10);
        cuadroProcesos.setVgap(5);

        int rowIndex = 0;

        for (Proceso proceso : listaDeProcesos) {
            Label labelId = new Label("ID: " + proceso.getId());
            Label labelNombre = new Label("Nombre: " + proceso.getNombre());

            String nombrebtn = "Seleccionar";
            if (listadoComboBoxProcesos.contains(proceso.getNombre())) {
                nombrebtn = "Deseleccionar";
            }
            Button botonSeleccionar = new Button(nombrebtn);
            if (listadoComboBoxProcesos.contains(proceso.getNombre())) {

                botonSeleccionar.setOnAction(e -> {
                    String nombreProceso = proceso.getNombre();
                    listadoComboBoxProcesos.remove(nombreProceso);
                    cmbVincularActividadAproceso.setItems(null);
                    cmbVincularActividadAproceso.setItems(listadoComboBoxProcesos);
                });
            } else {
                botonSeleccionar.setOnAction(e -> {
                    String nombreProceso = proceso.getNombre();
                    listadoComboBoxProcesos.add(nombreProceso);
                    cmbVincularActividadAproceso.setItems(null);
                    cmbVincularActividadAproceso.setItems(listadoComboBoxProcesos);
                });
            }


            cuadroProcesos.addRow(rowIndex,
                    labelId,               // Columna 0, Fila rowIndex
                    labelNombre,           // Columna 1, Fila rowIndex
                    botonSeleccionar);     // Columna 2, Fila rowIndex

            rowIndex++;
        }

        cuadroProcesos.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-padding: 10px;");

        alerta.getDialogPane().setContent(cuadroProcesos);
        alerta.showAndWait();
    }


    public void agregarActividadTarea(ActionEvent event) {

        List<Actividad> listaDeProcesos = modelFactoryController.obtenerActividades(); // Obtener la lista de procesos

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Detalles del Proceso");
        alerta.setHeaderText(null);
        alerta.initStyle(StageStyle.UTILITY);
        alerta.initModality(Modality.APPLICATION_MODAL);

        GridPane cuadroProcesos = new GridPane();
        cuadroProcesos.setHgap(10);
        cuadroProcesos.setVgap(5);

        int rowIndex = 0;

        for (Actividad proceso : listaDeProcesos) {
            Label labelId = new Label("Nombre: " + proceso.getNombre());
            Label labelNombre = new Label("Descripcion: " + proceso.getDescripcion());

            String nombrebtn = "Seleccionar";
            if (listadoComboBoxTareas.contains(proceso.getNombre())) {
                nombrebtn = "Deseleccionar";
            }
            Button botonSeleccionar = new Button(nombrebtn);
            if (listadoComboBoxTareas.contains(proceso.getNombre())) {

                botonSeleccionar.setOnAction(e -> {
                    String nombreProceso = proceso.getNombre();
                    listadoComboBoxTareas.remove(nombreProceso);
                    cmbVincularTareaAactividad.setItems(null);
                    cmbVincularTareaAactividad.setItems(listadoComboBoxProcesos);
                });
            } else {
                botonSeleccionar.setOnAction(e -> {
                    String nombreProceso = proceso.getNombre();
                    listadoComboBoxTareas.add(nombreProceso);
                    cmbVincularTareaAactividad.setItems(null);
                    cmbVincularTareaAactividad.setItems(listadoComboBoxProcesos);
                });
            }


            cuadroProcesos.addRow(rowIndex,
                    labelId,               // Columna 0, Fila rowIndex
                    labelNombre,           // Columna 1, Fila rowIndex
                    botonSeleccionar);     // Columna 2, Fila rowIndex

            rowIndex++;
        }

        cuadroProcesos.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-padding: 10px;");

        alerta.getDialogPane().setContent(cuadroProcesos);
        alerta.showAndWait();
    }

    public void buscarDetalleGeneral(ActionEvent event) {
        Toggle seleccionado = toggleBuscador.getSelectedToggle();
        RadioButton radioButtonSeleccionado = (RadioButton) seleccionado;
        String seleccionTareaObligatoria = radioButtonSeleccionado.getText();

        String nombreBuscador = txtBuscar_tabDetalle.getText();

        switch (seleccionTareaObligatoria) {
            case "Proceso":
                procesoBuscarDetalle = modelFactoryController.obtenerProceso(nombreBuscador);

                if (procesoBuscarDetalle != null) {
                    labelTiempoMax.setText(String.valueOf(procesoBuscarDetalle.calcularTiempoMax()));
                    labelTiempoMin.setText(String.valueOf(procesoBuscarDetalle.calcularTiempoMin()));
                }

                break;
            case "Actividad":
                actividadBuscarDetalle = modelFactoryController.obtenerActividad(nombreBuscador);
                if (actividadBuscarDetalle != null) {
                    labelTiempoMax.setText(String.valueOf(actividadBuscarDetalle.calcularTiempoMax()));
                    labelTiempoMin.setText(String.valueOf(actividadBuscarDetalle.calcularTiempoMin()));
                }
                break;
            case "Tarea":
                tareaBuscarDetalle = modelFactoryController.obtenerTarea(nombreBuscador);
                if (tareaBuscarDetalle != null) {
                    labelTiempoMax.setText(String.valueOf(tareaBuscarDetalle.getDuracionMinutos()));
                    labelTiempoMin.setDisable(true);
                }
                break;
        }
    }


    public void intercambiarActividades(ActionEvent event) {
        intercambiarActividadesAction();
    }

    private void intercambiarActividadesAction() {
        String valor = pedirNumero();
        if (valor.equals("1")) {
            intercambiarActividadPuesto();
        } else if (valor.equals("2")) {
            intercambiarContenido();
        } else if (valor.equals("3")) {
            intercambiarActividadUnicamente();
        } else {
            mostrarMensaje("Error", "El valor dado no es valido", "Ingrese una opcion valida", Alert.AlertType.ERROR);
        }
    }

    private String pedirNombre() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Actividades");
        dialog.setHeaderText("Por favor, ingresa el nombre de la actividad");
        dialog.setContentText("Nombre:");
        // Mostrar el cuadro de diálogo y esperar a que el usuario ingrese algo
        Optional<String> nombreActividad = dialog.showAndWait();
        String nombreActividadObjetivo = nombreActividad.orElse("");
        return nombreActividadObjetivo;
    }

    private void intercambiarActividadUnicamente() {
        boolean fallido = true;
        while (fallido) {
            try {
                String actividad1 = pedirNombre();
                String actividad2 = pedirNombre();
                if (procesoBuscarDetalle.verificarNombre(actividad1) && procesoBuscarDetalle.verificarNombre(actividad2)) {
                    procesoBuscarDetalle.intercambiarActividadesUnicamente(actividad1, actividad2);
                    fallido = false;
                    cargarListas();
                    // modelFactoryController.iniciarSalvarDatosPrueba();
                } else {
                    fallido = pedirNuevamente();
                }
            } catch (ActividadException e) {
                mostrarMensaje("Error", "Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void intercambiarContenido() {
        boolean fallido = true;
        while (fallido) {
            try {
                String actividad1 = pedirNombre();
                String actividad2 = pedirNombre();
                if (procesoBuscarDetalle.verificarNombre(actividad1) && procesoBuscarDetalle.verificarNombre(actividad2)) {
                    procesoBuscarDetalle.intercambiarContenido(actividad1, actividad2);
                    fallido = false;
                    cargarListas();
                    //modelFactoryController.iniciarSalvarDatosPrueba();
                } else {
                    fallido = pedirNuevamente();
                }
            } catch (ActividadException e) {
                mostrarMensaje("Error", "Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void intercambiarActividadPuesto() {
        boolean fallido = true;
        while (fallido) {
            try {
                String actividad1 = pedirNombre();
                String actividad2 = pedirNombre();
                if (procesoBuscarDetalle.verificarNombre(actividad1) && procesoBuscarDetalle.verificarNombre(actividad2)) {
                    procesoBuscarDetalle.intercambiarActividades(actividad1, actividad2);
                    cargarListas();
                    //modelFactoryController.iniciarSalvarDatosPrueba();
                    fallido = false;
                } else {
                    fallido = pedirNuevamente();
                }
            } catch (ActividadException e) {
                mostrarMensaje("Error", "Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private boolean pedirNuevamente() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Los valores dados no corresponden, quieres intentar de nuevo:");
        alert.setContentText("Selecciona 'OK' para Sí o 'Cancelar' para No.");

        // Configurar los botones personalizados
        ButtonType buttonTypeSi = new ButtonType("OK");
        ButtonType buttonTypeNo = new ButtonType("Cancelar");

        alert.getButtonTypes().setAll(buttonTypeSi, buttonTypeNo);

        // Mostrar el cuadro de diálogo y esperar a que el usuario seleccione una opción
        Optional<ButtonType> resultado = alert.showAndWait();

        // Determinar el resultado basado en la opción seleccionada por el usuario
        return resultado.isPresent() && resultado.get() == buttonTypeSi;
    }


    private String pedirNumero() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Pedir opcion");
        dialog.setHeaderText("Elige una opción:");
        VBox headerContent = new VBox();
        Label headerLabel1 = new Label("1) Intercambiar todo");
        Label headerLabel2 = new Label("2) Intercambiar solo el contenido,");
        Label headerLabel3 = new Label("3) Intercambiar solo la actividad");
        headerContent.getChildren().addAll(headerLabel1, headerLabel2, headerLabel3);
        dialog.setGraphic(headerContent);
        dialog.setContentText("Opción:");
        // Mostrar el cuadro de diálogo y esperar a que el usuario ingrese algo
        Optional<String> nombreActividad = dialog.showAndWait();
        String nombreActividadObjetivo = nombreActividad.orElse("");
        return nombreActividadObjetivo;
    }

    public void mostrarActividades(ActionEvent event) {
        if (procesoBuscarDetalle != null) {
            ArrayList<Actividad> lista = modelFactoryController.obtenerActividadesDeProceso(procesoBuscarDetalle);
            mostrarDetallesActividades(lista);


        } else if (tareaBuscarDetalle != null) {
            ArrayList<Actividad> lista = modelFactoryController.obtenerListaActividadesDeTarea(tareaBuscarDetalle);
            mostrarDetallesActividades(lista);
        }
    }

    public void mostrarDetallesActividades(List<Actividad> actividades) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles de Actividades");
        alert.setHeaderText(null);

        VBox contenedorActividades = new VBox(); // VBox para contener los detalles de las actividades

        for (Actividad actividad : actividades) {
            VBox cuadroActividad = new VBox();
            cuadroActividad.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-padding: 10px;");

            Label labelNombre = new Label("Nombre: " + actividad.getNombre());
            Label labelDescripcion = new Label("Descripción: " + actividad.getDescripcion());
            Label labelObligatoria = new Label("Obligatoria: " + (actividad.isObligatoria() ? "Sí" : "No"));

            cuadroActividad.getChildren().addAll(labelNombre, labelDescripcion, labelObligatoria);
            contenedorActividades.getChildren().add(cuadroActividad);
        }
        alert.getDialogPane().setContent(contenedorActividades);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true); // Para mantener la ventana emergente en primer plano
        alert.showAndWait();
    }

    public void mostrarTareas(ActionEvent event) {
        if (procesoBuscarDetalle != null) {
            ArrayList<Tarea> list = modelFactoryController.obtenerTareasDeProceso(procesoBuscarDetalle);
            mostrarDetallesTareas(list);

        } else if (actividadBuscarDetalle != null) {
            ArrayList<Tarea> list = modelFactoryController.obtenerTareasDeActividad(actividadBuscarDetalle);
            mostrarDetallesTareas(list);

        }
    }

    public void mostrarDetallesTareas(List<Tarea> tareas) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles de Tareas");
        alert.setHeaderText(null);

        VBox contenedorTareas = new VBox(); // VBox para contener los detalles de las tareas

        for (Tarea tarea : tareas) {
            VBox cuadroTarea = new VBox();
            cuadroTarea.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-padding: 10px;");

            Label labelNombre = new Label("Nombre: " + tarea.getNombre());
            Label labelDescripcion = new Label("Descripción: " + tarea.getDescripcion());
            Label labelObligatoria = new Label("Obligatoria: " + (tarea.isObligatoria() ? "Sí" : "No"));
            Label labelDuracion = new Label("Duración (minutos): " + tarea.getDuracionMinutos());

            cuadroTarea.getChildren().addAll(labelNombre, labelDescripcion, labelObligatoria, labelDuracion);
            contenedorTareas.getChildren().add(cuadroTarea);
        }

        alert.getDialogPane().setContent(contenedorTareas);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true); // Para mantener la ventana emergente en primer plano

        alert.showAndWait();
    }

    public void mostrarProcesos(ActionEvent event) {
        if (tareaBuscarDetalle != null) {
            ArrayList<Proceso> list = modelFactoryController.obtenerProcesosDeTarea(tareaBuscarDetalle);
            mostrarDetallesProcesos(list);

        } else if (actividadBuscarDetalle != null) {
            ArrayList<Proceso> list = modelFactoryController.obtenerListaProcesosDeActividad(actividadBuscarDetalle);
            mostrarDetallesProcesos(list);


        }
    }

    public void mostrarDetallesProcesos(List<Proceso> procesos) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles de Procesos");
        alert.setHeaderText(null);

        VBox contenedorProcesos = new VBox(); // VBox para contener los detalles de los procesos

        for (Proceso proceso : procesos) {
            VBox cuadroProceso = new VBox();
            cuadroProceso.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-padding: 10px;");

            Label labelId = new Label("ID: " + proceso.getId());
            Label labelNombre = new Label("Nombre: " + proceso.getNombre());

            cuadroProceso.getChildren().addAll(labelId, labelNombre);
            contenedorProcesos.getChildren().add(cuadroProceso);
        }

        alert.getDialogPane().setContent(contenedorProcesos);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true); // Para mantener la ventana emergente en primer plano

        alert.showAndWait();
    }

    @FXML
    public void generarReporteProcesos(ActionEvent event) {
        List<Proceso> listaProcesos = modelFactoryController.obtenerListaProcesoUsuarioActual();

        vBoxProcesosUsuario.getChildren().clear();

        for (Proceso proceso : listaProcesos) {
            VBox cuadroProceso = new VBox();
            cuadroProceso.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-padding: 10px;");

            Label labelId = new Label("ID: " + proceso.getId());
            Label labelNombre = new Label("Nombre: " + proceso.getNombre());

            HBox botonesActividadesTareas = new HBox(); // Contenedor para los botones
            Button btnVerActividades = new Button("Ver Actividades");
            Button btnVerTareas = new Button("Ver Tareas");

            // Lógica para los botones, por ejemplo, asignar eventos o acciones
            btnVerActividades.setOnAction(e -> {
                // Lógica para mostrar actividades relacionadas con este proceso
                ArrayList<Actividad> actividads = modelFactoryController.obtenerActividadesDeProceso(proceso);
                mostrarDetallesActividades(actividads);
            });

            btnVerTareas.setOnAction(e -> {
                ArrayList<Tarea> list = modelFactoryController.obtenerTareasDeProceso(proceso);
                mostrarDetallesTareas(list);
                // Lógica para mostrar tareas relacionadas con este proceso

            });

            botonesActividadesTareas.getChildren().addAll(btnVerActividades, btnVerTareas);
            cuadroProceso.getChildren().addAll(labelId, labelNombre, botonesActividadesTareas);
            vBoxProcesosUsuario.getChildren().add(cuadroProceso);
        }
    }

    public void agregarProcesoUsuario(ActionEvent event) {
        List<UsuarioApp> listaDeProcesos = modelFactoryController.obtenerUsuariosApp(); // Obtener la lista de procesos

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Detalles del Proceso");
        alerta.setHeaderText(null);
        alerta.initStyle(StageStyle.UTILITY);
        alerta.initModality(Modality.APPLICATION_MODAL);

        GridPane cuadroProcesos = new GridPane();
        cuadroProcesos.setHgap(10);
        cuadroProcesos.setVgap(5);

        int rowIndex = 0;

        for (UsuarioApp proceso : listaDeProcesos) {
            Label labelId = new Label("Nombre Usuario: " + proceso.getNombreUsuario());
            Label labelNombre = new Label("Tipo Usuario: " + proceso.getTipoUsuario());

            String nombrebtn = "Seleccionar";
            if (listadoComboUsuarios.contains(proceso.getNombreUsuario())) {
                nombrebtn = "Deseleccionar";
            }
            Button botonSeleccionar = new Button(nombrebtn);
            if (listadoComboUsuarios.contains(proceso.getNombreUsuario())) {

                botonSeleccionar.setOnAction(e -> {
                    String nombreProceso = proceso.getNombreUsuario();
                    listadoComboUsuarios.remove(nombreProceso);
                    comboBoxUsuario.setItems(null);
                    comboBoxUsuario.setItems(listadoComboUsuarios);
                });
            } else {
                botonSeleccionar.setOnAction(e -> {
                    String nombreProceso = proceso.getNombreUsuario();
                    listadoComboUsuarios.add(nombreProceso);
                    comboBoxUsuario.setItems(null);
                    comboBoxUsuario.setItems(listadoComboUsuarios);
                });
            }


            cuadroProcesos.addRow(rowIndex,
                    labelId,               // Columna 0, Fila rowIndex
                    labelNombre,           // Columna 1, Fila rowIndex
                    botonSeleccionar);     // Columna 2, Fila rowIndex

            rowIndex++;
        }

        cuadroProcesos.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1px; -fx-padding: 10px;");

        alerta.getDialogPane().setContent(cuadroProcesos);
        alerta.showAndWait();
    }
}