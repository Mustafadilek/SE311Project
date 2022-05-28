import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ObserverPattern {
    public static void main(String[] args) {
        ArrayList<Application> apps = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            apps.add(new Application("TestApp" + (i + 1), i));

        Device keyboard = new Keyboard();
        Device mouse = new Mouse();
        keyboard.interrupt();
        mouse.interrupt();

        keyboard.Register(apps);
        mouse.Register(apps.get(0));
        keyboard.interrupt();
        mouse.interrupt();

        keyboard.Unregister(apps.get(1));
        keyboard.interrupt();
        mouse.interrupt();

        keyboard.UnregisterAll();
        mouse.UnregisterAll();
    }
}

abstract class Device {
    protected String data;
    private ArrayList<Application> applications = new ArrayList<>();

    public void Register(Application application) { applications.add(application); }

    public void Register(List<Application> applications) { this.applications.addAll(applications); }

    public void Unregister(Application application) {
        for (int i = 0; i < applications.size(); i++) {
            if (applications.get(i).getName().equals(application.getName())) {
                applications.get(i).setDevice(null);
                applications.remove(i);
                return;
            }
        }
    }

    public void UnregisterAll() {
        for (Application application : applications)
            application.setDevice(null);

        applications.clear();
    }

    public void Notify() {
        Application prioritized_app = null;

        for (Application application : applications) {
            if (prioritized_app == null || application.getPriority() > prioritized_app.getPriority())
                prioritized_app = application;
        }

        if (prioritized_app != null)
            prioritized_app.ConsumeData(this);
        else
            System.out.println("No registered applications were found.");
    }

    public String getData() { return data; }

    abstract public void interrupt();
}

class Keyboard extends Device {
    private Scanner scanner = new Scanner(System.in);

    public void interrupt() {
        System.out.println("Please enter a character input from the keyboard.");
        data = Character.toString(scanner.nextLine().charAt(0));

        Notify();
    }
}

class Mouse extends Device {
    public void interrupt() {
        data = "Click";

        Notify();
    }
}

class Application {
    private final String name;
    private int priority = 0;

    private String device_data;
    private Device device;

    public Application(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public Application(String name) {
        this.name = name;
    }

    public void ConsumeData(Device device) {
        this.device = device;
        device_data = device.getData();

        System.out.println("Data \"" + device_data + "\" has been consumed by " + name);
    }

    public String getName() { return name; }
    public void setPriority(int priority) { this.priority = priority; }
    public int getPriority() { return priority; }

    public String getDeviceData() { return device_data; }
    public void setDevice(Device device) { this.device = device; }
    public Device getDevice() { return device; }
}
