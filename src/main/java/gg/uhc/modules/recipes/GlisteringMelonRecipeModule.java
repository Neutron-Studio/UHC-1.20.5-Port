/*
 * Project: UHC
 * Class: gg.uhc.uhc.modules.recipes.GlisteringMelonRecipeModule
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
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.Objects;

public class GlisteringMelonRecipeModule extends DisableableModule implements Listener {

    protected static final String ICON_NAME = "Glistering Melon Recipe";
    private NamespacedKey recipeKey;

    public GlisteringMelonRecipeModule() {
        setId("GlisteringMelonRecipe");

        this.iconName = ICON_NAME;
        this.icon.setType(Material.GLISTERING_MELON_SLICE);
        this.icon.setWeight(ModuleRegistry.CATEGORY_RECIPIES);
    }

    private void registerRecipe() {
        if (recipeKey == null) {
            recipeKey = new NamespacedKey(Objects.requireNonNull(plugin, "Plugin not set"), "glistering_melon_block");
        }

        Bukkit.removeRecipe(recipeKey);

        ShapelessRecipe modified = new ShapelessRecipe(recipeKey, new ItemStack(Material.GLISTERING_MELON_SLICE, 1));
        modified.addIngredient(Material.GOLD_BLOCK);
        modified.addIngredient(Material.MELON_SLICE);

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
        if (recipe == null || recipe.getResult().getType() != Material.GLISTERING_MELON_SLICE) {
            return;
        }

        if (RecipeUtil.hasRecipeGotMaterial(recipe, isEnabled() ? Material.GOLD_NUGGET : Material.GOLD_BLOCK)) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }
}
