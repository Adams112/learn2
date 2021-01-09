package com.jzq.jdk.uml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;

public class UmlScriptGenerator {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Collection<Class<?>> classes;
    private Collection<ClassRelation> relations;

    void generate(Collection<Class<?>> classes, UmlScriptGenerateContext context) {
        this.classes = new HashSet<>();
        this.relations = new HashSet<>();
        Deque<Class<?>> toHandle = new ArrayDeque<>(classes);


        while (!toHandle.isEmpty()) {
            Class<?> clazz = toHandle.pollFirst();
            this.classes.add(clazz);

            Class<?> superclass = clazz.getSuperclass();
            Class<?>[] interfaces = clazz.getInterfaces();

            if (superclass != null &&
                    (classes.contains(superclass) || context.getUmlScriptGenerateCondition().shouldAdd(superclass))) {
                if (!this.classes.contains(superclass)) {
                    toHandle.addLast(superclass);
                }
                relations.add(new ClassRelation(superclass, clazz, ClassRelationEnum.EXTENDS));
            }

            for (Class<?> inter : interfaces) {
                if (classes.contains(inter) || context.getUmlScriptGenerateCondition().shouldAdd(inter)) {
                    try {
                        if (!this.classes.contains(inter)) {
                            toHandle.addLast(inter);
                        }
                        relations.add(new ClassRelation(inter, clazz,
                                clazz.isInterface() ? ClassRelationEnum.IMPLEMENTS : ClassRelationEnum.EXTENDS));
                    } catch (Throwable t) {
                        logger.error(t.getMessage(), t);
                        logger.error("{}", clazz.getSimpleName());
                    }

                }
            }
        }

        logger.info("generate {} classes and {} relations", this.classes.size(), relations.size());
    }

    public Collection<Class<?>> getClasses() {
        return classes;
    }

    public Collection<ClassRelation> getRelations() {
        return relations;
    }
}
