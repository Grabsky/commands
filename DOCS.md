# Documentation
Documentation page is still in progress.

## Overview
1. **[Commands](#commands)** - learn how to create commands.
2. **[Arguments](#arguments)** - learn about built-in argument types and how to use them.
3. **[Exceptions](#exceptions)** - learn about built-in exceptions and how to handle them.
4. **[Completions](#completions)** - learn about command completions and how to use them.

## Commands
Defining a simple `/tell <player> <message>` command:
```java
public final class TellCommand extends RootCommand {

    public TellCommand() {
        super("tell", { "w", "pm" }, "example.command.tell", "/tell <player> <message>", "Sends private message to specified player.");
        //    name    aliases        permission              usage                       description
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
// EXAMPLE 1 - for regular classes
handler.registerCommand(new ExampleCommand(plugin));

// EXAMPLE 3 - for classes with zero-arg constructors
handler.registerCommand(ExampleCommand.class);
```
<br />

## Arguments
Built-in arguments. All of them implement `ArgumentParser<T>` and some of them `CompletionsProvider`:
```scala
┌─ cloud.grabsky.commands.argument
│   ├─ StringArgument
│   │   ├─ StringArgument.LITERAL ────── (String) (default)
│   │   │   └─ eg. "string"
│   │   │
│   │   └─ StringArgument.GREEDY ─────── (String)
│   │       └─ eg. "string with spaces"
│   │
│   ├─ BooleanArgument ───────────────── (Boolean) (default)
│   │   └─ either "true" or "false"
│   │
│   ├─ IntegerArgument ───────────────── (Integer) (default)
│   │   └─ eg. "1" or "-27"
│   │
│   ├─ FloatArgument ─────────────────── (Float) (default)
│   │   └─ eg. "1.5" or "-27.777"
│   │
│   ├─ DoubleArgument ────────────────── (Double) (default)
│   │   └─ eg. "1.000000005" or "-27.7"
│   │
│   ├─ ComponentArgument
│   │   ├─ ComponentArgument.LITERAL ─── (Component) (default)
│   │   │   └─ eg. "<green>green_string"
│   │   │
│   │   └─ ComponentArgument.GREEDY ──── (Component)
│   │       └─ eg. "<rainbow>rainbow string with spaces"
│   │
│   ├─ EnchantmentArgument ───────────── (Enchantment) (default)
│   │   └─ eg. "minecraft:sharpness"
│   │
│   ├─ EntityTypeArgument ────────────── (EntityType) (default)
│   │   └─ eg. "minecraft:creeper"
│   │
│   ├─ MaterialArgument ──────────────── (Material) (default)
│   │   └─ eg. "minecraft:diamond"
│   │
│   ├─ PlayerArgument ────────────────── (Player) (default)
│   │   └─ eg. "@s" or "PlayerName"
│   │
│   ├─ PositionArgument ──────────────── (Position) (default)
│   │   └─ eg. "0.0 64.0 0.0"
│   │
│   └─ WorldArgument ─────────────────── (World) (default)
└─      └─ eg. "minecraft:the_end"
```

Using non-default argument parsers:
```java
@Override
public void onCommand(final RootCommandContext context, final ArgumentQueue arguments) {
    // getting command executor as CommandSender
    final CommandSender sender = context.getExecutor().asCommandSender();
    // getting next parameter as "greedy" string by specifying argument parser
    final String longText = arguments.next(String.class, StringArgument.GREEDY).asRequired();
    // displaying text to the sender
    sender.sendMessage(longText);
}
```

<br />

## Exceptions
Built-in exceptions:
```scala
┌─ CommandLogicException
│   ├─ ArgumentParseException
│   │   ├─ BooleanParseException ───────── (BooleanArgument)
│   │   ├─ EnchantmentParseException ───── (EnchantmentArgument)
│   │   ├─ MaterialParseException ──────── (MaterialArgument)
│   │   ├─ NamespacedKeyParseException ─── (NamespacedKeyArgument)
│   │   ├─ NumberParseException
│   │   │   ├─ IntegerParseException ───── (IntegerArgument)
│   │   │   ├─ DoubleParseException ────── (DoubleArgument)
│   │   │   └─ FloatParseException ─────── (FloatArgument)
│   │   ├─ PlayerParseException ────────── (PlayerArgument)
│   │   ├─ PositionParseException ──────── (PositionArgument)
│   │   └─ WorldParseException ─────────── (WorldArugment)
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
