import java.io.File;

public class AdapterPattern {
    public static void main(String[] args) {
        File file = new File("test.txt");
        DP myDpProgrammingLanguage = new DP();

        myDpProgrammingLanguage.fprintf(file, "test");
    }
}

enum OS {
    LINUX,
    BSD,
    NT
}

class DP {
    // we need to get os type from envrioment variables
    // System.getProperty("os.name");
    OS os;
    PrintFunctionAdapter printAdapter = new PrintFunctionAdapter();
    public int fprintf(File handle, String str) {
        try {
            if(os == OS.BSD) {
                printAdapter.PrintBSDFunctionAdapter(handle, str);
            }
            if (os == OS.NT) {
                printAdapter.PrintNTFunctionAdapter(str, handle);
            }
            if (os == OS.LINUX) {
                printAdapter.PrintLinuxFunctionAdapter(handle, str);
            }
            return 0;
        }
        catch (Exception e) {
            throw e;
        }

    }
}


class PrintFunctionAdapter {
    private BSD bsd;
    private  Linux linux;
    private  NT nt;
    public int PrintBSDFunctionAdapter(File handle, String str) {
        try{
            return bsd.uprintf(str, handle);
        }catch (Exception e) {
            throw e;
        }

    }
    public int PrintLinuxFunctionAdapter(File handle, String str){
        try {
            return linux.uprintf(str, handle);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public int PrintNTFunctionAdapter(String str, File handle) {
        try {
            byte[] charArray = str.getBytes();
            nt.printf(charArray, handle);
            return 0;
        }
        catch (Exception e) {
            throw e;
        }

    }
}

class Linux {
    public int uprintf(String str, File handle) {
        return 0;
    }
}
class BSD {
    public int uprintf(String str, File handle) {
        return 0;
    }
}
class NT {
    public int printf(byte[] charArray, File hadle) {
        return 0;
    }
}
