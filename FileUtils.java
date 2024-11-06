import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static boolean deleteDirectory(File directory) {
        if (!directory.exists()) {
            return false;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }
    
    public static boolean createBackupDirectory(String path) {
        try {
            Files.createDirectories(Paths.get(path));
            return true;
        } catch (Exception e) {
            System.err.println("Error creating directory: " + e.getMessage());
            return false;
        }
    }
}
