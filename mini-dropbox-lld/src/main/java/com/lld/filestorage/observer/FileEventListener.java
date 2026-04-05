package com.lld.filestorage.observer;

/**
 * Observer Pattern — subscriber contract.
 *
 * ISP: A single focused method; listeners are not forced to implement
 * unrelated callbacks. New event types are modelled as new FileEvent subtypes
 * (OCP) without changing this interface.
 */
public interface FileEventListener {
    void onEvent(FileEvent event);
}
