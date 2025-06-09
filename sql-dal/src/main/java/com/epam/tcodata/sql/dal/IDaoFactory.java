package com.epam.tcodata.sql.dal;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.service.IService;

import java.nio.file.Path;
import java.util.Set;

public interface IDaoFactory extends AutoCloseable {
    <T> IService service(Class<T> clazz);

    /**
     * Returns strictly typed service by its entity class.
     *
     * @param factory this factory.
     * @param clazz entity class.
     * @return instance of service.
     */
    static <T, S extends IService> S service(IDaoFactory factory, Class<T> clazz) {
        return (S) factory.service(clazz);
    }

    Set<Class<?>> knownEntityClasses();

    /**
     * Getter for secret storage.
     *
     * @return
     */
    ISecretStorage getSecretStorage();

    /**
     * Restore data from given backup. You need to store all data into one folder.
     * Each service has its own file in that folder with name as full class name and extention 'json'.
     * Format of these file is of course json. Any entities are not removed from database. Remove it by yourself if you
     * need it. IDs of all entities will be kept as they were in the database on the moment of backup.
     *
     * @param path Path object for given directory.
     * @throws Exception
     */
    void restore(Path path);

    /**
     * Makes a backup to given directory. Only read-write services will store their data. And only services that
     * were created before with IDaoFactory.service(factory, class) invoke.
     *
     * @param path
     * @throws Exception
     */
    void backup(Path path);

    /**
     * Build a connection string for the given database using hostName, database and port. They are got from secure storage.
     *
     * @param hostName host name
     * @param port port number
     * @param database database name
     * @return combined connection string
     */
    String buildURL(String hostName, String port, String database);
}
