
// Utility class for displaying info to the user via the console
public class Console {

    public static void logParent(String text, boolean error) {
        System.out.println("*");
        char symbol = '>';
        if(error) symbol = '!';
        System.out.println("* [" + symbol + "] " + text);
    }

    public static void logChild(String text, boolean error) {
        char symbol = '+';
        if(error) symbol = '-';
        System.out.println("*\t " + "[" + symbol + "] " + text);
    }

    public static void logNewLine() {
        System.out.println("*");
    }

    public static void printTitle() {
        System.out.println();
        System.out.println("***************************************************");
        System.out.println("*             Minecraft Texture Tool              ");
        System.out.println("*         Author: github.com/crt.soluble          ");                       
        System.out.println("***************************************************");
        
        //System.out.println();
    }
}
