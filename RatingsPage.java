import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * RatingsPage lets users rate mess dishes with stars (1-5).
 * Ratings are saved to a file and persist across app restarts.
 */
public class RatingsPage extends JPanel {

    private final MenuData menuData;
    private final Runnable onBack;
    private JPanel dishListPanel;
    private HashMap<String, Integer> userRatings;

    public RatingsPage(MenuData menuData, Runnable onBack) {
        this.menuData = menuData;
        this.onBack = onBack;
        setBackground(UIConstants.BG_DARK);
        setLayout(new BorderLayout());

        // Load saved ratings from file
        userRatings = UserData.loadRatings();

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
    }

    // ─── Header with back button and title ───
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(10, 10, 25));
        header.setBorder(new EmptyBorder(16, 20, 16, 20));

        JButton backBtn = new JButton("← Back");
        backBtn.setFont(UIConstants.FONT_BODY_BOLD);
        backBtn.setForeground(UIConstants.ACCENT_LIGHT);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> onBack.run());

        JLabel title = new JLabel("⭐ Rate Mess Dishes");
        title.setFont(UIConstants.FONT_HEADING);
        title.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("  Click stars to rate. Ratings are saved automatically!");
        subtitle.setFont(UIConstants.FONT_SMALL);
        subtitle.setForeground(UIConstants.TEXT_MUTED);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        header.add(backBtn, BorderLayout.WEST);
        header.add(titlePanel, BorderLayout.CENTER);
        return header;
    }

    // ─── Body with all dishes listed ───
    private JScrollPane buildBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UIConstants.BG_DARK);
        body.setBorder(new EmptyBorder(16, 24, 24, 24));

        // Get all unique dish names from the menu
        List<String> days = menuData.getDays();
        String[] meals = menuData.getMealTypes();

        // Go through each day and meal to show all dishes
        for (String day : days) {
            // Day heading
            JLabel dayLabel = new JLabel("📅 " + day);
            dayLabel.setFont(UIConstants.FONT_SUBHEAD);
            dayLabel.setForeground(UIConstants.ACCENT_LIGHT);
            dayLabel.setBorder(new EmptyBorder(16, 0, 8, 0));
            body.add(dayLabel);

            for (String meal : meals) {
                List<FoodItem> items = menuData.getMeal(day, meal);
                if (items.isEmpty()) continue;

                // Meal label
                JLabel mealLabel = new JLabel("   " + meal);
                mealLabel.setFont(UIConstants.FONT_BODY_BOLD);
                mealLabel.setForeground(UIConstants.TEXT_SECONDARY);
                mealLabel.setBorder(new EmptyBorder(4, 0, 4, 0));
                body.add(mealLabel);

                // Show each dish with star buttons
                for (FoodItem item : items) {
                    if (!item.isStaple()) { // Don't rate staples like rice/chapati
                        body.add(buildDishRow(item));
                        body.add(Box.createVerticalStrut(4));
                    }
                }
            }
        }

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBackground(UIConstants.BG_DARK);
        scroll.getViewport().setBackground(UIConstants.BG_DARK);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    // ─── One row: dish name + 5 star buttons ───
    private JPanel buildDishRow(FoodItem item) {
        RoundedPanel row = new RoundedPanel(10, UIConstants.BG_CARD);
        row.setLayout(new BorderLayout(12, 0));
        row.setBorder(new EmptyBorder(10, 16, 10, 16));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Hover effect
        row.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { row.setBgColor(UIConstants.BG_CARD_HOVER); }
            public void mouseExited(MouseEvent e) { row.setBgColor(UIConstants.BG_CARD); }
        });

        // Dish name on the left
        JLabel name = new JLabel(item.getName() + "  (" + item.getCalories() + " Kcal)");
        name.setFont(UIConstants.FONT_BODY);
        name.setForeground(UIConstants.TEXT_PRIMARY);

        // Star buttons on the right
        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        starsPanel.setOpaque(false);

        // Check if user already rated this dish
        int currentRating = userRatings.getOrDefault(item.getName(), 0);

        // Create 5 star buttons
        JButton[] starButtons = new JButton[5];
        for (int i = 0; i < 5; i++) {
            final int starValue = i + 1;
            JButton star = new JButton(starValue <= currentRating ? "★" : "☆");
            star.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            star.setForeground(starValue <= currentRating ? UIConstants.AMBER : UIConstants.TEXT_MUTED);
            star.setContentAreaFilled(false);
            star.setBorderPainted(false);
            star.setFocusPainted(false);
            star.setCursor(new Cursor(Cursor.HAND_CURSOR));
            star.setPreferredSize(new Dimension(32, 32));
            starButtons[i] = star;

            // When user clicks a star
            star.addActionListener(e -> {
                // Save the rating
                userRatings.put(item.getName(), starValue);
                UserData.saveRating(item.getName(), starValue);

                // Update all star buttons to show the new rating
                for (int j = 0; j < 5; j++) {
                    if (j < starValue) {
                        starButtons[j].setText("★");
                        starButtons[j].setForeground(UIConstants.AMBER);
                    } else {
                        starButtons[j].setText("☆");
                        starButtons[j].setForeground(UIConstants.TEXT_MUTED);
                    }
                }
            });

            starsPanel.add(star);
        }

        row.add(name, BorderLayout.CENTER);
        row.add(starsPanel, BorderLayout.EAST);
        return row;
    }
}
