import java.util.ArrayList;
import java.util.Calendar;

public class App {

    public static void main(String[] args) {

        ArrayList<Command> commands = new ArrayList<>();

        Device hardDisk = new HardDisk("Hard Disk 1");
        Device cpu = new CPU("CPU 1");
        Device ioDevice = new IODevice("I/O Device 1");

        Command command = new DeviceCommand(hardDisk);
        Command command1 = new DeviceCommand(cpu);
        Command command2 = new DeviceCommand(ioDevice);

        commands.add(command);
        commands.add(command1);
        commands.add(command2);

        OS os = new OS(commands);
        os.shutdown();
    }

}
abstract class Device{

    protected String name;

    private ArrayList<String> resetLog;

    public Device(String name) {
        this.name = name;
        resetLog = new ArrayList<>();
    }

    public void reset(){

       showInfo();
       prepareToReset();
       logDeviceReset();

    }

    private void logDeviceReset(){

        String log = name + " reset at " + Calendar.getInstance().getTime();
        resetLog.add(log);
        System.out.println(log);
        System.out.println("Reset has been logged.\n");

    }

    private void showInfo(){
        System.out.println("Reseting " + name + "...");
    }
    abstract void prepareToReset();

    public ArrayList<String> getResetLog() {
        return resetLog;
    }
}
class CPU extends Device{


    public CPU(String name) {
        super(name);
    }

    @Override
    void prepareToReset() {
        terminateProcesses();
    }

    private void terminateProcesses(){
        System.out.println("Terminating all processes...");
    }
}
class HardDisk extends Device{


    public HardDisk(String name) {
        super(name);
    }

    @Override
    void prepareToReset() {
        writeData();
        closeFiles();
    }

    private void writeData(){
        System.out.println("Writing data which are in the buffer...");
    }
    private void closeFiles(){
        System.out.println("Closing files...");
    }
}
class IODevice extends Device{


    public IODevice(String name) {
        super(name);
    }

    @Override
    void prepareToReset() {
        disconnect();
        uninstall();
    }

    private void disconnect(){
        System.out.println("Disconnecting...");
    }
    private void uninstall(){
        System.out.println("Uninstalling...");
    }
}

interface Command{

    void Execute();

}

class DeviceCommand implements Command{

    private Device device;

    public DeviceCommand(Device device) {
        this.device = device;
    }

    @Override
    public void Execute() {
        device.reset();
    }
}

class OS{

    private ArrayList<Command> commands;

    public OS(ArrayList<Command> commands) {
        this.commands = commands;
    }

    public void shutdown(){
        for(Command command : commands)
            command.Execute();
    }

}
