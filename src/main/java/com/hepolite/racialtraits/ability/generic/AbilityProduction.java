package com.hepolite.racialtraits.ability.generic;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.hunger.Node;
import com.hepolite.coreutility.settings.Settings;
import com.hepolite.coreutility.util.InventoryHelper;
import com.hepolite.coreutility.util.StringHelper;
import com.hepolite.racialtraits.ability.Ability;
import com.hepolite.racialtraits.ability.components.ComponentItemCost;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityProduction extends Ability
{
	private final ComponentItemCost itemCost = new ComponentItemCost();

	public AbilityProduction(Race race, String name)
	{
		super(race, name);
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		Settings settings = getSettings();

		float hungerConsumed = settings.getFloat("Level " + skill.getLevel() + ".cost.hunger");
		List<ItemStack> itemsConsumed = settings.getItems("Level " + skill.getLevel() + ".cost.items");
		List<ItemStack> itemsProduced = settings.getItems("Level " + skill.getLevel() + ".produced.items");

		List<String> missingResources = new ArrayList<String>();
		Node node = CoreUtility.getHungerHandler().getHungerNode(player);
		if (node.getTotal() < hungerConsumed)
			missingResources.add(String.format("%.0f * Hunger points", Math.ceil(hungerConsumed - node.getTotal())));
		missingResources.addAll(itemCost.findMissing(player, itemsConsumed));
		if (missingResources.size() > 0)
		{
			player.sendMessage(ChatColor.RED + "You still need the following resources:");
			player.sendMessage(missingResources.toArray(new String[0]));
			return false;
		}

		node.changeSaturation(-hungerConsumed);
		itemCost.consume(player, itemsConsumed);
		InventoryHelper.addWithDrop(player, itemsProduced);

		List<String> producedItems = new ArrayList<String>();
		for (ItemStack item : itemsProduced)
		{
			String name = InventoryHelper.getItemName(item);
			if (name == null)
				name = StringHelper.toTitleCase(item.getType().toString().replaceAll("_", " "));
			producedItems.add(item.getAmount() + " * " + name);
		}
		player.sendMessage(ChatColor.AQUA + "Produced the following item(s):");
		player.sendMessage(producedItems.toArray(new String[0]));
		skill.startCooldown();
		return false;
	}
}
