package org.happybaras.server.domain.enums;

public enum PermitTypeEnum {
    UNIQUE("unique"),
    MULTIPLE("multiple");

    public final String value;

    private PermitTypeEnum(String value) {
        this.value = value;
    }
}
