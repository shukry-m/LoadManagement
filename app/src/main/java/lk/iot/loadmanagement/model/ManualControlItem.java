package lk.iot.loadmanagement.model;

public class ManualControlItem {

    private String homeApplianceName;
    private int MA_HA_ID;
    private int MA_HA_STATUS;

    public ManualControlItem() {
    }

    public ManualControlItem(String homeApplianceName, int MA_HA_ID, int MA_HA_STATUS) {
        this.homeApplianceName = homeApplianceName;
        this.MA_HA_ID = MA_HA_ID;
        this.MA_HA_STATUS = MA_HA_STATUS;
    }

    public String getHomeApplianceName() {
        return homeApplianceName;
    }

    public void setHomeApplianceName(String homeApplianceName) {
        this.homeApplianceName = homeApplianceName;
    }

    public int getMA_HA_ID() {
        return MA_HA_ID;
    }

    public void setMA_HA_ID(int MA_HA_ID) {
        this.MA_HA_ID = MA_HA_ID;
    }

    public int getMA_HA_STATUS() {
        return MA_HA_STATUS;
    }

    public void setMA_HA_STATUS(int MA_HA_STATUS) {
        this.MA_HA_STATUS = MA_HA_STATUS;
    }
}
