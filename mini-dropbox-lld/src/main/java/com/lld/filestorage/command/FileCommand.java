package com.lld.filestorage.command;

/**
 * Command Pattern — command abstraction.
 *
 * SRP  : Each command encapsulates exactly one operation and its parameters.
 * OCP  : New operations (MoveCommand, RenameCommand) are added as new classes.
 * DIP  : The invoker (service) depends on this interface, not concrete commands.
 *
 * Trade-off: execute() returns Object for flexibility in an interview setting.
 * In production, use generics: Command<T> with T execute().
 */
public interface FileCommand {
    /**
     * Executes the command.
     * @return operation result (FileVersion for upload, byte[] for download, etc.)
     */
    Object execute();
}
