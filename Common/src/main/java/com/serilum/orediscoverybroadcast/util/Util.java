package com.serilum.orediscoverybroadcast.util;

import com.natamus.collective.config.DuskConfig;
import com.natamus.collective.config.DuskConfigMigration;
import com.natamus.collective.functions.*;
import com.natamus.collective.services.Services;
import com.serilum.orediscoverybroadcast.config.ConfigHandler;
import com.serilum.orediscoverybroadcast.data.Constants;
import com.serilum.orediscoverybroadcast.data.Variables;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Util {
	public static final WeakHashMap<Level, List<BlockPos>> ignoredOreBlockPositions = new WeakHashMap<>();

	private static final HashMap<Block, ChatFormatting> blockColourMap = new HashMap<>();
	private static final HashMap<Block, ChatFormatting> defaultColourMap = new HashMap<>();

	private static final String dirpath = DataFunctions.getConfigDirectory() + File.separator + Reference.MOD_ID;
	private static final File dir = new File(dirpath);
	private static final File colourMapFile = new File(dirpath + File.separator + "colourmap.txt");

	public static void attemptConfigProcessing(Level level) {
		if (!Variables.processedConfig) {
			try {
				processConfig(level);
				Variables.processedConfig = true;
			} catch (Exception ex) {
				Constants.logger.warn("[" + Reference.NAME + "] Error: Unable to generate config.");
				ex.printStackTrace();
			}
		}
	}

	public static void processConfig(Level level) throws IOException {
		setDefaultColourMap();
		blockColourMap.clear();

		Registry<Block> blockRegistry = level.registryAccess().lookupOrThrow(Registries.BLOCK);

		if (colourMapFile.isFile()) {
			String colourMapContent = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "colourmap.txt")));
			for (String line : colourMapContent.split(",")) {
				if (line.contains("//")) {
					continue;
				}

				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}

				String[] lspl = line.split("\\|");
				if (lspl.length != 2) {
					Constants.logger.warn("[" + Reference.NAME + "] Incorrect split length in line: " + line);
					continue;
				}

				String blockName = lspl[0].replace("\n", "").trim();
				String colourIndexString = lspl[1].replace("\n", "").trim();
				if (!NumberFunctions.isNumeric(colourIndexString)) {
					Constants.logger.warn("[" + Reference.NAME + "] The colour index " + colourIndexString + " is not a number.");
					continue;
				}

				Identifier blockRL = Identifier.parse(blockName);
				Optional<Holder.Reference<Block>> optionalBlockReference = blockRegistry.get(blockRL);
				if (optionalBlockReference.isEmpty()) {
					Constants.logger.warn("[" + Reference.NAME + "] 2: Unable to find block with resource location: " + line);
					continue;
				}

				int colourIndex = Integer.parseInt(colourIndexString);
				ChatFormatting colour = ColourFunctions.getById(colourIndex);
				if (colour == null) {
					Constants.logger.warn("[" + Reference.NAME + "] Unable to find colour for colour id: " + colourIndex);
					continue;
				}

				blockColourMap.put(optionalBlockReference.get().value(), colour);
			}

			return;
		}

		if (!dir.isDirectory()) {
			boolean ignored = dir.mkdirs();
		}

		PrintWriter colourMapWriter = new PrintWriter(dirpath + File.separator + "colourmap.txt", StandardCharsets.UTF_8);
		writeColourMapHeader(colourMapWriter);

		List<String> oreBlockNames = new ArrayList<>();
		HashMap<String, Block> oreBlockMap = new HashMap<>();
		for (Block block : blockRegistry) {
			if (isOre(block)) {
				Identifier rl = blockRegistry.getKey(block);
				if (rl == null) {
					continue;
				}

				oreBlockNames.add(rl.toString());
				oreBlockMap.put(rl.toString(), block);
			}
		}

		Collections.sort(oreBlockNames);

		for (String oreBlockName : oreBlockNames) {
			Block block = oreBlockMap.get(oreBlockName);

			int colourIndex = 9;
			if (defaultColourMap.containsKey(block)) {
				colourIndex = defaultColourMap.get(block).ordinal();
			}

			colourMapWriter.println(oreBlockName + "|" + colourIndex + ",");
			blockColourMap.put(block, ColourFunctions.getById(colourIndex));
		}

		colourMapWriter.close();
	}

	private static void writeColourMapHeader(PrintWriter colourMapWriter) {
		colourMapWriter.println("// To change the colour of the broadcasted message; change the number behind the ore,");
		colourMapWriter.println("// Possible colours; black=0; dark_blue=1; dark_green=2; dark_aqua=3; dark_red=4; dark_purple=5; gold=6; gray=7; dark_gray=8; blue=9; green=10; aqua=11; red=12; light_purple=13; yellow=14; white=15,\n");
	}

	public static String readColourMap() {
		String colourMapContent;
		try {
			colourMapContent = Files.readString(colourMapFile.toPath());
		}
		catch (IOException ignored) {
			return "";
		}

		List<String> oreColours = new ArrayList<>();
		for (String line : colourMapContent.split(",")) {
			if (line.contains("//")) {
				continue;
			}

			String[] lspl = line.trim().split("\\|");
			if (lspl.length == 2) {
				oreColours.add(lspl[0].trim() + "=" + lspl[1].trim());
			}
		}
		return String.join(",", oreColours);
	}

	public static void writeColourMap(MinecraftServer server, String oreColoursText) {
		try {
			PrintWriter colourMapWriter = new PrintWriter(dirpath + File.separator + "colourmap.txt", StandardCharsets.UTF_8);
			writeColourMapHeader(colourMapWriter);

			for (String oreColour : oreColoursText.split(",")) {
				String[] oreColourParts = oreColour.split("=");
				if (oreColourParts.length == 2) {
					colourMapWriter.println(oreColourParts[0].strip() + "|" + oreColourParts[1].strip() + ",");
				}
			}

			colourMapWriter.close();
		}
		catch (IOException ex) {
			Constants.logger.warn("[" + Reference.NAME + "] Unable to write the colour map.");
			return;
		}

		Variables.processedConfig = false;
		if (server != null) {
			attemptConfigProcessing(server.overworld());
		}
	}

	public static boolean isBlacklistedOre(Level level, Block block) {
		return ConfigFunctions.isInIdList(ConfigHandler.oreBlockBlacklist, BlockFunctions.getBlockId(level, block));
	}

	public static void moveBlacklistFileIntoConfig() {
		String disabledOres = DuskConfigMigration.takeDisabledIdsFromFile(Reference.MOD_ID, "blacklist.txt");
		if (disabledOres == null) {
			return;
		}

		ConfigHandler.oreBlockBlacklist = disabledOres;
		DuskConfig.write(Reference.MOD_ID);
	}

	private static void setDefaultColourMap() {
		defaultColourMap.put(Blocks.ANCIENT_DEBRIS, ChatFormatting.DARK_PURPLE);
		defaultColourMap.put(Blocks.COAL_ORE, ChatFormatting.DARK_GRAY);
		defaultColourMap.put(Blocks.DEEPSLATE_COAL_ORE, ChatFormatting.DARK_GRAY);
		defaultColourMap.put(Blocks.COPPER_ORE, ChatFormatting.YELLOW);
		defaultColourMap.put(Blocks.DEEPSLATE_COPPER_ORE, ChatFormatting.YELLOW);
		defaultColourMap.put(Blocks.DIAMOND_ORE, ChatFormatting.AQUA);
		defaultColourMap.put(Blocks.DEEPSLATE_DIAMOND_ORE, ChatFormatting.AQUA);
		defaultColourMap.put(Blocks.EMERALD_ORE, ChatFormatting.DARK_GREEN);
		defaultColourMap.put(Blocks.DEEPSLATE_EMERALD_ORE, ChatFormatting.DARK_GREEN);
		defaultColourMap.put(Blocks.GOLD_ORE, ChatFormatting.GOLD);
		defaultColourMap.put(Blocks.DEEPSLATE_GOLD_ORE, ChatFormatting.GOLD);
		defaultColourMap.put(Blocks.NETHER_GOLD_ORE, ChatFormatting.GOLD);
		defaultColourMap.put(Blocks.IRON_ORE, ChatFormatting.GRAY);
		defaultColourMap.put(Blocks.DEEPSLATE_IRON_ORE, ChatFormatting.GRAY);
		defaultColourMap.put(Blocks.LAPIS_ORE, ChatFormatting.BLUE);
		defaultColourMap.put(Blocks.NETHER_QUARTZ_ORE, ChatFormatting.WHITE);
		defaultColourMap.put(Blocks.REDSTONE_ORE, ChatFormatting.RED);
	}

	public static boolean isOre(BlockState blockState) {
		return isOre(blockState, blockState.getBlock());
	}
	public static boolean isOre(Block block) {
		return isOre(block.defaultBlockState(), block);
	}
	public static boolean isOre(BlockState blockState, Block block) {
		return Services.BLOCKTAGS.isOre(blockState) || block.equals(Blocks.ANCIENT_DEBRIS);
	}

	public static ChatFormatting getBroadcastColour(Block block) {
		if (blockColourMap.containsKey(block)) {
			return blockColourMap.get(block);
		}
		return ChatFormatting.BLUE;
	}

	public static boolean shouldBeIgnored(Level level, BlockPos blockPos) {
		boolean shouldIgnore = HashMapFunctions.computeIfAbsent(ignoredOreBlockPositions, level, k -> new ArrayList<>()).contains(blockPos);
		ignoredOreBlockPositions.get(level).remove(blockPos);
		return shouldIgnore;
	}

	public static int getOreCount(Level level, BlockPos blockPos, Block block) {
		if (!ignoredOreBlockPositions.containsKey(level)) {
			ignoredOreBlockPositions.put(level, new ArrayList<>());
		}

		int addSize = 1;
		List<BlockPos> connectedOres = BlockPosFunctions.getBlocksNextToEachOther(level, blockPos, Arrays.asList(block));
		for (BlockPos connectedPos : connectedOres) {
			if (!ignoredOreBlockPositions.get(level).contains(connectedPos)) {
				ignoredOreBlockPositions.get(level).add(connectedPos);
			}
			else {
				addSize -= 1;
			}
		}

		return connectedOres.size() + addSize;
	}
}
