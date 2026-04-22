import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;

/**
 * ComparisonPage shows a cost comparison between eating in mess vs ordering outside.
 * Helps students see how much money they save by eating in mess.
 */
public class ComparisonPage extends JPanel {

    private final Runnable onBack;

    // Average costs (in Rupees)
    private static final int MESS_MONTHLY_FEE = 4500;   // Monthly mess fee
    private static final int DAYS_IN_MONTH = 30;

    // Average outside food costs per meal
    private static final int OUTSIDE_BREAKFAST = 80;
    private static final int OUTSIDE_LUNCH = 150;
    private static final int OUTSIDE_SNACKS = 60;
    private static final int OUTSIDE_DINNER = 160;

    public ComparisonPage(Runnable onBack) {
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

        JLabel title = new JLabel("💰 Mess vs Outside Food");
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

        // ─── Per-meal comparison table ───
        JLabel tableTitle = new JLabel("📊 Per Meal Cost Comparison");
        tableTitle.setFont(UIConstants.FONT_SUBHEAD);
        tableTitle.setForeground(UIConstants.TEXT_PRIMARY);
        tableTitle.setBorder(new EmptyBorder(0, 0, 12, 0));
        body.add(tableTitle);

        // Table header
        body.add(buildTableRow("Meal", "Mess (₹)", "Outside (₹)", "You Save", true));
        body.add(Box.createVerticalStrut(6));

        // Calculate mess cost per meal (monthly fee / 30 days / 4 meals)
        int messPerMeal = MESS_MONTHLY_FEE / DAYS_IN_MONTH / 4;

        // Add rows for each meal
        body.add(buildTableRow("🌅 Breakfast", "₹" + messPerMeal, "₹" + OUTSIDE_BREAKFAST,
            "₹" + (OUTSIDE_BREAKFAST - messPerMeal), false));
        body.add(Box.createVerticalStrut(6));

        body.add(buildTableRow("🍛 Lunch", "₹" + messPerMeal, "₹" + OUTSIDE_LUNCH,
            "₹" + (OUTSIDE_LUNCH - messPerMeal), false));
        body.add(Box.createVerticalStrut(6));

        body.add(buildTableRow("🍿 Snacks", "₹" + messPerMeal, "₹" + OUTSIDE_SNACKS,
            "₹" + (OUTSIDE_SNACKS - messPerMeal), false));
        body.add(Box.createVerticalStrut(6));

        body.add(buildTableRow("🌙 Dinner", "₹" + messPerMeal, "₹" + OUTSIDE_DINNER,
            "₹" + (OUTSIDE_DINNER - messPerMeal), false));

        body.add(Box.createVerticalStrut(30));

        // ─── Monthly Summary ───
        JLabel summaryTitle = new JLabel("📅 Monthly Summary");
        summaryTitle.setFont(UIConstants.FONT_SUBHEAD);
        summaryTitle.setForeground(UIConstants.TEXT_PRIMARY);
        summaryTitle.setBorder(new EmptyBorder(0, 0, 12, 0));
        body.add(summaryTitle);

        int outsideMonthly = (OUTSIDE_BREAKFAST + OUTSIDE_LUNCH + OUTSIDE_SNACKS + OUTSIDE_DINNER) * DAYS_IN_MONTH;
        int savings = outsideMonthly - MESS_MONTHLY_FEE;

        // Mess cost card
        body.add(buildSummaryCard("🏫 Mess Monthly Cost", "₹" + MESS_MONTHLY_FEE, UIConstants.GREEN));
        body.add(Box.createVerticalStrut(8));

        // Outside cost card
        body.add(buildSummaryCard("🛵 Outside Monthly Cost", "₹" + outsideMonthly, UIConstants.RED));
        body.add(Box.createVerticalStrut(8));

        // Savings card
        body.add(buildSummaryCard("💰 Monthly Savings with Mess", "₹" + savings, UIConstants.AMBER));
        body.add(Box.createVerticalStrut(20));

        // ─── Advice ───
        RoundedPanel adviceCard = new RoundedPanel(14, new Color(34, 197, 94, 15));
        adviceCard.setLayout(new BoxLayout(adviceCard, BoxLayout.Y_AXIS));
        adviceCard.setBorder(new EmptyBorder(16, 20, 16, 20));
        adviceCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel adviceIcon = new JLabel("💡 Smart Tip");
        adviceIcon.setFont(UIConstants.FONT_BODY_BOLD);
        adviceIcon.setForeground(UIConstants.GREEN);

        JLabel advice1 = new JLabel("Eating in mess saves you approximately ₹" + savings + " per month!");
        advice1.setFont(UIConstants.FONT_BODY);
        advice1.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel advice2 = new JLabel("That's ₹" + (savings * 12) + "/year — enough for a new phone! 📱");
        advice2.setFont(UIConstants.FONT_BODY);
        advice2.setForeground(UIConstants.TEXT_SECONDARY);

        adviceCard.add(adviceIcon);
        adviceCard.add(Box.createVerticalStrut(8));
        adviceCard.add(advice1);
        adviceCard.add(Box.createVerticalStrut(4));
        adviceCard.add(advice2);
        body.add(adviceCard);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBackground(UIConstants.BG_DARK);
        scroll.getViewport().setBackground(UIConstants.BG_DARK);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    // ─── Build one row of the comparison table ───
    private RoundedPanel buildTableRow(String meal, String messCost, String outsideCost, String savings, boolean isHeader) {
        RoundedPanel row = new RoundedPanel(10, isHeader ? new Color(40, 35, 80) : UIConstants.BG_CARD);
        row.setLayout(new GridLayout(1, 4, 8, 0));
        row.setBorder(new EmptyBorder(12, 16, 12, 16));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        Font font = isHeader ? UIConstants.FONT_BODY_BOLD : UIConstants.FONT_BODY;
        Color color = isHeader ? UIConstants.ACCENT_LIGHT : UIConstants.TEXT_PRIMARY;

        JLabel col1 = new JLabel(meal);     col1.setFont(font); col1.setForeground(color);
        JLabel col2 = new JLabel(messCost); col2.setFont(font); col2.setForeground(isHeader ? color : UIConstants.GREEN);
        JLabel col3 = new JLabel(outsideCost); col3.setFont(font); col3.setForeground(isHeader ? color : UIConstants.RED);
        JLabel col4 = new JLabel(savings);  col4.setFont(font); col4.setForeground(isHeader ? color : UIConstants.AMBER);

        row.add(col1); row.add(col2); row.add(col3); row.add(col4);
        return row;
    }

    // ─── Build a summary card ───
    private RoundedPanel buildSummaryCard(String label, String value, Color valueColor) {
        RoundedPanel card = new RoundedPanel(12, UIConstants.BG_CARD);
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_BODY);
        lbl.setForeground(UIConstants.TEXT_SECONDARY);

        JLabel val = new JLabel(value);
        val.setFont(UIConstants.FONT_HEADING);
        val.setForeground(valueColor);

        card.add(lbl, BorderLayout.WEST);
        card.add(val, BorderLayout.EAST);
        return card;
    }
}
