package com.epam.tcodata.secure.storage.dal.factory;

import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.common.ResourceUtils;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.exception.SecretStorageException;

import java.io.Serializable;
import java.util.Properties;

/**
 * The factory for creation of instance of ISecretStorage.
 * Reference to the factory will be passed into another factories that need its service.
 * For every environment in settings.xml there is a special property with name secretStorageFactoryClass
 * where full qualified class name of implementation factory must be written.
 */
public interface ISecretStorageFactory extends  Serializable {
    String SECRET_STORAGE_FACTORY_CLASS = "secretStorageFactoryClass";
    String SECRET_STORAGE_PROPERTIES = "secret-storage.properties";

    /**
     * Creates an instance of ISecretStorage. To override this method don't forget to invoke init() method with
     * properties for the new created storage.
     *
     * @param properties property file that will be pass for initialization to the storage.
     * @return instance of the storage.
     */
    ISecretStorage createSecretStorage(Properties properties);

    /**
     * Creates an instance of ISecretStorageFactory based on settings. A real class for factory will be taken from
     * settings.xml.
     *
     * @return instance of factory
     * @throws Exception
     */
    static ISecretStorageFactory createDefaultFactory() {
        Properties props = ResourceUtils.readProperties(SECRET_STORAGE_PROPERTIES);
        String clazzName = ResourceUtils.getProperty(null, props, SECRET_STORAGE_FACTORY_CLASS);
        ISecretStorageFactory secretStorageFactory = null;
        try {
            secretStorageFactory = FactoryUtil.loadFactory(ISecretStorageFactory.class, clazzName);
        } catch (Exception  e) {
            throw new SecretStorageException("Couldn't create SecretStorageFactory: " + clazzName, e);
        }

        return secretStorageFactory;
    }
}
