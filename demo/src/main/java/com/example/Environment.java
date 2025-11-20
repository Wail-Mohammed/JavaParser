package com.example;

import java.util.*;

class Environment {
    private final Deque<Map<String, Integer>> scopes = new ArrayDeque<>();

    Environment() {
        scopes.push(new HashMap<>());
    } // global

    void push() {
        scopes.push(new HashMap<>());
    }

    void pop() {
        scopes.pop();
    }

    boolean isDeclaredHere(String name) {
        return scopes.peek().containsKey(name);
    }

    void declare(String name, int value) {
        if (isDeclaredHere(name))
            throw new RuntimeException("Semantic error: variable '" + name + "' already declared in this scope");
        scopes.peek().put(name, value);
    }

    boolean contains(String name) {
        for (var m : scopes)
            if (m.containsKey(name))
                return true;
        return false;
    }

    int get(String name) {
        for (var m : scopes)
            if (m.containsKey(name))
                return m.get(name);
        throw new RuntimeException("Semantic error: variable '" + name + "' not declared");
    }

    void set(String name, int value) {
        for (var m : scopes) {
            if (m.containsKey(name)) {
                m.put(name, value);
                return;
            }
        }
        throw new RuntimeException("Semantic error: variable '" + name + "' not declared");
    }
}
