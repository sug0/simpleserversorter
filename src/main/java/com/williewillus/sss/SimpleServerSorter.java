package com.williewillus.sss;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;

public class SimpleServerSorter implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerStartCallback.EVENT.register(srv -> SortCommand.register(srv.getCommandManager().getDispatcher()));
	}
}
