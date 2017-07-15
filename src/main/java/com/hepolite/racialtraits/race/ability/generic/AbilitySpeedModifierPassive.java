package com.hepolite.racialtraits.race.ability.generic;

import org.bukkit.entity.Player;

import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.ability.Ability;
import com.hepolite.racialtraits.race.components.ComponentMovementModifier;

public class AbilitySpeedModifierPassive extends Ability
{
	private final ComponentMovementModifier modifier;

	public AbilitySpeedModifierPassive(Race race, String name)
	{
		super(race, name);
		modifier = new ComponentMovementModifier(getName());
	}

	@Override
	public final void onInitialize(Player player, int level)
	{
		float flySpeed = getSettings().getFloat("Level " + level + ".fly");
		float walkSpeed = getSettings().getFloat("Level " + level + ".walk");
		if (flySpeed != 0.0f)
			modifier.setFly(player, 0.0f, flySpeed, 0.0f);
		if (walkSpeed != 0.0f)
			modifier.setWalk(player, 0.0f, walkSpeed, 0.0f);
	}

	@Override
	public final void onDeinitialize(Player player)
	{
		modifier.wipe(player);
	}
}
