package com.hepolite.racialtraits.race.ability.generic;

import org.bukkit.entity.Player;

import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.ability.Ability;
import com.hepolite.racialtraits.race.components.ComponentMovementModifier;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilitySpeedModifierActive extends Ability
{
	private final ComponentMovementModifier modifier;

	public AbilitySpeedModifierActive(Race race, String name)
	{
		super(race, name);
		modifier = new ComponentMovementModifier(getName());
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		int level = skill.getLevel();
		float flySpeed = getSettings().getFloat("Level " + level + ".fly");
		float walkSpeed = getSettings().getFloat("Level " + level + ".walk");
		int duration = getSettings().getInt("Level " + level + ".duration");

		if (flySpeed != 0.0f)
			modifier.setFly(player, 0.0f, flySpeed, 0.0f, duration);
		if (walkSpeed != 0.0f)
			modifier.setWalk(player, 0.0f, walkSpeed, 0.0f, duration);
		return true;
	}

	@Override
	public final void onDeinitialize(Player player)
	{
		modifier.wipe(player);
	}
}
