import java.util.ArrayList;

public class ObserverPattern {
    public static void main(String[] args) {
        User[] users = new User[5];
        for (int i = 0; i < users.length; i++)
            users[i] = new User("user" + (i + 1));

        Controller controller = new AirConditionerSystem(23.4);
        for (User user : users)
            controller.Attach(user);

        for (int i = 0; i < users.length; i++) {
            controller.setTemperature(22.3, true);
            controller.setTemperature(22.3, true);
            controller.setTemperature(22.3, true);
            controller.setTemperature(22.3, true);
        }

        for (User user : users)
            user.setController(controller);

        for (User user : users) {
            user.setControllerTemperature(22.3);
            user.setControllerTemperature(22.3);
            user.setControllerTemperature(22.3);
            user.setControllerTemperature(22.3);
        }

        controller.Detach(users[0]);
        users[0].setControllerTemperature(26.4);

        System.out.println(controller.getCounter());

        controller.DetachAll();
    }
}

abstract class Controller {
    protected int notify_counter;
    protected double temperature;
    protected ArrayList<User> users = new ArrayList<>();

    public Controller(double temperature) { this.temperature = temperature; }

    public void Attach (User user) { users.add(user); }

    public void Detach (User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getName().equals(user.getName())) {
                users.get(i).setController(null);
                users.remove(i);
                return;
            }
        }
    }

    public void DetachAll() {
        for (User user : users)
            user.setController(null);

        users.clear();
    }

    public void Notify() {
        for (User user : users) {
            user.Update(this);
            notify_counter++;
        }
    }

    public int getCounter() { return notify_counter; }
    public double getTemperature() { return temperature; }

    abstract public void setTemperature(double new_temperature, Boolean notify);
}

class AirConditionerSystem extends Controller {

    public AirConditionerSystem(double temperature) { super(temperature); }

    public void setTemperature(double new_temperature, Boolean notify) {
        temperature = new_temperature;

        if (notify)
            Notify();
    }
}

interface Observer {
    void Update(Controller controller);
}

class User implements Observer {
    private final String user_name;
    private static int direct_temperature_change;

    private double controller_temperature;
    private Controller controller;

    public User(String name) { user_name = name; }

    public void Update(Controller controller) {
        this.controller = controller;
        controller_temperature = controller.getTemperature();

        System.out.println("Notified " + user_name + " of controller's temperature change to " + controller_temperature);
    }

    public void setControllerTemperature(double new_temperature) {
        if (controller == null) {
            System.out.println("No controller found for " + user_name);
            return;
        }

        controller.setTemperature(new_temperature, false);

        if (++User.direct_temperature_change % 4 == 0)
            controller.Notify();
    }

    public String getName() { return user_name; }
    public double getControllerTemperature() { return controller_temperature; }
    public Controller getController() { return controller; }
    public void setController(Controller controller) { this.controller = controller; }
}
