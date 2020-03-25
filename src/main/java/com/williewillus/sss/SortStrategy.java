package com.williewillus.sss;

import com.google.common.collect.ComparisonChain;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.util.Comparator;

public enum SortStrategy {
	BY_ID((s1, s2) -> {
		return Registry.ITEM.getId(s1.getItem()).compareTo(Registry.ITEM.getId(s2.getItem()));
	}),
	BY_TYPE((s1, s2) -> {
		String g1 = s1.getItem().getGroup() == null ? "" : s1.getItem().getGroup().getName();
		String g2 = s2.getItem().getGroup() == null ? "" : s2.getItem().getGroup().getName();
		return ComparisonChain.start().compare(g1, g2).compare(s1, s2, BY_ID.comparator).result();
	});

	public final Comparator<ItemStack> comparator;

	SortStrategy(Comparator<ItemStack> comparator) {
		this.comparator = comparator;
	}
}
