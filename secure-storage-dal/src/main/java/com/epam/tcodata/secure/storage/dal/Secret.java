package com.epam.tcodata.secure.storage.dal;

import java.util.*;
import java.util.stream.Stream;

/**
 * This class allows to identify secrets depending on their belonging to the certain system.
 * There are several levels of hierarchy now:
 * Secret / Subsystem / Section / parameter.
 * The root of the whole hierarchy is Secret itself.
 * Subsystems are Sql, Hive, EventHub.
 * Sections for each subsystem are their own. For example, Sql has three sections (called databases)
 * PUMPS, MDM, SPEEDLAYER.
 * Parameters are common for every sections in each subsystem.
 * <p/>
 * There are two ways how to identify some secret. The first one is identifying on compile time. This way is possible
 * when the section is well known on compile time. For example:
 * Secret.Sql.PUMPS.user describes the secret with name "Secret.Sql.PUMPS.user"
 * Another way, when a section is unknown, the another syntax should be used:
 * Secret.Sql.user.usingSection("MDM") describes the secret with name "Secret.Sql.MDM.user".
 */
public final class Secret {
    /**
     * Collects all possible identifier for all subsystems, sections, parameters.
     * Can be useful to check that all parameters are added into SecretStorage and to get all of them.
     *
     * @return set of @ISecretIdentity
     */
    public static Set<ISecretIdentity> collectAllIdentifiers() {
        Set<ISecretIdentity> secretIdentitySet = new LinkedHashSet<>();
        collectPossibleNames(secretIdentitySet, Sql.DB.class, Sql.class);
        collectPossibleNames(secretIdentitySet, EventHub.NameSpace.class, EventHub.class);
        collectPossibleNames(secretIdentitySet, Hive.DB.class, Hive.class);
        collectPossibleNames(secretIdentitySet, Mix.Section.class, Mix.class);
        collectPossibleNames(secretIdentitySet, Redis.Section.class, Redis.class);
        collectPossibleNames(secretIdentitySet, StorageAccount.ACCOUNT.class, StorageAccount.class);
        return secretIdentitySet;
    }

    /**
     * Finds identity matched to the given full name. This method is useful to guarantee that string representation is correct.
     * ISecretStorage interface can work only with ISecretIdentity's, not with strings. So, you need to transform a string into
     * ISecretIdentity entity before using ISecretStorage.
     *
     * @param fullName full name.
     * @return identity
     */
    public static ISecretIdentity selectByName(String fullName) {

        Stream<ISecretDivision> stream = Arrays.stream(
                        new ISecretDivision[][]{Sql.values(), EventHub.values(), Hive.values(), Mix.values(), Redis.values(), StorageAccount.values()})
                .flatMap(a -> Arrays.stream(a));

        Optional<ISecretIdentity> found = stream
                .map(div -> div.selectByName(fullName))
                .filter(Objects::nonNull)
                .findFirst();
        return found.orElse(null);
    }

    public enum Sql implements ISecretDivision {
        user,
        password;

        public enum DB {
            PUMPS,
            MDM,
            SPEEDLAYER
        }

        public enum PUMPS implements ISecretIdentity {
            user,
            password
        }

        public enum MDM implements ISecretIdentity {
            user,
            password
        }

        public enum SPEEDLAYER implements ISecretIdentity {
            user,
            password
        }

        @Override
        public ISecretIdentity usingSection(String sectionName) {
            Sql.DB database = Enum.valueOf(DB.class, sectionName);
            String name = this.name();
            switch (database) {
                case PUMPS:
                    return Enum.valueOf(PUMPS.class, name);
                case MDM:
                    return Enum.valueOf(MDM.class, name);
                case SPEEDLAYER:
                    return Enum.valueOf(SPEEDLAYER.class, name);
                default:
                    throw new NoSuchFieldError(sectionName);
            }
        }

        @Override
        public ISecretIdentity selectByName(String fullName) {
            return selectStaticIdentity(fullName, DB.class, Sql.class);
        }
    }

    public enum EventHub implements ISecretDivision {
        accessKey;

        public enum NameSpace {
            Raw,
            Overtaking,
            RoadCondition,
            ConfirmedOvertaking
        }

        public enum Raw implements ISecretIdentity {
            accessKey
        }

        public enum Overtaking implements ISecretIdentity {
            accessKey
        }

        public enum RoadCondition implements ISecretIdentity {
            accessKey
        }

        public enum ConfirmedOvertaking implements ISecretIdentity {
            accessKey
        }

        @Override
        public ISecretIdentity usingSection(String sectionName) {
            NameSpace namespace = Enum.valueOf(NameSpace.class, sectionName);
            String name = this.name();
            switch (namespace) {
                case Raw:
                    return Enum.valueOf(Raw.class, name);
                case Overtaking:
                    return Enum.valueOf(Overtaking.class, name);
                case RoadCondition:
                    return Enum.valueOf(RoadCondition.class, name);
                case ConfirmedOvertaking:
                    return Enum.valueOf(ConfirmedOvertaking.class, name);
                default:
                    throw new NoSuchFieldError(sectionName);
            }
        }

        @Override
        public ISecretIdentity selectByName(String fullName) {
            return selectStaticIdentity(fullName, NameSpace.class, EventHub.class);
        }
    }

    public enum Hive implements ISecretDivision {
        connect,
        key;

        public enum DB {
            RAW,
            PREPARED
        }

        public enum RAW implements ISecretIdentity {
            connect,
            key
        }

        public enum PREPARED implements ISecretIdentity {
            connect,
            key
        }

        @Override
        public ISecretIdentity usingSection(String sectionName) {
            DB database = Enum.valueOf(Hive.DB.class, sectionName);
            String name = this.name();
            switch (database) {
                case RAW:
                    return Enum.valueOf(RAW.class, name);
                case PREPARED:
                    return Enum.valueOf(PREPARED.class, name);
                default:
                    throw new NoSuchFieldError(sectionName);
            }
        }

        @Override
        public ISecretIdentity selectByName(String fullName) {
            return selectStaticIdentity(fullName, DB.class, Hive.class);
        }
    }

    public enum Mix implements ISecretDivision {
        clientId,
        clientSecret,
        name;

        public enum Section {
            COMMON
        }

        public enum COMMON implements ISecretIdentity {
            clientId,
            clientSecret
        }

        public enum ACCOUNT implements ISecretIdentity {
            name
        }

        @Override
        public ISecretIdentity usingSection(String sectionName) {
            if (this == name) {
                return new DynamicSecretIdentity(ACCOUNT.name,
                        Section.COMMON.name().equals(sectionName)
                                ? "*"
                                : sectionName);
            } else {
                return Enum.valueOf(COMMON.class, this.name());
            }
        }

        @Override
        public ISecretIdentity selectByName(String fullName) {
            ISecretIdentity secretIdentity = selectStaticIdentity(fullName, Section.class, Mix.class);
            if (secretIdentity != null) {
                return secretIdentity;
            }
            int prevIndex = fullName.indexOf(ISecretIdentity.SEPARATOR + this.name());
            int lastIndex = fullName.indexOf(ISecretIdentity.SEPARATOR, prevIndex + 1);
            if (lastIndex >= 0) {
                String accountName = fullName.substring(lastIndex + 1);
                ISecretIdentity accountIdentity = Mix.name.usingSection(accountName);
                if (accountIdentity.buildSecretFullName().equals(fullName)) {
                    return accountIdentity;
                }
            }
            return null;
        }
    }

    public enum Redis implements ISecretDivision {
        accessKey;

        public enum Section {
            COMMON
        }

        public enum COMMON implements ISecretIdentity {
            accessKey
        }

        @Override
        public ISecretIdentity usingSection(String sectionName) {
            Section section = Enum.valueOf(Section.class, sectionName);
            String name = this.name();
            switch (section) {
                case COMMON:
                    return Enum.valueOf(COMMON.class, name);
                default:
                    throw new NoSuchFieldError(sectionName);
            }
        }

        @Override
        public ISecretIdentity selectByName(String fullName) {
            return selectStaticIdentity(fullName, Section.class, Redis.class);
        }
    }

    public enum StorageAccount implements ISecretDivision {
        connectionString;

        public enum ACCOUNT {
            MAIN,
            LOGS
        }

        public enum MAIN implements ISecretIdentity {
            connectionString
        }

        public enum LOGS implements ISecretIdentity {
            connectionString
        }

        @Override
        public ISecretIdentity usingSection(String sectionName) {
            StorageAccount.ACCOUNT account = Enum.valueOf(ACCOUNT.class, sectionName);
            String name = this.name();
            switch (account) {
                case MAIN:
                    return Enum.valueOf(MAIN.class, name);
                case LOGS:
                    return Enum.valueOf(LOGS.class, name);
                default:
                    throw new NoSuchFieldError(sectionName);
            }
        }

        @Override
        public ISecretIdentity selectByName(String fullName) {
            return selectStaticIdentity(fullName, ACCOUNT.class, StorageAccount.class);
        }
    }


    private static <T extends Enum<T>, U extends Enum<U> & ISecretDivision> void collectPossibleNames(Set<ISecretIdentity> secretIdentitySet,
                                                                                                      Class<T> sectionClass,
                                                                                                      Class<U> parametersClass) {
        for (Enum<T> section : sectionClass.getEnumConstants()) {
            for (ISecretDivision parameter : parametersClass.getEnumConstants()) {
                secretIdentitySet.add(parameter.usingSection(section.name()));
            }
        }
    }

    private static <T extends Enum<T>, U extends Enum<U> & ISecretDivision> ISecretIdentity selectStaticIdentity(String fullName,
                                                                                                                 Class<T> sectionClass,
                                                                                                                 Class<U> parametersClass) {

        for (Enum<T> section : sectionClass.getEnumConstants()) {
            for (ISecretDivision parameter : parametersClass.getEnumConstants()) {
                ISecretIdentity secretIdentity = parameter.usingSection(section.name());
                if (secretIdentity.buildSecretFullName().equals(fullName)) {
                    return secretIdentity;
                }
            }
        }
        return null;
    }

}

