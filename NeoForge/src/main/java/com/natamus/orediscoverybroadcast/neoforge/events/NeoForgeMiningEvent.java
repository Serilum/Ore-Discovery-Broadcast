package com.natamus.orediscoverybroadcast.neoforge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.orediscoverybroadcast.events.MiningEvent;
import com.natamus.orediscoverybroadcast.util.Util;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

@EventBusSubscriber
public class NeoForgeMiningEvent {
    @SubscribeEvent
    public static void onWorldLoad(LevelEvent.Load e) {
        Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
        if (level == null) {
            return;
        }

		Util.attemptBlacklistProcessing(level);
    }

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		MiningEvent.onBlockBreak(level, e.getPlayer(), e.getPos(), e.getState(), null);
	}
}
