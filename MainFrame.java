import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final MenuData menuData;

    public MainFrame() {
        setTitle("Bennett Mess Food Advisor");
        setSize(1050, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));

        menuData = new MenuData();
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(UIConstants.BG_DARK);

        // Pages
        HomePage home = new HomePage(menuData,
            () -> showPage("dashboard"),
            () -> showPage("search"),
            () -> showPage("ratings"),
            () -> showPage("comparison"),
            () -> showPage("meallog")
        );
        DashboardPage dashboard = new DashboardPage(menuData, () -> showPage("home"));
        SearchPage search = new SearchPage(menuData, () -> showPage("home"));
        RatingsPage ratings = new RatingsPage(menuData, () -> showPage("home"));
        ComparisonPage comparison = new ComparisonPage(() -> showPage("home"));
        MealLogPage meallog = new MealLogPage(menuData, () -> showPage("home"));

        cardPanel.add(home, "home");
        cardPanel.add(dashboard, "dashboard");
        cardPanel.add(search, "search");
        cardPanel.add(ratings, "ratings");
        cardPanel.add(comparison, "comparison");
        cardPanel.add(meallog, "meallog");

        add(cardPanel);
        showPage("home");
    }

    private void showPage(String name) {
        cardLayout.show(cardPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }
}
