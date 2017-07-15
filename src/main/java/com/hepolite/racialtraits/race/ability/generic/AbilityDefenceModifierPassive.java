package com.hepolite.racialtraits.race.ability.generic;

import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.damage.DamageClass;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.ability.Ability;
import com.hepolite.racialtraits.race.components.ComponentDefenceModifier;

public class AbilityDefenceModifierPassive extends Ability
{
	private final ComponentDefenceModifier defenceModifier;

	public AbilityDefenceModifierPassive(Race race, String name)
	{
		super(race, name);
		defenceModifier = new ComponentDefenceModifier(getName());
	}

	public AbilityDefenceModifierPassive(Race race, String name, DamageClass damageClass)
	{
		super(race, name);
		defenceModifier = new ComponentDefenceModifier(getName(), damageClass);
	}

	public AbilityDefenceModifierPassive(Race race, String name, DamageType damageType)
	{
		super(race, name);
		defenceModifier = new ComponentDefenceModifier(getName(), damageType);
	}

	@Override
	public final void onInitialize(Player player, int level)
	{
		float scale = getSettings().getFloat("Level " + level + ".scale");
		float multiplier = getSettings().getFloat("Level " + level + ".multiplier");
		float flat = getSettings().getFloat("Level " + level + ".flat");
		defenceModifier.setDefence(player, scale, multiplier, flat);
	}

	@Override
	public final void onDeinitialize(Player player)
	{
		defenceModifier.wipe(player);
	}
}
