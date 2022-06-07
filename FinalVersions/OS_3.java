import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OS_3 {
    public static void main(String[] args) {
        ApplicationManager applicationManager = ApplicationManager.getInstance();
        ArrayList<Application> apps = applicationManager.getApplications();
        ArrayList<Device> devices = applicationManager.getDevices();

        for (int i = 0; i < 2; i++)
            apps.add(new Application("TestApp" + (i + 1), i));

        devices.add(new Keyboard("keyboard"));
        devices.add(new Mouse("mouse"));
        devices.add(new NetworkPort("network port"));

        for (Device device : devices)
            device.interrupt();

        devices.get(0).Register(apps);
        devices.get(1).Register(apps.get(0));
        devices.get(2).Register(apps.get(1));
        for (Device device : devices)
            device.interrupt();

        devices.get(0).Unregister(apps.get(1));
        for (Device device : devices)
            device.interrupt();

        for (Device device : devices)
            device.UnregisterAll();
    }
}

abstract class Device {
    protected String name, data;
    private final ArrayList<Application> applications = new ArrayList<>();

    public Device(String name) { this.name = name; }

    public void Register(Application application) {
        applications.add(application);

        System.out.println(getName() + " has been registered with " + application.getName());
    }

    public void Register(List<Application> applications) {
        this.applications.addAll(applications);

        System.out.print(getName() + " has been registered with");
        for (Application application : applications)
            System.out.print(", " + application.getName());
        System.out.println();
    }

    public void Unregister(Application application) {
        for (int i = 0; i < applications.size(); i++) {
            if (applications.get(i).getName().equals(application.getName())) {
                System.out.println(getName() + " has been unregistered with " + application.getName());
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
        System.out.println(getName() + " has been unregistered with all applications");
    }

    public void Notify() {
        Application prioritized_app = GetPrioritizedApp();

        if (prioritized_app != null)
            prioritized_app.ConsumeData(this);
        else
            System.out.println("No registered applications were found.");
    }

    private Application GetPrioritizedApp() {
        Application prioritized_app = null;

        for (Application application : applications) {
            if (prioritized_app == null || application.getPriority() > prioritized_app.getPriority())
                prioritized_app = application;
        }

        return prioritized_app;
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public String getData() { return data; }

    abstract public void interrupt();
}

class NetworkPort extends Device {
    public NetworkPort(String name) { super(name); }

    public void interrupt() {
        data = "Network Port Data";

        Notify();
    }
}

class Keyboard extends Device {
    private final Scanner scanner = new Scanner(System.in);

    public Keyboard(String name) { super(name); }

    public void interrupt() {
        System.out.println("\nPlease enter a character input from the keyboard.");
        data = Character.toString(scanner.nextLine().charAt(0));

        Notify();
    }
}

class Mouse extends Device {
    public Mouse(String name) { super(name); }

    public void interrupt() {
        data = "Click";

        Notify();
    }
}

interface Observer {
    void ConsumeData(Device device);
}

class Application implements Observer {
    private final String name;
    private int priority = 0;

    private String device_data;
    private Device device;

    public Application(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public Application(String name) { this.name = name; }

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

class ApplicationManager {
    private ArrayList<Application> applications = new ArrayList<>();
    private ArrayList<Device> devices = new ArrayList<>();

    private static ApplicationManager instance = new ApplicationManager();

    private ApplicationManager() {  }
    public static ApplicationManager getInstance() { return instance; }

    public ArrayList<Application> getApplications() { return applications; }
    public ArrayList<Device> getDevices() { return devices; }
}
