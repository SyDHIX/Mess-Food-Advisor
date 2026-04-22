import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * UserData handles saving and loading user ratings and meal logs to text files.
 * - ratings.txt stores dish ratings (format: dishName=4.5)
 * - meal_log.txt stores what the user ate (format: date|day|meal|dishName)
 */
public class UserData {

    // File names for storing data
    private static final String RATINGS_FILE = "ratings.txt";
    private static final String MEAL_LOG_FILE = "meal_log.txt";

    // ─── RATINGS ───

    /**
     * Save a rating for a dish. If the dish already has a rating, it gets updated.
     */
    public static void saveRating(String dishName, int stars) {
        // First, load all existing ratings
        HashMap<String, Integer> ratings = loadRatings();

        // Update or add the new rating
        ratings.put(dishName, stars);

        // Write all ratings back to file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(RATINGS_FILE));
            for (String name : ratings.keySet()) {
                writer.write(name + "=" + ratings.get(name));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving rating: " + e.getMessage());
        }
    }

    /**
     * Load all ratings from file into a HashMap.
     * Returns empty map if file doesn't exist yet.
     */
    public static HashMap<String, Integer> loadRatings() {
        HashMap<String, Integer> ratings = new HashMap<>();

        File file = new File(RATINGS_FILE);
        if (!file.exists()) {
            return ratings; // No ratings saved yet
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                // Each line is like: "Paneer Do Pyaza=4"
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String name = parts[0];
                    int stars = Integer.parseInt(parts[1]);
                    ratings.put(name, stars);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading ratings: " + e.getMessage());
        }

        return ratings;
    }

    // ─── MEAL LOG ───

    /**
     * Log a meal the user ate. Appends to the file (doesn't overwrite).
     */
    public static void logMeal(String day, String mealType, String dishName) {
        try {
            // 'true' means append mode - adds to end of file
            BufferedWriter writer = new BufferedWriter(new FileWriter(MEAL_LOG_FILE, true));

            // Get current date and time
            String dateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            // Write: date|day|meal|dishName
            writer.write(dateTime + "|" + day + "|" + mealType + "|" + dishName);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error logging meal: " + e.getMessage());
        }
    }

    /**
     * Load all meal logs from file.
     * Each entry is a String array: [dateTime, day, mealType, dishName]
     */
    public static List<String[]> loadMealLog() {
        List<String[]> logs = new ArrayList<>();

        File file = new File(MEAL_LOG_FILE);
        if (!file.exists()) {
            return logs; // No meals logged yet
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                // Each line is like: "2026-04-22 15:30|Monday|Lunch|Paneer Do Pyaza"
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    logs.add(parts);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading meal log: " + e.getMessage());
        }

        return logs;
    }

    /**
     * Clear all meal history by deleting the file.
     */
    public static void clearMealLog() {
        File file = new File(MEAL_LOG_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
