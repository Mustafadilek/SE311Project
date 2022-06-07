//BERKAY SARITAŞ
//ONUR URAL
//ARİF BATUHAN YILDIRIMOĞLU
//MUSTAFA DİLEK
//OS Modeling

import java.util.ArrayList;

public class OS {
    public static void main(String[] args) {
        //OS managers
        FileSystemManager fileSystemManager = FileSystemManager.getInstance();
        ApplicationManager applicationManager = ApplicationManager.getInstance();
        OsResetManager osResetManager = OsResetManager.getInstance();

        //file system creation for a Linux system
        fileSystemManager.createFileSystem(new LinuxFileSystem(), "directory0");

        fileSystemManager.addFile("file1", "txt", "directory0");
        fileSystemManager.addDirectory("directory1", "directory0");
        fileSystemManager.displayFileSystem();

        fileSystemManager.removeElement("file1.txt");
        fileSystemManager.displayFileSystem();

        fileSystemManager.addDirectory("directory2", "directory1");
        fileSystemManager.addFile("file1", "txt", "directory0");
        fileSystemManager.addFile("file2", "txt", "directory1");
        fileSystemManager.addFile("file3", "txt", "directory2");
        fileSystemManager.displayFileSystem();

        //application interruption with devices
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

        //OS reset for a Linux system
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

        osResetManager.setResetCommands(commands);

        osResetManager.shutdown();
    }
}
