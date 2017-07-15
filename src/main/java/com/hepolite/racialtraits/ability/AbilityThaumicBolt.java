package com.hepolite.racialtraits.ability;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.projectile.Projectile;
import com.hepolite.racialtraits.projectiles.ProjectileThaumicBolt;
import com.hepolite.racialtraits.race.Race;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityThaumicBolt extends Ability
{
	public AbilityThaumicBolt(Race race)
	{
		super(race, "Thaumic Bolt");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		int level = skill.getLevel();
		float speed = getSettings().getFloat("Level " + level + ".speed");
		float damage = getSettings().getFloat("Level " + level + ".damage");

		Location origin = player.getLocation().add(0.0, 1.25, 0.0);
		Projectile bolt = new ProjectileThaumicBolt(player, origin, damage);
		bolt.setVelocity(player.getEyeLocation().getDirection().multiply(speed));
		CoreUtility.getProjectileHandler().addProjectile(bolt);
		return true;
	}
}
