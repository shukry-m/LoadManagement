package lk.iot.lmsApp.model;

public class HomeAppliance {

    private String H_ID;
    private String USER_ID;
    private String H_LABEL;
    private String T_5_TO_8;
    private String T_8_TO_17;
    private String T_17_TO_22;
    private String T_22_TO_5;
    private String M__STATUS;

    public HomeAppliance() {
    }

    public String getH_ID() {
        return H_ID;
    }

    public void setH_ID(String h_ID) {
        H_ID = h_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getH_LABEL() {
        return H_LABEL;
    }

    public void setH_LABEL(String h_LABEL) {
        H_LABEL = h_LABEL;
    }

    public String getT_5_TO_8() {
        return T_5_TO_8;
    }

    public void setT_5_TO_8(String t_5_TO_8) {
        T_5_TO_8 = t_5_TO_8;
    }

    public String getT_8_TO_17() {
        return T_8_TO_17;
    }

    public void setT_8_TO_17(String t_8_TO_17) {
        T_8_TO_17 = t_8_TO_17;
    }

    public String getT_17_TO_22() {
        return T_17_TO_22;
    }

    public void setT_17_TO_22(String t_17_TO_22) {
        T_17_TO_22 = t_17_TO_22;
    }

    public String getT_22_TO_5() {
        return T_22_TO_5;
    }

    public void setT_22_TO_5(String t_22_TO_5) {
        T_22_TO_5 = t_22_TO_5;
    }

    public String getM__STATUS() {
        return M__STATUS;
    }

    public void setM__STATUS(String m__STATUS) {
        M__STATUS = m__STATUS;
    }

    @Override
    public String toString() {
        return "HomeAppliance{" +
                "H_ID='" + H_ID + '\'' +
                ", USER_ID='" + USER_ID + '\'' +
                ", H_LABEL='" + H_LABEL + '\'' +
                ", T_5_TO_8='" + T_5_TO_8 + '\'' +
                ", T_8_TO_17='" + T_8_TO_17 + '\'' +
                ", T_17_TO_22='" + T_17_TO_22 + '\'' +
                ", T_22_TO_5='" + T_22_TO_5 + '\'' +
                ", M__STATUS='" + M__STATUS + '\'' +
                '}';
    }
}
