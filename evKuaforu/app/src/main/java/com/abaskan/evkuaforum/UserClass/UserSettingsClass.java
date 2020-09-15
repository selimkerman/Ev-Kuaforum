package com.abaskan.evkuaforum.UserClass;

public class UserSettingsClass {
    private String settingName;
    private String SettingIconName;

    public UserSettingsClass() {
    }

    public UserSettingsClass(String settingName, String settingIconName) {
        this.settingName = settingName;
        SettingIconName = settingIconName;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public String getSettingIconName() {
        return SettingIconName;
    }

    public void setSettingIconName(String settingIconName) {
        SettingIconName = settingIconName;
    }
}
