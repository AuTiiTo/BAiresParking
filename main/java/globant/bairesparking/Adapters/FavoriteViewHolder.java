package globant.bairesparking.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import globant.bairesparking.R;

/**
 * @author s.ruiz
 */

public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    public TextView direccion;
    public TextView disponibilidad;
    public TextView precio;
    public ImageView fav;

    public FavoriteViewHolder(View itemView) {
        super(itemView);
        direccion = (TextView) itemView.findViewById(R.id.tv_fav_title);
        precio = (TextView) itemView.findViewById(R.id.tv_fav_price);
        disponibilidad = (TextView) itemView.findViewById(R.id.tv_fav_places);
        fav = (ImageView) itemView.findViewById(R.id.iv_fav_on);
    }
}
