import java.util.ArrayList;
abstract class FileSystemElement {
    protected String name;
    abstract void Add(FileSystemElement element, String parentDirectoryName);
    abstract void Remove(String elementName);
    abstract public void Display(int indent);
    public String displayName() { return name; }
}
abstract class FileSystem {
    abstract public File createFile(String name, String extension);
    abstract public Directory createDirectory(String name);
}
class LinuxFileSystem extends FileSystem {

    @Override
    public File createFile(String name, String extension) {
        return new LinuxFile(name, extension);
    }

    @Override
    public Directory createDirectory(String name) {
        return new LinuxDirectory(name);
    }
}
class BSDFileSystem extends FileSystem {

    @Override
    public File createFile(String name, String extension) {
        return new BSDFile(name, extension);
    }

    @Override
    public Directory createDirectory(String name) {
        return new BSDDirectory(name);
    }
}
class NTFileSystem extends FileSystem {

    @Override
    public File createFile(String name, String extension) {
        return new NTFile(name, extension);
    }

    @Override
    public Directory createDirectory(String name) {
        return new NTDirectory(name);
    }
}
abstract class File extends FileSystemElement {
    protected String extension;
    public File(String name, String extension) {
        this.name = name;
        this.extension = extension;
    }
    void Add(FileSystemElement element, String parentDirectoryName) {  }
    void Remove(String elementName) {  }
    public void Display(int indent) {
        for (int i = 0; i < indent; i++)
            System.out.print("-");
        System.out.println(" " + name);
    }
    public String displayName(){return name + "." + extension;}
}
class LinuxFile extends File{
    public LinuxFile(String name, String extension){
        super(name, extension);
        System.out.println("Linux file created");
    }
}
class BSDFile extends File{
    public BSDFile(String name, String extension){
        super(name, extension);
        System.out.println("BSD file created");
    }
}
class NTFile extends File{
    public NTFile(String name, String extension){
        super(name, extension);
        System.out.println("NT file created");
    }
}
abstract class Directory extends FileSystemElement {
    protected ArrayList<FileSystemElement> elements = new ArrayList<>();
    public Directory(String name) { this.name = name; }
    public void Add(FileSystemElement element, String parentDirectoryName) {
        if (parentDirectoryName.equals(name)) {
            elements.add(element);
            return;
        }
        for (FileSystemElement fileSystemElement : elements)
            fileSystemElement.Add(element, parentDirectoryName);
    }
    public void Remove(String elementName) {
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).displayName().equals(elementName)) {
                elements.remove(i);
                return;
            }
        }
        System.out.println(elementName + " could not found.");
    }
    public void Display(int indent) {
        for (int i = 0; i < indent; i++)
            System.out.print("-");
        System.out.println("+ " + name);
        for (FileSystemElement element : elements)
            element.Display(indent + 2);
    }
}
class LinuxDirectory extends Directory{
    public LinuxDirectory(String name){
        super(name);
        System.out.println("Linux Directory created");
    }
}
class BSDDirectory extends Directory{
    public BSDDirectory(String name){
        super(name);
        System.out.println("BSD Directory created");
    }
}
class NTDirectory extends Directory{
    public NTDirectory(String name){
        super(name);
        System.out.println("NFT Directory created");
    }
}
class FileSystemManager {
    private FileSystem fileSystem;
    private FileSystemElement rootDirectory;
    private static final FileSystemManager instance = new FileSystemManager();
    private FileSystemManager() {  }
    public static FileSystemManager getInstance() { return instance; }
    public void createFileSystem(FileSystem fileSystem, String rootDirectoryName) {
        this.fileSystem = fileSystem;
        rootDirectory = fileSystem.createDirectory(rootDirectoryName);
    }
    public void addFile(String fileName, String fileExtension, String parentDirectoryName) {
        if (fileSystem == null) {
            System.out.println("Please first create a file system.");
            return;
        }
        rootDirectory.Add(fileSystem.createFile(fileName, fileExtension), parentDirectoryName);
    }
    public void addDirectory(String directoryName, String parentDirectoryName) {
        if (fileSystem == null) {
            System.out.println("Please first create a file system.");
            return;
        }
        rootDirectory.Add(fileSystem.createDirectory(directoryName), parentDirectoryName);
    }
    public void removeElement(String elementName) { rootDirectory.Remove(elementName); }
    public void displayFileSystem() {
        if (rootDirectory == null) {
            System.out.println("Please first create a file system.");
            return;
        }
        System.out.println("\nDisplaying file system");
        rootDirectory.Display(1);
        System.out.println();
    }
}
public class OsSystem_1 {
    public static void main(String[] args) {
        FileSystemManager fileSystemManager = FileSystemManager.getInstance();
        FileSystem linux = new LinuxFileSystem();
        FileSystem bsd = new BSDFileSystem();
        FileSystem nt = new NTFileSystem();
        fileSystemManager.createFileSystem(bsd, "test");
        fileSystemManager.createFileSystem(nt, "test");
        fileSystemManager.createFileSystem(linux, "d0");
        fileSystemManager.addFile("f1", "txt", "d0");
        fileSystemManager.addDirectory("d1", "d0");
        fileSystemManager.displayFileSystem();
        fileSystemManager.removeElement("f1");
        fileSystemManager.displayFileSystem();
        fileSystemManager.addDirectory("d2", "d1");
        fileSystemManager.addFile("f1", "txt", "d0");
        fileSystemManager.addFile("f2", "txt", "d1");
        fileSystemManager.addFile("f3", "txt", "d2");
        fileSystemManager.displayFileSystem();
    }
}
