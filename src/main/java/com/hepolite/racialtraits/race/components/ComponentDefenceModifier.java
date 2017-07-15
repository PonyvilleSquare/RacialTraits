package com.hepolite.racialtraits.race.components;

import org.bukkit.entity.LivingEntity;

import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.coreutility.apis.attributes.AttributeType;
import com.hepolite.coreutility.apis.damage.DamageClass;
import com.hepolite.coreutility.apis.damage.DamageType;

public class ComponentDefenceModifier
{
	private final String name;
	private final String attribute;

	public ComponentDefenceModifier(String name)
	{
		this.name = name;
		this.attribute = AttributeType.DEFENCE_ALL;
	}

	public ComponentDefenceModifier(String name, DamageClass damageClass)
	{
		this.name = name;
		this.attribute = AttributeType.DEFENCE_CLASS(damageClass);
	}

	public ComponentDefenceModifier(String name, DamageType damageType)
	{
		this.name = name;
		this.attribute = AttributeType.DEFENCE_TYPE(damageType);
	}

	/** Sets the defence modifier represented by this component */
	public final void setDefence(LivingEntity entity, float scale, float multiplier, float flat)
	{
		AttributeAPI.get(entity, attribute).getModifier(name).setScale(scale)
				.setMultiplier(multiplier).setFlat(flat);
	}

	/** Sets the defence modifier represented by this component */
	public final void setDefence(LivingEntity entity, float scale, float multiplier, float flat,
			int lifetime)
	{
		AttributeAPI.get(entity, attribute).getModifier(name).setScale(scale)
				.setMultiplier(multiplier).setFlat(flat).setLifetime(lifetime);
	}

	/** Sets the lifetime of the class defence modifier represented by this component */
	public final void setLifetime(LivingEntity entity, int lifetime)
	{
		AttributeAPI.get(entity, attribute).getModifier(name).setLifetime(lifetime);
	}

	/** Wipes the component for any damage, removing the data for the given entity */
	public final void wipe(LivingEntity entity)
	{
		AttributeAPI.get(entity, attribute).removeModifier(name);
	}
}
