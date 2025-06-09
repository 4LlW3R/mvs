package com.epam.tcodata.eventhub.dal.exceptions;

import java.util.Objects;

/**
 * Exception thrown when operation with repository has failed. This exception can be
 * inspected using the {@link #getCause()} method.
 */
public class RuntimeEventHubException extends RuntimeException {
    private static final long serialVersionUID = -8067923833623788139L;

    /**
     * Constructs an {@code RuntimeEventHubException} with no detail message.
     * The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable) initCause}.
     */
    protected RuntimeEventHubException() {
    }

    /**
     * Constructs an {@code ExecutionException} with the specified detail
     * message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause(Throwable) initCause}.
     *
     * @param message the detail message
     */
    public RuntimeEventHubException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code RuntimeEventHubException} with the specified detail
     * message and cause.
     *
     * @param message the detail message
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method)
     */
    public RuntimeEventHubException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@code RuntimeEventHubException} with the specified cause.
     * The detail message is set to {@code (cause == null ? null :
     * cause.toString())} (which typically contains the class and
     * detail message of {@code cause}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method)
     */
    public RuntimeEventHubException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates wrapped unchecked {@link RuntimeEventHubException} exception instance from given checked {@link Exception}
     * exception instance.
     *
     * @param toBeWrapped given checked exception to be wrapped
     * @return {@link RuntimeEventHubException}
     */
    public static RuntimeEventHubException createWrappedException(Exception toBeWrapped) {
        Objects.requireNonNull(toBeWrapped);

        RuntimeEventHubException wrappedException = new RuntimeEventHubException("Unexpected fault!", toBeWrapped);
        wrappedException.addSuppressed(toBeWrapped);
        return wrappedException;
    }
}
