package com.hepolite.racialtraits.race.ability;

import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.damage.DamageClass;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.components.ComponentDefenceModifier;

public class AbilityBug extends Ability
{
	private final ComponentDefenceModifier elementalDefenceModifier;
	private final ComponentDefenceModifier magicalDefenceModifier;
	private final ComponentDefenceModifier physicalDefenceModifier;

	public AbilityBug(Race race)
	{
		super(race, "Bug");
		elementalDefenceModifier = new ComponentDefenceModifier(getName(), DamageClass.ELEMENTAL);
		magicalDefenceModifier = new ComponentDefenceModifier(getName(), DamageClass.MAGICAL);
		physicalDefenceModifier = new ComponentDefenceModifier(getName(), DamageClass.PHYSICAL);
	}

	@Override
	public final void onInitialize(Player player, int level)
	{
		float elemental = getSettings().getFloat("elemental");
		float magical = getSettings().getFloat("magical");
		float physical = getSettings().getFloat("physical");
		elementalDefenceModifier.setDefence(player, 0.0f, elemental, 0.0f);
		magicalDefenceModifier.setDefence(player, 0.0f, magical, 0.0f);
		physicalDefenceModifier.setDefence(player, 0.0f, physical, 0.0f);
	}

	@Override
	public final void onDeinitialize(Player player)
	{
		elementalDefenceModifier.wipe(player);
		magicalDefenceModifier.wipe(player);
		physicalDefenceModifier.wipe(player);
	}
}
