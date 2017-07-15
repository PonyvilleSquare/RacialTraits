package com.hepolite.racialtraits.race.races;

import com.hepolite.coreutility.apis.damage.DamageClass;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.ability.AbilityCropExpert;
import com.hepolite.racialtraits.race.ability.AbilityFarmer;
import com.hepolite.racialtraits.race.ability.AbilityFertilizing;
import com.hepolite.racialtraits.race.ability.AbilityGreenHooves;
import com.hepolite.racialtraits.race.ability.AbilityGroundPound;
import com.hepolite.racialtraits.race.ability.AbilityGrowthExpertise;
import com.hepolite.racialtraits.race.ability.AbilityHoofStrike;
import com.hepolite.racialtraits.race.ability.AbilityKick;
import com.hepolite.racialtraits.race.ability.AbilityLeap;
import com.hepolite.racialtraits.race.ability.AbilityNaturalConnection;
import com.hepolite.racialtraits.race.ability.AbilityObservant;
import com.hepolite.racialtraits.race.ability.AbilitySturdy;
import com.hepolite.racialtraits.race.ability.AbilitySwiftness;
import com.hepolite.racialtraits.race.ability.AbilityTendAnimals;
import com.hepolite.racialtraits.race.ability.generic.AbilityDefenceModifierPassive;
import com.hepolite.racialtraits.race.ability.generic.AbilitySpeedModifierPassive;
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
