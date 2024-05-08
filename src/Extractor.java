
import java.nio.file.*;
import java.util.jar.*;
import java.io.InputStream;
// import java.util.ArrayList;
import java.util.Enumeration;

public class Extractor {

    private JarFile MinecraftJarFile;
    private Path MinecraftJarPath;
    
    public Extractor(Path inputPath, String version)
    {   
        ResolvePath(inputPath, version);
        LoadJar();
    }

    private void ResolvePath(Path inputPath, String version)
    {   
        try {            
            MinecraftJarPath = inputPath.resolve("versions").resolve(version).resolve(version + ".jar");
            if(!Files.exists(MinecraftJarPath))
            {
                App.Errors.add("Failed to locate jar file for version " + version + ": " + MinecraftJarPath);
            } else {
                // System.out.println("Jar file found for version " + version + ": " + "\n*\t [>] " + MinecraftJarPath);
                Console.logChild("Jar file found for version " + version + ": " + MinecraftJarPath, false);   
            }

        } catch(Exception e) {
            App.Errors.add("Failed to resolve the path to jar file for version " + version + ": " + inputPath);
        }
    }

    // Check for errors from Extractor in App or this can crash with a null exception
    private void LoadJar()
    {
        try {
            MinecraftJarFile = new JarFile(MinecraftJarPath.toString());
            
            // System.out.println("*\t [+] Jar file loaded into memory");
            Console.logChild("Jar file loaded", false);
        } catch(Exception e) {
            App.Errors.add("Failed to load jar file");
        }
    }

    public void Extract(Path outputPath)
    {   
        // System.out.println("*\t [+] Extraction started");
        Console.logChild("Extraction started", false);

        if(MinecraftJarFile == null) {
            App.Errors.add("Failed to resolve jar file entries");
            return;
        }
        
        Enumeration<JarEntry> entries = MinecraftJarFile.entries();

        

    //----- Define texture paths
        
        Path blockPath = outputPath.resolve("block");
        Path itemPath = outputPath.resolve("item");
        Path armorPath = outputPath.resolve("models").resolve("armor");
        
    
    //-----    
    
        try {
            Files.createDirectories(blockPath);
            Files.createDirectories(itemPath);
            Files.createDirectories(armorPath);
        } catch(Exception e) {
            App.Errors.add("Failed to create one or more output directories");
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
                    App.Errors.add("Failed to copy item texture(s) to output folder");
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
                    App.Errors.add("Failed to copy block texture(s) to output folder");
                }
            }

            if(Paths.get(entry.getName()).getParent() != null 
                && Paths.get(entry.getName()).getParent().equals(Paths.get("assets/minecraft/textures/models/armor/"))) {
                
                try(InputStream is = MinecraftJarFile.getInputStream(entry)) {
                    String fileName = Paths.get(entry.getName()).getFileName().toString();
                    Files.copy(is, armorPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                } catch(Exception e) {
                    App.Errors.add("Failed to copy armor texture(s) to output folder");
                }
            }
        }

        Console.logChild("Extraction output sent to: " + outputPath.toString(), false);
    }
}