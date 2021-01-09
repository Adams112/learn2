package com.jzq.jdk.uml;

public class ClassRelation {
    private Class<?> a;
    private Class<?> b;
    private ClassRelationEnum relation;

    public ClassRelation(Class<?> a, Class<?> b, ClassRelationEnum relation) {
        this.a = a;
        this.b = b;
        this.relation = relation;
    }

    public Class<?> getA() {
        return a;
    }

    public Class<?> getB() {
        return b;
    }

    public ClassRelationEnum getRelation() {
        return relation;
    }
}
