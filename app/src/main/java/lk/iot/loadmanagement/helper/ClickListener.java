package lk.iot.loadmanagement.helper;

import android.view.View;
import android.widget.CompoundButton;

public interface ClickListener extends CompoundButton.OnCheckedChangeListener {
    void onPositionClicked(int position, View view);
     void onCheckedChanged(CompoundButton cb, boolean on);
     void onCheckedChanged(int position,CompoundButton cb, boolean on);
}
