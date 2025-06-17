public class DestinationSorter {

    // BST düğümü
    public class BSTNode {
        String cityName;
        ArrivalBufferQueue parcelList;  // Şehir için kuyruk (FIFO)
        BSTNode left, right;

        public BSTNode(String cityName) {
            this.cityName = cityName;
            this.parcelList = new ArrivalBufferQueue(Config.getInt("CityQueueSize"));  // kendi ParcelQueue sınıfını kullan
            this.left = null;
            this.right = null;
        }
    }

    private BSTNode root;  // Ağacın kökü

    public DestinationSorter() {
        root = null;
    }

    public int countCities() {
        return countCitiesRecursive(root);
    }

    private int countCitiesRecursive(BSTNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + countCitiesRecursive(node.left) + countCitiesRecursive(node.right);
    }

    // Şehir adına göre kargo sayısını döndüren metod
    public int countCityParcels (String city){
        BSTNode node = findCityNode(root, city);  // kökten başlayarak arama
        if (node != null) {
            return node.parcelList.totalParcels();  // şehir bulunduysa kuyruğun boyutunu döner
        }
        else {
            System.out.println("City not found: " + city);
            return 0;  // şehir bulunamazsa 0 döner
        }
    }

    public ArrivalBufferQueue getCityParcels(String city) {

        BSTNode node = findCityNode(root, city);  // kökten başlayarak arama
        if (node != null) {
            return node.parcelList;
        }
        return null; // şehir yoksa null döner
    }

    public BSTNode findCityNode(String city) {
        return findCityNode(root, city);
    }

    public boolean cityExists(String city) {
        return findCityNode(city) != null;
    }



    // Yardımcı metot: BST içinde city adına göre node'u arar
    private BSTNode findCityNode(BSTNode current, String city) {
        if (current == null) return null;

        int cmp = city.compareToIgnoreCase(current.cityName);

        if (cmp == 0) return current;  // şehir bulundu
        else if (cmp < 0) return findCityNode(current.left, city);  // sola git
        else return findCityNode(current.right, city);              // sağa git
    }


    // En fazla kargo olan şehri döndüren metod
    public String getMaxParcelCity() {
        BSTNode maxCity = getMaxParcelCityRecursive(root);
        String result = (maxCity != null ? maxCity.cityName : "None");
        Logger.log("[INFO]: Max parcel city is " + result);
        return result;
    }



    //Eğer eşit sayıda kargo içeren varsa koda göre ilk seçmiş olduğunu döndürür
    private BSTNode getMaxParcelCityRecursive(BSTNode node) {
        if (node == null) return null; // Ağaç boşsa dur

        // Önce kendini en büyük varsay
        BSTNode max = node;

        // Sol çocukta daha büyük varsa onu al
        BSTNode leftMax = getMaxParcelCityRecursive(node.left);
        if (leftMax != null && leftMax.parcelList.totalParcels() > max.parcelList.totalParcels()) {
            max = leftMax;
        }

        // Sağ çocukta daha büyük varsa onu al
        BSTNode rightMax = getMaxParcelCityRecursive(node.right);
        if (rightMax != null && rightMax.parcelList.totalParcels() > max.parcelList.totalParcels()) {
            max = rightMax;
        }

        // En büyük olanı döndür
        return max;
    }



    public void insertParcel(Parcel parcel) {
        root = insertParcelRecursive(root, parcel);
    }


    // Yardımcı metot: Parcel'i BST'ye ekler eğer şehir zaten varsa kuyruğa ekler
    // eğer şehir yoksa yeni bir düğüm oluşturur
    private BSTNode insertParcelRecursive(BSTNode node, Parcel parcel) {
        if (node == null) {
            BSTNode newNode = new BSTNode(parcel.destinationCity);
            newNode.parcelList.enqueue(parcel);
            return newNode;
        }

        int cmp = parcel.destinationCity.compareTo(node.cityName);

        if (cmp == 0) {
            node.parcelList.enqueue(parcel);
        } else if (cmp < 0) {
            node.left = insertParcelRecursive(node.left, parcel);
        } else {
            node.right = insertParcelRecursive(node.right, parcel);
        }
        return node;
    }

    public String findCityWithMaxParcels() {
        return findCityWithMaxParcelsRecursive(root, null, 0);
    }

    private String findCityWithMaxParcelsRecursive(BSTNode node, String maxCity, int max) {
        if (node == null) return maxCity;

        int current = node.parcelList.totalParcels();  // ← toplam parcel sayısını alıyoruz
        if (current > max) {
            max = current;
            maxCity = node.cityName;
        }

        // Sola ve sağa dolaşırken max değeri korumalıyız:
        maxCity = findCityWithMaxParcelsRecursive(node.left, maxCity, max);
        maxCity = findCityWithMaxParcelsRecursive(node.right, maxCity, Math.max(max, current));

        return maxCity;
    }


    public void printAllCityParcelCounts() {
        printAllCityParcelCountsRecursive(root);
    }

    private void printAllCityParcelCountsRecursive(BSTNode node) {
        if (node == null) return;

        printAllCityParcelCountsRecursive(node.left);

        // Burada her şehir ve o şehre ait total bilgisi yazdırılıyor
        System.out.println("City " + node.cityName + " had " + node.parcelList.totalParcels() + " different parcels.");
        Logger.log("[INFO]: City: " + node.cityName + ", Total Parcels: " + node.parcelList.totalParcels());
        Logger.logToFile("DispatchedReturnedsAndReport.txt", "City: " + node.cityName + ", Total Parcels: " + node.parcelList.totalParcels());
        printAllCityParcelCountsRecursive(node.right);
    }

    public int getBSTHeight() {
        return getBSTHeightRecursive(root);
    }

    private int getBSTHeightRecursive(BSTNode node) {
        if (node == null) return -1; // sadece kök varsa 0, hiç yoksa -1
        int leftHeight = getBSTHeightRecursive(node.left);
        int rightHeight = getBSTHeightRecursive(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }



    // Şehir isimlerini sıralı şekilde yazdıran metod
    public void printCities() {
        inOrderTraversal(root);
    }

    private void inOrderTraversal(BSTNode node) {
        if (node == null) return;
        inOrderTraversal(node.left);
        System.out.println(node.cityName);
        node.parcelList.printQueue();
        inOrderTraversal(node.right);
    }


}
