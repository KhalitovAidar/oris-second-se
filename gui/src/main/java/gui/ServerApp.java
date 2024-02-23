package gui;

import org.json.JSONObject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp extends Application {
    private TextArea textArea;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        textArea = new TextArea();
        textArea.setEditable(false);

        Scene scene = new Scene(textArea, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Server Log");
        primaryStage.show();

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(12345);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        JSONObject jsonMessage = new JSONObject(inputLine);
                        String message = jsonMessage.getString("message");

                        System.out.println(message);

                        Platform.runLater(() -> {
                            textArea.appendText(message.concat("\n"));
                        });
                    }

                    in.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}