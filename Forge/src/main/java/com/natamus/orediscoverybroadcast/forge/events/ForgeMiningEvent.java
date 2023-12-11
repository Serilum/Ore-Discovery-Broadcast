package com.natamus.orediscoverybroadcast.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.orediscoverybroadcast.events.MiningEvent;
import com.natamus.orediscoverybroadcast.util.Util;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeMiningEvent {
    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load e) {
        Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
        if (level == null) {
            return;
        }

		Util.attemptBlacklistProcessing(level);
    }

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		MiningEvent.onBlockBreak(level, e.getPlayer(), e.getPos(), e.getState(), null);
	}
}
