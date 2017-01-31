package globant.bairesparking.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import globant.bairesparking.R;

/**
 * @author s.ruiz
 */

public class EmptyFavoriteAdapter extends RecyclerView.Adapter<EmptyFavoriteViewHolder> {
    @Override
    public EmptyFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View emptyLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_parking_empty, parent, false);
        EmptyFavoriteViewHolder emptyViewHolder = new EmptyFavoriteViewHolder(emptyLayoutView);
        return emptyViewHolder;
    }

    @Override
    public void onBindViewHolder(EmptyFavoriteViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
