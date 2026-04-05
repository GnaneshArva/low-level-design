package com.notification.model;

/**
 * Supported notification delivery channels.
 * OCP: Adding a new channel requires adding an enum constant + new channel class only.
 */
public enum ChannelType {
    EMAIL, SMS, PUSH
}
