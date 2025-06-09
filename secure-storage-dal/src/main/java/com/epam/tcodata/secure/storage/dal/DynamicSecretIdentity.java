package com.epam.tcodata.secure.storage.dal;

import java.util.Objects;

public class DynamicSecretIdentity implements ISecretIdentity {

    private ISecretIdentity selector;
    private String name;

    public DynamicSecretIdentity(ISecretIdentity selector, String name) {
        this.selector = selector;
        this.name = name;
    }

    @Override
    public String buildSecretFullName() {
        return this.selector.buildSecretFullName() + ISecretIdentity.SEPARATOR + this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicSecretIdentity that = (DynamicSecretIdentity) o;
        return Objects.equals(selector, that.selector)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(selector, name);
    }

    @Override
    public String toString() {
        return "DynamicSecretIdentity{"
                + "selector=" + selector
                + ", name='" + name + '\''
                + '}';
    }
}
