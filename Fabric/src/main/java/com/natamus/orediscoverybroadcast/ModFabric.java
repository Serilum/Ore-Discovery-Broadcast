package com.natamus.orediscoverybroadcast;

import com.natamus.collective.check.RegisterMod;
import com.natamus.orediscoverybroadcast.events.MiningEvent;
import com.natamus.orediscoverybroadcast.util.Reference;
import com.natamus.orediscoverybroadcast.util.Util;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class ModFabric implements ModInitializer {
	
	@Override
	public void onInitialize() {
		setGlobalConstants();
		ModCommon.init();

		loadEvents();

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadEvents() {
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel level) -> {
			Util.attemptBlacklistProcessing(level);
		});

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			MiningEvent.onBlockBreak(world, player, pos, state, entity);
		});
	}

	private static void setGlobalConstants() {

	}
}
