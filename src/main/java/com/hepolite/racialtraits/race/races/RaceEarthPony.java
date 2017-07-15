package com.hepolite.racialtraits.race.races;

import com.hepolite.coreutility.apis.damage.DamageClass;
import com.hepolite.racialtraits.ability.AbilityCropExpert;
import com.hepolite.racialtraits.ability.AbilityFarmer;
import com.hepolite.racialtraits.ability.AbilityFertilizing;
import com.hepolite.racialtraits.ability.AbilityGreenHooves;
import com.hepolite.racialtraits.ability.AbilityGroundPound;
import com.hepolite.racialtraits.ability.AbilityGrowthExpertise;
import com.hepolite.racialtraits.ability.AbilityHoofStrike;
import com.hepolite.racialtraits.ability.AbilityKick;
import com.hepolite.racialtraits.ability.AbilityLeap;
import com.hepolite.racialtraits.ability.AbilityNaturalConnection;
import com.hepolite.racialtraits.ability.AbilityObservant;
import com.hepolite.racialtraits.ability.AbilitySturdy;
import com.hepolite.racialtraits.ability.AbilitySwiftness;
import com.hepolite.racialtraits.ability.AbilityTendAnimals;
import com.hepolite.racialtraits.ability.generic.AbilityDefenceModifierPassive;
import com.hepolite.racialtraits.ability.generic.AbilitySpeedModifierPassive;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.resource.ResourceStamina;

public class RaceEarthPony extends Race
{
	public RaceEarthPony()
	{
		super("Earth Pony", new ResourceStamina());
		addAbility(new AbilityCropExpert(this));
		addAbility(new AbilityFarmer(this));
		addAbility(new AbilityFertilizing(this));
		addAbility(new AbilityGreenHooves(this));
		addAbility(new AbilityGroundPound(this));
		addAbility(new AbilityGrowthExpertise(this));
		addAbility(new AbilitySpeedModifierPassive(this, "Haste (Earth Pony)"));
		addAbility(new AbilityHoofStrike(this));
		addAbility(new AbilityKick(this));
		addAbility(new AbilityLeap(this));
		addAbility(new AbilityNaturalConnection(this));
		addAbility(new AbilityObservant(this));
		addAbility(new AbilitySturdy(this));
		addAbility(new AbilitySwiftness(this));
		addAbility(new AbilityTendAnimals(this));
		addAbility(new AbilityDefenceModifierPassive(this, "Toughness", DamageClass.PHYSICAL));
	}
}
