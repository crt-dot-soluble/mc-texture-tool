
import java.nio.file.*;
import java.util.jar.*;
import java.io.InputStream;
// import java.util.ArrayList;
import java.util.Enumeration;

public class Extractor {

    private JarFile MinecraftJarFile;
    private Path MinecraftJarPath;
    //private ArrayList<String> MinecraftJarEntries;
    
    public Extractor(Path inputPath, String version)
    {   
        System.out.println("* [>] Initializing the extractor...");
        ResolvePath(inputPath, version);
        LoadJar();
    }

    private void ResolvePath(Path inputPath, String version)
    {   
        try {            
            MinecraftJarPath = inputPath.resolve("versions").resolve(version).resolve(version + ".jar");
            if(!Files.exists(MinecraftJarPath))
            {
                App.Errors.add("[-] Failed to locate jar file for version " + version + ": " + MinecraftJarPath);
            } else {
                System.out.println("*\t [+] Jar file found for version " + version + ": " + MinecraftJarPath);   
            }

        } catch(Exception e) {
            App.Errors.add("[-] Failed to resolve the path to jar file for version " + version + ": " + inputPath);
        }
    }

    // Check for errors from Extractor in App or this can crash with a null exception
    private void LoadJar()
    {
        try {
            MinecraftJarFile = new JarFile(MinecraftJarPath.toString());
            System.out.println("*\t [+] Jar file loaded into memory");
        } catch(Exception e) {
            App.Errors.add("[-] Failed to load jar file into memory");
        }
    }

    public void Extract(Path outputPath)
    {   
        System.out.println("*\t [+] Extraction started");

        Enumeration<JarEntry> entries = MinecraftJarFile.entries();
        Path blockPath = outputPath.resolve("block");
        Path itemPath = outputPath.resolve("item");
        
        try {
            Files.createDirectories(blockPath);
            Files.createDirectories(itemPath);
        } catch(Exception e) {
            App.Errors.add("[-] Failed to create one or more output directories");
        }

        while(entries.hasMoreElements())
        {
            JarEntry entry = entries.nextElement();

            // Item texture extraction
            if(Paths.get(entry.getName()).getParent() != null 
                && Paths.get(entry.getName()).getParent().equals(Paths.get("assets/minecraft/textures/item/")))
            {
                try(InputStream is = MinecraftJarFile.getInputStream(entry))
                {   String fileName = Paths.get(entry.getName()).getFileName().toString();
                    Files.copy(is, itemPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                } catch(Exception e) {
                    App.Errors.add("[-] Failed to copy texture(s) to output folder");
                }
            }
            

            // Block texture extraction
            if(Paths.get(entry.getName()).getParent() != null 
                && Paths.get(entry.getName()).getParent().equals(Paths.get("assets/minecraft/textures/block/")))
            {
                try(InputStream is = MinecraftJarFile.getInputStream(entry))
                {   String fileName = Paths.get(entry.getName()).getFileName().toString();
                    Files.copy(is, blockPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                } catch(Exception e) {
                    App.Errors.add("[-] Failed to copy texture(s) to output folder");
                }
            }
        }

        System.out.println("*\t [+] Extraction output sent to: " + outputPath.toString());
    }
}