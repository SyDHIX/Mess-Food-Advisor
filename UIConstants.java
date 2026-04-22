import java.awt.*;

public class UIConstants {

    // ─── Dark Theme Colors ───
    public static final Color BG_DARK       = new Color(15, 15, 30);
    public static final Color BG_CARD       = new Color(25, 25, 50);
    public static final Color BG_CARD_HOVER = new Color(35, 35, 65);
    public static final Color BG_INPUT      = new Color(30, 30, 55);
    public static final Color ACCENT        = new Color(124, 92, 252);
    public static final Color ACCENT_LIGHT  = new Color(167, 139, 250);
    public static final Color GREEN         = new Color(34, 197, 94);
    public static final Color AMBER         = new Color(245, 158, 11);
    public static final Color RED           = new Color(239, 68, 68);
    public static final Color PINK          = new Color(236, 72, 153);
    public static final Color CYAN          = new Color(56, 189, 248);
    public static final Color TEXT_PRIMARY  = new Color(240, 240, 245);
    public static final Color TEXT_SECONDARY= new Color(160, 160, 184);
    public static final Color TEXT_MUTED    = new Color(107, 107, 133);
    public static final Color BORDER        = new Color(50, 50, 80);
    public static final Color TRANSPARENT   = new Color(0, 0, 0, 0);

    // ─── Fonts ───
    public static final Font FONT_TITLE     = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font FONT_HEADING   = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBHEAD   = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BODY      = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL     = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SMALL_BOLD= new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_TINY      = new Font("Segoe UI", Font.BOLD, 10);
    public static final Font FONT_MEGA      = new Font("Segoe UI", Font.BOLD, 52);

    // ─── Category Colors ───
    public static Color getCategoryColor(String category) {
        return switch (category) {
            case "Veg1", "Main", "Salad"  -> GREEN;
            case "Veg2", "Accompaniment"  -> new Color(74, 222, 128);
            case "Dal", "Cereal", "Bread" -> AMBER;
            case "Staple", "Raita"        -> ACCENT_LIGHT;
            case "Dessert"                -> PINK;
            case "Soup", "Beverage"       -> CYAN;
            case "Snack"                  -> new Color(251, 146, 60);
            case "Fruit"                  -> new Color(163, 230, 53);
            case "Egg"                    -> new Color(251, 191, 36);
            case "Chutney"               -> GREEN;
            default                       -> TEXT_SECONDARY;
        };
    }

    // ─── Rating Color ───
    public static Color getRatingColor(double rating) {
        if (rating >= 4.0) return GREEN;
        if (rating >= 3.0) return AMBER;
        return RED;
    }

    // ─── Stars string ───
    public static String getStars(double rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            if (rating >= i) sb.append("★");
            else if (rating >= i - 0.5) sb.append("☆");
            else sb.append("·");
        }
        return sb.toString();
    }

    // ─── Rendering hints for smooth graphics ───
    public static void applyRenderingHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}
