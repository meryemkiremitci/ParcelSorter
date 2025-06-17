public class Parcel {
    private static int idCounter = 1;
    public int parcelID;
    public String destinationCity;
    public int priority;
    public String size;
    public int arrivalTick;

    public Parcel(String destinationCity, int  priority, String size, int arrivalTick) {
        this.parcelID = idCounter++;
        this.destinationCity = destinationCity;
        this.priority = priority;
        this.size = size;
        this.arrivalTick = arrivalTick;
    }

    public int getParcelID() {
        return parcelID;
    }
}
