package order;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class Ingredients {

    private static List<String> ingredients;

    public Ingredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public static List<String> getIncorrectIngredients() {
        ingredients = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            ingredients.add(RandomStringUtils.randomAlphanumeric(24));
        }
        return ingredients;
    }
}
