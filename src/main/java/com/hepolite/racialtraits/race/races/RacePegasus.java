package com.hepolite.racialtraits.race.races;

import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.racialtraits.ability.AbilityAttitudeControl;
import com.hepolite.racialtraits.ability.AbilityCarry;
import com.hepolite.racialtraits.ability.AbilityChangeWeather;
import com.hepolite.racialtraits.ability.AbilityCharged;
import com.hepolite.racialtraits.ability.AbilityCloudSeed;
import com.hepolite.racialtraits.ability.AbilityDash;
import com.hepolite.racialtraits.ability.AbilityFlight;
import com.hepolite.racialtraits.ability.AbilityFlyFaster;
import com.hepolite.racialtraits.ability.AbilityFlyer;
import com.hepolite.racialtraits.ability.AbilityGlide;
import com.hepolite.racialtraits.ability.AbilityGust;
import com.hepolite.racialtraits.ability.AbilityHailstorm;
import com.hepolite.racialtraits.ability.AbilityLightning;
import com.hepolite.racialtraits.ability.AbilityPreen;
import com.hepolite.racialtraits.ability.generic.AbilityDefenceModifierPassive;
import com.hepolite.racialtraits.ability.generic.AbilityHungerModifierFlight;
import com.hepolite.racialtraits.ability.generic.AbilityProduction;
import com.hepolite.racialtraits.race.Race;

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
