package com.williewillus.sss;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

/**
 * Represents a sub-view of the wrapped Inventory in slot range [start, end)
 */
public class InventorySlice implements Inventory {
	private final Inventory delegate;
	private final int start;
	private final int len;

	public InventorySlice(Inventory delegate, int start, int len) {
		this.delegate = delegate;
		this.start = start;
		this.len = len;

		if (len < 0) {
			throw new IllegalArgumentException("Negative len");
		}

		if (start < 0 || start >= delegate.getInvSize()) {
			throw new IllegalArgumentException("Start out of bounds: " + start);
		}

		int end = start + len - 1;
		if (end < 0 || end >= delegate.getInvSize()) {
			throw new IllegalArgumentException("End out of bounds: " + end);
		}

	}

	@Override
	public int getInvSize() {
		return len;
	}

	@Override
	public boolean isInvEmpty() {
		for (int i = start; i < start + len; i++) {
			if (!getInvStack(i).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	private int shift(int slot) {
		if (slot < 0 || slot >= getInvSize()) {
			throw new IndexOutOfBoundsException(String.format("%d out of bounds [%d, %d)", slot, 0, getInvSize()));
		}
		return start + slot;
	}

	@Override
	public ItemStack getInvStack(int slot) {
		return delegate.getInvStack(shift(slot));
	}

	@Override
	public ItemStack takeInvStack(int slot, int amount) {
		return delegate.takeInvStack(shift(slot), amount);
	}

	@Override
	public ItemStack removeInvStack(int slot) {
		return delegate.removeInvStack(shift(slot));
	}

	@Override
	public void setInvStack(int slot, ItemStack stack) {
		delegate.setInvStack(shift(slot), stack);

	}

	@Override
	public void markDirty() {
		delegate.markDirty();
	}

	@Override
	public boolean canPlayerUseInv(PlayerEntity player) {
		return delegate.canPlayerUseInv(player);
	}

	@Override
	public void clear() {
		for (int i = start; i < start + len; i++) {
			delegate.setInvStack(i, ItemStack.EMPTY);
		}
	}
}
