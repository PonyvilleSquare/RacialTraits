package com.hepolite.racialtraits.race.ability;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.projectile.Projectile;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.projectiles.ProjectileThaumicBlast;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityThaumicBlast extends Ability
{
	public AbilityThaumicBlast(Race race)
	{
		super(race, "Thaumic Blast");
	}

	@Override
	public final boolean onCast(Player player, PlayerSkill skill)
	{
		int level = skill.getLevel();
		float speed = getSettings().getFloat("Level " + level + ".speed");
		float damage = getSettings().getFloat("Level " + level + ".damage");
		float radius = getSettings().getFloat("Level " + level + ".radius");

		Location origin = player.getLocation().add(0.0, 1.25, 0.0);
		Projectile bolt = new ProjectileThaumicBlast(player, origin, damage, radius);
		bolt.setVelocity(player.getEyeLocation().getDirection().multiply(speed));
		CoreUtility.getProjectileHandler().addProjectile(bolt);
		return true;
	}
}
