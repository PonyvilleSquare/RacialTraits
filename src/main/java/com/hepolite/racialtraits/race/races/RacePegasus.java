package com.hepolite.racialtraits.race.races;

import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.ability.AbilityAttitudeControl;
import com.hepolite.racialtraits.race.ability.AbilityCarry;
import com.hepolite.racialtraits.race.ability.AbilityChangeWeather;
import com.hepolite.racialtraits.race.ability.AbilityCharged;
import com.hepolite.racialtraits.race.ability.AbilityCloudSeed;
import com.hepolite.racialtraits.race.ability.AbilityDash;
import com.hepolite.racialtraits.race.ability.AbilityFlight;
import com.hepolite.racialtraits.race.ability.AbilityFlyFaster;
import com.hepolite.racialtraits.race.ability.AbilityFlyer;
import com.hepolite.racialtraits.race.ability.AbilityGlide;
import com.hepolite.racialtraits.race.ability.AbilityGust;
import com.hepolite.racialtraits.race.ability.AbilityHailstorm;
import com.hepolite.racialtraits.race.ability.AbilityLightning;
import com.hepolite.racialtraits.race.ability.AbilityPreen;
import com.hepolite.racialtraits.race.ability.generic.AbilityDefenceModifierPassive;
import com.hepolite.racialtraits.race.ability.generic.AbilityHungerModifierFlight;
import com.hepolite.racialtraits.race.ability.generic.AbilityProduction;

public class RacePegasus extends Race
{
	public RacePegasus()
	{
		super("Pegasus", null);
		addAbility(new AbilityAttitudeControl(this));
		addAbility(new AbilityCarry(this, "Carry"));
		addAbility(new AbilityChangeWeather(this));
		addAbility(new AbilityProduction(this, "Charge Cloud"));
		addAbility(new AbilityCharged(this));
		addAbility(new AbilityCloudSeed(this));
		addAbility(new AbilityProduction(this, "Cooling"));
		addAbility(new AbilityProduction(this, "Cloud Production"));
		addAbility(new AbilityDash(this));
		addAbility(new AbilityFlight(this, false));
		addAbility(new AbilityFlyer(this));
		addAbility(new AbilityFlyFaster(this));
		addAbility(new AbilityGlide(this));
		addAbility(new AbilityGust(this));
		addAbility(new AbilityHailstorm(this));
		addAbility(new AbilityLightning(this));
		addAbility(new AbilityHungerModifierFlight(this, "Long-Range Flyer"));
		addAbility(new AbilityPreen(this));
		addAbility(new AbilityProduction(this, "Prism"));
		addAbility(new AbilityDefenceModifierPassive(this, "Soft Landing", DamageType.FALL));
		addAbility(new AbilityProduction(this, "Water Condensation"));
	}
}
