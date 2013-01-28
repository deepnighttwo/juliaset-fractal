/**
 * This source code belongs to Mark Zang, the author. To use it for
 * commercial/business purpose, please contact DeepNightTwo@gmail.com
 * 
 * @author Mark Zang
 * 
 */

package com.deepnighttwo.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceUtil {

    static ResourceBundle resource = null;
    static {
        resource = ResourceBundle.getBundle("com.deepnighttwo.language.MainUI", Locale.getDefault());
    }

    public static String getString(String key) {
        if (key == null) {
            return "";
        }
        String realkey = key.replace(' ', '_');
        String value = null;
        try {
            value = resource.getString(realkey);
            if (value == null) {
                value = key;
            }
        } catch (MissingResourceException ex) {
            value = key;
        }
        return value;
    }
}
