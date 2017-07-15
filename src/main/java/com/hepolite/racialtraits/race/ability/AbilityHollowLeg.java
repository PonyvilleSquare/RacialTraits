package com.hepolite.racialtraits.race.ability;

import org.bukkit.entity.Player;

import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.components.ComponentHungerModifier;

public class AbilityHollowLeg extends Ability
{
	private final ComponentHungerModifier modifier;

	public AbilityHollowLeg(Race race)
	{
		super(race, "Hollow Leg");
		modifier = new ComponentHungerModifier(getName());
	}

	@Override
	public final void onInitialize(Player player, int level)
	{
		float additional = getSettings().getFloat("Level " + level);
		modifier.setMax(player, 0.0f, 0.0f, additional);
	}

	@Override
	public final void onDeinitialize(Player player)
	{
		modifier.wipe(player);
	}
}
