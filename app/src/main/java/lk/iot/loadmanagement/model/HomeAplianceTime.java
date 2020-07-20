package lk.iot.loadmanagement.model;

public class HomeAplianceTime {

    private String homeApplianceName;
    private int T_HA_ID;
    private int T_5_TO_8;
    private int T_8_TO_17;
    private int T_17_TO_22;
    private int T_22_TO_5;

    public HomeAplianceTime() { }

    public HomeAplianceTime(String homeApplianceName,int t_HA_ID, int t_5_TO_8, int t_8_TO_17, int t_17_TO_22, int t_22_TO_5) {
        this.homeApplianceName = homeApplianceName;
        T_HA_ID = t_HA_ID;
        T_5_TO_8 = t_5_TO_8;
        T_8_TO_17 = t_8_TO_17;
        T_17_TO_22 = t_17_TO_22;
        T_22_TO_5 = t_22_TO_5;
    }

    public String getHomeApplianceName() {
        return homeApplianceName;
    }

    public void setHomeApplianceName(String homeApplianceName) {
        this.homeApplianceName = homeApplianceName;
    }

    public int getT_HA_ID() {
        return T_HA_ID;
    }

    public void setT_HA_ID(int t_HA_ID) {
        T_HA_ID = t_HA_ID;
    }

    public int getT_5_TO_8() {
        return T_5_TO_8;
    }

    public void setT_5_TO_8(int t_5_TO_8) {
        T_5_TO_8 = t_5_TO_8;
    }

    public int getT_8_TO_17() {
        return T_8_TO_17;
    }

    public void setT_8_TO_17(int t_8_TO_17) {
        T_8_TO_17 = t_8_TO_17;
    }

    public int getT_17_TO_22() {
        return T_17_TO_22;
    }

    public void setT_17_TO_22(int t_17_TO_22) {
        T_17_TO_22 = t_17_TO_22;
    }

    public int getT_22_TO_5() {
        return T_22_TO_5;
    }

    public void setT_22_TO_5(int t_22_TO_5) {
        T_22_TO_5 = t_22_TO_5;
    }

    @Override
    public String toString() {
        return "HomeAplianceTime{" +
                "homeApplianceName='" + homeApplianceName + '\'' +
                ", T_HA_ID=" + T_HA_ID +
                ", T_5_TO_8=" + T_5_TO_8 +
                ", T_8_TO_17=" + T_8_TO_17 +
                ", T_17_TO_22=" + T_17_TO_22 +
                ", T_22_TO_5=" + T_22_TO_5 +
                '}';
    }
}
