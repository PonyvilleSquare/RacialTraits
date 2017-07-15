package com.hepolite.racialtraits.ability;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.entites.EntityLocater.ConeLocater;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityDistantReach extends Ability
{
	public AbilityDistantReach(Race race)
	{
		super(race, "Distant Reach");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		float range = getSettings().getFloat("range");
		float angle = getSettings().getFloat("angle");

		Location start = player.getEyeLocation();
		ConeLocater cone = new ConeLocater(start, start.getDirection().multiply(range), angle);
		Collection<Item> allItems = cone.get(Item.class);
		Collection<Item> items = cone.getUnobstructed(start, false, Item.class);
		if (items.size() == 0)
		{
			if (allItems.size() == 0)
				player.sendMessage(ChatColor.RED + "Found no items to teleport to you");
			else
				player.sendMessage(ChatColor.RED + "Could not bring in the items in range");
			return false;
		}

		for (Item item : items)
			item.teleport(player);
		return true;
	}
}
