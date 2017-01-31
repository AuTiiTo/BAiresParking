package globant.bairesparking.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @author s.ruiz
 */
public interface ViewTypeDelegateAdaters {
    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);
    void onBindViewHolder(ParkingViewHolder holder, int position);
}
