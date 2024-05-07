import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import javax.imageio.ImageIO;

public class Grayscaler {

    public static BufferedImage grayscale(Path inputPath, Path outputPath) {
        File inputFolder = new File(inputPath.toString());
        File[] textures = inputFolder.listFiles();
        
        if (textures != null) {
            try {
                Files.createDirectories(outputPath);
            } catch (IOException e) {
                System.out.println("[-] Failed to create grayscaled texture output directory: " + outputPath);
            }

            for (File t : textures) {
                if (t.isFile() && t.getName().endsWith(".png")) {
                    try {
                        BufferedImage originalImage = ImageIO.read(t);
                        BufferedImage grayscaleImage = getGrayScaleImage(originalImage);
                        File grayscaleImageFile = new File(outputPath.toString(), t.getName());
                        ImageIO.write(grayscaleImage, "png", grayscaleImageFile);
                    } catch (IOException ioe) {
                        System.out.println("Failed to load texture file: " + t.getName());
                    }
                }
            }
            System.out.println("*\t [+] Grayscaling completed processing " + textures.length + " files: " + "\n*\t [>] " + outputPath);
        }

        return null; // Return null since we don't need the grayscale image here
    }

    // Writes the grayscale pixel data to a new BufferedImage object - utility for the Grayscale function
    private static BufferedImage getGrayScaleImage(BufferedImage originalImage) {
        BufferedImage grayscaleImage = new BufferedImage(
            originalImage.getWidth(),
            originalImage.getHeight(),
            BufferedImage.TYPE_INT_ARGB
        );

        for (int y = 0; y < grayscaleImage.getHeight(); y++) {
            for (int x = 0; x < grayscaleImage.getWidth(); x++) {
                Color color = new Color(originalImage.getRGB(x, y), true);

                // RGB to grayscale (single channel brightness) conversion
                // The weights (0.2989, 0.5870, and 0.1140) are chosen to approximate 
                // human perception of brightness.
                int gray = (int) (0.2989 * color.getRed() + 0.5870 * color.getGreen() + 0.1140 * color.getBlue());
                int argb = (color.getAlpha() << 24) | (gray << 16) | (gray << 8) | gray;
                grayscaleImage.setRGB(x, y, argb);
            }
        }

        return grayscaleImage;
    }
}
