package com.williewillus.sss;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;

public class SimpleServerSorter implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistry.INSTANCE.register(false, SortCommand::register);
	}
}
