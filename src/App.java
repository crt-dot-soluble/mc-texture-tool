import java.nio.file.*;
import java.util.Scanner;
import java.util.ArrayList;

public class App {

    // Globals, most are configurable via runtime flags
    private Scanner Scanner             = new Scanner(System.in);
    private Path InputPath              = Paths.get(System.getenv("APPDATA"), ".minecraft");
    private Path OutputPath             = Paths.get("").toAbsolutePath();
    private boolean GrayscaleOutput     = false;
    private String Version              = "1.20.6";
    
    public static ArrayList<String> Errors = new ArrayList<>();

    public static void main(String[] args) {
        App a = new App();
        Console.printTitle();

    //----- User Options
        if(args.length != 0) {
            Console.logParent("Setting user flags", false);
            a.setUserOptions(args);
        } else {
            Console.logParent("No user options provided, using defaults:", false);
        }

        Console.logParent("Executing with settings:", false);
        Console.logChild("Input Path: " + a.InputPath, false);
        Console.logChild("Output Path: " + a.OutputPath, false);
        Console.logChild("Grayscale Output: " + a.GrayscaleOutput, false);
        Console.logChild("Minecraft Version: " + a.Version, false);

        if(Errors.size() > 0) {
            a.ErrorOut();
        }

    //----- Jar Extractor
        Console.logParent("Initializing jar extractor", false);
        Extractor e = new Extractor(a.InputPath, a.Version);
        e.Extract(a.OutputPath.resolve("original"));
        if(Errors.size() > 0) {
            a.ErrorOut();
        }

    //----- Grayscaler
        if(a.GrayscaleOutput)
        {   
            Console.logParent("Grayscaling the extracted textures", false);
            Grayscaler.grayscale(Paths.get(a.OutputPath.toString(), "original", "item"), Paths.get(a.OutputPath.toString(), "grayscale", "item"));
            Grayscaler.grayscale(Paths.get(a.OutputPath.toString(), "original", "block"), Paths.get(a.OutputPath.toString(), "grayscale", "block"));
            Grayscaler.grayscale(Paths.get(a.OutputPath.toString(), "original", "models", "armor"), Paths.get(a.OutputPath.toString(), "grayscale", "models", "armor"));
        }
        if(Errors.size() > 0) {
            a.ErrorOut();
        }

        //Console.logNewLine();
        Console.logParent("Press any key to exit...", false);
        a.Scanner.nextLine();
    }

    // Set user provided options
    private void setUserOptions(String[] args) {
        for(int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                    if(!((i+1) > args.length)) setInputPath(args[i+1]);
                break;

                case "-o":
                    if(!((i+1) > args.length)) setOutputPath(args[i+1]);
                break;

                case "-v":
                    if(!((i+1) > args.length)) setVersion(args[i+1]);
                break;

                case "-g":
                    GrayscaleOutput = true;
                    Console.logChild("Grayscale output flag set: " + GrayscaleOutput, false);
                break;
                
                case "-?":
                Console.logChild("Help flag set", false);
                    Console.printHelp();
                    System.exit(0);
                break;

                default:
                    break;
            }
        }   
    }

    private void setOutputPath(String customPath)
    {
        try {
            Path path = Paths.get(customPath).toAbsolutePath();
            
            if(Files.exists(path))
            {
                OutputPath = path;
                Console.logChild("Output path has been set to: " + path, false);
            } else {
                Errors.add("Provided output path does not exist: " + path);
            }
            
        } catch(Exception e) {
            Errors.add("Provided output path could not be parsed: " + customPath);
        }
    }

    private void setInputPath(String customPath)
    {   
        try {
            Path path = Paths.get(customPath);
            
            if(Files.exists(path)) {
                InputPath = path;
                Console.logChild("Input path has been set to: " + path, false);
            } else {
                Errors.add("Provided input path does not exist: " + path);
            }
            
        } catch(InvalidPathException e) {
            Errors.add("Provided input path could not be parses: " + customPath);
        }
    }

    public void setVersion(String versionString) {
        if(McVersion.validate(versionString)) {
            Version = versionString;
            Console.logChild("Set version to: " + Version, false); 
        } else {
            Errors.add("Version string malformed: " + versionString);
        }
    }

    // Provides all the errors that have been reported and exits the app
    private void ErrorOut() {
        //System.out.println("* [!] Failed to process the request: ");
        Console.logParent("Failed to process the request:", true);

        for(String s : Errors)
        {
            Console.logChild(s, true);
        }

        //Console.logNewLine();
        Console.logParent("Press any key to exit...", false);
        
        Scanner.nextLine();
        System.exit(1);
    }
}
