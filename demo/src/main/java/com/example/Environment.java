package com.example;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Deque<Map<String, Integer>> scopes = new ArrayDeque<>();

    public Environment() {
        scopes.push(new HashMap<>()); // global scope
    }

    public void push() {
        scopes.push(new HashMap<>());
    }

    public void pop() {
        scopes.pop();
    }

    public void declare(String name, int value) {
        Map<String, Integer> top = scopes.peek();
        if (top.containsKey(name)) {
            throw new RuntimeException("Variable '" + name + "' already declared in this scope");
        }
        top.put(name, value);
    }

    public boolean contains(String name) {
        for (Map<String, Integer> scope : scopes) {
            if (scope.containsKey(name))
                return true;
        }
        return false;
    }

    public int get(String name) {
        for (Map<String, Integer> scope : scopes) {
            if (scope.containsKey(name))
                return scope.get(name);
        }
        throw new RuntimeException("Variable '" + name + "' not declared");
    }

    public void set(String name, int value) {
        for (Map<String, Integer> scope : scopes) {
            if (scope.containsKey(name)) {
                scope.put(name, value);
                return;
            }
        }
        throw new RuntimeException("Variable '" + name + "' not declared");
    }
}
