package com.williewillus.sss;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.Locale;

public class SortCommand {
	public static void register(CommandDispatcher<ServerCommandSource> disp) {
		LiteralArgumentBuilder<ServerCommandSource> selfRoot = CommandManager.literal("sortSelf");
		LiteralArgumentBuilder<ServerCommandSource> targetRoot = CommandManager.literal("sortTargeted");

		for (SortStrategy s : SortStrategy.values()) {
			selfRoot.then(CommandManager.literal(s.name().toLowerCase(Locale.ROOT)).executes(ctx -> sortSelf(ctx, s)));
			targetRoot.then(CommandManager.literal(s.name().toLowerCase(Locale.ROOT)).executes(ctx -> sortTargeted(ctx, s)));
		}

		CommandNode<ServerCommandSource> selfNode = disp.register(selfRoot);
		CommandNode<ServerCommandSource> targetNode = disp.register(targetRoot);
		disp.register(CommandManager.literal("ss").redirect(selfNode));
		disp.register(CommandManager.literal("st").redirect(targetNode));
	}

	private static int sortSelf(CommandContext<ServerCommandSource> ctx, SortStrategy strategy) throws CommandSyntaxException {
		ServerPlayerEntity player = ctx.getSource().getPlayer();
		Inventory inv = new InventorySlice(player.inventory, 9, 27);
		Sorter.sort(inv, strategy);
		player.playerContainer.sendContentUpdates();
		return Command.SINGLE_SUCCESS;
	}

	private static int sortTargeted(CommandContext<ServerCommandSource> ctx, SortStrategy strategy) throws CommandSyntaxException {
		ServerPlayerEntity player = ctx.getSource().getPlayer();
		HitResult hit = player.rayTrace(5, 0, false);
		if (hit.getType() == HitResult.Type.BLOCK) {
			BlockPos pos = ((BlockHitResult) hit).getBlockPos();
			BlockState state = player.getServerWorld().getBlockState(pos);

			Inventory inv = null;
			if (state.getBlock() instanceof ChestBlock) {
				inv = ChestBlock.getInventory((ChestBlock) state.getBlock(), state, player.getServerWorld(), pos, true);
			} else {
				BlockEntity be = player.getServerWorld().getBlockEntity(pos);
				if (be instanceof Inventory) {
					inv = (Inventory) be;
				}
			}

			if (inv != null) {
				Sorter.sort(inv, strategy);
				return Command.SINGLE_SUCCESS;
			}
		}
		return 0;
	}
}
