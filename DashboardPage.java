import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

public class DashboardPage extends JPanel {

    private final MenuData menuData;
    private final Runnable onBack;
    private String selectedDay;
    private String selectedMeal = "Breakfast";
    private JPanel foodGrid;
    private JPanel verdictPanel;
    private JLabel verdictIcon, verdictTitle, verdictAdvice;
    private JLabel statCal, statRating, statItems;
    private RoundedPanel statRatingCard;
    private JButton[] dayButtons;
    private JButton[] mealButtons;

    public DashboardPage(MenuData menuData, Runnable onBack) {
        this.menuData = menuData;
        this.onBack = onBack;
        setBackground(UIConstants.BG_DARK);
        setLayout(new BorderLayout());

        // Auto-select today
        String[] jDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        int dow = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK);
        selectedDay = jDays[dow - 1];

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);

        refreshMenu();
    }

    // ─── Header ───
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(10, 10, 25));
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        // Back button
        JButton backBtn = makeTextButton("← Back");
        backBtn.addActionListener(e -> onBack.run());

        JLabel title = new JLabel("🍽️ Daily Menu Explorer");
        title.setFont(UIConstants.FONT_HEADING);
        title.setForeground(UIConstants.TEXT_PRIMARY);

        header.add(backBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);

        // Day + Meal nav below header
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(new Color(10, 10, 25));
        nav.setBorder(new EmptyBorder(0, 20, 12, 20));

        // Day pills
        JPanel dayRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        dayRow.setOpaque(false);
        dayButtons = new JButton[7];
        List<String> days = menuData.getDays();
        for (int i = 0; i < days.size(); i++) {
            String day = days.get(i);
            JButton btn = makePillButton(day.substring(0, 3));
            btn.setActionCommand(day);
            int idx = i;
            btn.addActionListener(e -> {
                selectedDay = day;
                updateDayButtons();
                refreshMenu();
            });
            dayButtons[i] = btn;
            dayRow.add(btn);
        }
        nav.add(dayRow);

        // Meal tabs
        JPanel mealRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        mealRow.setOpaque(false);
        String[] mealTypes = menuData.getMealTypes();
        String[] mealEmojis = {"🌅","🍛","🍿","🌙"};
        mealButtons = new JButton[4];
        for (int i = 0; i < mealTypes.length; i++) {
            String mt = mealTypes[i];
            JButton btn = makePillButton(mealEmojis[i] + " " + mt);
            btn.setPreferredSize(new Dimension(140, 36));
            int idx = i;
            btn.addActionListener(e -> {
                selectedMeal = mt;
                updateMealButtons();
                refreshMenu();
            });
            mealButtons[i] = btn;
            mealRow.add(btn);
        }
        nav.add(mealRow);

        JPanel fullHeader = new JPanel(new BorderLayout());
        fullHeader.setBackground(new Color(10, 10, 25));
        fullHeader.add(header, BorderLayout.NORTH);
        fullHeader.add(nav, BorderLayout.CENTER);

        updateDayButtons();
        updateMealButtons();
        return fullHeader;
    }

    // ─── Body ───
    private JScrollPane buildBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UIConstants.BG_DARK);
        body.setBorder(new EmptyBorder(16, 24, 24, 24));

        // Verdict
        verdictPanel = new JPanel(new BorderLayout(16, 0));
        verdictPanel.setOpaque(false);
        verdictPanel.setBorder(new EmptyBorder(0, 0, 16, 0));

        RoundedPanel verdictCard = new RoundedPanel(16, UIConstants.BG_CARD);
        verdictCard.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 14));

        verdictIcon = new JLabel("🎉");
        verdictIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));

        JPanel verdictTextPanel = new JPanel();
        verdictTextPanel.setLayout(new BoxLayout(verdictTextPanel, BoxLayout.Y_AXIS));
        verdictTextPanel.setOpaque(false);

        verdictTitle = new JLabel("Loading...");
        verdictTitle.setFont(UIConstants.FONT_SUBHEAD);
        verdictTitle.setForeground(UIConstants.GREEN);

        verdictAdvice = new JLabel("");
        verdictAdvice.setFont(UIConstants.FONT_BODY);
        verdictAdvice.setForeground(UIConstants.TEXT_SECONDARY);

        verdictTextPanel.add(verdictTitle);
        verdictTextPanel.add(Box.createVerticalStrut(2));
        verdictTextPanel.add(verdictAdvice);

        verdictCard.add(verdictIcon);
        verdictCard.add(verdictTextPanel);
        verdictPanel.add(verdictCard, BorderLayout.CENTER);
        body.add(verdictPanel);

        // Stats row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 12, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        RoundedPanel calCard = new RoundedPanel(12, UIConstants.BG_CARD);
        calCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 14));
        JLabel calIcon = new JLabel("🔥"); calIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        statCal = new JLabel("0 Kcal");
        statCal.setFont(UIConstants.FONT_HEADING);
        statCal.setForeground(UIConstants.TEXT_PRIMARY);
        JLabel calLbl = new JLabel("  Total");
        calLbl.setFont(UIConstants.FONT_SMALL);
        calLbl.setForeground(UIConstants.TEXT_MUTED);
        calCard.add(calIcon); calCard.add(statCal); calCard.add(calLbl);

        statRatingCard = new RoundedPanel(12, UIConstants.BG_CARD);
        statRatingCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 14));
        JLabel ratIcon = new JLabel("⭐"); ratIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        statRating = new JLabel("0.0 / 5.0");
        statRating.setFont(UIConstants.FONT_HEADING);
        statRating.setForeground(UIConstants.TEXT_PRIMARY);
        JLabel ratLbl = new JLabel("  Avg");
        ratLbl.setFont(UIConstants.FONT_SMALL);
        ratLbl.setForeground(UIConstants.TEXT_MUTED);
        statRatingCard.add(ratIcon); statRatingCard.add(statRating); statRatingCard.add(ratLbl);

        RoundedPanel itemCard = new RoundedPanel(12, UIConstants.BG_CARD);
        itemCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 14));
        JLabel itIcon = new JLabel("📋"); itIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        statItems = new JLabel("0");
        statItems.setFont(UIConstants.FONT_HEADING);
        statItems.setForeground(UIConstants.TEXT_PRIMARY);
        JLabel itLbl = new JLabel("  Items");
        itLbl.setFont(UIConstants.FONT_SMALL);
        itLbl.setForeground(UIConstants.TEXT_MUTED);
        itemCard.add(itIcon); itemCard.add(statItems); itemCard.add(itLbl);

        statsRow.add(calCard);
        statsRow.add(statRatingCard);
        statsRow.add(itemCard);
        body.add(statsRow);
        body.add(Box.createVerticalStrut(16));

        // Food grid
        foodGrid = new JPanel(new GridLayout(0, 3, 12, 12));
        foodGrid.setOpaque(false);
        body.add(foodGrid);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBackground(UIConstants.BG_DARK);
        scroll.getViewport().setBackground(UIConstants.BG_DARK);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    // ─── Refresh ───
    private void refreshMenu() {
        List<FoodItem> items = menuData.getMeal(selectedDay, selectedMeal);
        int totalCal = menuData.getTotalCalories(items);
        double avgRating = menuData.getAverageRating(items);
        String verdict = menuData.getVerdict(avgRating);
        String level = menuData.getVerdictLevel(avgRating);
        String advice = menuData.getAdvice(avgRating, selectedMeal);

        // Verdict
        switch (level) {
            case "excellent" -> {
                verdictIcon.setText("🎉");
                verdictTitle.setForeground(UIConstants.GREEN);
            }
            case "decent" -> {
                verdictIcon.setText("😐");
                verdictTitle.setForeground(UIConstants.AMBER);
            }
            default -> {
                verdictIcon.setText("🚫");
                verdictTitle.setForeground(UIConstants.RED);
            }
        }
        verdictTitle.setText(verdict);
        verdictAdvice.setText(advice);

        // Stats
        statCal.setText(totalCal + " Kcal");
        statRating.setText(String.format("%.2f / 5.0", avgRating));
        statItems.setText(String.valueOf(items.size()));
        statRatingCard.setBorderColor(
            avgRating > 4.2 ? UIConstants.GREEN :
            avgRating > 3.6 ? UIConstants.AMBER : UIConstants.RED
        );

        // Food cards
        foodGrid.removeAll();
        for (FoodItem item : items) {
            foodGrid.add(buildFoodCard(item));
        }
        foodGrid.revalidate();
        foodGrid.repaint();
    }

    // ─── Food Card ───
    private RoundedPanel buildFoodCard(FoodItem item) {
        RoundedPanel card = new RoundedPanel(14, UIConstants.BG_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBgColor(UIConstants.BG_CARD_HOVER); }
            public void mouseExited(MouseEvent e) { card.setBgColor(UIConstants.BG_CARD); }
        });

        // Top row: name + category
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel name = new JLabel(item.getName());
        name.setFont(UIConstants.FONT_BODY_BOLD);
        name.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel cat = new JLabel(item.getCategory().toUpperCase());
        cat.setFont(UIConstants.FONT_TINY);
        cat.setForeground(UIConstants.getCategoryColor(item.getCategory()));
        cat.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(
                UIConstants.getCategoryColor(item.getCategory()).getRed(),
                UIConstants.getCategoryColor(item.getCategory()).getGreen(),
                UIConstants.getCategoryColor(item.getCategory()).getBlue(), 60), 1, true),
            new EmptyBorder(2, 6, 2, 6)
        ));

        topRow.add(name, BorderLayout.CENTER);
        topRow.add(cat, BorderLayout.EAST);
        card.add(topRow);
        card.add(Box.createVerticalStrut(12));

        // Bottom row: calories + rating
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);

        JLabel cal = new JLabel("🔥 " + item.getCalories() + " Kcal");
        cal.setFont(UIConstants.FONT_SMALL);
        cal.setForeground(UIConstants.TEXT_SECONDARY);

        if (item.isStaple()) {
            JLabel stapleBadge = new JLabel("STAPLE");
            stapleBadge.setFont(UIConstants.FONT_TINY);
            stapleBadge.setForeground(UIConstants.ACCENT_LIGHT);
            bottomRow.add(stapleBadge, BorderLayout.EAST);
        } else {
            JLabel ratingLabel = new JLabel(UIConstants.getStars(item.getRating()) + " " + String.format("%.1f", item.getRating()));
            ratingLabel.setFont(UIConstants.FONT_SMALL_BOLD);
            ratingLabel.setForeground(UIConstants.getRatingColor(item.getRating()));
            bottomRow.add(ratingLabel, BorderLayout.EAST);
        }

        bottomRow.add(cal, BorderLayout.WEST);
        card.add(bottomRow);

        return card;
    }

    // ─── Helpers ───
    private void updateDayButtons() {
        List<String> days = menuData.getDays();
        for (int i = 0; i < dayButtons.length; i++) {
            boolean active = days.get(i).equals(selectedDay);
            dayButtons[i].setBackground(active ? UIConstants.ACCENT : UIConstants.BG_CARD);
            dayButtons[i].setForeground(active ? Color.WHITE : UIConstants.TEXT_SECONDARY);
            dayButtons[i].setFont(active ? UIConstants.FONT_BODY_BOLD : UIConstants.FONT_BODY);
        }
    }

    private void updateMealButtons() {
        String[] mealTypes = menuData.getMealTypes();
        for (int i = 0; i < mealButtons.length; i++) {
            boolean active = mealTypes[i].equals(selectedMeal);
            mealButtons[i].setBackground(active ? new Color(40, 35, 80) : UIConstants.BG_CARD);
            mealButtons[i].setForeground(active ? UIConstants.TEXT_PRIMARY : UIConstants.TEXT_SECONDARY);
            mealButtons[i].setFont(active ? UIConstants.FONT_BODY_BOLD : UIConstants.FONT_BODY);
        }
    }

    private JButton makePillButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                UIConstants.applyRenderingHints(g2);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                g2.setColor(UIConstants.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 24, 24));
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setFont(UIConstants.FONT_BODY);
        btn.setForeground(UIConstants.TEXT_SECONDARY);
        btn.setBackground(UIConstants.BG_CARD);
        btn.setPreferredSize(new Dimension(70, 36));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        return btn;
    }

    private JButton makeTextButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.FONT_BODY_BOLD);
        btn.setForeground(UIConstants.ACCENT_LIGHT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
