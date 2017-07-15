package com.hepolite.racialtraits.race.ability;

import org.bukkit.entity.Player;

import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityObservant extends Ability
{
	public AbilityObservant(Race race)
	{
		super(race, "Observant");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		return false;
	}
}
