package ph.edu.dlsu.greencanyonairbnb.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageHandler {
    private static final String UPLOAD_DIR = "src/main/resources/img/";

    public static String saveImage(File file) {
        try {
            // Ensure directory exists
            Path path = Paths.get(UPLOAD_DIR);
            if (!Files.exists(path)) Files.createDirectories(path);

            // Copy file to resources
            Path target = path.resolve(file.getName());
            Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

            return file.getName(); // Return the name to save in DB
        } catch (IOException e) {
            e.printStackTrace();
            return "default.png";
        }
    }
}
