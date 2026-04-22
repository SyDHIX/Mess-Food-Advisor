package com.bennett.mess;

import org.springframework.stereotype.Component;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * UserData handles saving and loading user ratings and meal logs to text files.
 * - ratings.txt stores dish ratings (format: dishName=4)
 * - meal_log.txt stores what the user ate (format: date|day|meal|dishName)
 */
@Component
public class UserData {

    private static final String RATINGS_FILE = "ratings.txt";
    private static final String MEAL_LOG_FILE = "meal_log.txt";

    public void saveRating(String dishName, int stars) {
        HashMap<String, Integer> ratings = loadRatings();
        ratings.put(dishName, stars);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RATINGS_FILE))) {
            for (Map.Entry<String, Integer> e : ratings.entrySet()) {
                writer.write(e.getKey() + "=" + e.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving rating: " + e.getMessage());
        }
    }

    public HashMap<String, Integer> loadRatings() {
        HashMap<String, Integer> ratings = new HashMap<>();
        File file = new File(RATINGS_FILE);
        if (!file.exists()) return ratings;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    try { ratings.put(parts[0], Integer.parseInt(parts[1].trim())); }
                    catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading ratings: " + e.getMessage());
        }
        return ratings;
    }

    public void logMeal(String day, String mealType, String dishName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEAL_LOG_FILE, true))) {
            String dateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            writer.write(dateTime + "|" + day + "|" + mealType + "|" + dishName);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error logging meal: " + e.getMessage());
        }
    }

    public List<String[]> loadMealLog() {
        List<String[]> logs = new ArrayList<>();
        File file = new File(MEAL_LOG_FILE);
        if (!file.exists()) return logs;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) logs.add(parts);
            }
        } catch (IOException e) {
            System.out.println("Error loading meal log: " + e.getMessage());
        }
        return logs;
    }

    public void clearMealLog() {
        new File(MEAL_LOG_FILE).delete();
    }
}
