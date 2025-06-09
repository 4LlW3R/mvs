package com.epam.tcodata.eventhub.dal.exceptions;

import com.microsoft.azure.eventhubs.EventHubException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Utility class to work with exception handling.
 *
 * <p></p>EventHubException - if Service Bus service encountered problems during the operation
 * ExecutionException - if underlying wrapped future completed exceptionally;
 * see {@link java.util.concurrent.CompletableFuture} for details.
 * InterruptedException - if the current thread was interrupted while waiting;
 * see {@link java.util.concurrent.CompletableFuture} for details.
 * IOException - if the underlying Proton-J layer encounter network errors.
 */
public final class ExceptionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionUtil.class);
    private ExceptionUtil() {
        /***  Default implementation ***/
    }

    /**
     * Wraps given action {@link IThrowingAction} with regular exception handler transforming EventHub-specific
     * checked exceptions to unified more general {@link RuntimeEventHubException}.
     *
     * @param action lambda with dangerous action
     */
    public static void wrapEventhubExceptions(IThrowingAction<Exception> action) {
        try {
            action.invoke();
        } catch (EventHubException ex) {
            LOGGER.error(ex.getMessage());
            throw new RuntimeEventHubException(ex);
        } catch (ExecutionException ex) {
            LOGGER.error(ex.getMessage());
            throw new IllegalStateException(ex.getCause().getMessage());
        } catch (InterruptedException ex) {
            LOGGER.error(ex.getMessage());
            Thread.currentThread().interrupt();
            throw new RuntimeEventHubException(ex);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw new RuntimeEventHubException(ex);
        } catch (Exception e) {
            ExceptionUtil.<RuntimeEventHubException>throwAs(e);
        }
    }

    /**
     * Wraps given supplier function lambda {@link IThrowingSupplier} with regular exception handler transforming
     * EventHub-specific checked exceptions to unified more general {@link RuntimeEventHubException}.
     *
     * @param supplier lambda with dangerous action
     */
    public static <T> T wrapEventhubExceptions(IThrowingSupplier<T, Exception> supplier) {
        try {
            return supplier.get();
        } catch (InterruptedException ex) {
            LOGGER.error(ex.getMessage());
            Thread.currentThread().interrupt();
        } catch (EventHubException | ExecutionException | IOException ex) {
            LOGGER.error(ex.getMessage());
            throw new RuntimeEventHubException(ex);
        } catch (Exception e) {
            ExceptionUtil.<RuntimeEventHubException>throwAs(e);
            return null; //actually this code line is unreachable
        }
        return null;
    }

    /**
     * Dirty magic using JVM type erasure side effect:
     * provides the way to hide the fact that given checked exception is rethrown "as is" from compiler in order
     * to remove messy exception signatures from call hierarchy.
     *
     * @param e   given checked exception to be hided from compiler
     * @param <T> the type of any NON checked exception to hide real one
     * @throws T NON checked exception hiding real checked one from call hierarchy
     */
    private static <T extends Throwable> void throwAs(Throwable e) throws T {
        throw (T) e;
    }
}
