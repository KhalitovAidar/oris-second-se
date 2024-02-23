package game;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public abstract class Controller {

    private final List<ControllerListener> controllerListeners;

    public Controller() {
        this.controllerListeners = new LinkedList<>();
    }

    public final void addListener(ControllerListener controllerListener) {
        this.controllerListeners.add(controllerListener);
    }

    public final void sendEvent(ControllerEvent controllerEvent) {
        this.controllerListeners.forEach(
                controllerListener -> controllerListener.update(controllerEvent));

        try {
            Socket socket = new Socket("localhost", 12345);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("message", controllerEvent.toString());

            out.println(jsonMessage);
            socket.close();
        } catch (IOException e) {
            System.out.println("Ошибка при отправке на сервер");
        }
    }
}
