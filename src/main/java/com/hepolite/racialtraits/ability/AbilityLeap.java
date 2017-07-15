package com.hepolite.racialtraits.ability;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.hepolite.coreutility.apis.attributes.Attribute;
import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.coreutility.apis.attributes.AttributeType;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.racialtraits.ability.components.ComponentResourceCost;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityLeap extends Ability
{
	private final ComponentResourceCost resourceCost;

	public AbilityLeap(Race race)
	{
		super(race, "Leap");
		resourceCost = new ComponentResourceCost(race.getResource());
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		int level = skill.getLevel();
		float force = getSettings().getFloat("Level " + level + ".force");
		float lift = getSettings().getFloat("Level " + level + ".lift");
		float cost = getSettings().getFloat("Level " + level + ".cost");
		float protection = getSettings().getFloat("Level " + level + ".protection");
		int duration = getSettings().getInt("duration");

		Block block = player.getLocation().getBlock();
		if ((block.getRelative(0, -1, 0).isEmpty() && block.getRelative(0, -2, 0).isEmpty())
				|| block.getRelative(0, 1, 0).isLiquid())
			return false;

		if (!resourceCost.has(player, cost))
		{
			player.sendMessage(ChatColor.RED + "You are too tired to leap!");
			return false;
		}

		Attribute attribute = AttributeAPI.get(player, AttributeType.DEFENCE_TYPE(DamageType.FALL));
		attribute.getModifier(getName()).setFlat(protection).setLifetime(duration);

		Vector dir = player.getEyeLocation().getDirection().normalize();
		player.setVelocity(dir.multiply(force));
		DamageAPI.applyKnockback(player, player.getLocation(), 0.0f, lift);

		resourceCost.consume(player, cost);
		return true;
	}
}
