package globant.bairesparking.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import globant.bairesparking.R;

/**
 * @author s.ruiz
 */

public class EmptyFavoriteViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageView fav;

    public EmptyFavoriteViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.tv_fav_empty);
        fav = (ImageView) itemView.findViewById(R.id.iv_fav_empty);
    }
}
