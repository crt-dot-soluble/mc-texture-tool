
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class McVersion 
{
    public static boolean validate(String versionString)
    {
        String pattern = "^\\d+(\\.\\d+)+$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(versionString);
        return matcher.matches();
    }
}