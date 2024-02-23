package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Application {
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
                    System.out.println("Новое подключение");
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    while (true) {
                        int data = clientSocket.getInputStream().read();
                        if (data == -1) {
                            break;
                        }

                        System.out.print((char) data);
                        StringBuilder sb = new StringBuilder();
                        sb.append((char) data).append("\n");

                        String result = sb.toString();

                        Platform.runLater(() -> {
                            textArea.appendText(String.valueOf((char) data).concat("\n"));
                        });
                    }

                    out.close();
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