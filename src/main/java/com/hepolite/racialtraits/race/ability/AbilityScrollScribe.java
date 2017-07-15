package com.hepolite.racialtraits.race.ability;

import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import com.hepolite.coreutility.util.InventoryHelper;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.components.ComponentItemCost;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityScrollScribe extends Ability
{
	private final ComponentItemCost itemCost = new ComponentItemCost();

	public AbilityScrollScribe(Race race)
	{
		super(race, "Scroll Scribe");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		List<Enchantment> enchantments = getSettings().getEnchantmentTypes("enchantments");
		if (enchantments.size() == 0)
		{
			player.sendMessage(ChatColor.RED + "No valid enchantments were found."
					+ "Please contact the server administration");
			return false;
		}
		Enchantment enchantment = enchantments.get(random.nextInt(enchantments.size()));

		Collection<ItemStack> itemsConsumed = getSettings().getItems("cost");
		if (!itemCost.has(player, itemsConsumed))
		{
			player.sendMessage(ChatColor.RED + "You still need the following resources:");
			player.sendMessage(itemCost.findMissing(player, itemsConsumed).toArray(new String[0]));
			return false;
		}
		itemCost.consume(player, itemsConsumed);

		ItemStack produce = getSettings().getItem("product");
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) produce.getItemMeta();
		meta.addStoredEnchant(enchantment, 1, false);
		produce.setItemMeta(meta);

		produce.setAmount(InventoryHelper.add(player, produce));
		if (produce.getAmount() > 0)
			player.getWorld().dropItem(player.getLocation(), produce);
		return true;
	}
}
