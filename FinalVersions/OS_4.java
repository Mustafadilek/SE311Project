import java.util.ArrayList;
import java.util.Calendar;

public class OS_4 {

    public static void main(String[] args) {

        ArrayList<Command> commands = new ArrayList<>();

        OsDevice hardDisk = new HardDisk("Hard Disk 1", new LinuxIOAdapter(new LinuxFileSystemIO()),
                new LinuxFile("LinuxFile", "txt"));
        OsDevice cpu = new CPU("CPU 1");
        OsDevice ioDevice = new IODevice("I/O Device 1");

        Command command = new DeviceResetCommand(hardDisk);
        Command command1 = new DeviceResetCommand(cpu);
        Command command2 = new DeviceResetCommand(ioDevice);

        commands.add(command);
        commands.add(command1);
        commands.add(command2);

        OsResetManager os = OsResetManager.getInstance();
        os.setResetCommands(commands);

        os.shutdown();
    }

}

abstract class OsDevice{

    protected String name;

    private ArrayList<String> resetLog;

    public OsDevice(String name) {
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

class CPU extends OsDevice{


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

class HardDisk extends OsDevice{

    private final DpIO dpIO;
    private File file;

    public HardDisk(String name, DpIO dpIO, File file) {
        super(name);
        this.dpIO = dpIO;
        this.file = file;
    }

    @Override
    void prepareToReset() {
        writeData();
        closeFiles();
    }

    private void writeData(){
        System.out.println("Writing data which are in the buffer...");

        if (file != null)
            dpIO.fprintf(file, "Buffer Data");
    }
    private void closeFiles(){
        System.out.println("Closing files...");
    }
}

class IODevice extends OsDevice{


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

class DeviceResetCommand implements Command{

    private OsDevice device;

    public DeviceResetCommand(OsDevice device) {
        this.device = device;
    }

    @Override
    public void Execute() {
        device.reset();
    }
}

class OsResetManager {

    private ArrayList<Command> resetCommands;
    private static OsResetManager instance = new OsResetManager();

    private OsResetManager() {  }
    public static OsResetManager getInstance() { return instance; }

    public void shutdown(){
        System.out.println("\nResetting OS...\n");

        for(Command command : resetCommands)
            command.Execute();

        System.out.println("OS reset is complete.");
    }

    public void setResetCommands(ArrayList<Command> commands) { resetCommands = commands; }

}
