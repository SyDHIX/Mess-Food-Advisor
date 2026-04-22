import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;

public class SearchPage extends JPanel {

    private final MenuData menuData;
    private final Runnable onBack;
    private JTextField searchField;
    private JPanel resultsPanel;
    private JLabel resultCount;

    public SearchPage(MenuData menuData, Runnable onBack) {
        this.menuData = menuData;
        this.onBack = onBack;
        setBackground(UIConstants.BG_DARK);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
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

        JLabel title = new JLabel("🔍 Global Search");
        title.setFont(UIConstants.FONT_HEADING);
        title.setForeground(UIConstants.TEXT_PRIMARY);

        header.add(backBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        return header;
    }

    private JScrollPane buildBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UIConstants.BG_DARK);
        body.setBorder(new EmptyBorder(20, 24, 24, 24));

        // ─── Search Bar ───
        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setOpaque(false);
        searchBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        searchField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                UIConstants.applyRenderingHints(g2);
                g2.setColor(UIConstants.BG_INPUT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(UIConstants.BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        searchField.setFont(UIConstants.FONT_BODY);
        searchField.setForeground(UIConstants.TEXT_PRIMARY);
        searchField.setCaretColor(UIConstants.TEXT_PRIMARY);
        searchField.setOpaque(false);
        searchField.setBorder(new EmptyBorder(10, 18, 10, 18));
        searchField.setPreferredSize(new Dimension(0, 48));

        // Placeholder text
        searchField.setText("");
        addPlaceholder(searchField, "Search dishes, days, categories... (e.g. paneer, monday, dal)");

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { doSearch(); }
            public void removeUpdate(DocumentEvent e) { doSearch(); }
            public void changedUpdate(DocumentEvent e) { doSearch(); }
        });

        searchBar.add(searchField, BorderLayout.CENTER);
        body.add(searchBar);
        body.add(Box.createVerticalStrut(16));

        // ─── Quick filter chips ───
        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        chips.setOpaque(false);
        String[] quickFilters = {"Paneer", "Dal", "Monday", "Breakfast", "Dessert", "Snack", "Soup", "Friday"};
        for (String filter : quickFilters) {
            JButton chip = makeChipButton(filter);
            chip.addActionListener(e -> {
                searchField.setText(filter);
                doSearch();
            });
            chips.add(chip);
        }
        body.add(chips);
        body.add(Box.createVerticalStrut(8));

        // ─── Result Count ───
        resultCount = new JLabel("Type to search across all menus...");
        resultCount.setFont(UIConstants.FONT_SMALL);
        resultCount.setForeground(UIConstants.TEXT_MUTED);
        body.add(resultCount);
        body.add(Box.createVerticalStrut(12));

        // ─── Results Grid ───
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setOpaque(false);
        body.add(resultsPanel);

        // Show recommendations initially
        showRecommendations();

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBackground(UIConstants.BG_DARK);
        scroll.getViewport().setBackground(UIConstants.BG_DARK);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private void doSearch() {
        String query = searchField.getText().trim();
        resultsPanel.removeAll();

        if (query.isEmpty()) {
            resultCount.setText("Type to search across all menus...");
            showRecommendations();
            return;
        }

        List<MenuData.SearchResult> results = menuData.search(query);
        resultCount.setText(results.size() + " result" + (results.size() != 1 ? "s" : "") + " found for \"" + query + "\"");

        if (results.isEmpty()) {
            JLabel noResult = new JLabel("😕 No items found. Try another search term.");
            noResult.setFont(UIConstants.FONT_BODY);
            noResult.setForeground(UIConstants.TEXT_MUTED);
            noResult.setBorder(new EmptyBorder(30, 0, 0, 0));
            resultsPanel.add(noResult);
        } else {
            // Group by day+meal
            String lastGroup = "";
            for (MenuData.SearchResult sr : results) {
                String group = sr.day() + " — " + sr.meal();
                if (!group.equals(lastGroup)) {
                    JLabel groupLabel = new JLabel("📅 " + group);
                    groupLabel.setFont(UIConstants.FONT_BODY_BOLD);
                    groupLabel.setForeground(UIConstants.ACCENT_LIGHT);
                    groupLabel.setBorder(new EmptyBorder(12, 0, 6, 0));
                    resultsPanel.add(groupLabel);
                    lastGroup = group;
                }
                resultsPanel.add(buildResultCard(sr));
                resultsPanel.add(Box.createVerticalStrut(6));
            }
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private void showRecommendations() {
        JLabel recLabel = new JLabel("⭐ Recommended Dishes");
        recLabel.setFont(UIConstants.FONT_SUBHEAD);
        recLabel.setForeground(UIConstants.TEXT_PRIMARY);
        recLabel.setBorder(new EmptyBorder(8, 0, 12, 0));
        resultsPanel.add(recLabel);

        List<MenuData.SearchResult> top = menuData.getTopRated(10);
        for (MenuData.SearchResult sr : top) {
            resultsPanel.add(buildResultCard(sr));
            resultsPanel.add(Box.createVerticalStrut(6));
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private RoundedPanel buildResultCard(MenuData.SearchResult sr) {
        FoodItem item = sr.item();
        RoundedPanel card = new RoundedPanel(12, UIConstants.BG_CARD);
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // Hover
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBgColor(UIConstants.BG_CARD_HOVER); }
            public void mouseExited(MouseEvent e) { card.setBgColor(UIConstants.BG_CARD); }
        });

        // Left: name + meta
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel name = new JLabel(item.getName());
        name.setFont(UIConstants.FONT_BODY_BOLD);
        name.setForeground(UIConstants.TEXT_PRIMARY);

        JLabel meta = new JLabel(sr.day() + "  ·  " + sr.meal() + "  ·  🔥 " + item.getCalories() + " Kcal");
        meta.setFont(UIConstants.FONT_SMALL);
        meta.setForeground(UIConstants.TEXT_MUTED);

        left.add(name);
        left.add(Box.createVerticalStrut(4));
        left.add(meta);

        // Right: rating + category
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);

        JLabel cat = new JLabel(item.getCategory().toUpperCase());
        cat.setFont(UIConstants.FONT_TINY);
        cat.setForeground(UIConstants.getCategoryColor(item.getCategory()));
        cat.setAlignmentX(Component.RIGHT_ALIGNMENT);

        if (!item.isStaple()) {
            JLabel ratingLabel = new JLabel(UIConstants.getStars(item.getRating()) + "  " + String.format("%.1f", item.getRating()));
            ratingLabel.setFont(UIConstants.FONT_SMALL_BOLD);
            ratingLabel.setForeground(UIConstants.getRatingColor(item.getRating()));
            ratingLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            right.add(ratingLabel);
            right.add(Box.createVerticalStrut(4));
        }
        right.add(cat);

        card.add(left, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private JButton makeChipButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                UIConstants.applyRenderingHints(g2);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(UIConstants.BORDER);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 20, 20));
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setFont(UIConstants.FONT_SMALL);
        btn.setForeground(UIConstants.ACCENT_LIGHT);
        btn.setBackground(new Color(124, 92, 252, 25));
        btn.setPreferredSize(new Dimension(btn.getFontMetrics(btn.getFont()).stringWidth(text) + 28, 30));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        return btn;
    }

    private void addPlaceholder(JTextField field, String placeholder) {
        field.setForeground(UIConstants.TEXT_MUTED);
        field.setText(placeholder);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(UIConstants.TEXT_PRIMARY);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(UIConstants.TEXT_MUTED);
                }
            }
        });
    }
}
