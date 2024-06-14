package app.toolbox;

import app.model.User;
import app.service.ManagerForm;

/**
 * Utility class to set application themes based on user style preferences.
 */
public class StyleLoginUser {

    /**
     * Sets the application theme based on user's style preference.
     *
     * @param berechtigung User object containing style information
     */
    public static void ChekStyle(User berechtigung) {
        // Check user's style preference and set corresponding theme
        if ("Cyan".equals(berechtigung.getStyle())) {
            ManagerForm.getInstance().setThemesIntelli("/com/formdev/flatlaf/intellijthemes/themes/Cyan.theme.json");
        } else if ("FlatMacDarkLaf".equals(berechtigung.getStyle())) {
            ManagerForm.getInstance().setThemes("com.formdev.flatlaf.themes.FlatMacDarkLaf");
        } else {
            ManagerForm.getInstance().setThemes("com.formdev.flatlaf.themes.FlatMacLightLaf");
        }
    }
}
