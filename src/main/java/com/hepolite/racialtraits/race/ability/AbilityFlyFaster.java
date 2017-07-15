package com.hepolite.racialtraits.race.ability;

import org.bukkit.entity.Player;

import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.components.ComponentHungerModifier;
import com.hepolite.racialtraits.race.components.ComponentMovementModifier;

public class AbilityFlyFaster extends Ability
{
	private final ComponentMovementModifier speedModifier;
	private final ComponentHungerModifier hungerModifier;

	public AbilityFlyFaster(Race race)
	{
		super(race, "Fly Faster");
		speedModifier = new ComponentMovementModifier(getName());
		hungerModifier = new ComponentHungerModifier(getName());
	}

	@Override
	public final void onInitialize(Player player, int level)
	{
		float speed = getSettings().getFloat("Level " + level + ".speed");
		float hunger = getSettings().getFloat("Level " + level + ".hunger");

		speedModifier.setFly(player, 0.0f, speed, 0.0f);
		hungerModifier.setDecrease(player, 0.0f, hunger, 0.0f);
	}

	@Override
	public final void onDeinitialize(Player player)
	{
		speedModifier.wipe(player);
		hungerModifier.wipe(player);
	}
}
