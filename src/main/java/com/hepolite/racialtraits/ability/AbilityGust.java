package com.hepolite.racialtraits.ability;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.apis.entites.EntityLocater.ConeLocater;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityGust extends Ability
{
	public AbilityGust(Race race)
	{
		super(race, "Gust");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		float range = getSettings().getFloat("range");
		float angle = getSettings().getFloat("angle");
		float damage = getSettings().getFloat("damage");
		float force = getSettings().getFloat("force");
		float lift = getSettings().getFloat("lift");

		Damage dmg = new Damage(DamageType.BLUNT, damage);

		boolean hitAnything = false;
		Location start = player.getEyeLocation();
		ConeLocater cone = new ConeLocater(start, start.getDirection().multiply(range), angle);
		for (LivingEntity entity : cone.getUnobstructed(start, true, LivingEntity.class))
		{
			if (entity == player)
				continue;
			if (DamageAPI.damage(entity, player, dmg))
			{
				DamageAPI.applyKnockback(entity, player.getLocation(), force, lift);
				hitAnything = true;
			}
		}
		return hitAnything;
	}
}
