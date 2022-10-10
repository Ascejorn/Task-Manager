package service;

import Interfaces.HistoryManager;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
        nodeMap.put(task.getId(), last);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        Node node = first;
        while (node != null) {
            result.add(node.getTask());
            node = node.next;
        }
        return result;
    }

    @Override
    public void remove(Integer taskId) {
        Node node = nodeMap.get(taskId);
        removeNode(node);
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.next != null && node.prev != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        if (node.next == null && node.prev != null) {
            last = node.prev;
            last.next = null;
        }
        if (node.next != null && node.prev == null) {
            first = node.next;
            first.prev = null;
        }
        if (node.next == null && node.prev == null) {
            first = null;
            last = null;
        }
        nodeMap.remove(node.task.getId());
    }

    private void linkLast(Task task) {
        Node node = new Node(task, last, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }

    public static class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        public Task getTask() {
            return task;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task +
                    ", prev=" + (prev != null ? prev.task : null) +
                    ", next=" + (next != null ? next.task : null) +
                    '}';
        }
    }
}
