# Documentation - 1.X
Documentation page is still in progress.

## Overview
1. **[Commands](#commands)** - learn how to create commands.
2. **[Arguments](#arguments)** - learn about built-in argument types and how to use them.
3. **[Exceptions](#exceptions)** - learn about built-in exceptions and how to handle them.
4. **[Completions](#completions)** - learn about command completions and how to use them.

<br />

## Commands
Defining a simple `/tell <player> <message>` command:
```java
public final class TellCommand extends RootCommand {

    public TellCommand() {
        super("tell", List.of("w", "pm"), "example.command.tell", "/tell <player> <message>", "Sends private message to specified player.");
        //    name    aliases             permission              usage                       description
    }

    @Override
    public CompletionsProvider onTabComplete(final RootCommandContext context, final int index) throws CommandLogicException {
        return (index == 0) ? CompletionsProvider.of(Player.class) : CompletionsProvider.EMPTY;
    }

    @Override
    public void onCommand(final RootCommandContext context, final ArgumentQueue queue) throws CommandLogicException {
        // getting command executor as Player
        final Player sender = context.getExecutor().asPlayer();
        // getting arguments
        final Player target = queue.next(Player.class).asRequired();
        final String message = queue.next(String.class, StringArgument.GREEDY).asRequired();
        // sending messages
        sender.sendMessage("[You -> " + target.getName() + "]: " + message);
        target.sendMessage("[" + sender.getName() + " -> You]: " + message);
    }

}
```

Registering a command:
```java
// EXAMPLE 1 - Regular classes.
handler.registerCommand(new ExampleCommand(plugin));

// EXAMPLE 2 - Zero-arg-constructor classes can be optionally registered like that.
handler.registerCommand(ExampleCommand.class);
```
<br />

## Arguments
Built-in arguments. All of them implement `ArgumentParser<T>` and some of them `CompletionsProvider`:
```scala
┌─ cloud.grabsky.commands.argument
│   ├─ StringArgument
│   │   ├─ StringArgument.LITERAL ──────── (String) (default)
│   │   └─ StringArgument.GREEDY ───────── (String)
│   ├─ ComponentArgument
│   │   ├─ ComponentArgument.LITERAL ───── (Component) (default)
│   │   └─ ComponentArgument.GREEDY ────── (Component)
│   ├─ ShortArgument
│   │   └─ ShortArgument.DEFAULT_RANGE ─── (Short) (default)
│   │   └─ ShortArgument.ofRange(...) ──── (Short)
│   ├─ IntegerArgument
│   │   └─ IntegerArgument.DEFAULT_RANGE ─ (Integer) (default)
│   │   └─ IntegerArgument.ofRange(...) ── (Integer)
│   ├─ LongArgument
│   │   └─ LongArgument.DEFAULT_RANGE ──── (Long) (default)
│   │   └─ LongArgument.ofRange(...) ───── (Long)
│   ├─ FloatArgument
│   │   └─ FloatArgument.DEFAULT_RANGE ─── (Float) (default)
│   │   └─ FloatArgument.ofRange(...) ──── (Float)
│   ├─ DoubleArgument
│   │   └─ DoubleArgument.DEFAULT_RANGE ── (Double) (default)
│   │   └─ DoubleArgument.ofRange(...) ─── (Double)
│   ├─ BooleanArgument ─────────────────── (Boolean)
│   ├─ EnchantmentArgument ─────────────── (Enchantment)
│   ├─ EntityTypeArgument ──────────────── (EntityType)
│   ├─ MaterialArgument ────────────────── (Material)
│   ├─ OfflinePlayerArgument ───────────── (OfflinePlayer)
│   ├─ PlayerArgument ──────────────────── (Player)
│   ├─ PositionArgument ────────────────── (Position)
│   ├─ UUIDArgument ────────────────────── (UUID)
└─  └─ WorldArgument ───────────────────── (World)
```

Using non-default argument parsers:
```java
@Override
// Example command: /edit-translations (property) (value)
public void onCommand(final RootCommandContext context, final ArgumentQueue arguments) {
    // Getting command executor and trying to cast it to a Player.
    final CommandSender sender = context.getExecutor().asCommandSender();
    // Parsing NEXT command argument to (LITERAL) String.
    final String property = arguments.next(String.class).asRequired();
    final String value = arguments.next(String.class, StringArgument.GREEDY).asRequired();
    // Doing some command logic... following line is just an example.
    translations.set(property, value);
    // Sending confirmation message to the command executor.
    sender.sendMessage("Property '" + property + "' has now value of '" + value + "'");
}
```

<br />

## Exceptions
Built-in exceptions:
```scala│
┌─ CommandLogicException
│   ├─ ArgumentParseException
│   │   ├─ StringArgument.Exception
│   │   ├─ ComponentArgument.Exception
│   │   ├─ ShortArgument
│   │   │   └─ ShortArgument.ParseException
│   │   │   └─ ShortArgument.RangeException
│   │   ├─ IntegerArgument
│   │   │   └─ IntegerArgument.ParseException
│   │   │   └─ IntegerArgument.RangeException
│   │   ├─ LongArgument
│   │   │   └─ LongArgument.ParseException
│   │   │   └─ LongArgument.RangeException
│   │   ├─ FloatArgument
│   │   │   └─ FloatArgument.ParseException
│   │   │   └─ FloatArgument.RangeException
│   │   ├─ DoubleArgument
│   │   │   └─ DoubleArgument.ParseException
│   │   │   └─ DoubleArgument.RangeException
│   │   ├─ BooleanArgument.Exception
│   │   ├─ EnchantmentArgument.Exception
│   │   ├─ EntityTypeArgument.Exception
│   │   ├─ MaterialArgument.Exception
│   │   ├─ OfflinePlayerArgument.Exception
│   │   ├─ PlayerArgument.Exception
│   │   ├─ PositionArgument.Exception
│   │   ├─ UUIDArgument.Exception
│   │   └─ WorldArgument.Exception
│   ├─ CommandConditionException
│   ├─ IncompatibleParserException
│   ├─ IncompatibleSenderException
└─  └─ MissingInputException
```

By default, all exceptions send non user-friendly errors. Here's how to override them:
```java
handler.setExceptionHandler(BooleanParseException.class, (exc, context) -> {
    context.getExecutor().asCommandSender().sendMessage("Invalid input for boolean argument: " + exc.getInputValue());
});
```

<br />

## Completions
Defining completions for simple `/give <player> <material> <amount>` command:
```java
//   CMD: /give <player> <material> <amount>
// INDEX:       00000000 1111111111 22222222
@Override
public @Nullable CompletionsProvider onTabComplete(final RootCommandContext context, final int index) {
    return switch (index) {
        case 0 -> CompletionsProvider.of(Player.class);
        case 1 -> CompletionsProvider.of(Material.class);
        case 2 -> CompletionsProvider.of("1", "16", "32", "64");
        default -> CompletionsProvider.EMPTY;
    };
}
```

Overriding default completions providers:
```java
// EXAMPLE 1
handler.setCompletionsProvider(Boolean.class, CompletionsProvider.of("true", "false"));

// EXAMPLE 2
handler.setCompletionsProvider(OfflinePlayer.class, (context) -> {
    return Bukkit.getOfflinePlayers().stream().map(OfflinePlayer::getUniqueId).toList();
});
```
