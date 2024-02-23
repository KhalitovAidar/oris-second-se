package game;

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
    }
}
