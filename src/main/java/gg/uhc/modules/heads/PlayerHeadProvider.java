/*
 * Project: UHC
 * Class: gg.uhc.uhc.modules.heads.PlayerHeadProvider
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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerHeadProvider {

    public ItemStack getPlayerHeadItem(String name) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        if (meta != null) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
            meta.setOwningPlayer(offlinePlayer);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    public ItemStack getPlayerHeadItem(Player player) {
        return getPlayerHeadItem(player.getName());
    }

    public void setBlockAsHead(Player player, Block headBlock) {
        setBlockAsHead(player.getName(), headBlock, BlockFaceXZ.getClosest(player));
    }

    public void setBlockAsHead(String name, Block headBlock, BlockFaceXZ direction) {
        headBlock.setType(Material.PLAYER_HEAD, false);

        Rotatable rotatable = (Rotatable) headBlock.getBlockData();
        rotatable.setRotation(direction.getBlockFace());
        headBlock.setBlockData(rotatable, false);

        Skull state = (Skull) headBlock.getState();
        state.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        state.update(true, false);
    }
}
