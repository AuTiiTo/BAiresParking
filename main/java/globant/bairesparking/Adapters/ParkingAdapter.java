package globant.bairesparking.Adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import globant.bairesparking.Commons.ParkingsDBHelper;
import globant.bairesparking.Managers.ParkingItem;
import globant.bairesparking.R;

/**
 * @author s.ruiz
 */

public class ParkingAdapter extends RecyclerView.Adapter<ParkingViewHolder> implements Filterable {

    private List<ParkingItem> originalList;
    private List<ParkingItem> filteredList;

    private Context mContext;
    private ParkingsDBHelper parkingDB;
    private FavoriteAdapter favAd;
    private CustomFilter filter;

    public ParkingAdapter(Context context, FavoriteAdapter favoriteAdapter) {
        parkingDB = new ParkingsDBHelper(context);
        parkingDB.onCreate(parkingDB.getWritableDatabase());
        favAd = favoriteAdapter;

        originalList = new ArrayList<>();
        filteredList = new ArrayList<>();
        filter = new CustomFilter();
    }

    @Override
    public ParkingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_parking, null);
        ParkingViewHolder parkingViewHolding = new ParkingViewHolder(itemLayoutView);
        return parkingViewHolding;
    }

    @Override
    public void onBindViewHolder(final ParkingViewHolder holder, int position) {
        final ParkingItem item = filteredList.get(position);
        mContext = holder.itemView.getContext();

        final String direccion = item.getStreet()
                                     .concat(" ")
                                     .concat(String.valueOf(item.getNumber()));
        holder.nombre.setText(String.valueOf(direccion));
        float metersTo = item.getDistance();
        String distance = "";
        if (metersTo > 1000) {
            metersTo /= 1000.00;
            distance = String.format(Locale.US, "%.2f", metersTo).concat(" kmts");
        } else {
            distance = String.format(Locale.US, "%.2f", metersTo).concat(" mts");
        }

        holder.distancia.setText(distance);
        if (item.getCorner() != null && !item.getCorner().isEmpty()) {
            String corner = "\nEsq. ".concat(item.getCorner());
            holder.direccion.setText(corner);
        } else {
            holder.direccion.setText("");
        }
        holder.precio.setText(mContext.getString(R.string.price_hour, String.valueOf(item.getCost_per_hour())));
        int availability = itemAvailability(item.getAvailable_places(), item.getTotal_places());
        switch (availability) {
            case Constant.HIGH_AVAILABILITY:
                holder.disponibilidad.setText(mContext.getString(R.string.high_availability));
                holder.disponibilidad.setTextColor(mContext.getResources().getColor(R.color.high));
                break;
            case Constant.MEDIUM_AVAILABILITY:
                holder.disponibilidad.setText(mContext.getString(R.string.medium_availability));
                holder.disponibilidad.setTextColor(mContext.getResources().getColor(R.color.medium));
                break;
            case Constant.LOW_AVAILABILITY:
                holder.disponibilidad.setText(mContext.getString(R.string.low_availability));
                holder.disponibilidad.setTextColor(mContext.getResources().getColor(R.color.low));
                break;
            default:
                holder.disponibilidad.setText(mContext.getString(R.string.no_availability));
                holder.disponibilidad.setTextColor(mContext.getResources().getColor(R.color.no));
        }
        if (item.isFavorite()) {
            holder.fav.setImageDrawable(mContext.getDrawable(android.R.drawable.star_big_on));
        } else {
            holder.fav.setImageDrawable(mContext.getDrawable(android.R.drawable.star_big_off));
        }
        setClickListener(holder, item, parkingDB);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void addParking(ParkingItem parkingItem, Location mLastLocation) {
        if (parkingItem.getCost_per_hour() > 0) {
            parkingItem.setDistance(itemDistance(mLastLocation, parkingItem));
            filteredList.add(parkingItem);
            originalList.add(parkingItem);
            sortList();
            notifyDataSetChanged();
        }
    }

    public void sortList() {
        Collections.sort(filteredList, new Comparator<ParkingItem>() {
            @Override
            public int compare(ParkingItem item1, ParkingItem item2) {
                return String.valueOf(item1.getDistance()).compareTo(String.valueOf(item2.getDistance()));
            }
        });
    }

    public void setClickListener(final ParkingViewHolder holder, final ParkingItem parkingItem, final ParkingsDBHelper parkingDB) {

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parkingItem.isFavorite()) {
                    removeFav(parkingItem);
                } else {
                    insertFav(parkingItem);
                }
                notifyItemChanged(originalList.indexOf(parkingItem));
            }

            public void removeFav(ParkingItem item) {
                parkingDB.deleteFavorite(parkingItem.getId());
                parkingItem.setFavorite(false);
                favAd.removeFavorite(item);
            }

            public void insertFav(ParkingItem item) {
                parkingDB.insertFavorite(parkingItem.getId());
                parkingItem.setFavorite(true);
                favAd.addFavorite(item);
            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                String uri = "tel: " + String.valueOf(parkingItem.getId());
                intentCall.setData(Uri.parse(uri));
                view.getContext().startActivity(intentCall);
            }
        });

        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMap = new Intent(Intent.ACTION_VIEW);
                String uri = "geo:0,0?q=" + String.valueOf(parkingItem.getLatitude()) + "," + String.valueOf(parkingItem.getLongitude()) + "" +
                        "(" + parkingItem.getStreet() + " " + parkingItem.getNumber() + ")";
                intentMap.setData(Uri.parse(uri));
                view.getContext().startActivity(intentMap);
            }
        });
    }

    public int itemAvailability(int available, int total) {
        if (total > 0 && available > 0) {
            if ((available * 100) / total > 50)
                return Constant.HIGH_AVAILABILITY;
            else if ((available * 100) / total > 20)
                return Constant.MEDIUM_AVAILABILITY;
            else
                return Constant.LOW_AVAILABILITY;
        }
        return Constant.NO_AVAILABILITY;
    }

    public float itemDistance(Location mobileLocation, ParkingItem item) {
        Location itemLocation = new Location("");
        itemLocation.setLatitude(item.getLatitude());
        itemLocation.setLongitude(item.getLongitude());
        float distanceTo = 0;
        try {
            distanceTo = mobileLocation.distanceTo(itemLocation);
        } catch (Exception exp) {
            distanceTo = 0;
        }
        return distanceTo;
    }

    public void updateLocation(Location location) {
        for (ParkingItem item : originalList) {
            item.setDistance(itemDistance(location, item));
        }
        sortList();
        notifyDataSetChanged();
    }

    public void cleanAdapter() {
        filteredList.clear();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults resultsToReturn = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) { //This happens only when input is empty
                resultsToReturn.values = originalList;
                resultsToReturn.count = originalList.size();
                return resultsToReturn;
            }


            String searchTerm = charSequence.toString().toLowerCase();

            List<ParkingItem> sourceList = originalList;
            List<ParkingItem> filteredList = new ArrayList<>();

            for (ParkingItem parking : sourceList) {
                final String currentStreet = parking.getStreet().toLowerCase();
                if (currentStreet.contains(searchTerm)) {
                    filteredList.add(parking);
                }
            }

            resultsToReturn.values = filteredList;
            resultsToReturn.count = filteredList.size();

            return resultsToReturn;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredList = (List<ParkingItem>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
