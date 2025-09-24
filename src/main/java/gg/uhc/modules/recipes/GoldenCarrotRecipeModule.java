/*
 * Project: UHC
 * Class: gg.uhc.uhc.modules.recipes.GoldenCarrotRecipeModule
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Graham Howden <graham_howden1 at yahoo.co.uk>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package gg.uhc.uhc.modules.recipes;

import gg.uhc.uhc.modules.DisableableModule;
import gg.uhc.uhc.modules.ModuleRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Objects;

public class GoldenCarrotRecipeModule extends DisableableModule implements Listener {

    protected static final String ICON_NAME = "Golden Carrot Recipe";
    private NamespacedKey recipeKey;

    public GoldenCarrotRecipeModule() {
        setId("GoldenCarrotRecipe");

        this.iconName = ICON_NAME;
        this.icon.setType(Material.GOLDEN_CARROT);
        this.icon.setWeight(ModuleRegistry.CATEGORY_RECIPIES);
    }

    private void registerRecipe() {
        if (recipeKey == null) {
            recipeKey = new NamespacedKey(Objects.requireNonNull(plugin, "Plugin not set"), "golden_carrot_block");
        }

        Bukkit.removeRecipe(recipeKey);

        ShapedRecipe modified = new ShapedRecipe(recipeKey, new ItemStack(Material.GOLDEN_CARROT, 1));
        modified.shape("AAA", "ABA", "AAA");
        modified.setIngredient('A', Material.GOLD_INGOT);
        modified.setIngredient('B', Material.CARROT);

        Bukkit.addRecipe(modified);
    }

    @Override
    protected boolean isEnabledByDefault() {
        return true;
    }

    @Override
    public void initialize() throws InvalidConfigurationException {
        registerRecipe();
        super.initialize();
    }

    @EventHandler
    public void on(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();

        if (recipe == null || recipe.getResult().getType() != Material.GOLDEN_CARROT) {
            return;
        }

        if (RecipeUtil.hasRecipeGotMaterial(recipe, isEnabled() ? Material.GOLD_NUGGET : Material.GOLD_INGOT)) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }
}
