package com.lld.filestorage.service;

import com.lld.filestorage.domain.FileSystemNode;
import com.lld.filestorage.domain.Folder;
import com.lld.filestorage.exception.NodeNotFoundException;

/**
 * Stateless path resolution helper.
 *
 * SRP: Only navigates a path string to a Folder node — nothing else.
 * DIP: Commands depend on this abstraction, not on Folder's raw API.
 *
 * Path format: "/" = root, "/docs/work" = nested folder.
 */
public final class FolderNavigator {

    private FolderNavigator() { /* utility — no instances */ }

    /**
     * Resolves a slash-delimited path from the given root Folder.
     *
     * @param root   the root of the file system tree
     * @param path   logical path e.g. "/", "/docs", "/docs/q1"
     * @return the Folder at the given path
     * @throws NodeNotFoundException if any segment in the path does not exist or is not a folder
     */
    public static Folder navigate(final Folder root, final String path) {
        if (path == null || path.isBlank()) throw new IllegalArgumentException("path required");

        // Normalise: strip leading slash, split on "/"
        final String normalised = path.startsWith("/") ? path.substring(1) : path;

        if (normalised.isEmpty()) {
            return root;   // path was "/" — return root directly
        }

        final String[] segments = normalised.split("/");
        Folder current = root;

        for (final String segment : segments) {
            if (segment.isBlank()) continue; // handle double slashes gracefully

            final Folder snapshot = current;  // effectively-final capture for lambda
            final FileSystemNode next = current.findChild(segment)
                    .orElseThrow(() -> new NodeNotFoundException(
                            "Folder segment '" + segment + "' not found under '" + snapshot.getName() + "'"));

            if (!next.isFolder()) {
                throw new NodeNotFoundException("'" + segment + "' is a file, not a folder.");
            }

            current = (Folder) next;
        }

        return current;
    }
}
