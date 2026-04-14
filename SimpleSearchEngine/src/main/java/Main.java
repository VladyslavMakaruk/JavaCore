import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String inputDataFile = args[1];
        new UserMenu(3,inputDataFile).Start();
    }
}
