package com.lld.filestorage.domain;

import com.lld.filestorage.exception.DuplicateNameException;
import com.lld.filestorage.exception.NodeNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Composite Pattern — Composite node.
 * A Folder can contain both Files and other Folders, enabling arbitrary depth.
 *
 * SRP  : Owns child management only.
 * OCP  : New node types (e.g. Symlink) can be added without touching Folder.
 *
 * Business rules enforced:
 *   - Child names must be unique within this folder.
 *   - Deleting a folder is a recursive operation (cascade handled by service).
 */
public final class Folder extends FileSystemNode {

    private final List<FileSystemNode> children = new ArrayList<>();

    public Folder(final String name, final User owner) {
        super(name, owner);
    }

    // ── Child management ─────────────────────────────────────────────────────

    /**
     * Adds a node. Enforces uniqueness of names within this folder.
     * @throws DuplicateNameException if a child with the same name already exists.
     */
    public void addChild(final FileSystemNode node) {
        if (node == null) throw new IllegalArgumentException("node must not be null");
        boolean duplicate = children.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(node.getName()));
        if (duplicate) {
            throw new DuplicateNameException(
                    "'" + node.getName() + "' already exists in folder '" + getName() + "'");
        }
        children.add(node);
    }

    public void removeChild(final String name) {
        boolean removed = children.removeIf(c -> c.getName().equalsIgnoreCase(name));
        if (!removed) {
            throw new NodeNotFoundException("'" + name + "' not found in folder '" + getName() + "'");
        }
    }

    public Optional<FileSystemNode> findChild(final String name) {
        return children.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<FileSystemNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    // ── Permission inheritance ───────────────────────────────────────────────

    /**
     * Business rule: permissions propagate from parent folder down to children.
     * Called by the service layer when a child needs an inherited access check.
     */
    public void propagatePermissions(final Permission permission) {
        addPermission(permission);
        for (FileSystemNode child : children) {
            child.addPermission(permission);
            if (child.isFolder()) {
                ((Folder) child).propagatePermissions(permission);
            }
        }
    }

    // ── Composite ────────────────────────────────────────────────────────────

    @Override public boolean isFolder() { return true; }
}
