package me.davydovep.recipesapp_6.service;

import me.davydovep.recipesapp_6.model.Ingredient;
import me.davydovep.recipesapp_6.model.Recipe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class ValidateService {

    public boolean isNotValid(Recipe recipe) {
        boolean result = StringUtils.isBlank(recipe.getTitle()) ||
                CollectionUtils.isEmpty(recipe.getIngredients()) ||
                CollectionUtils.isEmpty(recipe.getSteps()) ||
                recipe.getCookingTime() > 0;
        if (!result) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                result = result || isNotValid(ingredient);
            }
        }
        return result;
    }



    public boolean isNotValid(Ingredient ingredient) {
        return StringUtils.isBlank(ingredient.getTitle()) ||
                StringUtils.isBlank(ingredient.getMeasureUnit()) ||
                ingredient.getAmount() > 0;
    }
}
