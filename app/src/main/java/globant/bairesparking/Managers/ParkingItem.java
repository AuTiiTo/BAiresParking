package globant.bairesparking.Managers;

/**
 * @author s.ruiz
 */

public class ParkingItem{
    private long id;
    private float cost_per_hour;
    private String street;
    private int number;
    private int available_places;
    private boolean favorite;
    private boolean closed;
    private float cost_12_hours;
    private int total_places;
    private String corner;
    private double latitude;
    private double longitude;
    private float distance;

    public long getId() {
        return id;
    }

    public int getAvailable_places() {
        return available_places;
    }

    public float getCost_per_hour() {
        return cost_per_hour;
    }

    public String getStreet() {
        return street;
    }

    public int getNumber() {
        return number;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean getClosed() {
        return closed;
    }

    public float getCost_12_hours() {
        return cost_12_hours;
    }

    public int getTotal_places() {
        return total_places;
    }

    public String getCorner() {
        return corner;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
    @Override
    public String toString() {
        return "ParkingItem{" +
                "id=" + id +
                ", closed=" + closed +
                ", available_places=" + available_places +
                ", cost_per_hour=" + cost_per_hour +
                ", cost_12_hours=" + cost_12_hours +
                ", total_places=" + total_places +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", corner='" + corner + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
