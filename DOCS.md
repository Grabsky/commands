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
public enum TellCommand extends RootCommand {
    /* SINGLETON */ INSTANCE;

    public TellCommand() {
        super("tell", null, "example.command.tell", "/tell <player> <message>", "Sends private message to specified player.");
    }

    @Override
    public CompletionsProvider onTabComplete(final RootCommandContext context, final int index) {
        return (index == 0) ? CompletionsProvider.of(Player.class) : CompletionsProvider.EMPTY;
    }

    @Override
    public void onCommand(final RootCommandContext context, final ArgumentQueue queue) throws CommandLogicException {
        // getting command executor as Player
        final Player sender = context.getExecutor().asPlayer();
        // getting arguments
        final Player target = queue.next(Player.class).asRequired();
        final Component message = queue.next(Component.class, ComponentArgument.GREEDY).asRequired();
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

// EXAMPLE 2 - for enums
handler.registerCommand(ExampleCommand.INSTANCE);

// EXAMPLE 3 - for enums or classes with zero-arg constructors
handler.registerCommand(ExampleCommand.class);
```
<br />

## Arguments
Built-in arguments. All of them extend `ArgumentParser<T>` and some of them `CompletionsProvider`:
```
┌─ StringArgument
│   ├─ StringArgument.LITERAL ───── (String) ──────── [default]
│   └─ StringArgument.GREEDY ────── (String) ──────── [optional]
├─ BooleanArgument ──────────────── (Boolean) ─────── [default]
├─ IntegerArgument ──────────────── (Integer) ─────── [default]
├─ FloatArgument ────────────────── (Float) ───────── [default]
├─ DoubleArgument ───────────────── (Double) ──────── [default]
├─ ComponentArgument
│   ├─ ComponentArgument.LITERAL ── (Component) ───── [default]
│   └─ ComponentArgument.GREEDY ─── (Component) ───── [optional]
├─ EnchantmentArgument ──────────── (Enchantment) ─── [default]
├─ EntityTypeArgument ───────────── (EntityType) ──── [default]
├─ MaterialArgument ─────────────── (Material) ────── [default]
├─ PlayerArgument ───────────────── (Player) ──────── [default]
└─ PositionArgument ─────────────── (Position) ────── [default]
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
```
┌─ CommandLogicException
│   ├─ ArgumentParseException
│   │   ├─ BooleanParseException ──────── (BooleanArgument)
│   │   ├─ EnchantmentParseException ──── (EnchantmentArgument)
│   │   ├─ MaterialParseException ─────── (MaterialArgument)
│   │   ├─ NamespacedKeyParseException ── (NamespacedKeyArgument)
│   │   ├─ NumberParseException
│   │   │   ├─ IntegerParseException ──── (IntegerArgument)
│   │   │   ├─ DoubleParseException ───── (DoubleArgument)
│   │   │   └─ FloatParseException ────── (FloatArgument)
│   │   ├─ PlayerParseException ───────── (PlayerArgument)
│   │   └─ PositionParseException ─────── (PositionArgument)
│   ├─ CommandConditionException
│   ├─ IncompatibleParserException
│   ├─ IncompatibleSenderException
└─  └─ MissingInputException
```
Overriding default exception handlers:
```java
handler.setExceptionHandler(BooleanParseException.class, (exc, context) -> {
    context.getExecutor().asCommandSender().sendMessage("Invalid input for boolean argument: " + exc.getInputValue());
});
```

<br />

## Completions
Handling command completions:
```java
// Example for /give <player> <material> <amount>
//                   ^^^^^^^^ ^^^^^^^^^^ ^^^^^^^^
// INDEX:            00000000 1111111111 22222222
@Override
public CompletionsProvider onTabComplete(final RootCommandContext context, final int index) {
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
private static final List<String> BOOLEANS = List.of("true", "false");
handler.setCompletionsProvider(Boolean.class, BOOLEANS);

// EXAMPLE 2
handler.setCompletionsProvider(Boolean.class, CompletionsProvider.of("true", "false"));
        
// EXAMPLE 3
handler.setCompletionsProviedr(OfflinePlayer.class, (context) -> {
    return Bukkit.getOfflinePlayers().stream().map(OfflinePlayer::getUniqueId).toList();
});
```
