package com.epam.tcodata.mdm;

import com.epam.tcodata.models.mdm.AssigningReason;

import java.util.UUID;

/**
 * The class that describes a decision for every surrogate key assignment.
 */
public class Decision {

    private String key;
    private UUID surrogateKey;
    private AssigningReason assigningReason;
    private Long keyMappingId;

    /**
     * Defines if the current object is valid.
     * @return true if the reason makes sense.
     */
    public boolean isOk() {
        return this.assigningReason != null && this.assigningReason != AssigningReason.NONE;
    }

    /**
     * Factory method for NONE case.
     * @return an instance with the given reason.
     */
    public static Decision noneKey() {
        return new Decision(null, null, AssigningReason.NONE, null);
    }

    /**
     * Factory method for INITIAL_KEY case.
     *
     * @param key - key value to which surrogate key is mapped.
     * @param surrogateKey - new value of the key.
     * @return an instance with the given reason.
     */
    public static Decision initialKey(String key, UUID surrogateKey) {
        return new Decision(key, surrogateKey, AssigningReason.INITIAL_KEY, null);
    }

    /**
     * Factory method for NEW_KEY case.
     *
     * @param key - key value to which surrogate key is mapped.
     * @param surrogateKey - new value of the key.
     * @return an instance with the given reason.
     */
    public static Decision newKey(String key, UUID surrogateKey) {
        return new Decision(key, surrogateKey, AssigningReason.NEW_KEY, null);
    }

    /**
     * Factory method for RENEW_KEY case.
     *
     * @param key - key value to which surrogate key is mapped.
     * @param surrogateKey - new value of the key.
     * @param keyMappingId - the id of KeyMapping record that contains the same surrogate key.
     * @return an instance with the given reason.
     */
    public static Decision renewKey(String key, UUID surrogateKey, long keyMappingId) {
        return new Decision(key, surrogateKey, AssigningReason.RENEW_KEY, keyMappingId);
    }

    /**
     * Factory method for FOUND_KEY case.
     *
     * @param key - key value to which surrogate key is mapped.
     * @param surrogateKey - new value of the key.
     * @return an instance with the given reason.
     */
    public static Decision foundKey(String key, UUID surrogateKey, long keyMappingId, boolean speculative) {
        return new Decision(key, surrogateKey, speculative ? AssigningReason.SPECULATIVE_KEY : AssigningReason.FOUND_KEY, keyMappingId);
    }
    /**
     * Factory method for common case.
     *
     * @param reason - given reason.
     * @param key - key value to which surrogate key is mapped.
     * @param surrogateKey - new value of the key.
     * @return an instance with the given reason.
     */
    public static Decision anyDesicion(AssigningReason reason, String key, UUID surrogateKey, Long keyMappingId) {
        return new Decision(key, surrogateKey, reason, keyMappingId);
    }

    /**
     * Internal constructor.
     * @param key - key value to which surrogate key is mapped.
     * @param surrogateKey - new value of the key.
     * @param assigningReason - the reason why this key was assigned.
     * @param keyMappingId - in case of reassigning of surrogate key is ID of KeyMapping record.
     */
    private Decision(String key, UUID surrogateKey, AssigningReason assigningReason, Long keyMappingId) {

        this.key = key;
        this.surrogateKey = surrogateKey;
        this.assigningReason = assigningReason;
        this.keyMappingId = keyMappingId;
    }

    public String getKey() {
        return key;
    }

    public UUID getSurrogateKey() {
        return this.surrogateKey;
    }

    public AssigningReason getAssigningReason() {
        return this.assigningReason;
    }

    public Long getKeyMappingId() {
        return this.keyMappingId;
    }

    @Override
    public String toString() {
        return "Decision{"
                + "key=" + key
                + ", surrogateKey=" + surrogateKey
                + ", assigningReason=" + assigningReason
                + ", keyMappingId=" + keyMappingId
                + '}';
    }
}
