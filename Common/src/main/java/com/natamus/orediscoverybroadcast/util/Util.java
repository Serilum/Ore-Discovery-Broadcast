package com.natamus.orediscoverybroadcast.util;

import com.natamus.collective.functions.DataFunctions;
import com.natamus.collective.services.Services;
import com.natamus.orediscoverybroadcast.data.Constants;
import com.natamus.orediscoverybroadcast.data.Variables;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Util {
	public static List<Block> blockBlacklist = new ArrayList<Block>();

	private static final String dirpath = DataFunctions.getConfigDirectory() + File.separator + Reference.MOD_ID;
	private static final File dir = new File(dirpath);
	private static final File file = new File(dirpath + File.separator + "blacklist.txt");

	public static void attemptBlacklistProcessing(Level level) {
		if (!Variables.processedBlacklist) {
			try {
				processBlackList(level);
				Variables.processedBlacklist = true;
			} catch (Exception ex) {
				Constants.logger.warn("[" + Reference.NAME + "] Error: Unable to generate flower list.");
			}
		}
	}

	public static void processBlackList(Level level) throws IOException {
		Registry<Block> blockRegistry = level.registryAccess().registryOrThrow(Registries.BLOCK);

		PrintWriter writer = null;
		if (!dir.isDirectory() || !file.isFile()) {
			if (dir.mkdirs()) {
				writer = new PrintWriter(dirpath + File.separator + "blacklist.txt", StandardCharsets.UTF_8);
				writer.println("// To add an ore to the blacklist, add an exclamation mark (!) in front of it.");
			}
		}
		else {
			String blacklistContent = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "blacklist.txt")));
			for (String line : blacklistContent.split(",")) {
				if (line.contains("//")) {
					continue;
				}
				if (!line.startsWith("!")) {
					continue;
				}

				String blockName = line.replace("\n", "").replace("!", "").trim();

				ResourceLocation blockRL = new ResourceLocation(blockName);
				Block block = blockRegistry.get(blockRL);
				if (block == null) {
					Constants.logger.warn("[" + Reference.NAME + "] Unable to find block with resource location: " + line);
					continue;
				}

				blockBlacklist.add(block);
			}

			return;
		}

		List<String> oreBlockNames = new ArrayList<String>();
		for (Block block : blockRegistry) {
			if (isOre(block)) {
				ResourceLocation rl = blockRegistry.getKey(block);
				if (rl == null) {
					continue;
				}

				oreBlockNames.add(rl.toString());
			}
		}

		Collections.sort(oreBlockNames);

		for (String oreBlockName : oreBlockNames) {
			String prefix = "";
			if (oreBlockName.contains("quartz")) {
				prefix = "!";
			}
			writer.println(prefix + oreBlockName + ",");
		}

		writer.close();
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
}
