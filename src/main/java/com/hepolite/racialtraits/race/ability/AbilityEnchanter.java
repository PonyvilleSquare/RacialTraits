package com.hepolite.racialtraits.race.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;

import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityEnchanter extends Ability
{
	public AbilityEnchanter(Race race)
	{
		super(race, "Enchanter");
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onEnchantItem(EnchantItemEvent event)
	{
		Player player = event.getEnchanter();
		PlayerSkill skill = getSkill(player);
		if (skill == null)
			return;

		boolean costAtLeastOneLevel = true;
		int magicValue = costAtLeastOneLevel ? 0 : 1;

		// This line is mostly magic, don't modify it unless you know what you're doing...
		player.setLevel(player.getLevel()
				+ Math.min(magicValue + event.whichButton(), skill.getLevel()));
	}
}
