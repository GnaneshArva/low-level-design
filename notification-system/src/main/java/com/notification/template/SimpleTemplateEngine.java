package com.notification.template;

import com.notification.exception.TemplateNotFoundException;
import com.notification.model.Notification;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple placeholder-based template engine.
 *
 * Templates use {{key}} syntax bound against a Map<String,String> templateData.
 *
 * Design note: In production this would be replaced by Freemarker or Mustache
 * for richer expressions, partials, and i18n support. This implementation is
 * intentionally minimal to stay interview-legible without framework imports.
 *
 * Trade-off: The registry is populated at construction time (compile-time safety).
 * In production, templates would be loaded from a database or filesystem.
 */
public class SimpleTemplateEngine implements TemplateEngine {

    private final Map<String, String> templates;

    public SimpleTemplateEngine() {
        templates = new HashMap<>();
        registerDefaultTemplates();
    }

    @Override
    public String render(Notification notification) {
        String templateKey = notification.getTemplateKey();
        String template = templates.get(templateKey);

        if (template == null) {
            throw new TemplateNotFoundException(templateKey);
        }

        return bindData(template, notification.getTemplateData());
    }

    /**
     * Replaces {{key}} placeholders using the data object.
     * Supports Map<String,String> data. Extend for richer types.
     */
    @SuppressWarnings("unchecked")
    private String bindData(String template, Object data) {
        if (data == null) return template;

        if (data instanceof Map) {
            Map<String, String> dataMap = (Map<String, String>) data;
            String result = template;
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            return result;
        }

        // Fallback: bind the whole data object as {{value}}
        return template.replace("{{value}}", data.toString());
    }

    public void registerTemplate(String key, String template) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("Template key required");
        if (template == null)             throw new IllegalArgumentException("Template body required");
        templates.put(key, template);
    }

    // ── Default templates loaded at startup ───────────────────────────────────

    private void registerDefaultTemplates() {
        templates.put("OTP",
                "Hello {{name}}, your OTP is {{otp}}. Valid for 10 minutes.");

        templates.put("ORDER_CONFIRMATION",
                "Hi {{name}}, your order #{{orderId}} has been confirmed. " +
                "Estimated delivery: {{deliveryDate}}.");

        templates.put("WELCOME",
                "Welcome to our platform, {{name}}! " +
                "We're glad to have you on board.");

        templates.put("ALERT",
                "ALERT: {{message}}. Please take immediate action.");

        templates.put("PROMO",
                "Hi {{name}}, enjoy {{discount}}% off your next purchase. " +
                "Use code: {{code}}");
    }
}
