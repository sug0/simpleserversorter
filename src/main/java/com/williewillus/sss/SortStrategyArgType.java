package com.williewillus.sss;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SortStrategyArgType implements ArgumentType<SortStrategy> {
	private static final List<String> STRING_NAMES = Collections.unmodifiableList(
					Arrays.stream(SortStrategy.values())
									.map(s -> s.name().toLowerCase(Locale.ROOT))
									.collect(Collectors.toList()));

	public static SortStrategy get(String name, CommandContext<ServerCommandSource> ctx) {
		return ctx.getArgument(name, SortStrategy.class);
	}

	@Override
	public SortStrategy parse(StringReader reader) throws CommandSyntaxException {
		String next = reader.readUnquotedString();
		if (!STRING_NAMES.contains(next)) {
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
		}
		return SortStrategy.valueOf(next.toUpperCase(Locale.ROOT));
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		for (String name : STRING_NAMES) {
			if (name.startsWith(builder.getRemaining().toLowerCase())) {
				builder.suggest(name);
			}
		}
		return builder.buildFuture();
	}

	@Override
	public Collection<String> getExamples() {
		return STRING_NAMES;
	}
}
