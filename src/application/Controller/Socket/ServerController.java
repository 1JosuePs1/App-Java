package application.Controller.Socket;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Fabricio, Alejandro, Josue
 * @version 1.0
 * @since 20-08-2022
 *
 * Controller destinado al Servidor
 */
public class ServerController extends Thread   {


    @FXML
    private Button btnCloseServer;
    @FXML
    private Text lblClientOnline;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private static DataOutputStream out;
    public static boolean serverOn = false;


    /**
     * Metodo para editar el playlist seleccionado por el usuario lanzando la vista de editar playlist
     * @param port int, referencia al puerto donde se inicializara el socket
     */
    public void initServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        clientOnline();
        out = new DataOutputStream(clientSocket.getOutputStream());
    }

    /**
     * Metodo utilizado enviar Strings haciendo uso del DataOutputStream
     * */
    public static void sendData(String msj) throws IOException {
        out.writeUTF(msj);
    }

    /**
     * Metodo utilizado para cerrar el socket inicializado
     * */
    public void stopServer() throws IOException {

        if (clientSocket != null) {
            out.close();

            clientSocket.close();
            serverSocket.close();

            System.out.println("Servidor cerrado");
        }
        serverSocket = null;
        Stage stage = (Stage) btnCloseServer.getScene().getWindow();
        stage.close();
    }

    /**
     * Metodo utilizado para mostrar en la vista de servidor que se recibio una conexion
     * */
    public void clientOnline () {
        serverOn = true;

        lblClientOnline.setText("Conexion recibida");
        System.out.println("Conexion Recibida de: " + clientSocket.getInetAddress());
    }

    @Override
    public void run() {
        try {
            initServer(12345);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor del controller utilizado para inicializar  un nuevo hilo para el proceso del socket
     * */
    public ServerController () {
        start();
    }

}
