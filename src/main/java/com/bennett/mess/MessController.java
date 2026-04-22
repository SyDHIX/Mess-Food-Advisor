package com.bennett.mess;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MessController {

    private final MenuData menuData;
    private final UserData userData;

    public MessController(MenuData menuData, UserData userData) {
        this.menuData = menuData;
        this.userData = userData;
    }

    // ─── Home ───
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("topItems", menuData.getTopRated(6));
        return "home";
    }

    // ─── Dashboard ───
    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(defaultValue = "") String day,
            @RequestParam(defaultValue = "Breakfast") String meal,
            Model model) {

        List<String> days = menuData.getDays();
        if (day.isEmpty()) {
            // Auto-select today
            String[] jDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
            int dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            day = jDays[dow - 1];
            if (!days.contains(day)) day = days.get(0);
        }

        List<FoodItem> items = menuData.getMeal(day, meal);
        double avgRating = menuData.getAverageRating(items);

        model.addAttribute("days", days);
        model.addAttribute("mealTypes", menuData.getMealTypes());
        model.addAttribute("selectedDay", day);
        model.addAttribute("selectedMeal", meal);
        model.addAttribute("items", items);
        model.addAttribute("totalCal", menuData.getTotalCalories(items));
        model.addAttribute("avgRating", String.format("%.2f", avgRating));
        model.addAttribute("itemCount", items.size());
        model.addAttribute("verdict", menuData.getVerdict(avgRating));
        model.addAttribute("verdictLevel", menuData.getVerdictLevel(avgRating));
        model.addAttribute("advice", menuData.getAdvice(avgRating, meal));
        return "dashboard";
    }

    // ─── Search ───
    @GetMapping("/search")
    public String search(
            @RequestParam(defaultValue = "") String q,
            Model model) {

        List<MenuData.SearchResult> results;
        String resultLabel;

        if (q.isEmpty()) {
            results = menuData.getTopRated(10);
            resultLabel = "⭐ Recommended Dishes";
        } else {
            results = menuData.search(q);
            resultLabel = results.size() + " result" + (results.size() != 1 ? "s" : "") + " found for \"" + q + "\"";
        }

        model.addAttribute("query", q);
        model.addAttribute("results", results);
        model.addAttribute("resultLabel", resultLabel);
        model.addAttribute("quickFilters", new String[]{"Paneer","Dal","Monday","Breakfast","Dessert","Snack","Soup","Friday"});
        return "search";
    }

    // ─── Ratings ───
    @GetMapping("/ratings")
    public String ratings(Model model) {
        HashMap<String, Integer> userRatings = userData.loadRatings();

        // Build a nested structure: day -> meal -> list of RatingEntry
        // Use LinkedHashMap to preserve insertion order
        LinkedHashMap<String, LinkedHashMap<String, List<RatingEntry>>> grouped = new LinkedHashMap<>();

        for (String day : menuData.getDays()) {
            LinkedHashMap<String, List<RatingEntry>> mealMap = new LinkedHashMap<>();
            for (String meal : menuData.getMealTypes()) {
                List<RatingEntry> entries = new ArrayList<>();
                for (FoodItem item : menuData.getMeal(day, meal)) {
                    if (!item.isStaple()) {
                        int userRating = userRatings.getOrDefault(item.getName(), 0);
                        entries.add(new RatingEntry(day, meal, item, userRating));
                    }
                }
                if (!entries.isEmpty()) mealMap.put(meal, entries);
            }
            if (!mealMap.isEmpty()) grouped.put(day, mealMap);
        }

        model.addAttribute("grouped", grouped);
        return "ratings";
    }

    @PostMapping("/ratings/save")
    @ResponseBody
    public Map<String, String> saveRating(@RequestParam String dish, @RequestParam int stars) {
        userData.saveRating(dish, stars);
        return Map.of("status", "ok");
    }

    // ─── Comparison ───
    @GetMapping("/comparison")
    public String comparison(Model model) {
        int messMonthly = 4500;
        int days = 30;
        int messPerMeal = messMonthly / days / 4;

        int outsideBreakfast = 80, outsideLunch = 150, outsideSnacks = 60, outsideDinner = 160;
        int outsideMonthly = (outsideBreakfast + outsideLunch + outsideSnacks + outsideDinner) * days;
        int savings = outsideMonthly - messMonthly;

        model.addAttribute("messPerMeal", messPerMeal);
        model.addAttribute("messMonthly", messMonthly);
        model.addAttribute("outsideBreakfast", outsideBreakfast);
        model.addAttribute("outsideLunch", outsideLunch);
        model.addAttribute("outsideSnacks", outsideSnacks);
        model.addAttribute("outsideDinner", outsideDinner);
        model.addAttribute("outsideMonthly", outsideMonthly);
        model.addAttribute("savings", savings);
        model.addAttribute("savingsYearly", savings * 12);
        return "comparison";
    }

    // ─── Meal Log ───
    @GetMapping("/meallog")
    public String mealLog(Model model) {
        List<String> days = menuData.getDays();
        String[] jDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        int dow = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String today = jDays[dow - 1];
        if (!days.contains(today)) today = days.get(0);

        List<String[]> logs = userData.loadMealLog();
        Collections.reverse(logs);

        model.addAttribute("days", days);
        model.addAttribute("mealTypes", menuData.getMealTypes());
        model.addAttribute("today", today);
        model.addAttribute("logs", logs);
        model.addAttribute("logCount", logs.size());
        return "meallog";
    }

    @GetMapping("/meallog/items")
    @ResponseBody
    public List<Map<String, Object>> getMealItems(
            @RequestParam String day,
            @RequestParam String meal) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (FoodItem item : menuData.getMeal(day, meal)) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("name", item.getName());
            m.put("calories", item.getCalories());
            result.add(m);
        }
        return result;
    }

    @PostMapping("/meallog/add")
    @ResponseBody
    public Map<String, String> logMeal(
            @RequestParam String day,
            @RequestParam String meal,
            @RequestParam String dish) {
        userData.logMeal(day, meal, dish);
        return Map.of("status", "ok");
    }

    @PostMapping("/meallog/clear")
    public String clearLog() {
        userData.clearMealLog();
        return "redirect:/meallog";
    }

    // ─── Helper record ───
    public record RatingEntry(String day, String meal, FoodItem item, int userRating) {}
}
