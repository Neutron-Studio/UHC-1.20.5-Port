/*
 * Project: UHC
 * Class: gg.uhc.uhc.modules.heads.GoldenHeadsModule
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

package gg.uhc.uhc.modules.heads;

import com.google.common.collect.ImmutableMap;
import gg.uhc.uhc.modules.DisableableModule;
import gg.uhc.uhc.modules.ModuleRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GoldenHeadsModule extends DisableableModule implements Listener {

    public static final String HEAD_NAME = ChatColor.GOLD + "Golden Head";
    protected static final String ICON_NAME = "Golden Heads";
    protected static final String LORE_PATH = "lore";
    protected static final NumberFormat formatter = NumberFormat.getNumberInstance();
    protected static final int TICKS_PER_HALF_HEART = 25;

    static {
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(1);
    }

    protected int healAmount;
    private NamespacedKey recipeKey;

    public GoldenHeadsModule() {
        setId("GoldenHeads");

        this.iconName = ICON_NAME;
        this.icon.setType(Material.PLAYER_HEAD);
        this.icon.setWeight(ModuleRegistry.CATEGORY_APPLES);
    }

    private void registerRecipe() {
        if (recipeKey == null) {
            recipeKey = new NamespacedKey(Objects.requireNonNull(plugin, "Plugin not set"), "golden_head");
        }

        Bukkit.removeRecipe(recipeKey);

        ShapedRecipe modified = new ShapedRecipe(recipeKey, new ItemStack(Material.GOLDEN_APPLE, 1));
        modified.shape("AAA", "ABA", "AAA");
        modified.setIngredient('A', Material.GOLD_INGOT);
        modified.setIngredient('B', Material.PLAYER_HEAD);

        Bukkit.addRecipe(modified);
    }

    public int getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(int amount) {
        this.healAmount = amount;
        config.set("heal amount", this.healAmount);
        saveConfig();
        rerender();
    }

    @Override
    protected boolean isEnabledByDefault() {
        return true;
    }

    @Override
    public void initialize() throws InvalidConfigurationException {
        if (!config.contains("heal amount")) {
            config.set("heal amount", 6);
        }

        if (!config.isInt("heal amount")) {
            throw new InvalidConfigurationException("Invalid value at " + config.getCurrentPath() + ".heal amount (" + config.get("heal amount") + ")");
        }

        healAmount = config.getInt("heal amount");
        registerRecipe();
        super.initialize();
    }

    @Override
    protected void renderEnabled() {
        super.renderEnabled();
        icon.setAmount(healAmount);
    }

    @Override
    protected void renderDisabled() {
        super.renderDisabled();
        icon.setAmount(1);
    }

    @Override
    protected List<String> getEnabledLore() {
        return messages.evalTemplates(ENABLED_LORE_PATH, ImmutableMap.of("amount", formatter.format(healAmount / 2D)));
    }

    @EventHandler
    public void on(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null || event.getRecipe().getResult().getType() != Material.GOLDEN_APPLE) {
            return;
        }

        ItemStack centre = event.getInventory().getMatrix()[4];
        if (centre == null || centre.getType() != Material.PLAYER_HEAD) {
            return;
        }

        if (!isEnabled()) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
            return;
        }

        SkullMeta meta = (SkullMeta) centre.getItemMeta();
        String owner = Optional.ofNullable(meta)
                .map(skullMeta -> {
                    if (skullMeta.getOwningPlayer() != null) {
                        var owningPlayer = skullMeta.getOwningPlayer();
                        return owningPlayer.getName() != null ? owningPlayer.getName() : owningPlayer.getUniqueId().toString();
                    }
                    return skullMeta.hasOwner() ? skullMeta.getOwner() : null;
                })
                .orElse("Manually Crafted");

        ItemStack goldenHeadItem = getGoldenHeadItem(owner);
        event.getInventory().setResult(goldenHeadItem);
    }

    @EventHandler
    public void on(PlayerItemConsumeEvent event) {
        if (isEnabled() && isGoldenHead(event.getItem())) {
            event.getPlayer().addPotionEffect(new PotionEffect(
                    PotionEffectType.REGENERATION,
                    TICKS_PER_HALF_HEART * healAmount,
                    1
            ));
        }
    }

    public boolean isGoldenHead(ItemStack itemStack) {
        if (itemStack.getType() != Material.GOLDEN_APPLE) {
            return false;
        }

        ItemMeta im = itemStack.getItemMeta();

        return im != null && im.hasDisplayName() && im.getDisplayName().equals(HEAD_NAME);
    }

    public ItemStack getGoldenHeadItem(String playerName) {
        ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, 1);

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(HEAD_NAME);

            ImmutableMap<String, String> context = ImmutableMap.of(
                    "player", playerName,
                    "amount", Integer.toString(getHealAmount())
            );
            List<String> lore = getMessageTemaplates().evalTemplates(LORE_PATH, context);
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }
}
