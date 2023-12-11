package com.natamus.orediscoverybroadcast.events;

import com.natamus.collective.fakeplayer.FakePlayer;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.services.Services;
import com.natamus.orediscoverybroadcast.config.ConfigHandler;
import com.natamus.orediscoverybroadcast.data.Constants;
import com.natamus.orediscoverybroadcast.util.Reference;
import com.natamus.orediscoverybroadcast.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public class MiningEvent {
	private static final HashMap<String, HashMap<String, Integer>> playerLastFoundTicks = new HashMap<String, HashMap<String, Integer>>();

	public static void onBlockBreak(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		if (level.isClientSide) {
			return;
		}

		if (ConfigHandler.onlyRunOnDedicatedServers) {
			if (!level.getServer().isDedicatedServer()) {
				return;
			}
		}
		
		if (ConfigHandler.ignoreFakePlayers) {
			if (player instanceof FakePlayer) {
				return;
			}
		}

		if (ConfigHandler.ignoreCreativePlayers) {
			if (player.isCreative()) {
				return;
			}
		}

		ItemStack handStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!Services.TOOLFUNCTIONS.isPickaxe(handStack)) {
			return;
		}

		Block block = state.getBlock();
		if (!Util.isOre(state, block)) {
			return;
		}
		if (Util.blockBlacklist.contains(block)) {
			return;
		}

		int playerTickCount = player.tickCount;
		String playerName = player.getName().getString();

		String oreName = block.getName().getString();
		if (ConfigHandler.hideDeepslateFromName) {
			Registry<Block> blockRegistry = level.registryAccess().registryOrThrow(Registries.BLOCK);
			ResourceLocation blockResourceLocation = blockRegistry.getKey(block);
			if (blockResourceLocation.toString().contains("deepslate_")) {
				ResourceLocation defaultOreLocation = new ResourceLocation(blockResourceLocation.toString().replace("deepslate_", ""));
				Block defaultOreBlock = blockRegistry.get(defaultOreLocation);
				if (defaultOreBlock != null) {
					oreName = defaultOreBlock.getName().getString();
				}
			}
		}
		if (ConfigHandler.lowercaseOreName) {
			oreName = oreName.toLowerCase();
		}

		boolean shouldBroadcast = true;

		if (!playerLastFoundTicks.containsKey(playerName)) {
			playerLastFoundTicks.put(playerName, new HashMap<String, Integer>());
		}
		else {
			HashMap<String, Integer> lastFound = playerLastFoundTicks.get(playerName);

			if (lastFound.containsKey(oreName)) {
				int lastFoundTicks = lastFound.get(oreName);

				if (playerTickCount-lastFoundTicks <= ConfigHandler.tickDelayBetweenSameOreBroastcasts) {
					shouldBroadcast = false;
				}
			}
		}

		if (shouldBroadcast) {
			String message = ConfigHandler.messageFormat.replace("%player%", playerName).replace("%ore%", oreName);

			ChatFormatting messageColour = ChatFormatting.getById(ConfigHandler.messageColourIndex);
			if (messageColour == null) {
				Constants.logger.warn("[" + Reference.NAME + "] Unable to find text formatting colour with index '" + ConfigHandler.messageColourIndex + "'.");
				return;
			}

			StringFunctions.broadcastMessage(level, message, messageColour);
		}

		playerLastFoundTicks.get(playerName).put(oreName, playerTickCount);
	}
}
