package com.serilum.orediscoverybroadcast;

import com.natamus.collective.config.DuskConfigCustomFile;
import com.serilum.orediscoverybroadcast.config.ConfigHandler;
import com.serilum.orediscoverybroadcast.util.Reference;
import com.serilum.orediscoverybroadcast.util.Util;

public class ModCommon {

	public static void init() {
		ConfigHandler.initConfig();
		load();
	}

	private static void load() {
		Util.moveBlacklistFileIntoConfig();

		DuskConfigCustomFile.addIdColours(Reference.MOD_ID, "colourmap.txt", "Ore Colours", "The colour of the broadcast message per ore.", "block", "ores", Util::readColourMap, Util::writeColourMap);
	}
}
