# Documentation
This documentation page is still in progress.

### Types
```
StringArgument              (String)                        [default = true]
BooleanArgument             (Boolean)                       [default = true]
IntegerArgument             (Integer)                       [default = true]
FloatArgument               (Float)                         [default = true]
DoubleArgument              (Double)                        [default = true]
   
ComponentArgument           (Component)                     [default = true]
EnchantmentArgument         (Enchantment)                   [default = true]
EntityTypeArgument          (EntityType)                    [default = true]
MaterialArgument            (Material)                      [default = true]
PlayerArgument              (Player)                        [default = true]
PositionArgument            (Position)                      [default = true]

GreedyStringArgument        (String)                        [default = false]
GreedyComponentArgument     (Component)                     [default = false]
```
Using non-default argument parsers:
```java
public void onCommand(final RootCommandContext context, final ArgumentQueue arguments) {
    // getting raw (base) sender instance
    final CommandSender sender = context.getExecutor().raw();
    // getting next parameter as "greedy" string by specifying argument parser
    final String longText = arguments.next(String.class, GreedyStringArgument.class).asRequired();
    // displaying text to the sender
    sender.sendMessage(longText);
}
```

<br />

### Exceptions
```
CommandLogicException
├── ArgumentParseException
│   ├── BooleanParseException      (BooleanArgument)
│   ├── EnchantmentParseException  (EnchantmentArgument)
│   ├── MaterialParseException     (MaterialArgument)
│   ├── NumberParseException
│   │   ├── IntegerParseException  (IntegerArgument)
│   │   ├── DoubleParseException   (DoubleArgument)
│   │   └── FloatParseException    (FloatArgument)
│   ├── PlayerParseException       (PlayerArgument)
│   └── PositionParseException     (PositionArgument)
├── CommandConditionException
├── IncompatibleParserException
├── IncompatibleSenderException
└── MissingInputException
```
Overriding default exception handlers:
> **NOTE:** `Argument#asRequired(Component)` takes priority over default exception handler.
```java
handler.setExceptionHandler(BooleanParseException.class, (exc, context) -> {
    context.getExecutor().raw().sendMessage("Invalid input for boolean argument: " + exc.getInputValue());
});
```
