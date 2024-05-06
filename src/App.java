import java.nio.file.*;
import java.util.Scanner;
import java.util.ArrayList;

public class App {

    private Scanner Scanner = new Scanner(System.in);
    private Path InputPath = Paths.get(System.getenv("APPDATA"), ".minecraft").toAbsolutePath();
    private Path OutputPath = Paths.get("").toAbsolutePath();
    private boolean GrayscaleOutput = false;
    private String Version = "1.20.6";
    

    public static ArrayList<String> Errors = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        App a = new App();
        System.out.println();
        a.PrintTitle();

        if(args.length != 0) {
            a.SetUserOptions(args);
        } else {
            System.out.println("* [>] No user options provided, using defaults: ");
            System.out.println("*\t [+] Input Path: " + a.InputPath);
            System.out.println("*\t [+] Output Path: " + a.OutputPath);
            System.out.println("*\t [+] Grayscale Output: " + a.GrayscaleOutput);
            System.out.println("*\t [+] Version: " + a.Version);
        }
        
        // User option errors
        if(Errors.size() > 0)
        {
            a.ErrorOut();
        }

        Extractor e = new Extractor(a.InputPath, a.Version);
        e.Extract(a.OutputPath.resolve("original"));

        // Extractor/File I/O errors
        if(Errors.size() > 0)
        {
            a.ErrorOut();
        }

        if(a.GrayscaleOutput)
        {   
            System.out.println("* [>] Grayscaling the extracted textures");
            Grayscaler.Grayscale(Paths.get(a.OutputPath.toString(), "original", "item"), Paths.get(a.OutputPath.toString(), "grayscale", "item"));
            Grayscaler.Grayscale(Paths.get(a.OutputPath.toString(), "original", "block"), Paths.get(a.OutputPath.toString(), "grayscale", "block"));
        }

        // Grayscale/File I/O errors
        if(Errors.size() > 0)
        {
            a.ErrorOut();
        }

        System.out.println();
    }

    // Set user provided options
    private void SetUserOptions(String[] args) {
        System.out.println("* [>] Setting user provided options...");
        for(int i = 0; i < args.length; i++)
        {
            switch (args[i]) {
                case "-i":
                    if(!((i+1) > args.length))
                        SetInputPath(args[i+1]);
                    break;

                case "-o":
                    if(!((i+1) > args.length))
                        SetOutputPath(args[i+1]);
                    break;

                case "-v":
                    if(!((i+1) > args.length))
                        setVersion(args[i+1]);
                    break;

                case "-g":
                        GrayscaleOutput = true;
                        System.out.println("*\t [+] Grayscale output flag set: " + GrayscaleOutput);
                    break;
                default:
                    break;
            }
        }   
    }

    private void SetOutputPath(String customPath)
    {
        try {
            Path path = Paths.get(customPath).toAbsolutePath();
            
            if(Files.exists(path))
            {
                OutputPath = path;
                System.out.println("*\t [+] Output path has been set to: " + path);
            } else {
                Errors.add("[-] Provided output path does not exist: " + path);
            }
            
        } catch(Exception e) {
            Errors.add("[-] Provided output path could not be parsed: " + customPath);
        }
    }

    private void SetInputPath(String customPath)
    {   
        try {
            Path path = Paths.get(customPath);
            
            if(Files.exists(path))
            {
                InputPath = path;
                System.out.println("*\t [+] Input path has been set to: " + path);
            } else {
                Errors.add("[-] Provided input path does not exist: " + path);
            }
            
        } catch(Exception e) {
            Errors.add("[-] Provided input path could not be parses: " + customPath);
        }
    }

    public void setVersion(String versionString) {
        if(McVersion.validate(versionString))
        {
            Version = versionString;
            System.out.println("*\t [+] Set version to: " + Version); 
        } else {
            Errors.add("[-] Version string malformed: " + versionString);
        }
    }

    private void ErrorOut()
    {
        System.out.println("* [!] Couldn't process the request because the following errors occured:");
        for(String s : Errors)
        {
            System.out.println("*\t"+ s);
        }
        System.out.println("*");
        System.out.println("* [>] Press any key to exit...");
        
        Scanner.nextLine();
        System.exit(1);
    }

    private void PrintTitle() {
        System.out.println("***************************************************");
        System.out.println("*             Minecraft Texture Tool              ");
        System.out.println("*         Author: github.com/crt.soluble          ");                       
        System.out.println("***************************************************");
        
        //System.out.println();
    }
}
