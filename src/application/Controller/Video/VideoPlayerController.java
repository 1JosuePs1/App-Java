package application.Controller.Video;

import application.DAO.Playlist.DAOPlaylistImpl;
import application.DAO.Video.DAOVideoImpl;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import static application.Controller.Playlist.ListarController.currentPlaylist;
import static application.Controller.Socket.ServerController.sendData;
import static application.Controller.Socket.ServerController.serverOn;
import static application.Controller.Video.ListarController.currentVideo;


/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 01-08-2022
 *
 * Controller destinado a la vista del dashboard principal
 */
public class VideoPlayerController implements Initializable {

    DAOVideoImpl DAOVideo = new DAOVideoImpl(); //Instancia del DAO video
    DAOPlaylistImpl DAOPlaylist = new DAOPlaylistImpl(); //Instancia del DAO playlist
    public static String lastScenne;

    // Contenedor principal de la aplicación
    @FXML
    private VBox reproductorContainer;


    // Objetos utilizados para mostrar y trabajar con el video.
    @FXML
    private MediaView mediaViewVideo;
    private MediaPlayer mediaPlayer;
    private Media mediaVideo;


    // Contiene todos los elementos debajo del video.
    @FXML
    private HBox controlesContainer;

    // El botón que reproduce, pausa y reinicia.
    @FXML
    private Button btnAccionVideo;


    // Labels que se utilizan para mostrar el tiempo actual y total del video.
    @FXML
    private Label labelTiempoActual;
    @FXML
    private Label labelTiempoTotal;


    @FXML
    private Label controlFullScreen;
    @FXML
    private Label controlVolumen;


    //Slider utilizado para cambiar el volumen.
    @FXML
    private Slider sliderVolumen;

    // Slider para controlar y rastrear el tiempo actual del video.
    @FXML
    private Slider sliderTiempo;

    @FXML
    private Label btnLike;

    //Variables para validar los estados del videos
    private boolean videoFinalizado = false;
    private boolean videoEnReproduccion = true;
    private boolean videoEnMute = false;

    // ImageViews usados como iconos para los controles del video
    private ImageView iconPlay;
    private ImageView iconPausa;
    private ImageView iconReiniciar;
    private ImageView iconVolumen;
    private ImageView iconFullScreen;
    private ImageView iconMuteado;
    private ImageView iconSalirFullScreen;
    private ImageView iconLike;
    private ImageView iconLiked;


    /**
     * Funcion para obtener la fecha y tiempo actual en el momento de ejecucion
     * @return String de la fecha
     */
    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();

        return dateFormat.format(date);
    }

    /**
     * Metodo realizar la actulizacion de ultima reproduccion del video
     */
    public void updateLastPlayedVideo () throws SQLException {
        currentVideo.setUltimaReproduccion(getCurrentDate());
        DAOVideo.updateLastPlayed(currentVideo);
    }

    /**
     * Metodo realizar la actulizacion de ultima reproduccion del playlist
     */
    public void updateLastPlayedPlaylist () throws SQLException {
        if (currentPlaylist != null) {
            currentPlaylist.setUltimaReproduccion(getCurrentDate());
            DAOPlaylist.updateLastPlayed(currentPlaylist);
        }
    }


    /**
     * Constructor del controller, utilizado para ejecutar las actulizaciones de ultimas reproducciones
     *
     */
    public VideoPlayerController() {
        //------- DAO -----------
        try {
            updateLastPlayedVideo();
            updateLastPlayedPlaylist();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initMediaIcons();

        setVideoDefaultSettings();

        initActionControls();
        initTimeControls();
        initVolumeControls();
        initResponsive();

        setVideoLikeControls();
    }



    /**
     * Metodo que se encarga de la construcciones de los iconos del reproductor
     *
     */
    public void initMediaIcons() {
        //Define iconos variables para botones del reproductor -
        //Usados para los controles los cuales cambian segun el estado del video
        //Ruta y propiedades de Height y Width
        iconPlay = new ImageView(new Image(new File("src/resources/images/play-btn.png").toURI().toString()));
        iconPlay.setFitWidth(35);
        iconPlay.setFitHeight(35);

        //icono pausa
        iconPausa = new ImageView(new Image(new File("src/resources/images/stop-btn.png").toURI().toString()));
        iconPausa.setFitHeight(35);
        iconPausa.setFitWidth(35);

        //icono reiniciar
        iconReiniciar = new ImageView(new Image(new File("src/resources/images/restart-btn.png").toURI().toString()));
        iconReiniciar.setFitWidth(35);
        iconReiniciar.setFitHeight(35);

        //icono volumen
        iconVolumen = new ImageView(new Image(new File("src/resources/images/volume.png").toURI().toString()));
        iconVolumen.setFitWidth(35);
        iconVolumen.setFitHeight(35);

        //icono full screen
        iconFullScreen = new ImageView(new Image(new File("src/resources/images/fullscreen.png").toURI().toString()));
        iconFullScreen.setFitHeight(35);
        iconFullScreen.setFitWidth(35);

        //icono mute
        iconMuteado = new ImageView(new Image(new File("src/resources/images/mute.png").toURI().toString()));
        iconMuteado.setFitWidth(35);
        iconMuteado.setFitHeight(35);

        // icono salir de full screen
        iconSalirFullScreen = new ImageView(new Image(new File("src/resources/images/exitscreen.png").toURI().toString()));
        iconSalirFullScreen.setFitHeight(35);
        iconSalirFullScreen.setFitWidth(35);

        // icono dar like a video
        iconLike = new ImageView(new Image(new File("src/resources/images/like.png").toURI().toString()));
        iconLike.setFitHeight(35);
        iconLike.setFitWidth(35);

        // icono video likeado
        iconLiked = new ImageView(new Image(new File("src/resources/images/liked.png").toURI().toString()));
        iconLiked.setFitHeight(35);
        iconLiked.setFitWidth(35);

    }

    /**
     * Metodo que define las configuraciones iniciales del video
     *
     */
    public void setVideoDefaultSettings() {
        // Para crear el media player se debe tener una estructura de objectos anidados
        // media, media player, y media view.
        // el media player envuelve el media y el media view envuelve el media player.
        //Pasar por aca la ruta del video que sera recibido por parametro
        //Reemplazar luego
        mediaVideo = new Media(new File(currentVideo.getRuta()).toURI().toString());
        mediaPlayer = new MediaPlayer(mediaVideo);
        mediaViewVideo.setMediaPlayer(mediaPlayer);


        mediaPlayer.play();//Inicia la reproduccion del video al cargar la vista

        if (serverOn) {
            try {
                sendData("--set:"+currentVideo.getRuta());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        btnAccionVideo.setGraphic(iconPausa); //Al iniciar el botón de accion debe tener el icono de pausa porque se está reproduciendo.
        controlVolumen.setGraphic(iconVolumen); //Setea el icono de volumen
        sliderVolumen.setValue(1); //Setea full volumen al iniciar el video
        controlFullScreen.setGraphic(iconFullScreen); //El video no comienza en full screen, setea el icono para pasar a full screen

        if (currentVideo.getCalificacion() == 1) {
            btnLike.setGraphic(iconLiked);
        } else {
            btnLike.setGraphic(iconLike);
        }
    }

    /**
     * Metodo que se encarga de las acciones que realiza el boton de accion princial, para validar si debe iniciar, pausar o reiniciar el video
     *
     */
    public void initActionControls () {
        //Funcion para dar accion al video (Play, Pausa, Reinciar)
        btnAccionVideo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //obtiene el btn de accion
                Button btn = (Button) actionEvent.getSource();

                sicronizarLabelTiempoActual();

                // Si el video termino pasa el tiempo a 0 y reinicia las variables de validacion
                if (videoFinalizado) {
                    sliderTiempo.setValue(0);
                    videoFinalizado = true;
                    videoEnReproduccion = false;

                    System.out.println("aqui");

                    if (serverOn) {
                        try {
                            sendData("--set:"+currentVideo.getRuta());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                // Si el video se está reproduciendo pausa el video y setea el icono de play al btn de accion
                if (videoEnReproduccion) {
                    btn.setGraphic(iconPlay);
                    mediaPlayer.pause();
                    videoEnReproduccion = false;

                    if (serverOn) {
                        try {
                            sendData("--pause");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    // Si el video esta pausado activa el play y setea el icono de pausa al btn de accion
                    btn.setGraphic(iconPausa);
                    mediaPlayer.play();
                    videoEnReproduccion = true;

                    if (serverOn) {
                        try {
                            sendData("--play");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

     /**
     * Metodo que se encarga de los controles de tiempo del video en reproduccion
    * */
    public void initTimeControls () {
        /*-------- CONTROL DE TIEMPO DEL VIDEOPLAYER -------------- */

        /**
         * Esto verifica la duración del video adjunto al reproductor .
         * Si el video activa su reproduccion el control de tiempo cambia tambien
         */
        mediaPlayer.totalDurationProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldDuration, Duration newDuration) {
                // newDuration es el tiempo del video actual, oldDuration es la duración del video anterior - El tiempo se establece en milisegundos
                sliderTiempo.setMax(newDuration.toSeconds());
                labelTiempoTotal.setText(getTimeVideo(newDuration));
            }
        });

        // La propiedad valueChanging indica si el control de tiempo está en proceso de cambio.
        // Cuando es verdadero, indica que el valor actual del control  está cambiando.
        sliderTiempo.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging, Boolean isChanging) {
                sicronizarLabelTiempoActual();
                //Cuando el control no interactura con el usuario configura el video al tiempo actual del control
                if (!isChanging) {
                    mediaPlayer.seek(Duration.seconds(sliderTiempo.getValue()));
                }
            }
        });

        // ValueProperty() es el valor actual del control de tiempo. Este valor debe estar siempre entre min y max.
        sliderTiempo.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                sicronizarLabelTiempoActual();
                // obtiene el tiempo actual del video en segundos.
                double currentTime = mediaPlayer.getCurrentTime().toSeconds();
                if (Math.abs(currentTime - newValue.doubleValue()) > 0.5) {
                    mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                }
                validarVideoTerminado(labelTiempoActual.getText(), labelTiempoTotal.getText());
            }
        });


        //Funcion que ejecuta el cambio del slider de tiempo segun avance el tiempo de reproduccion del video
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldTime, Duration newTime) {
                sicronizarLabelTiempoActual();
                if (!sliderTiempo.isValueChanging()) {
                    sliderTiempo.setValue(newTime.toSeconds());
                }
                validarVideoTerminado(labelTiempoActual.getText(), labelTiempoTotal.getText());
            }
        });

    }

    /**
     * Metodo que se encarga de los controles de volumen del video en reproduccion
     * */
    public void initVolumeControls () {
        /*-------- VOLUMEN DEL VIDEO PLAYER -------------- */

        // Vincula el volumen del video al control de volumen de la vista
        mediaPlayer.volumeProperty().bindBidirectional(sliderVolumen.valueProperty());
        sliderVolumen.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                //Establece el volumen del video al valor del control deslizante.
                mediaPlayer.setVolume(sliderVolumen.getValue());

                //Valida que el video no este muteado, de ser asi cambia el icono a mute
                if (mediaPlayer.getVolume() != 0.0) {
                    controlVolumen.setGraphic(iconVolumen);
                    videoEnMute = false;
                } else {
                    controlVolumen.setGraphic(iconMuteado);
                    videoEnMute = true;
                }
            }
        });

         /*
           Segun se haga click en el icono de volumen valida si esta muteado, de ser asi
           setea el icono de volumen y establece al full, si no esta muteado
           setea el icono de mutea y el volumen a 0
           SOLO ACEPTA VALORES DE 0.0 A 1
         */
        controlVolumen.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (videoEnMute) {
                    controlVolumen.setGraphic(iconVolumen);
                    sliderVolumen.setValue(1);
                    videoEnMute = false;
                }
                else {
                    controlVolumen.setGraphic(iconMuteado);
                    sliderVolumen.setValue(0);
                    videoEnMute = true;
                }
            }
        });
    }

    /**
     * Metodo que se encarga del Responsive y Full Screen del reproductor
     * */
    public void initResponsive() {
        //------- Responsive y Full Screen -----------

        // Vincula el height del reproductor al height de la escena menos la altura de los controles
        // del reproductor facilitar el responsive y el cambio a full screen
        reproductorContainer.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observableValue, Scene scene, Scene newScene) {
                if (scene == null && newScene != null) {
                    // Match the height of the video to the height of the scene minus the hbox controls height.
                    mediaViewVideo.fitHeightProperty().bind(newScene.heightProperty().subtract(controlesContainer.heightProperty().add(20)));
                }
            }
        });

        //Funcion para el full screen
        /*
         On click valida si la escena esta en full screen,
         de ser asi pasa la escena a ventana y setea el icono de full screen o viceversa
        */
        controlFullScreen.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Label label = (Label) mouseEvent.getSource();
                Stage stage = (Stage) label.getScene().getWindow();
                if (stage.isFullScreen()) {
                    stage.setFullScreen(false);
                    controlFullScreen.setGraphic(iconFullScreen);
                } else {
                    stage.setFullScreen(true);
                    controlFullScreen.setGraphic(iconSalirFullScreen);
                }
            }
        });
    }

    /**
     * Metodo que se encarga del control de likes al video en reproduccion
     * */
    public void setVideoLikeControls () {
        btnLike.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                try {

                    if (currentVideo.getCalificacion() == 1) {
                        btnLike.setGraphic(iconLike);
                        currentVideo.setCalificacion(0);
                    } else {
                        btnLike.setGraphic(iconLiked);
                        currentVideo.setCalificacion(1);
                    }

                    DAOVideo.like(currentVideo);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    /**
     * Esta función recibe el tiempo del video y calcula los segundos, minutos y horas.
     * Devuelve las horas, minutos y segundos del video.
     * El tiempo se da en milisegundos. (Por ejemplo, 750,0 ms).
     * @param time - El tiempo del video
     * @return String de los segundos, minutos y horas.
     */
    public String getTimeVideo(Duration time) {

        int horas = (int) time.toHours();
        int minutos = (int) time.toMinutes();
        int segundos = (int) time.toSeconds();

        // Fix problema del tiempo al tener 61 segundos, minutos u horas
        if (segundos > 59)  {
            segundos %= 60;
        }
        if (minutos > 59) {
            minutos %= 60;
        }
        if (horas > 59) {
            horas %= 60;
        }

        //Retorna el formato de establecido segun los valores obtenidos
        return String.format("%d:%02d:%02d", horas, minutos, segundos);
    }


    //Funcion para sincronizar el texto de la etiqueta de tiempo actual con el tiempo actual del video.
    public void sicronizarLabelTiempoActual() {
        labelTiempoActual.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() {
                return getTimeVideo(mediaPlayer.getCurrentTime()) + " / ";
            }
        }, mediaPlayer.currentTimeProperty()));
    }

    // Funcion para validar si el video ha terminado
    // Valida que el texto de los labels de tiempo coincida de ser asi el video termino
    public void validarVideoTerminado(String tiempoActual, String tiempoTotal) {

        //Recorra las letras del las etiquetas para igualarlas
        for (int i = 0; i < tiempoTotal.length(); i++) {

            //Si el video no ha terminado setea la variable indicadora a false y revisa si se esta reproducciendo o no
            if (tiempoActual.charAt(i) != tiempoTotal.charAt(i)) {
                videoFinalizado = false;
                if (videoEnReproduccion) {
                    btnAccionVideo.setGraphic(iconPausa);
                }
                else {
                    btnAccionVideo.setGraphic(iconPlay);
                }
                break;
            } else {
                //Si la validacion fue true setea la variable indicadora a true y el icono de accion a reiniciar
                videoFinalizado = true;
                btnAccionVideo.setGraphic(iconReiniciar);
            }
        }
    }

    /**
     * Metodo utilizado para volver a la ultima vista del usuario
     * */
    public void goBack() throws IOException {
        if (serverOn) {
            try {
                sendData("--exit");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Parent root = FXMLLoader.load(this.getClass().getResource("/application/View/"+lastScenne));
        Scene subScene = new Scene(root);
        Stage primaryStage = (Stage) this.reproductorContainer.getScene().getWindow();
        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();
    }
}
