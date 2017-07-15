package com.hepolite.racialtraits.projectiles;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.apis.entites.EntityLocater.SphereLocater;
import com.hepolite.coreutility.projectile.Projectile;
import com.hepolite.coreutility.util.effects.EffectHelper;

public class ProjectileThaumicBlast extends Projectile
{
	private final LivingEntity caster;
	private final Damage damage;
	private final float radius;

	public ProjectileThaumicBlast(LivingEntity caster, Location origin, float damage, float radius)
	{
		super(origin);
		this.caster = caster;
		this.damage = new Damage(DamageType.MAGIC, damage);
		this.radius = radius;
		origin.getWorld().playSound(origin, Sound.ENTITY_WITHER_HURT, 0.4f, 0.0f);
	}

	@Override
	public void onTick(int tick)
	{
		Entry<Location, LivingEntity> entry = getCollision(caster, false);
		if (entry != null)
		{
			Location target = entry.getKey();
			if (entry.getValue() == null)
				target.subtract(getVelocity().normalize().multiply(2.0));

			EffectHelper.magicalBolt(getLocation(), target);
			EffectHelper.magicalDischarge(target);
			EffectHelper.explosionEffect(target);
			invalidate();

			SphereLocater sphere = new SphereLocater(target, radius);
			for (LivingEntity entity : sphere.getUnobstructed(target, false, LivingEntity.class))
				DamageAPI.damage(entity, caster, damage);
		}

		if (isValid())
			EffectHelper.magicalBolt(getLocation(), getLocation().add(getVelocity()));
		updatePositionAndVelocity();
	}
}
