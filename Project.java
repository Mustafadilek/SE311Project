import java.util.ArrayList;
import java.util.Scanner;
interface FileElement{
    void AccessSubFiles(FileElement d);
}
abstract class Part{
    abstract public String displayName();
    abstract public String displayExtension();
}
abstract class OSFactory{
    abstract public File createFile();
    abstract public Directory createDirectory();
}
class Linux extends OSFactory{

    @Override
    public File createFile() {
        return new LinuxFile();
    }

    @Override
    public Directory createDirectory() {
        return new LinuxDirectory();
    }
}
class BSD extends OSFactory{

    @Override
    public File createFile() {
        return new BSDFile();
    }

    @Override
    public Directory createDirectory() {
        return new BSDDirectory();
    }
}
class NT extends OSFactory{

    @Override
    public File createFile() {
        return new NTFile();
    }

    @Override
    public Directory createDirectory() {
        return new NTDirectory();
    }
}
abstract class File extends Part implements FileElement{
    protected String name;
    protected String extension;
    public String displayName(){
        return name;
    }
    public String displayExtension(){return extension;}
}
class LinuxFile extends File{
    public LinuxFile(){
        Scanner sc= new Scanner(System.in);
        name = new String("Linux File");
        System.out.println("Enter File extension: ");
        extension=sc.next();
        System.out.println("Linux file created");
    }

    @Override
    public void AccessSubFiles(FileElement d) {
        System.out.println("We cannot access sub-files !");
    }
}
class BSDFile extends File{
    public BSDFile(){
        Scanner sc= new Scanner(System.in);
        name = new String("BSD File");
        System.out.println("Enter File extension: ");
        extension=sc.next();
        System.out.println("BSD file created");
    }

    @Override
    public void AccessSubFiles(FileElement d) {
        System.out.println("We cannot access sub-files !");
    }
}
class NTFile extends File{
    public NTFile(){
        Scanner sc= new Scanner(System.in);
        name = new String("NT File");
        System.out.println("Enter File extension: ");
        extension=sc.next();
        System.out.println("NT file created");
    }

    @Override
    public void AccessSubFiles(FileElement d) {
        System.out.println("We cannot access sub-files !");
    }
}
abstract class Directory extends Part implements FileElement{
    protected String name;
    protected String extension;
    public String displayName(){
        return name;
    }
    public String displayExtension(){
        return extension;
    }

}
class LinuxDirectory extends Directory{
    public LinuxDirectory(){
        Scanner sc= new Scanner(System.in);
        name = new String("Linux Directory");
        System.out.println("Enter Directory extension: ");
        extension=sc.next();
        System.out.println("Linux Directory created");
    }

    @Override
    public void AccessSubFiles(FileElement d) {

    }
    private	ArrayList<FileElement> elements = new ArrayList<FileElement>();
}
class BSDDirectory extends Directory{
    public BSDDirectory(){
        Scanner sc= new Scanner(System.in);
        name = new String("BSD Directory");
        System.out.println("Enter Directory extension: ");
        extension=sc.next();
        System.out.println("BSD Directory created");
    }

    @Override
    public void AccessSubFiles(FileElement d) {

    }
}
class NTDirectory extends Directory{
    public NTDirectory(){
        Scanner sc= new Scanner(System.in);
        name = new String("NFT Directory");
        System.out.println("Enter Directory extension: ");
        extension=sc.next();
        System.out.println("NFT Directory created");
    }

    @Override
    public void AccessSubFiles(FileElement d) {
        
    }
}
class CreateOS{
    private ArrayList<Part> parts;
    public void createOS(OSFactory fct){
        parts= new ArrayList<Part>();
        parts.add(fct.createFile());
        parts.add(fct.createDirectory());
    }
    void displayParts() {
        System.out.println("\tListing Parts\n\t-------------");
        parts.forEach(p  -> System.out.println("\t"+ p.displayName()+"."+p.displayExtension()));
    }
}
public class Project {
    public static void main(String[] args) {
        OSFactory linux = new Linux();
        OSFactory nt = new NT();
        CreateOS os = new CreateOS();
        System.out.println("Creating linux");
        os.createOS(linux);
        os.displayParts();
    }
}
