package com.hepolite.racialtraits.ability;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.apis.entites.EntityLocater.LineLocater;
import com.hepolite.coreutility.util.InventoryHelper;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityDisarm extends Ability
{
	public AbilityDisarm(Race race)
	{
		super(race, "Disarm");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		float range = getSettings().getFloat("range");

		Location start = player.getEyeLocation();
		LineLocater line = new LineLocater(start, start.getDirection().multiply(range));
		LivingEntity target = line.getFirstUnobstructed(start, player, false, LivingEntity.class);
		if (target == null)
		{
			player.sendMessage(ChatColor.RED + "There was no valid target in front of you!");
			return false;
		}

		boolean result = false;
		if (target instanceof Player)
			result = dropPlayerItem((Player) target);
		else
			result = dropMobItem(target);

		if (!result)
			player.sendMessage(ChatColor.RED + "There was no valid target in front of you!");
		return result;
	}

	/** Causes the item the player is holding to be moved elsewhere in the inventory */
	private final boolean dropPlayerItem(Player player)
	{
		EntityEquipment equipment = player.getEquipment();
		ItemStack item = equipment.getItemInMainHand();
		if (item == null || item.getType() == Material.AIR)
			return false;

		Inventory inventory = player.getInventory();
		int slot = getJunkSlot(inventory, item);
		if (slot == -1)
			return false;

		equipment.setItemInMainHand(inventory.getItem(slot));
		inventory.setItem(slot, item);
		return true;
	}

	/** Causes the item the mob is holding to drop its item, if it holds any */
	private final boolean dropMobItem(LivingEntity entity)
	{
		EntityEquipment equipment = entity.getEquipment();
		ItemStack item = equipment == null ? null : equipment.getItemInMainHand();
		if (item == null || item.getType() == Material.AIR)
			return false;

		entity.getWorld().dropItem(entity.getEyeLocation(), item);
		equipment.setItemInMainHand(null);
		return true;
	}

	/** Finds a slot with random junk in it; returns a random slot if no slot was found */
	private final int getJunkSlot(Inventory inventory, ItemStack item)
	{
		List<Integer> possibleSlots = new ArrayList<Integer>();
		for (int i = Math.min(36, inventory.getSize()) - 1; i >= 0; i--)
		{
			ItemStack current = inventory.getItem(i);
			if (current == null || current.getType() == Material.AIR
					|| !InventoryHelper.areTypesEqual(item, current))
				possibleSlots.add(i);
		}
		return possibleSlots.size() == 0 ? -1 : possibleSlots.get(random.nextInt(possibleSlots
				.size()));
	}
}
