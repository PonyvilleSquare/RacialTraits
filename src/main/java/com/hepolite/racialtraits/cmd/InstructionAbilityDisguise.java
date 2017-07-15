package com.hepolite.racialtraits.cmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.cmd.Instruction;
import com.hepolite.coreutility.util.StringHelper;

public class InstructionAbilityDisguise extends Instruction
{
	private final Collection<IReplacer> replacers = new ArrayList<IReplacer>();

	public InstructionAbilityDisguise()
	{
		super("Disguise", "libsdisguises.disguise.any", -1);
		setPlayerOnly();

		registerSubInstruction(new InstructionAbilityDisguiseHelp());

		replacers.add(new ReplacerBaby());
		replacers.add(new ReplacerColor());
		replacers.add(new ReplacerHideSelf());
		replacers.add(new ReplacerType());
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<parameters>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Disguises the player with the given parameters");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows the player to disguises as a mob with certain settings specified by the user";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		Player player = (Player) sender;

		insertDefaultParameters(arguments);
		if (runSizeValidation(player, arguments))
			return true;

		// player.sendMessage(ChatColor.RED + "Command: " + ChatColor.WHITE + buildCommand(applyReplacers(arguments)));
		player.performCommand(buildCommand(applyReplacers(arguments)));
		return false;
	}

	// ////////////////////////////////////////////////////////////////////////////

	/** Applies default argument values, to ensure that the disguise command does not break */
	private final void insertDefaultParameters(List<String> arguments)
	{
		// Ensure that there is always a valid mob to disguise as
		if (arguments.size() == 0)
			arguments.add("Wolf");
		String form = arguments.get(0).replaceAll("_", "");

		// Ensure that slimes/magma cubes always have a size set
		if (form.equalsIgnoreCase("slime") || form.equalsIgnoreCase("magmacube"))
		{
			Collection<String> size = new ArrayList<String>();
			size.add("setSize");
			size.add("1");
			arguments.addAll(1, size);
		}
	}

	/** Runs the slime/magma cube size validation test; returns true if the test fails */
	private final boolean runSizeValidation(Player player, List<String> arguments)
	{
		for (int i = 0; i < arguments.size() - 1; ++i)
		{
			String first = arguments.get(i);
			String next = arguments.get(i + 1);
			if (first.equalsIgnoreCase("setSize") && StringHelper.asInt(next) > 2)
			{
				player.sendMessage(ChatColor.RED + "You are unable to take such a large body");
				return true;
			}
		}
		return false;
	}

	// ////////////////////////////////////////////////////////////////////////////

	/** Applies all replacing operators on the command */
	private final Collection<String> applyReplacers(Collection<String> arguments)
	{
		String form = null;

		Collection<String> replaced = new ArrayList<String>();
		for (String string : arguments)
		{
			if (form == null)
				form = string.replaceAll("_", "");

			for (IReplacer replacer : replacers)
				string = replacer.replace(form, string);
			replaced.add(string);
		}
		return replaced;
	}

	/** Builds the command from the arguments */
	private final String buildCommand(Collection<String> arguments)
	{
		StringBuilder builder = new StringBuilder("libsdisguises:disguise");
		for (String string : arguments)
			builder.append(" ").append(string);
		String command = builder.toString();
		while (command.contains("  "))
			command = command.replaceAll("  ", " ");
		return command;
	}

	// ////////////////////////////////////////////////////////////////////////////

	private interface IReplacer
	{
		/** Takes the input string and replaces it with something, based on what the entity type is */
		public String replace(String form, String input);
	}

	private final class ReplacerBaby implements IReplacer
	{
		@Override
		public String replace(String form, String input)
		{
			return input.replaceAll("-b", "baby");
		}
	}

	private final class ReplacerColor implements IReplacer
	{
		@Override
		public String replace(String form, String input)
		{
			if (form.equalsIgnoreCase("horse") || form.equalsIgnoreCase("sheep"))
				input = input.replaceAll("-c:", "setColor ");
			else if (form.equalsIgnoreCase("ocelot") || form.equalsIgnoreCase("rabbit"))
				input = input.replaceAll("-c:", "setType ");
			else if (form.equalsIgnoreCase("parrot"))
				input = input.replaceAll("-c:", "setVariant ");
			else if (form.equalsIgnoreCase("wolf"))
				input = input.replaceAll("-c:", "setCollarColor ");
			return input;
		}
	}

	private final class ReplacerHideSelf implements IReplacer
	{
		@Override
		public String replace(String form, String input)
		{
			return input.replaceAll("-h", "setViewSelfDisguise false");
		}
	}

	private final class ReplacerType implements IReplacer
	{
		@Override
		public String replace(String form, String input)
		{
			if (form.equalsIgnoreCase("horse"))
				input = input.replaceAll("-t:", "setStyle ");
			else if (form.equalsIgnoreCase("ocelot") || form.equalsIgnoreCase("rabbit"))
				input = input.replaceAll("-t:", "setType ");
			else if (form.equalsIgnoreCase("parrot"))
				input = input.replaceAll("-t:", "setVariant ");
			else if (form.equalsIgnoreCase("villager"))
				input = input.replaceAll("-t:", "setProfession ");
			return input;
		}
	}
}
