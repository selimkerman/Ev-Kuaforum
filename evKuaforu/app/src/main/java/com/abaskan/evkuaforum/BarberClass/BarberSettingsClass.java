package com.abaskan.evkuaforum.BarberClass;

public class BarberSettingsClass {
    private String settingName;
    private String SettingIconName;

    public BarberSettingsClass() {
    }

    public BarberSettingsClass(String settingName, String settingIconName) {
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
