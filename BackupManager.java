import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupManager {
    private static final String BACKUP_DIR = "backups/";

    public static void main(String[] args) {
        // Initialize BackupManager
        BackupManager manager = new BackupManager();
        
        // Example of scheduling daily backup
        new Scheduler(manager::backupFiles, 24 * 60 * 60 * 1000);  // Every 24 hours
    }

    public void backupFiles() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String zipFileName = BACKUP_DIR + "backup_" + timestamp + ".zip";
        try {
            Files.createDirectories(Paths.get(BACKUP_DIR));
            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName))) {
                addDirectoryToZip(new File("selected_folder_to_backup"), "", zos);
            }
            log("Backup created at " + zipFileName);
        } catch (IOException e) {
            log("Error during backup: " + e.getMessage());
        }
    }

    private void addDirectoryToZip(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
        for (File file : folder.listFiles()) {
            String zipEntryName = parentFolder + file.getName();
            if (file.isDirectory()) {
                addDirectoryToZip(file, zipEntryName + "/", zos);
            } else {
                zos.putNextEntry(new ZipEntry(zipEntryName));
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }
                }
                zos.closeEntry();
            }
        }
    }

    private void log(String message) {
        try (FileWriter fw = new FileWriter(BACKUP_DIR + "backup.log", true)) {
            fw.write(new Date() + ": " + message + "\n");
        } catch (IOException e) {
            System.err.println("Logging error: " + e.getMessage());
        }
    }
}
