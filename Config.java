import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final Map<String, String> settings = new HashMap<>();

    public static String[] getCitiesFromConfig(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("cities=")) {
                    String cityLine = line.substring("cities=".length());
                    return cityLine.split(",");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String[0]; // Dosya okunamazsa boş dizi döner
    }        String[] cityArray = getCitiesFromConfig("config.txt");


    public static void load(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("=") || line.startsWith("#")) continue; // yorum satırı veya boş satır atla
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    settings.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Config file reading error: " + e.getMessage());
        }
    }

    // Genel erişim metodları
    public static String get(String key) {
        return settings.get(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(settings.get(key));
    }

    public static double getDouble(String key) {
        String value = settings.get(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid double for key: " + key);
            }
        }
        return 0.0; // ya da varsayılan bir değer
    }
}
