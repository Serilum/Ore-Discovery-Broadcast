package com.natamus.orediscoverybroadcast.neoforge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.orediscoverybroadcast.events.MiningEvent;
import com.natamus.orediscoverybroadcast.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

		Util.attemptConfigProcessing(level);
    }

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		MiningEvent.onBlockBreak(level, e.getPlayer(), e.getPos(), e.getState(), null);
	}

	@SubscribeEvent
	public static void onEntityBlockPlace(BlockEvent.EntityPlaceEvent e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		Entity entity = e.getEntity();
		if (!(entity instanceof LivingEntity)) {
			return;
		}

		MiningEvent.onEntityBlockPlace(level, e.getPos(), e.getPlacedBlock(), (LivingEntity)entity, null);
	}
}
