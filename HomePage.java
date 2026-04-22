import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

public class HomePage extends JPanel {

    private final Runnable onExploreClicked;
    private final Runnable onSearchClicked;
    private final Runnable onRatingsClicked;
    private final Runnable onComparisonClicked;
    private final Runnable onMealLogClicked;
    private final MenuData menuData;
    private float animProgress = 0f;
    private Timer animTimer;

    public HomePage(MenuData menuData, Runnable onExploreClicked, Runnable onSearchClicked,
                    Runnable onRatingsClicked, Runnable onComparisonClicked, Runnable onMealLogClicked) {
        this.menuData = menuData;
        this.onExploreClicked = onExploreClicked;
        this.onSearchClicked = onSearchClicked;
        this.onRatingsClicked = onRatingsClicked;
        this.onComparisonClicked = onComparisonClicked;
        this.onMealLogClicked = onMealLogClicked;
        setBackground(UIConstants.BG_DARK);
        setLayout(new BorderLayout());
        setOpaque(true);

        // Entrance animation
        animTimer = new Timer(16, e -> {
            animProgress += 0.02f;
            if (animProgress >= 1f) { animProgress = 1f; animTimer.stop(); }
            repaint();
        });
        animTimer.start();

        // Build content
        add(buildCenter(), BorderLayout.CENTER);
    }

    private JPanel buildCenter() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new GridBagLayout());

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // ─── Emoji icon ───
        JLabel icon = new JLabel("\uD83C\uDF5B", SwingConstants.CENTER);  // 🍛
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(icon);
        content.add(Box.createVerticalStrut(16));

        // ─── Title ───
        JLabel title = new JLabel("Bennett Mess Advisor");
        title.setFont(UIConstants.FONT_MEGA);
        title.setForeground(UIConstants.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(8));

        // ─── Tagline ───
        JLabel tagline = new JLabel("Know your plate before you go \uD83E\uDD42");
        tagline.setFont(UIConstants.FONT_SUBHEAD);
        tagline.setForeground(UIConstants.TEXT_SECONDARY);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(tagline);
        content.add(Box.createVerticalStrut(40));

        // ─── Stats Row ───
        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        statsRow.setOpaque(false);
        statsRow.add(buildStatBubble("7", "Days"));
        statsRow.add(buildStatBubble("4", "Meals/Day"));
        statsRow.add(buildStatBubble("28", "Menus"));
        content.add(statsRow);
        content.add(Box.createVerticalStrut(40));

        // ─── Main Buttons (Explore + Search) ───
        JPanel mainButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        mainButtons.setOpaque(false);

        JButton exploreBtn = createStyledButton("\uD83C\uDF7D  Explore Menu", UIConstants.ACCENT, true);
        exploreBtn.addActionListener(e -> onExploreClicked.run());

        JButton searchBtn = createStyledButton("\uD83D\uDD0D  Search Food", UIConstants.BG_CARD, false);
        searchBtn.addActionListener(e -> onSearchClicked.run());

        mainButtons.add(exploreBtn);
        mainButtons.add(searchBtn);
        content.add(mainButtons);
        content.add(Box.createVerticalStrut(16));

        // ─── New Feature Buttons (Ratings, Comparison, Meal Log) ───
        JPanel featureButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        featureButtons.setOpaque(false);

        JButton ratingsBtn = createStyledButton("⭐ Rate Dishes", new Color(245, 158, 11), true);
        ratingsBtn.setPreferredSize(new Dimension(170, 44));
        ratingsBtn.addActionListener(e -> onRatingsClicked.run());

        JButton compareBtn = createStyledButton("💰 Mess vs Outside", new Color(34, 197, 94), true);
        compareBtn.setPreferredSize(new Dimension(190, 44));
        compareBtn.addActionListener(e -> onComparisonClicked.run());

        JButton logBtn = createStyledButton("📝 Meal Log", new Color(56, 189, 248), true);
        logBtn.setPreferredSize(new Dimension(160, 44));
        logBtn.addActionListener(e -> onMealLogClicked.run());

        featureButtons.add(ratingsBtn);
        featureButtons.add(compareBtn);
        featureButtons.add(logBtn);
        content.add(featureButtons);
        content.add(Box.createVerticalStrut(50));

        // ─── Top Picks ───
        JLabel topLabel = new JLabel("⭐ Top Rated This Week");
        topLabel.setFont(UIConstants.FONT_SUBHEAD);
        topLabel.setForeground(UIConstants.TEXT_PRIMARY);
        topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(topLabel);
        content.add(Box.createVerticalStrut(16));

        JPanel topGrid = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        topGrid.setOpaque(false);
        List<MenuData.SearchResult> topItems = menuData.getTopRated(6);
        for (MenuData.SearchResult sr : topItems) {
            topGrid.add(buildTopCard(sr));
        }
        content.add(topGrid);

        wrapper.add(content);
        return wrapper;
    }

    private RoundedPanel buildStatBubble(String value, String label) {
        RoundedPanel bubble = new RoundedPanel(16, UIConstants.BG_CARD);
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        bubble.setPreferredSize(new Dimension(110, 80));

        JLabel valLabel = new JLabel(value, SwingConstants.CENTER);
        valLabel.setFont(UIConstants.FONT_HEADING);
        valLabel.setForeground(UIConstants.ACCENT_LIGHT);
        valLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(label, SwingConstants.CENTER);
        nameLabel.setFont(UIConstants.FONT_SMALL);
        nameLabel.setForeground(UIConstants.TEXT_MUTED);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bubble.add(valLabel);
        bubble.add(Box.createVerticalStrut(4));
        bubble.add(nameLabel);
        return bubble;
    }

    private RoundedPanel buildTopCard(MenuData.SearchResult sr) {
        FoodItem item = sr.item();
        RoundedPanel card = new RoundedPanel(14, UIConstants.BG_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        card.setPreferredSize(new Dimension(170, 95));

        // Hover
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBgColor(UIConstants.BG_CARD_HOVER); }
            public void mouseExited(MouseEvent e) { card.setBgColor(UIConstants.BG_CARD); }
        });

        JLabel name = new JLabel(item.getName());
        name.setFont(UIConstants.FONT_BODY_BOLD);
        name.setForeground(UIConstants.TEXT_PRIMARY);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel info = new JLabel(sr.day() + " · " + sr.meal());
        info.setFont(UIConstants.FONT_SMALL);
        info.setForeground(UIConstants.TEXT_MUTED);
        info.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rating = new JLabel(UIConstants.getStars(item.getRating()) + " " + item.getRating());
        rating.setFont(UIConstants.FONT_SMALL_BOLD);
        rating.setForeground(UIConstants.getRatingColor(item.getRating()));
        rating.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(name);
        card.add(Box.createVerticalStrut(4));
        card.add(info);
        card.add(Box.createVerticalStrut(4));
        card.add(rating);
        return card;
    }

    private JButton createStyledButton(String text, Color bg, boolean filled) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                UIConstants.applyRenderingHints(g2);
                if (filled) {
                    g2.setColor(bg);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                } else {
                    g2.setColor(bg);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 24, 24));
                    g2.setColor(UIConstants.BORDER);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, 24, 24));
                }
                g2.setColor(UIConstants.TEXT_PRIMARY);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(UIConstants.FONT_BODY_BOLD);
        btn.setForeground(UIConstants.TEXT_PRIMARY);
        btn.setPreferredSize(new Dimension(200, 48));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);

        btn.addMouseListener(new MouseAdapter() {
            Color orig = bg;
            public void mouseEntered(MouseEvent e) {
                btn.repaint();
            }
        });
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        UIConstants.applyRenderingHints(g2);

        // Gradient overlay from top
        GradientPaint gp = new GradientPaint(0, 0, new Color(124, 92, 252, 15),
            0, getHeight(), new Color(15, 15, 30, 0));
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Subtle circles in background
        float a = Math.min(animProgress, 1f);
        int alpha = (int)(12 * a);
        g2.setColor(new Color(124, 92, 252, alpha));
        g2.fillOval(getWidth()-300, -100, 500, 500);
        g2.setColor(new Color(34, 197, 94, alpha/2));
        g2.fillOval(-150, getHeight()-200, 400, 400);

        g2.dispose();
    }
}
