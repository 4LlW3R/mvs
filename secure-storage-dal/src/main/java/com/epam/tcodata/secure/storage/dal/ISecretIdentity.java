package com.epam.tcodata.secure.storage.dal;

/**
 * This interface is responsible for making proper secret name for
 * the SecretStorage.
 * Such names consist from names of subsystems, sections, parameters separated by dashes ('-')
 */
public interface ISecretIdentity {

    String SEPARATOR = "-";

    /**
     * Builds full secret name from parts matched to subsystems, sections, parameters.
     *
     * @return full name.
     */
    default String buildSecretFullName() {
        Class<? extends ISecretIdentity> aClass = getClass();
        return buildSecretFullName(aClass);
    }


    /**
     * Builds full secret name for the given class. Used as a recursion call for enclosed classes.
     *
     * @param aClass class of current identity object
     * @return string representation
     */
    default String buildSecretFullName(Class<?> aClass) {
        Class<?> enclosingClass = aClass.getEnclosingClass();
        String prefix = enclosingClass == null
                ? ""
                : buildSecretFullName(enclosingClass) + SEPARATOR;

        String suffix;
        if (ISecretIdentity.class.isAssignableFrom(aClass)) {
            suffix = aClass.getSimpleName()
                    + SEPARATOR
                    + ((Enum) this).name();
        } else {
            suffix = aClass.getSimpleName();
        }
        return prefix + suffix;
    }
}
