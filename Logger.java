import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class Logger {
    private static final String DEFAULT_LOG_FILE = "simulation_log.txt";
    private static final HashSet<String> clearedFiles = new HashSet<>(); // Hangi dosyalar temizlendi

    // Varsayılan dosyaya log yazar
    public static void log(String message) {
        logToFile(DEFAULT_LOG_FILE, message);
    }

    // Özel dosya ismiyle log yazar
    public static void logToFile(String fileName, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String fullMessage = "[" + timestamp + "] " + message;

        try {
            // Dosya henüz temizlenmediyse bir kere temizle
            if (!clearedFiles.contains(fileName)) {
                FileWriter clearWriter = new FileWriter(fileName, false); // Üzerine yaz
                clearWriter.close();
                clearedFiles.add(fileName);
            }

            // Dosyaya ekle
            try (FileWriter writer = new FileWriter(fileName, true)) {
                writer.write(fullMessage + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Log yazılamadı (" + fileName + "): " + e.getMessage());
        }
    }
}
