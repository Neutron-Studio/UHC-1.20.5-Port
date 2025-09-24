package gg.uhc.uhc.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;

public final class TextComponentUtil {

    private TextComponentUtil() {
    }

    public static BaseComponent[] createItemHover(ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR) {
            return TextComponent.fromLegacyText(ChatColor.GRAY + "No item");
        }

        StringBuilder builder = new StringBuilder();
        ItemMeta meta = stack.getItemMeta();

        if (meta != null && meta.hasDisplayName()) {
            builder.append(ChatColor.RESET).append(meta.getDisplayName());
        } else {
            builder.append(ChatColor.YELLOW).append(prettyMaterialName(stack.getType()));
        }

        List<String> lore = meta != null ? meta.getLore() : null;
        if (lore != null && !lore.isEmpty()) {
            for (String line : lore) {
                builder.append("\n").append(ChatColor.GRAY)
                        .append(ChatColor.translateAlternateColorCodes('&', line));
            }
        }

        return TextComponent.fromLegacyText(builder.toString());
    }

    private static String prettyMaterialName(Material type) {
        String lower = type.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
        if (lower.isEmpty()) {
            return type.name();
        }

        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
