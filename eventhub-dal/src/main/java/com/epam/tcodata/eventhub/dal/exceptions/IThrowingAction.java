package com.epam.tcodata.eventhub.dal.exceptions;

/**
 * Represents wrapper for action that can throw checked exception.
 *
 * <p>Remarks: lambda realizing interface can have actual parameters captured from parent scope as closure.
 * <code>
 * T someVar1;
 * IThrowingAction&lt;IOException&gt; lambda = () -> callSomebody(someVar1);
 * </code>
 * </p>
 *
 * @param <E> the type of checked exception that could be thrown
 */
@FunctionalInterface
public interface IThrowingAction<E extends Exception> {
    void invoke() throws E;
}
