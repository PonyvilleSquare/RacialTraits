package com.hepolite.racialtraits.ability;

import org.bukkit.entity.Player;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.apis.attributes.Attribute;
import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.coreutility.movement.MovementType;
import com.hepolite.racialtraits.ability.components.ComponentDefenceModifier;
import com.hepolite.racialtraits.ability.components.ComponentMovementModifier;
import com.hepolite.racialtraits.ability.components.ComponentResourceCost;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.resource.ResourceStamina;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilitySturdy extends Ability
{
	private final ComponentMovementModifier speedModifier;
	private final ComponentDefenceModifier defenceModifier;
	private final ComponentResourceCost resourceCost;

	public AbilitySturdy(Race race)
	{
		super(race, "Sturdy");
		speedModifier = new ComponentMovementModifier(getName());
		defenceModifier = new ComponentDefenceModifier(getName());
		resourceCost = new ComponentResourceCost(race.getResource());
	}

	@Override
	public final void onInitialize(Player player, int level)
	{
		float walkSpeed = getSettings().getFloat("Level " + level + ".walk");
		float defence = getSettings().getFloat("Level " + level + ".defence");
		speedModifier.setWalk(player, 0.0f, walkSpeed, 0.0f);
		defenceModifier.setDefence(player, 0.0f, defence, 0.0f);
	}

	@Override
	public final void onDeinitialize(Player player)
	{
		speedModifier.wipe(player);
		defenceModifier.wipe(player);
	}

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		MovementType type = CoreUtility.getMovementHandler().getMovementType(player);
		float amount = getSettings().getFloat("regen." + type.name().toLowerCase());

		Attribute attribute = AttributeAPI.get(player, ResourceStamina.ATTRIBUTE_REGEN).setBaseValue(amount);
		resourceCost.restore(player, attribute.getValue());
	}
}
