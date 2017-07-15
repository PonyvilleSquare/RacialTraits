package com.hepolite.racialtraits.race.ability;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.components.ComponentToggle;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityNightVision extends Ability
{
	ComponentToggle toggle = new ComponentToggle(false);

	public AbilityNightVision(Race race)
	{
		super(race, "Night Vision");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		toggle.toggle(player);
		if (toggle.get(player))
			player.sendMessage(String.format("%sDisabled%s Night Vision. Please wait for your eyes to adapt...", ChatColor.RED, ChatColor.WHITE));
		else
			player.sendMessage(String.format("%sEnabled%s Night Vision. Please wait for your eyes to adapt...", ChatColor.AQUA, ChatColor.WHITE));
		return false;
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		if (tick % 200 == 0 && toggle.get(player))
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 500, 0, true), true);
	}
}
