package com.hepolite.racialtraits.race.ability.generic;

import org.bukkit.entity.Player;

import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.ability.Ability;
import com.hepolite.racialtraits.race.components.ComponentHungerModifier;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityHungerModifierFlight extends Ability
{
	private final ComponentHungerModifier modifier;

	public AbilityHungerModifierFlight(Race race, String name)
	{
		super(race, name);
		modifier = new ComponentHungerModifier(getName());
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		if ((!player.isFlying() && !player.isGliding()) || tick % 20 != 0)
			return;

		float hunger = getSettings().getFloat("Level " + skill.getLevel());
		modifier.setDecrease(player, 0.0f, -hunger, 0.0f, 3);
	}

	@Override
	public final void onDeinitialize(Player player)
	{
		modifier.wipe(player);
	}
}
