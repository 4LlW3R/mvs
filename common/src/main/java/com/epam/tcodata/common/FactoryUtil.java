package com.epam.tcodata.common;

import com.epam.tcodata.common.exception.WrongFactoryClassException;

public class FactoryUtil {

    private FactoryUtil() {
    }

    /**
     * Loads the pointed factory by its name (full path to factory class) and create its instance
     * if it is a descendant of the given base factory.
     *
     * @param baseClazz - base factory class (only for checking of hierarchy).
     * @param className - name of the class that we wish to found and instantiate.
     * @param <T>       - underneath type.
     * @return an instance of the pointed factory.
     * @throws InstantiationException exception while trying to instantiate factory class.
     */
    public static <T> T loadFactory(Class<T> baseClazz, String className)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class<T> clazz;
        try {
            clazz = (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(className);
            if (baseClazz.isAssignableFrom(clazz)) {
                return clazz.newInstance();
            }
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Factory class " + className + " is possibly renamed or removed", e);
        }
        throw new WrongFactoryClassException("Factory class " + className + " is not derived from " + baseClazz);
    }
}
