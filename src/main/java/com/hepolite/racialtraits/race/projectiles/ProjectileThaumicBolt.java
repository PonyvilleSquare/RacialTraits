package com.hepolite.racialtraits.race.projectiles;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.projectile.Projectile;
import com.hepolite.coreutility.util.effects.EffectHelper;

public class ProjectileThaumicBolt extends Projectile
{
	private final LivingEntity caster;
	private final Damage damage;

	public ProjectileThaumicBolt(LivingEntity caster, Location origin, float damage)
	{
		super(origin);
		this.caster = caster;
		this.damage = new Damage(DamageType.MAGIC, damage);
		origin.getWorld().playSound(origin, Sound.ENTITY_WITHER_HURT, 0.4f, 0.0f);
	}

	@Override
	public void onTick(int tick)
	{
		Entry<Location, LivingEntity> entry = getCollision(caster, false);
		if (entry != null)
		{
			EffectHelper.magicalBolt(getLocation(), entry.getKey());
			EffectHelper.magicalDischarge(entry.getKey());
			invalidate();

			if (entry.getValue() != null)
				DamageAPI.damage(entry.getValue(), caster, damage);
		}

		if (isValid())
			EffectHelper.magicalBolt(getLocation(), getLocation().add(getVelocity()));
		updatePositionAndVelocity();
	}
}
