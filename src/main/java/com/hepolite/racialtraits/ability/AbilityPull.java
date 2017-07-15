package com.hepolite.racialtraits.ability;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.apis.entites.EntityLocater.LineLocater;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityPull extends Ability
{
	public AbilityPull(Race race)
	{
		super(race, "Pull");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		float range = getSettings().getFloat("range");
		float force = getSettings().getFloat("force");
		float lift = getSettings().getFloat("lift");
		float damage = getSettings().getFloat("damage");

		Location start = player.getEyeLocation();
		LineLocater line = new LineLocater(start, start.getDirection().multiply(range));
		LivingEntity target = line.getFirstUnobstructed(start, player, true, LivingEntity.class);
		if (DamageAPI.damage(target, player, new Damage(DamageType.BLUNT, damage)))
		{
			DamageAPI.applyKnockback(target, player.getLocation(), -force, lift);
			return true;
		}
		return false;
	}
}
