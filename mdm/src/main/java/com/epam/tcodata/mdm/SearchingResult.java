package com.epam.tcodata.mdm;

import java.util.Objects;
import java.util.UUID;

/**
 * Immutable class that represents searching result of related entities by their natural keys.
 */
public class SearchingResult {
    private UUID uuid;
    private String name;

    public SearchingResult(String name, UUID uuid) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public String toString() {
        return "SearchingResult{"
                + "uuid=" + uuid
                + ", name='" + name + '\''
                + '}';
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchingResult that = (SearchingResult) o;
        return Objects.equals(uuid, that.uuid)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uuid, name);
    }
}
