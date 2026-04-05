package com.notification.template;

import com.notification.model.Notification;

/**
 * Abstraction for template resolution and rendering.
 *
 * ISP: Deliberately split into render-only responsibility.
 * A separate TemplateRegistry interface could handle registration if needed.
 *
 * DIP: NotificationService depends on this interface, not a concrete engine.
 * This allows swapping to Freemarker, Mustache, or i18n-aware engines without
 * changing service logic.
 */
public interface TemplateEngine {

    /**
     * Renders the notification message by resolving its templateKey and binding
     * templateData into the template.
     *
     * @param notification the notification containing templateKey and templateData
     * @return the fully rendered message string
     * @throws com.notification.exception.TemplateNotFoundException if templateKey has no mapping
     */
    String render(Notification notification);
}
