package globant.bairesparking.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import globant.bairesparking.R;

/**
 * @author s.ruiz
 */

public class ParkingViewHolder extends RecyclerView.ViewHolder {
    public TextView nombre;
    public TextView direccion;
    public TextView distancia;
    public TextView precio;
    public TextView disponibilidad;
    public ImageView map;
    public ImageView fav;
    public ImageView call;

    public ParkingViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        nombre = (TextView) itemLayoutView.findViewById(R.id.tv_parking_name);
        direccion = (TextView) itemLayoutView.findViewById(R.id.tv_parking_direction);
        distancia = (TextView) itemLayoutView.findViewById(R.id.tv_parking_distance);
        precio = (TextView) itemLayoutView.findViewById(R.id.tv_parking_price);
        disponibilidad = (TextView) itemLayoutView.findViewById(R.id.tv_parking_availability);
        //Icons
        map = (ImageView) itemLayoutView.findViewById(R.id.iv_view_in_map);
        fav = (ImageView) itemLayoutView.findViewById(R.id.iv_fav);
        call = (ImageView) itemLayoutView.findViewById(R.id.iv_call);
    }
}
