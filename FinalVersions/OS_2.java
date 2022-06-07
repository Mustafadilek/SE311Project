public class OS_2 {
    public static void main(String[] args) {
        DpIO LinuxIO = new LinuxIOAdapter(new LinuxFileSystemIO());
        DpIO BsdIO = new BsdIOAdapter(new BsdFileSystemIO());
        DpIO NtIO = new NtIOAdapter( new NtFileSystemIO());

        File linuxFile = new LinuxFile("Linux File", "txt");
        File bsdFile = new BSDFile("BSD File", "txt");
        File ntFile = new NTFile("NT File", "txt");

        LinuxIO.fprintf(linuxFile, "linux data");
        BsdIO.fprintf(bsdFile, "bsd data");
        NtIO.fprintf(ntFile, "nt data");

        System.out.println("Linux file data: " + linuxFile.getData());
        System.out.println("BSD file data: " + bsdFile.getData());
        System.out.println("NT file data: " + ntFile.getData());
    }
}

class DpIO {
    public int fprintf(File handle, String str) {
        if (handle == null) {
            System.out.println("File could not found.");
            return 0;
        }

        handle.writeData(str);
        return 1;
    }
}

class LinuxFileSystemIO {
    public int uprintf(String str, File handle) {
        if (handle == null) {
            System.out.println("File could not found.");
            return 0;
        }

        handle.writeData(str);
        return 1;
    }
}

class BsdFileSystemIO {
    public int uprintf(String str, File handle) {
        if (handle == null) {
            System.out.println("File could not found.");
            return 0;
        }

        handle.writeData(str);
        return 1;
    }
}

class NtFileSystemIO {
    public int printf(byte[] charArray, File handle) {
        if (handle == null) {
            System.out.println("File could not found.");
            return 0;
        }

        handle.writeData(new String(charArray));
        return 1;
    }
}

class LinuxIOAdapter extends DpIO {
    private final LinuxFileSystemIO linuxFileSystemIO;

    public LinuxIOAdapter(LinuxFileSystemIO linuxFileSystemIO) { this.linuxFileSystemIO = linuxFileSystemIO; }

    public int fprintf(File handle, String str) { return linuxFileSystemIO.uprintf(str, handle); }
}

class BsdIOAdapter extends DpIO {
    private final BsdFileSystemIO bsdFileSystemIO;

    public BsdIOAdapter(BsdFileSystemIO bsdFileSystemIO) { this.bsdFileSystemIO = bsdFileSystemIO; }

    public int fprintf(File handle, String str) { return bsdFileSystemIO.uprintf(str, handle); }
}

class NtIOAdapter extends DpIO {
    private final NtFileSystemIO ntFileSystemIO;

    public NtIOAdapter(NtFileSystemIO ntFileSystemIO) { this.ntFileSystemIO = ntFileSystemIO; }

    public int fprintf(File handle, String str) { return ntFileSystemIO.printf(str.getBytes(), handle); }
}
