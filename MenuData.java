import java.util.*;

public class MenuData {

    private final LinkedHashMap<String, LinkedHashMap<String, List<FoodItem>>> menu = new LinkedHashMap<>();
    private final Random rng = new Random(42);

    public MenuData() {
        // ===== MONDAY =====
        add("Monday", "Breakfast", new Object[][]{
            {"Vada Pao",276,"Main"},{"Boiled Egg",95,"Egg"},{"Sweetlime",50,"Fruit"},
            {"Daliya",55,"Cereal"},{"Bread/Jam",282,"Bread"},{"Tea/Coffee",30,"Beverage"}
        });
        add("Monday", "Lunch", new Object[][]{
            {"Punjabi Dum Aloo",165,"Veg1"},{"Carrot Beans Poriyal",125,"Veg2"},
            {"Dal Makhani",185,"Dal"},{"Steamed Rice",130,"Staple"},{"Sambar",135,"Staple"},
            {"Chappati",85,"Staple"},{"Green Salad",30,"Salad"},{"Masala Chaas",50,"Raita"}
        });
        add("Monday", "Snacks", new Object[][]{
            {"Mix Pakoda",145,"Snack"},{"Green Chutney",20,"Chutney"},
            {"Tea",70,"Beverage"},{"Roohafzah",110,"Beverage"}
        });
        add("Monday", "Dinner", new Object[][]{
            {"Aloo Nutri Masala",148,"Veg1"},{"Egg Curry",145,"Veg2"},
            {"Masoor Dal",110,"Dal"},{"Steamed Rice",130,"Staple"},{"Chappati",85,"Staple"},
            {"Sweet Milk Daliya",55,"Dessert"},{"Laccha Onion",20,"Salad"},{"Sweet Corn Soup",70,"Soup"}
        });

        // ===== TUESDAY =====
        add("Tuesday", "Breakfast", new Object[][]{
            {"Indori Poha",110,"Main"},{"Green Chutney",20,"Chutney"},{"Sprouts",50,"Accompaniment"},
            {"Mix Fruits",59,"Fruit"},{"Bread/Jam",282,"Bread"},{"Tea/Coffee",30,"Beverage"}
        });
        add("Tuesday", "Lunch", new Object[][]{
            {"Paneer Do Pyaza",155,"Veg1"},{"Cabbage Matar",110,"Veg2"},
            {"Lubia Dal",105,"Dal"},{"Jeera Rice",135,"Staple"},{"Sambar",135,"Staple"},
            {"Chappati",85,"Staple"},{"Sirka Onion",20,"Salad"},{"Boondi Raita",75,"Raita"}
        });
        add("Tuesday", "Snacks", new Object[][]{
            {"Cheese Sandwich",90,"Snack"},{"Ketchup",60,"Chutney"},
            {"Coffee",75,"Beverage"},{"Ice Tea",110,"Beverage"}
        });
        add("Tuesday", "Dinner", new Object[][]{
            {"Soya Chaap Masala",145,"Veg1"},{"Aloo Methi Masala",125,"Veg2"},
            {"Dal Bukhara",115,"Dal"},{"Steamed Rice",130,"Staple"},{"Chappati",85,"Staple"},
            {"Anguri Boondi",210,"Dessert"},{"Green Salad",30,"Salad"},{"Manchow Soup",74,"Soup"}
        });

        // ===== WEDNESDAY =====
        add("Wednesday", "Breakfast", new Object[][]{
            {"Medu Vada",115,"Main"},{"Sambar/Chutney",65,"Accompaniment"},{"Boiled Egg",95,"Egg"},
            {"Watermelon",50,"Fruit"},{"Cornflakes",145,"Cereal"},{"Tea/Coffee",30,"Beverage"}
        });
        add("Wednesday", "Lunch", new Object[][]{
            {"Navratan Korma",150,"Veg1"},{"Kadai Soya",145,"Veg2"},
            {"Rajma",140,"Dal"},{"Steamed Rice",130,"Staple"},{"Sambar",135,"Staple"},
            {"Chappati",85,"Staple"},{"Laccha Onion",20,"Salad"},{"Curd",45,"Raita"}
        });
        add("Wednesday", "Snacks", new Object[][]{
            {"Masala Macaroni",115,"Snack"},{"Masala Tea",70,"Beverage"},{"Lemon Tang",115,"Beverage"}
        });
        add("Wednesday", "Dinner", new Object[][]{
            {"Gatta Curry",145,"Veg1"},{"Cabbage Fogat",130,"Veg2"},
            {"Kala Chana Masala",130,"Dal"},{"Steamed Rice",130,"Staple"},{"Chappati",85,"Staple"},
            {"Kesar Kheer",250,"Dessert"},{"Cucumber Carrot",60,"Salad"},{"Cauliflower Soup",40,"Soup"}
        });

        // ===== THURSDAY =====
        add("Thursday", "Breakfast", new Object[][]{
            {"Pav Bhaji",145,"Main"},{"Sweet Lemon",50,"Fruit"},
            {"Daliya",120,"Cereal"},{"Bread/Jam",282,"Bread"},{"Tea/Coffee",30,"Beverage"}
        });
        add("Thursday", "Lunch", new Object[][]{
            {"Kadi Pakoda",165,"Veg1"},{"Achari Aloo",145,"Veg2"},
            {"Dhaba Dal",110,"Dal"},{"Steamed Rice",130,"Staple"},{"Sambar",135,"Staple"},
            {"Chappati",85,"Staple"},{"Three Bean Salad",90,"Salad"},{"Masala Raita",75,"Raita"}
        });
        add("Thursday", "Snacks", new Object[][]{
            {"Sabudana Khichdi",110,"Snack"},{"Curd",60,"Raita"},
            {"Tea",70,"Beverage"},{"Roohafzah",110,"Beverage"}
        });
        add("Thursday", "Dinner", new Object[][]{
            {"Mattar Paneer",155,"Veg1"},{"Gazar Mattar Aloo",135,"Veg2"},
            {"Dal Lobia",110,"Dal"},{"Jeera Rice",135,"Staple"},{"Chappati",85,"Staple"},
            {"Shahi Tukda",310,"Dessert"},{"Green Salad",60,"Salad"},{"Tomato Soup",54,"Soup"}
        });

        // ===== FRIDAY =====
        add("Friday", "Breakfast", new Object[][]{
            {"Palak Puri",115,"Main"},{"Aloo Sabji",110,"Accompaniment"},
            {"Mix Fruits",59,"Fruit"},{"Chocos",210,"Cereal"},
            {"Bread/Jam",282,"Bread"},{"Tea/Coffee",30,"Beverage"}
        });
        add("Friday", "Lunch", new Object[][]{
            {"Lauki Chana Masala",115,"Veg1"},{"Mix Veg",145,"Veg2"},
            {"Amritsari Chole",185,"Dal"},{"Steamed Rice",130,"Staple"},{"Sambar",135,"Staple"},
            {"Chappati",85,"Staple"},{"Masala Onion",25,"Salad"},{"Mix Veg Raita",75,"Raita"}
        });
        add("Friday", "Snacks", new Object[][]{
            {"Bhelpuri",125,"Snack"},{"Green Chutney",110,"Chutney"},
            {"Tea",70,"Beverage"},{"Aam Panna",90,"Beverage"}
        });
        add("Friday", "Dinner", new Object[][]{
            {"Veg Kofta Curry",155,"Veg1"},{"Aloo Beans",125,"Veg2"},
            {"Moong Dal Tadka",110,"Dal"},{"Steamed Rice",130,"Staple"},{"Chappati",85,"Staple"},
            {"Sooji Halwa",165,"Dessert"},{"Carrot Beetroot",60,"Salad"},{"Hot n Sour Soup",64,"Soup"}
        });

        // ===== SATURDAY =====
        add("Saturday", "Breakfast", new Object[][]{
            {"Mix Veg Paratha",110,"Main"},{"Green Chutney",20,"Chutney"},
            {"Banana",86,"Fruit"},{"Masala Oats",110,"Cereal"},
            {"Bread/Jam",282,"Bread"},{"Tea/Coffee",30,"Beverage"}
        });
        add("Saturday", "Lunch", new Object[][]{
            {"Matar Paneer",155,"Veg1"},{"Sitafal",110,"Veg2"},
            {"Rajasthani Dal",145,"Dal"},{"Jeera Rice",135,"Staple"},{"Sambar",135,"Staple"},
            {"Chappati",85,"Staple"},{"Sirka Onion",25,"Salad"},{"Sweet Lassi",125,"Raita"}
        });
        add("Saturday", "Snacks", new Object[][]{
            {"Dahi Bhalla",135,"Snack"},{"Coffee",75,"Beverage"},{"Glucon-D",125,"Beverage"}
        });
        add("Saturday", "Dinner", new Object[][]{
            {"Mushroom Masala",165,"Veg1"},{"Dum Aloo",140,"Veg2"},
            {"Black Masoor Dal",110,"Dal"},{"Onion Rice",140,"Staple"},{"Chappati",85,"Staple"},
            {"Besan Ladoo",310,"Dessert"},{"Sirka Onion",20,"Salad"},{"Dal Shorba",77,"Soup"}
        });

        // ===== SUNDAY =====
        add("Sunday", "Breakfast", new Object[][]{
            {"Kulcha Matar",115,"Main"},{"Sweetlime",50,"Fruit"},
            {"Cornflakes",145,"Cereal"},{"Bread/Jam",282,"Bread"},{"Tea/Coffee",30,"Beverage"}
        });
        add("Sunday", "Lunch", new Object[][]{
            {"Loki Kofta",125,"Veg1"},{"Methi Aloo",135,"Veg2"},
            {"Moong Masoor Dal",110,"Dal"},{"Steamed Rice",130,"Staple"},{"Sambar",135,"Staple"},
            {"Chappati",85,"Staple"},{"Carrot Beetroot Onion",65,"Salad"},{"Veg Raita",65,"Raita"}
        });
        add("Sunday", "Snacks", new Object[][]{
            {"Veg Uthapam",110,"Snack"},{"Peanut Chutney",75,"Chutney"},
            {"Tea",70,"Beverage"},{"Lemon Tang",110,"Beverage"}
        });
        add("Sunday", "Dinner", new Object[][]{
            {"Sindhi Kadhi",145,"Veg1"},{"Veg Kholapuri",145,"Veg2"},
            {"Hyderabadi Dal",125,"Dal"},{"Peas Rice",140,"Staple"},{"Chappati",85,"Staple"},
            {"Fruit Custard",250,"Dessert"},{"Laccha Onion",20,"Salad"},{"Veg Clear Soup",55,"Soup"}
        });
    }

    private void add(String day, String meal, Object[][] items) {
        menu.putIfAbsent(day, new LinkedHashMap<>());
        List<FoodItem> list = new ArrayList<>();
        for (Object[] it : items) {
            String name = (String) it[0];
            int cal = (int) it[1];
            String cat = (String) it[2];
            boolean staple = cat.equals("Staple");
            double rating = staple ? -1.0 : Math.round((2.5 + rng.nextDouble() * 2.5) * 10.0) / 10.0;
            list.add(new FoodItem(name, cal, cat, rating, staple));
        }
        menu.get(day).put(meal, list);
    }

    // ─── Getters ───
    public List<String> getDays() {
        return new ArrayList<>(menu.keySet());
    }

    public String[] getMealTypes() {
        return new String[]{"Breakfast", "Lunch", "Snacks", "Dinner"};
    }

    public List<FoodItem> getMeal(String day, String mealType) {
        var dayMap = menu.get(day);
        if (dayMap == null) return Collections.emptyList();
        return dayMap.getOrDefault(mealType, Collections.emptyList());
    }

    // ─── Analysis ───
    public int getTotalCalories(List<FoodItem> items) {
        int total = 0;
        for (FoodItem f : items) total += f.getCalories();
        return total;
    }

    public double getAverageRating(List<FoodItem> items) {
        double sum = 0; int count = 0;
        for (FoodItem f : items) {
            if (!f.isStaple()) { sum += f.getRating(); count++; }
        }
        return count > 0 ? Math.round(sum / count * 100.0) / 100.0 : 0;
    }

    public String getVerdict(double avg) {
        if (avg > 4.2) return "Excellent! Don't miss this one.";
        if (avg > 3.6) return "Decent enough. You can survive this.";
        return "ABSOLUTELY AVOID IT!";
    }

    public String getVerdictLevel(double avg) {
        if (avg > 4.2) return "excellent";
        if (avg > 3.6) return "decent";
        return "avoid";
    }

    public String getAdvice(double avg, String mealType) {
        if (avg > 4.2) return "Head to the mess and enjoy the feast!";
        if (avg > 3.6) return "It's okay - go if you're not in mood to order.";
        return switch (mealType) {
            case "Breakfast" -> "Go have a Dosa from Southern Stories.";
            case "Lunch"     -> "Take a break and order from Snapeats/Bistro.";
            case "Snacks"    -> "Have a Maggi from Hotspot with a drink.";
            case "Dinner"    -> "Go have a Desi meal from Paid Mess.";
            default          -> "Try ordering from outside today.";
        };
    }

    // ─── Search ───
    public List<SearchResult> search(String query) {
        List<SearchResult> results = new ArrayList<>();
        String q = query.toLowerCase().trim();
        if (q.isEmpty()) return results;

        for (String day : menu.keySet()) {
            for (String meal : menu.get(day).keySet()) {
                for (FoodItem item : menu.get(day).get(meal)) {
                    boolean match = item.getName().toLowerCase().contains(q)
                        || item.getCategory().toLowerCase().contains(q)
                        || day.toLowerCase().contains(q)
                        || meal.toLowerCase().contains(q);
                    if (match) {
                        results.add(new SearchResult(day, meal, item));
                    }
                }
            }
        }
        return results;
    }

    // ─── Top Rated ───
    public List<SearchResult> getTopRated(int limit) {
        List<SearchResult> all = new ArrayList<>();
        for (String day : menu.keySet()) {
            for (String meal : menu.get(day).keySet()) {
                for (FoodItem item : menu.get(day).get(meal)) {
                    if (!item.isStaple()) {
                        all.add(new SearchResult(day, meal, item));
                    }
                }
            }
        }
        all.sort((a, b) -> Double.compare(b.item().getRating(), a.item().getRating()));
        return all.subList(0, Math.min(limit, all.size()));
    }

    // ─── Record for search results ───
    public record SearchResult(String day, String meal, FoodItem item) {}
}
