package com.hepolite.racialtraits.race.components;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.util.InventoryHelper;
import com.hepolite.coreutility.util.StringHelper;

public class ComponentItemCost
{
	/** Returns a list containing a name of everything the player is missing */
	public final List<String> findMissing(Player player, Collection<ItemStack> items)
	{
		List<String> list = new LinkedList<String>();
		if (items == null || items.size() == 0)
			return list;

		List<ItemStack> missingItems = InventoryHelper.findMissing(player, items);
		if (missingItems.size() > 0)
		{
			for (ItemStack item : missingItems)
			{
				String name = InventoryHelper.getItemName(item);
				if (name == null)
					name = StringHelper.toTitleCase(item.getType().toString().replaceAll("_", " "));
				list.add(item.getAmount() + " * " + name);
			}
		}
		return list;
	}

	/** Returns true if the player has all the needed resources */
	public final boolean has(Player player, Collection<ItemStack> items)
	{
		return InventoryHelper.findMissing(player, items).size() == 0;
	}

	/** Consumes the given resources from the given player */
	public final void consume(Player player, Collection<ItemStack> items)
	{
		InventoryHelper.remove(player, items);
	}
}
