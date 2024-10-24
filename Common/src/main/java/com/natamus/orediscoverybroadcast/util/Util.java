package com.natamus.orediscoverybroadcast.util;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.DataFunctions;
import com.natamus.collective.functions.HashMapFunctions;
import com.natamus.collective.functions.NumberFunctions;
import com.natamus.collective.services.Services;
import com.natamus.orediscoverybroadcast.data.Constants;
import com.natamus.orediscoverybroadcast.data.Variables;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
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
	public static final WeakHashMap<Level, List<BlockPos>> ignoredOreBlockPositions = new WeakHashMap<Level, List<BlockPos>>();

	public static List<Block> blockBlacklist = new ArrayList<Block>();

	private static final HashMap<Block, ChatFormatting> blockColourMap = new HashMap<Block, ChatFormatting>();
	private static final HashMap<Block, ChatFormatting> defaultColourMap = new HashMap<Block, ChatFormatting>();

	private static final String dirpath = DataFunctions.getConfigDirectory() + File.separator + Reference.MOD_ID;
	private static final File dir = new File(dirpath);
	private static final File blacklistFile = new File(dirpath + File.separator + "blacklist.txt");
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

		Registry<Block> blockRegistry = level.registryAccess().lookupOrThrow(Registries.BLOCK);

		PrintWriter blacklistWriter = null;
		PrintWriter colourMapWriter = null;
		if (blacklistFile.isFile() && colourMapFile.isFile()) {
			String blacklistContent = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "blacklist.txt")));
			for (String line : blacklistContent.split(",")) {
				if (line.contains("//")) {
					continue;
				}

				line = line.strip();
				if (line.isEmpty()) {
					continue;
				}
				if (!line.startsWith("!")) {
					continue;
				}

				String blockName = line.replace("\n", "").replace("!", "").trim();

				ResourceLocation blockRL = ResourceLocation.parse(blockName);
				Optional<Holder.Reference<Block>> optionalBlockReference = blockRegistry.get(blockRL);
				if (optionalBlockReference.isEmpty()) {
					Constants.logger.warn("[" + Reference.NAME + "] 1: Unable to find block with resource location: " + line);
					continue;
				}

				blockBlacklist.add(optionalBlockReference.get().value());
			}

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

				ResourceLocation blockRL = ResourceLocation.parse(blockName);
				Optional<Holder.Reference<Block>> optionalBlockReference = blockRegistry.get(blockRL);
				if (optionalBlockReference.isEmpty()) {
					Constants.logger.warn("[" + Reference.NAME + "] 2: Unable to find block with resource location: " + line);
					continue;
				}

				int colourIndex = Integer.parseInt(colourIndexString);
				ChatFormatting colour = ChatFormatting.getById(colourIndex);
				if (colour == null) {
					Constants.logger.warn("[" + Reference.NAME + "] Unable to find colour for colour id: " + colourIndex);
					continue;
				}

				blockColourMap.put(optionalBlockReference.get().value(), colour);
			}

			return;
		}
		else {
			if (!dir.isDirectory()) {
				boolean ignored = dir.mkdirs();
			}

			if (!blacklistFile.isFile()) {
				blacklistWriter = new PrintWriter(dirpath + File.separator + "blacklist.txt", StandardCharsets.UTF_8);
				blacklistWriter.println("// To add an ore to the blacklist; add an exclamation mark (!) in front of it,\n");
			}

			if (!colourMapFile.isFile()) {
				colourMapWriter = new PrintWriter(dirpath + File.separator + "colourmap.txt", StandardCharsets.UTF_8);
				colourMapWriter.println("// To change the colour of the broadcasted message; change the number behind the ore,");
				colourMapWriter.println("// Possible colours; black=0; dark_blue=1; dark_green=2; dark_aqua=3; dark_red=4; dark_purple=5; gold=6; gray=7; dark_gray=8; blue=9; green=10; aqua=11; red=12; light_purple=13; yellow=14; white=15,\n");
			}
		}

		List<String> oreBlockNames = new ArrayList<String>();
		HashMap<String, Block> oreBlockMap = new HashMap<String, Block>();
		for (Block block : blockRegistry) {
			if (isOre(block)) {
				ResourceLocation rl = blockRegistry.getKey(block);
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

			String prefix = "";
			if (oreBlockName.contains("quartz")) {
				prefix = "!";
			}

			if (blacklistWriter != null) {
				blacklistWriter.println(prefix + oreBlockName + ",");
			}

			int colourIndex = 9;
			if (defaultColourMap.containsKey(block)) {
				colourIndex = defaultColourMap.get(block).getId();
			}

			if (colourMapWriter != null) {
				colourMapWriter.println(oreBlockName + "|" + colourIndex + ",");
			}
			blockColourMap.put(block, ChatFormatting.getById(colourIndex));
		}

		if (blacklistWriter != null) {
			blacklistWriter.close();
		}
		if (colourMapWriter != null) {
			colourMapWriter.close();
		}
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
		boolean shouldIgnore = HashMapFunctions.computeIfAbsent(ignoredOreBlockPositions, level, k -> new ArrayList<BlockPos>()).contains(blockPos);
		ignoredOreBlockPositions.get(level).remove(blockPos);
		return shouldIgnore;
	}

	public static int getOreCount(Level level, BlockPos blockPos, Block block) {
		if (!ignoredOreBlockPositions.containsKey(level)) {
			ignoredOreBlockPositions.put(level, new ArrayList<BlockPos>());
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
