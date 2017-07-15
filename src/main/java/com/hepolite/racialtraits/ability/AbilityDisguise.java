package com.hepolite.racialtraits.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.libraryaddict.disguise.disguisetypes.AnimalColor;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.SheepWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.WolfWatcher;
import me.libraryaddict.disguise.events.DisguiseEvent;
import me.libraryaddict.disguise.events.UndisguiseEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.event.events.DamageEvent;
import com.hepolite.coreutility.event.events.PlayerFlightAllowEvent;
import com.hepolite.coreutility.util.InventoryHelper;
import com.hepolite.coreutility.util.TimeHelper;
import com.hepolite.coreutility.util.WeatherHelper;
import com.hepolite.coreutility.util.java.TriFunction;
import com.hepolite.racialtraits.RacialTraits;
import com.hepolite.racialtraits.ability.components.ComponentMovementModifier;
import com.hepolite.racialtraits.race.Race;
import com.hepolite.racialtraits.skillapi.SkillAPIHelper;
import com.sucy.skill.api.player.PlayerSkill;

public class AbilityDisguise extends Ability
{
	private final ComponentMovementModifier movementModifier;

	private final Map<UUID, Form> forms = new HashMap<UUID, Form>();
	private boolean disguisedWithForm = false; // LibsDisguises fires the disguise event BEFORE undisguise event; this is used to track when a player is changing disguise without undisguising first

	public AbilityDisguise(Race race)
	{
		super(race, "Disguise");
		movementModifier = new ComponentMovementModifier(getName());
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onPlayerDisguise(DisguiseEvent event)
	{
		Entity entity = event.getEntity();
		if (!(entity instanceof Player))
			return;
		Disguise disguise = event.getDisguise();
		DisguiseType type = disguise.getType();
		Player player = (Player) entity;

		if (!isAllowedToDisguise(player, disguise))
		{
			player.sendMessage(ChatColor.RED + "You are unable to take that form!");
			event.setCancelled(true);
			return;
		}

		final TriFunction<Player, Player, ItemStack, ItemStack> interaction;
		switch (type)
		{
		case COW:
			interaction = this::handleCowInteraction;
			break;
		case MUSHROOM_COW:
			interaction = this::handleMushroomCowInteraction;
			break;
		case SHEEP:
			interaction = this::handleSheepInteraction;
			break;
		case WOLF:
			interaction = this::handleWolfInteraction;
			break;
		default:
			interaction = null;
		}

		String path = "Disguises." + type.name().toLowerCase().replaceAll("_", "");
		Form form = new Form(disguise, interaction);
		form.canFly = getSettings().getBool(path + ".canFly");
		form.canSwim = getSettings().getBool(path + ".canSwim");
		form.canUseBlocks = getSettings().getBool(path + ".canUseBlocks");
		form.canUseItems = getSettings().getBool(path + ".canUseItems");
		form.hasArmor = getSettings().getBool(path + ".hasArmor");
		form.hasFireImmunity = getSettings().getBool(path + ".hasFireImmunity");
		form.isWaterBreather = getSettings().getBool(path + ".isWaterBreather");
		form.takesSunDamage = getSettings().getBool(path + ".takesSunDamage");
		form.takesWaterDamage = getSettings().getBool(path + ".takesWaterDamage");

		UUID uuid = player.getUniqueId();
		if (forms.containsKey(uuid))
			disguisedWithForm = true;
		forms.put(uuid, form);

		float flightSpeed = getSettings().getFloat(path + ".speedFlight");
		float walkSpeed = getSettings().getFloat(path + ".speedWalk");
		movementModifier.setFly(player, 0.0f, flightSpeed, 0.0f);
		movementModifier.setWalk(player, 0.0f, walkSpeed, 0.0f);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onPlayerUndisguise(UndisguiseEvent event)
	{
		Entity entity = event.getEntity();
		if (entity.getType() != EntityType.PLAYER)
			return;
		Player player = (Player) entity;

		if (disguisedWithForm)
			disguisedWithForm = false;
		else
		{
			forms.remove(player.getUniqueId());
			movementModifier.wipe(player);
		}
	}

	/** Returns true if the given player is allowed to disguise as the given disguise */
	private final boolean isAllowedToDisguise(Player player, Disguise disguise)
	{
		PermissionUser user = PermissionsEx.getUser(player);
		if (user.has("racialtraits.race.admin"))
			return true;

		PlayerSkill skill = getSkill(player);
		if (skill == null)
			return false;
		if (disguise.isPlayerDisguise() && skill.getLevel() < 2)
			return false;

		String path = "Disguises." + disguise.getType().name().toLowerCase().replaceAll("_", "");
		return getSettings().has(path);
	}

	// //////////////////////////////////////////////////////////////////////

	@Override
	public final void onTick(Player player, PlayerSkill skill, int tick)
	{
		Form form = forms.get(player.getUniqueId());
		if (form == null)
			return;

		if (tick % 40 == 0)
		{
			handleSunDamage(player, form);
			handleWaterDamage(player, form);
		}
		if (tick % 100 == 0)
			handleDrowning(player, form);
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public final void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		Form form = forms.get(player.getUniqueId());
		if (form != null && !form.canUseItems())
		{
			ItemStack item = event.getItem();
			if (item == null || item.getType() == Material.AIR)
				return;
			if (CoreUtility.getHungerHandler().canPlayerEat(player, item))
				return;

			event.getPlayer().sendMessage(ChatColor.RED + "You are unable to use that in your current form");
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerInteract(PlayerInteractEntityEvent event)
	{
		if (event.getRightClicked().getType() != EntityType.PLAYER)
			return;
		Player disguisedPlayer = (Player) event.getRightClicked();
		Form form = forms.get(disguisedPlayer.getUniqueId());
		if (form != null && form.getInteraction() != null)
		{
			Player player = event.getPlayer();
			PlayerInventory inv = player.getInventory();
			ItemStack item = event.getHand() == EquipmentSlot.HAND ? inv.getItemInMainHand() : inv.getItemInOffHand();

			item = form.getInteraction().apply(disguisedPlayer, player, item);
			if (InventoryHelper.isDestroyed(item))
				item = null;

			if (event.getHand() == EquipmentSlot.HAND)
				inv.setItemInMainHand(item);
			else
				inv.setItemInOffHand(item);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public final void onPlayerBreakBlock(BlockBreakEvent event)
	{
		Form form = forms.get(event.getPlayer().getUniqueId());
		if (form != null && !form.canUseBlocks())
		{
			event.getPlayer().sendMessage(ChatColor.RED + "You are unable to break that in your current form");
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public final void onPlayerPlaceBlock(BlockPlaceEvent event)
	{
		Form form = forms.get(event.getPlayer().getUniqueId());
		if (form != null && !form.canUseBlocks())
		{
			event.getPlayer().sendMessage(ChatColor.RED + "You are unable to place that in your current form");
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public final void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		Form form = forms.get(event.getPlayer().getUniqueId());
		if (form != null && !form.canUseItems())
		{
			if (RacialTraits.getCurrentTick() % 50 == 0)
				event.getPlayer().sendMessage(ChatColor.RED + "You are unable to pick up items in your current form");
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public final void onPlayerPickupItem(PlayerPickupArrowEvent event)
	{
		Form form = forms.get(event.getPlayer().getUniqueId());
		if (form != null && !form.canUseItems())
		{
			if (RacialTraits.getCurrentTick() % 50 == 0)
				event.getPlayer().sendMessage(ChatColor.RED + "You are unable to pick up items in your current form");
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public final void onPlayerTakeDamage(EntityDamageEvent event)
	{
		if (event.getEntityType() != EntityType.PLAYER)
			return;
		Form form = forms.get(event.getEntity().getUniqueId());
		if (form != null && !form.hasArmor())
		{
			if (event.isApplicable(DamageModifier.ARMOR))
				event.setDamage(DamageModifier.ARMOR, 0.0);
			if (event.isApplicable(DamageModifier.BLOCKING))
				event.setDamage(DamageModifier.BLOCKING, 0.0);
			if (event.isApplicable(DamageModifier.HARD_HAT))
				event.setDamage(DamageModifier.HARD_HAT, 0.0);
			if (event.isApplicable(DamageModifier.MAGIC))
				event.setDamage(DamageModifier.MAGIC, 0.0);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public final void onPlayerTakeDamage(DamageEvent event)
	{
		Form form = forms.get(event.getTarget().getUniqueId());
		if (form != null && form.hasFireImmunity())
		{
			DamageType type = event.getDamage().getDamageType();
			if (type == DamageType.FIRE)
				event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public final void onPlayerFlightCheck(PlayerFlightAllowEvent event)
	{
		Form form = forms.get(event.getPlayer().getUniqueId());
		if (form != null)
			event.setFligthAllowed(form.canSwim(event.getPlayer()) || form.canFly());
	}

	// //////////////////////////////////////////////////////////////////////

	/** Interaction with a cow disguise */
	private final ItemStack handleCowInteraction(Player target, Player observer, ItemStack item)
	{
		if (item.getType() == Material.BUCKET)
		{
			// TODO: Add some sort of cost in here
			target.sendMessage(ChatColor.RED + "Somebody milked you! How disturbing!");

			item.setAmount(item.getAmount() - 1);
			if (item.getAmount() <= 0)
			{
				item.setType(Material.MILK_BUCKET);
				item.setAmount(1);
			}
			else
				InventoryHelper.addWithDrop(observer, new ItemStack(Material.MILK_BUCKET));
		}
		return item;
	}

	/** Interaction with a mushrom cow disguise */
	private final ItemStack handleMushroomCowInteraction(Player target, Player observer, ItemStack item)
	{
		if (item.getType() == Material.BOWL)
		{
			// TODO: Add some sort of cost in here
			target.sendMessage(ChatColor.RED + "Somebody harvested mushrooms from you! How disturbing!");

			item.setAmount(item.getAmount() - 1);
			if (item.getAmount() <= 0)
			{
				item.setType(Material.MUSHROOM_SOUP);
				item.setAmount(1);
			}
			else
				InventoryHelper.addWithDrop(observer, new ItemStack(Material.MUSHROOM_SOUP));
		}
		return item;
	}

	/** Interaction with a sheep disguise */
	private final ItemStack handleSheepInteraction(Player target, Player observer, ItemStack item)
	{
		Form form = forms.get(target.getUniqueId());
		Disguise disguise = form.getDisguise();

		SheepWatcher watcher = (SheepWatcher) disguise.getWatcher();
		if (item.getType() == Material.INK_SACK)
		{
			AnimalColor oldColor = watcher.getColor();
			AnimalColor newColor = AnimalColor.getColor(15 - item.getDurability());
			if (!oldColor.equals(newColor))
			{
				target.sendMessage(ChatColor.RED + "Somebody changed your wool color! Your new color is " + newColor.name().toLowerCase().replaceAll("_", " "));
				watcher.setColor(newColor);
				item.setAmount(item.getAmount() - 1);
			}
		}
		if (item.getType() == Material.SHEARS && !watcher.isSheared())
		{
			// TODO: Add some sort of cost in here
			target.sendMessage(ChatColor.RED + "Somebody sheared you! You have no wool left!");
			watcher.setSheared(true);
			ItemStack wool = new ItemStack(Material.WOOL, 1 + random.nextInt(2), (short) watcher.getColor().getId());
			target.getWorld().dropItemNaturally(target.getLocation(), wool);
			item.setDurability((short) (item.getDurability() + 1));
		}

		return item;
	}

	/** Interaction with a wolf disguise */
	private final ItemStack handleWolfInteraction(Player target, Player observer, ItemStack item)
	{
		Form form = forms.get(target.getUniqueId());
		Disguise disguise = form.getDisguise();

		WolfWatcher watcher = (WolfWatcher) disguise.getWatcher();
		if (item.getType() == Material.INK_SACK)
		{
			AnimalColor oldColor = watcher.getCollarColor();
			AnimalColor newColor = AnimalColor.getColor(15 - item.getDurability());
			if (!oldColor.equals(newColor) && watcher.isTamed())
			{
				target.sendMessage(ChatColor.RED + "Somebody changed your wool color! Your new color is " + newColor.name().toLowerCase().replaceAll("_", " "));
				watcher.setCollarColor(newColor);
				item.setAmount(item.getAmount() - 1);
			}
		}

		return item;
	}

	/** Checks if the player should burn from the sun, and applies fire damage if applicable */
	private final void handleSunDamage(Player player, Form form)
	{
		if (!form.takesSunDamage() || !TimeHelper.isSunUp(player.getWorld()))
			return;
		if (player.getEyeLocation().getBlock().getLightFromSky() != 15)
			return;
		if (form.getDisguise().isMobDisguise())
		{
			MobDisguise disguise = (MobDisguise) form.getDisguise();
			if (!disguise.isAdult())
				return;
		}
		if (form.hasArmor())
		{
			ItemStack helmet = player.getEquipment().getHelmet();
			if (helmet != null && helmet.getType() != Material.AIR)
				return;
		}

		player.setFireTicks(100);
	}

	/** Checks if the player should take damage from water, and applies damage if applicable */
	private final void handleWaterDamage(Player player, Form form)
	{
		if (!form.takesWaterDamage())
			return;

		Material type = player.getLocation().getBlock().getType();
		boolean inWater = (type == Material.WATER || type == Material.STATIONARY_WATER);
		if (inWater || WeatherHelper.isRaining(player.getEyeLocation()))
			DamageAPI.damage(player, new Damage(DamageType.WATER, 1.0f));
	}

	/** Checks if the player should take suffocation damage, and applies damage if applicable */
	private final void handleDrowning(Player player, Form form)
	{
		if (!form.isWaterBreather())
			return;

		Material type = player.getEyeLocation().getBlock().getType();
		if (type != Material.WATER && type != Material.STATIONARY_WATER)
			DamageAPI.damage(player, new Damage(DamageType.SUFFOCATION, 1.0f));
		else
			player.setRemainingAir(player.getMaximumAir());
	}

	private final class Form
	{
		private final Disguise disguise;
		private final TriFunction<Player, Player, ItemStack, ItemStack> interaction;

		private boolean canFly = false;
		private boolean canSwim = false;
		private boolean canUseBlocks = false;
		private boolean canUseItems = false;
		private boolean hasArmor = false;
		private boolean hasFireImmunity = false;
		private boolean isWaterBreather = false;
		private boolean takesSunDamage = false;
		private boolean takesWaterDamage = false;

		private Form(Disguise disguise, TriFunction<Player, Player, ItemStack, ItemStack> interaction)
		{
			this.disguise = disguise;
			this.interaction = interaction;
		}

		/** Returns the disguise associated with this form */
		public final Disguise getDisguise()
		{
			return disguise;
		}

		/** Returns the interaction associated with the disguise; returns null if there is no interaction */
		public final TriFunction<Player, Player, ItemStack, ItemStack> getInteraction()
		{
			return interaction;
		}

		/** Returns true if the form is allowed to fly */
		public final boolean canFly()
		{
			if (canFly && disguise.isPlayerDisguise())
			{
				PlayerDisguise dis = (PlayerDisguise) disguise;
				OfflinePlayer target = Bukkit.getOfflinePlayer(dis.getGameProfile().getUUID());
				if (!target.hasPlayedBefore())
					return false;

				if (SkillAPIHelper.getSkill(target, "Flight") == null)
					return false;
			}
			return canFly;
		}

		/** Returns true if the form is allowed to swim */
		public final boolean canSwim(Player player)
		{
			Material type = player.getEyeLocation().getBlock().getType();
			if (type != Material.WATER && type != Material.STATIONARY_WATER)
				return false;

			if (canSwim && disguise.isPlayerDisguise())
			{
				PlayerDisguise dis = (PlayerDisguise) disguise;
				OfflinePlayer target = Bukkit.getOfflinePlayer(dis.getGameProfile().getUUID());
				if (!target.hasPlayedBefore())
					return false;

				if (SkillAPIHelper.getSkill(target, "Swim") == null)
					return false;
			}
			return canSwim;
		}

		/** Returns true if the disguise is allowed to use blocks */
		public final boolean canUseBlocks()
		{
			return canUseBlocks;
		}

		/** Returns true if the disguise is allowed to use items */
		public final boolean canUseItems()
		{
			return canUseItems;
		}

		/** Returns true if armor should count when being attacked */
		public final boolean hasArmor()
		{
			return hasArmor;
		}

		/** Returns true if the disguise is immune to fire */
		public final boolean hasFireImmunity()
		{
			return hasFireImmunity;
		}

		/** Returns true if the disguise can breathe underwater */
		public final boolean isWaterBreather()
		{
			if (isWaterBreather && disguise.isPlayerDisguise())
			{
				PlayerDisguise dis = (PlayerDisguise) disguise;
				OfflinePlayer target = Bukkit.getOfflinePlayer(dis.getGameProfile().getUUID());
				if (!target.hasPlayedBefore())
					return false;

				if (SkillAPIHelper.getSkill(target, "Aquatic Lifeform") == null)
					return false;
			}
			return isWaterBreather;
		}

		/** Returns true if the disguise is damaged by water */
		public final boolean takesWaterDamage()
		{
			return takesWaterDamage;
		}

		/** Returns true if the disguise is damaged by the sun */
		public final boolean takesSunDamage()
		{
			return takesSunDamage;
		}
	}
}
