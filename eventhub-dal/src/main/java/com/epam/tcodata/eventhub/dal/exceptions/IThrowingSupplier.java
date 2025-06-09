package com.epam.tcodata.eventhub.dal.exceptions;

/**
 * Represents a supplier of results that can throw checked exception.
 *
 * <p>This is {@link FunctionalInterface} whose functional method is {@link #get()}.</p>
 * <p>
 * Remarks: lambda realizing interface can have actual parameters captured from parent scope as closure.
 * <code>
 * T someVar1;
 * IThrowingSupplier&lt;TSomeResult, IOException&gt; lambda = () -> return callSomebody(someVar1);
 * </code>
 * </p>
 *
 * @param <T> the type of result
 * @param <E> the type of checked exception that could be thrown
 */
@FunctionalInterface
public interface IThrowingSupplier<T, E extends Exception> {
    /**
     * Gets a result.
     *
     * @return a result
     * @throws E any checked exception
     */
    T get() throws E;
}
