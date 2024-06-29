package org.happybaras.server.domain.enums;

public enum PermitStatusEnum {
    PENDING("pending"),
    APPROVED("approved"),
    REJECTED("rejected");

    public final String value;

    private PermitStatusEnum(String value) {
        this.value = value;
    }
}
