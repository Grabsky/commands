# Documentation
Documentation page is still in progress and may not be up-to-date.

## Overview
1. **[Commands](#commands)** - learn how to create commands.
2. **[Arguments](#arguments)** - learn about built-in argument types and how to use them.
3. **[Exceptions](#exceptions)** - learn about built-in exceptions and how to handle them.
4. **[Completions](#completions)** - learn about command completions and how to use them.

<br />

## Commands
Simple `/tell (player) (message)` command can be defined and registered like that:

```java
// MAIN CLASS
public final class MainClass extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // Creating a new instance of RootCommandManager.
        final RootCommandManager commands = new RootCommandManager(this);
        // Registering our command. (defined below)
        commands.register(TellCommand.class);
    }
    
}

// COMMAND CLASS
@Command(name = "tell", aliases = {"w", "pm"}, permission = "example.command.tell", usage = "/tell <player> <message>", description = "Sends private message to specified player.")
public final class TellCommand extends RootCommand {

    @Override
    public CompletionsProvider onTabComplete(final RootCommandContext context, final int index) throws CommandLogicException {
        return (index == 0) ? CompletionsProvider.of(Player.class) : CompletionsProvider.EMPTY;
    }

    @Override
    public void onCommand(final RootCommandContext context, final ArgumentQueue queue) throws CommandLogicException {
        // Getting command executor (sender) as Player.
        final Player sender = context.getExecutor().asPlayer();
        // Getting first argument (target) as Player.
        final Player target = queue.next(Player.class).asRequired();
        // Getting the rest argument (message) as greedy String.
        final String message = queue.next(String.class, StringArgument.GREEDY).asRequired();
        // Sending messages.
        sender.sendMessage("[You -> " + target.getName() + "]: " + message);
        target.sendMessage("[" + sender.getName() + " -> You]: " + message);
    }

}
```

<br />

## Arguments
Built-in arguments. All of them implement `ArgumentParser<T>` and some of them `CompletionsProvider`:
```python
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
public void onCommand(final @NotNull RootCommandContext context, final @NotNull ArgumentQueue arguments) {
    // Getting next argument (proprty) as String. 
    final String property = arguments.next(String.class).asRequired();
    // Getting next argument (value) as String. 
    final String value = arguments.next(String.class).asRequired();
    // Doing something with the arguments... following line is just an example.
    translations.set(property, value);
    // RootCommandExecutor implements Audience and can receive messages without un-wrapping.
    context.getExecutor().sendMessage("Property '" + property + "' has now value of '" + value + "'");
}
```

<br />

## Exceptions
Built-in exceptions:
```python
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
handler.setExceptionHandler(BooleanArgument.Exception.class, (e, context) -> {
    context.getExecutor().sendMessage("Invalid input for boolean argument: " + e.getInputValue());
});
```

<br />

## Completions
Defining completions for simple `/give <player> <material> <amount>` command:

```java
@Command(...)
public final class GiveCommand extends RootCommand {
    
    @Override
    public @Nullable CompletionsProvider onTabComplete(final @NotNull RootCommandContext context, final int index) {
        return switch (index) {
            case 0 -> CompletionsProvider.of(Player.class);
            case 1 -> CompletionsProvider.of(Material.class);
            case 2 -> CompletionsProvider.of("1", "16", "32", "64");
            default -> CompletionsProvider.EMPTY;
        };
    }
    
    @Override
    public void onCommand(...) {
        ...
    }
    
}
```

Default completion providers can be easily overriden:
```java
// EXAMPLE #1
handler.setCompletionsProvider(Boolean.class, CompletionsProvider.of("true", "false"));

// EXAMPLE #2
handler.setCompletionsProvider(OfflinePlayer.class, (context) -> {
    return Bukkit.getOfflinePlayers().stream().map(OfflinePlayer::getUniqueId).toList();
});
```