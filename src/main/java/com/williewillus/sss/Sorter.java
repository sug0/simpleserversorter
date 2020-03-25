package com.williewillus.sss;

import net.minecraft.container.Container;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Sorter {
	public static void sort(Inventory inventory, SortStrategy strategy) {
		List<ItemStack> tmp = new ArrayList<>(inventory.getInvSize());
		for (int i = 0; i < inventory.getInvSize(); i++) {
			tmp.add(inventory.getInvStack(i));
		}

		tmp.sort(strategy.comparator);
		compact(tmp);

		int i = 0;
		for (; i < tmp.size(); i++) {
			inventory.setInvStack(i, tmp.get(i));
		}

		while (i < inventory.getInvSize()) {
			inventory.setInvStack(i, ItemStack.EMPTY);
			i++;
		}
	}

	private static void compact(List<ItemStack> list) {
		for (int i = 0; i < list.size(); i++) {
			ItemStack left = list.get(i);
			for (int j = i + 1; j < list.size(); j++) {
				ItemStack right = list.get(j);
				if (Container.canStacksCombine(left, right)) {
					int amount = Math.min(left.getItem().getMaxCount() - left.getCount(), right.getCount());
					left.increment(amount);
					right.decrement(amount);
				}
			}
		}

		list.removeIf(ItemStack::isEmpty);
	}
}
