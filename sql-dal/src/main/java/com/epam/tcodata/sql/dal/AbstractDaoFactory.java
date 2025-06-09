package com.epam.tcodata.sql.dal;

import com.epam.tcodata.common.ResourceUtils;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.exception.BackupException;
import com.epam.tcodata.sql.dal.exception.OperationIsNotSupportedException;
import com.epam.tcodata.sql.dal.exception.RestoreException;
import com.epam.tcodata.sql.dal.service.IReadWriteService;
import com.epam.tcodata.sql.dal.service.IService;
import liquibase.util.file.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDaoFactory implements IDaoFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDaoFactory.class);

    private ISecretStorage secretStorage;
    private ConcurrentHashMap<Class<?>, IService> map = new ConcurrentHashMap<>();

    protected AbstractDaoFactory(ISecretStorage secretStorage) {
        this.secretStorage = secretStorage;
    }

    @Override
    public ISecretStorage getSecretStorage() {
        return this.secretStorage;
    }

    @Override
    public Set<Class<?>> knownEntityClasses() {
        return this.map.keySet();
    }

    @Override
    public <T> IService service(Class<T> clazz) {

        return this.map.get(clazz);
    }

    @Override
    public void close() throws Exception {
        this.map.values().forEach(v -> {
            try {
                v.close();
            }
            catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
    }

    @Override
    public void restore(Path path) {
        try {
            File[] files = path.toFile().listFiles();
            if (files != null) {
                for (File file : files) {
                    String name = file.getName();
                    String className = FilenameUtils.removeExtension(name);
                    Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass(className);
                    List<?> objects = ResourceUtils.loadData((Class<?>) aClass, file.getAbsolutePath());
                    IService service = IDaoFactory.service(this, aClass);
                    if (service instanceof IReadWriteService) {
                        IReadWriteService readWriteService = (IReadWriteService) service;
                        readWriteService.restore(objects);
                    } else {
                        throw new OperationIsNotSupportedException("Service " + service.getClass() + " not support Read/Write operations");
                    }
                }
            } else {
                throw new RestoreException("Backup files are not found.");
            }
        } catch (Exception e) {
            LOGGER.error("restore({})", path);
            throw new RestoreException(e.getMessage(), e);
        }
    }

    @Override
    public void backup(Path path) {
        try {
            Files.createDirectories(path);
            Set<Class<?>> classes = this.knownEntityClasses();
            for (Class<?> clazz : classes) {
                IService service = IDaoFactory.service(this, clazz);
                if (service instanceof IReadWriteService) {
                    IReadWriteService readWriteService = (IReadWriteService) service;
                    List list = readWriteService.readAll();
                    Path jsonPath = Paths.get(path.toString(), clazz.getName() + ".json");
                    ResourceUtils.saveData(clazz, list, jsonPath.toString());
                }
            }
        } catch (Exception e) {
            LOGGER.error("backup({})", path);
            throw new BackupException(e.getMessage(), e);
        }
    }

    @Override
    public String buildURL(String hostName, String port, String database) {

        return new StringBuilder("jdbc:sqlserver://")
                .append(hostName)
                .append(':')
                .append(port)
                .append(';')
                .append("databaseName=")
                .append(database)
                .toString();
    }

    protected final void register(Class<?> clazz, IService service) {
        this.map.put(clazz, service);
        if (service instanceof IReadWriteService && isNeededToCreate()) {
            service.checkConnection();
            ((IReadWriteService) service).createTable();
        }
    }

    protected boolean isNeededToCreate() {
        return false;
    }
}
