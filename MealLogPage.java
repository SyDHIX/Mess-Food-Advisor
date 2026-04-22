import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * MealLogPage lets users log what they ate and view their eating history.
 * Meal logs are saved to a file and persist across app restarts.
 */
public class MealLogPage extends JPanel {

    private final MenuData menuData;
    private final Runnable onBack;
    private JComboBox<String> dayDropdown;
    private JComboBox<String> mealDropdown;
    private JPanel foodChoicesPanel;
    private JPanel historyPanel;

    public MealLogPage(MenuData menuData, Runnable onBack) {
        this.menuData = menuData;
        this.onBack = onBack;
        setBackground(UIConstants.BG_DARK);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
    }

    // ─── Header ───
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

        JLabel title = new JLabel("📝 Meal Log");
        title.setFont(UIConstants.FONT_HEADING);
        title.setForeground(UIConstants.TEXT_PRIMARY);

        header.add(backBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        return header;
    }

    // ─── Body ───
    private JScrollPane buildBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UIConstants.BG_DARK);
        body.setBorder(new EmptyBorder(20, 24, 24, 24));

        // ─── Selection section ───
        JLabel selectLabel = new JLabel("🍽️ What did you eat?");
        selectLabel.setFont(UIConstants.FONT_SUBHEAD);
        selectLabel.setForeground(UIConstants.TEXT_PRIMARY);
        selectLabel.setBorder(new EmptyBorder(0, 0, 12, 0));
        body.add(selectLabel);

        // Dropdowns for day and meal
        JPanel dropdownRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        dropdownRow.setOpaque(false);
        dropdownRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Day dropdown
        String[] days = menuData.getDays().toArray(new String[0]);
        dayDropdown = new JComboBox<>(days);
        dayDropdown.setFont(UIConstants.FONT_BODY);
        dayDropdown.setPreferredSize(new Dimension(160, 36));

        // Auto-select today
        String[] jDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        int dow = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK);
        dayDropdown.setSelectedItem(jDays[dow - 1]);

        // Meal dropdown
        mealDropdown = new JComboBox<>(menuData.getMealTypes());
        mealDropdown.setFont(UIConstants.FONT_BODY);
        mealDropdown.setPreferredSize(new Dimension(140, 36));

        // Update food list when selection changes
        dayDropdown.addActionListener(e -> updateFoodChoices());
        mealDropdown.addActionListener(e -> updateFoodChoices());

        JLabel dayLabel = new JLabel("Day:");
        dayLabel.setFont(UIConstants.FONT_BODY_BOLD);
        dayLabel.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel mealLabel = new JLabel("Meal:");
        mealLabel.setFont(UIConstants.FONT_BODY_BOLD);
        mealLabel.setForeground(UIConstants.TEXT_SECONDARY);

        dropdownRow.add(dayLabel);
        dropdownRow.add(dayDropdown);
        dropdownRow.add(mealLabel);
        dropdownRow.add(mealDropdown);
        body.add(dropdownRow);
        body.add(Box.createVerticalStrut(12));

        // ─── Food choices (clickable items) ───
        foodChoicesPanel = new JPanel();
        foodChoicesPanel.setLayout(new BoxLayout(foodChoicesPanel, BoxLayout.Y_AXIS));
        foodChoicesPanel.setOpaque(false);
        body.add(foodChoicesPanel);
        body.add(Box.createVerticalStrut(24));

        // ─── History section ───
        JPanel historyHeader = new JPanel(new BorderLayout());
        historyHeader.setOpaque(false);
        historyHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel historyTitle = new JLabel("📋 Your Meal History");
        historyTitle.setFont(UIConstants.FONT_SUBHEAD);
        historyTitle.setForeground(UIConstants.TEXT_PRIMARY);

        JButton clearBtn = new JButton("🗑️ Clear History");
        clearBtn.setFont(UIConstants.FONT_SMALL);
        clearBtn.setForeground(UIConstants.RED);
        clearBtn.setContentAreaFilled(false);
        clearBtn.setBorderPainted(false);
        clearBtn.setFocusPainted(false);
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear all meal history?",
                "Clear History", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                UserData.clearMealLog();
                refreshHistory();
            }
        });

        historyHeader.add(historyTitle, BorderLayout.WEST);
        historyHeader.add(clearBtn, BorderLayout.EAST);
        body.add(historyHeader);
        body.add(Box.createVerticalStrut(10));

        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setOpaque(false);
        body.add(historyPanel);

        // Load initial data
        updateFoodChoices();
        refreshHistory();

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBackground(UIConstants.BG_DARK);
        scroll.getViewport().setBackground(UIConstants.BG_DARK);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    // ─── Show food items for selected day + meal ───
    private void updateFoodChoices() {
        foodChoicesPanel.removeAll();

        String day = (String) dayDropdown.getSelectedItem();
        String meal = (String) mealDropdown.getSelectedItem();
        List<FoodItem> items = menuData.getMeal(day, meal);

        JLabel hint = new JLabel("   Click a dish to log it:");
        hint.setFont(UIConstants.FONT_SMALL);
        hint.setForeground(UIConstants.TEXT_MUTED);
        hint.setBorder(new EmptyBorder(0, 0, 6, 0));
        foodChoicesPanel.add(hint);

        for (FoodItem item : items) {
            JButton dishBtn = new JButton("  ➕  " + item.getName() + "  (" + item.getCalories() + " Kcal)");
            dishBtn.setFont(UIConstants.FONT_BODY);
            dishBtn.setForeground(UIConstants.TEXT_PRIMARY);
            dishBtn.setBackground(UIConstants.BG_CARD);
            dishBtn.setHorizontalAlignment(SwingConstants.LEFT);
            dishBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            dishBtn.setBorderPainted(false);
            dishBtn.setFocusPainted(false);
            dishBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            dishBtn.addActionListener(e -> {
                // Save to file
                UserData.logMeal(day, meal, item.getName());

                // Show confirmation
                JOptionPane.showMessageDialog(this,
                    "✅ Logged: " + item.getName() + " (" + day + " " + meal + ")",
                    "Meal Logged!", JOptionPane.INFORMATION_MESSAGE);

                // Refresh history
                refreshHistory();
            });

            foodChoicesPanel.add(dishBtn);
            foodChoicesPanel.add(Box.createVerticalStrut(4));
        }

        foodChoicesPanel.revalidate();
        foodChoicesPanel.repaint();
    }

    // ─── Load and display meal history from file ───
    private void refreshHistory() {
        historyPanel.removeAll();

        List<String[]> logs = UserData.loadMealLog();

        if (logs.isEmpty()) {
            JLabel emptyLabel = new JLabel("   No meals logged yet. Start by clicking a dish above!");
            emptyLabel.setFont(UIConstants.FONT_BODY);
            emptyLabel.setForeground(UIConstants.TEXT_MUTED);
            emptyLabel.setBorder(new EmptyBorder(16, 0, 0, 0));
            historyPanel.add(emptyLabel);
        } else {
            // Show most recent first
            int totalCalories = 0;
            for (int i = logs.size() - 1; i >= 0; i--) {
                String[] entry = logs.get(i);
                // entry = [dateTime, day, mealType, dishName]

                RoundedPanel logCard = new RoundedPanel(10, UIConstants.BG_CARD);
                logCard.setLayout(new BorderLayout(8, 0));
                logCard.setBorder(new EmptyBorder(10, 14, 10, 14));
                logCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

                JLabel dishLabel = new JLabel(entry[3]);
                dishLabel.setFont(UIConstants.FONT_BODY_BOLD);
                dishLabel.setForeground(UIConstants.TEXT_PRIMARY);

                JLabel infoLabel = new JLabel(entry[1] + " · " + entry[2] + " · " + entry[0]);
                infoLabel.setFont(UIConstants.FONT_SMALL);
                infoLabel.setForeground(UIConstants.TEXT_MUTED);

                JPanel left = new JPanel();
                left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
                left.setOpaque(false);
                left.add(dishLabel);
                left.add(infoLabel);

                logCard.add(left, BorderLayout.CENTER);

                historyPanel.add(logCard);
                historyPanel.add(Box.createVerticalStrut(4));
            }

            // Total count
            JLabel countLabel = new JLabel("   Total meals logged: " + logs.size());
            countLabel.setFont(UIConstants.FONT_SMALL_BOLD);
            countLabel.setForeground(UIConstants.ACCENT_LIGHT);
            countLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
            historyPanel.add(countLabel);
        }

        historyPanel.revalidate();
        historyPanel.repaint();
    }
}
