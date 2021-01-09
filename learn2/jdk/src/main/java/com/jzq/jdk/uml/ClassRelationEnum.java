package com.jzq.jdk.uml;

public enum ClassRelationEnum {
    IMPLEMENTS(" <|.. "),
    EXTENDS(" <|-- ");

    private final String relation;

    ClassRelationEnum(String relation) {
        this.relation = relation;
    }

    public String getRelation() {
        return relation;
    }
}
