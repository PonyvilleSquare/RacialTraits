package com.hepolite.racialtraits.race.ability;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.event.events.DamageEvent;
import com.hepolite.coreutility.settings.Settings;
import com.hepolite.coreutility.util.effects.EffectHelper;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.race.components.ComponentItemCost;

public class AbilityCharged extends Ability
{
	private final ComponentItemCost itemCost;

	public AbilityCharged(Race race)
	{
		super(race, "Charged");
		itemCost = new ComponentItemCost();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerAttackEntity(DamageEvent event)
	{
		Player player = event.getAttackerAsPlayer();
		if (!validate(player) || event.getDamage().getDamageType() != DamageType.NORMAL)
			return;

		Settings settings = getSettings();
		Collection<ItemStack> items = settings.getItems("cost");
		float damage = settings.getFloat("damage");

		if (!itemCost.has(player, items))
			return;
		if (DamageAPI.damage(event.getTarget(), player, new Damage(DamageType.ELECTRICITY, damage)))
		{
			EffectHelper.electricalDischarge(event.getTarget().getEyeLocation());
			itemCost.consume(player, items);
			cast(player);
		}
	}
}
