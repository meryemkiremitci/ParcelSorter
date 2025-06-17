public class ParcelTracker {
    private final int SIZE = Config.getInt("SizeOfTable"); // Tablo boyutu
    private ParcelRecord[] table;

    public ParcelTracker() {
        table = new ParcelRecord[SIZE];
    }

    // İç sınıf: ParcelRecord
    private static class ParcelRecord {
        int parcelID;
        String status;
        ParcelRecord next;

        public ParcelRecord(int parcelID, String status) {
            this.parcelID = parcelID;
            this.status = status;
            this.next = null;
        }
    }

    public void logLastStatusCounts() {
        int waitingCount = 0;
        int inQueueCount = 0;
        int sortedCount = 0;
        int returnedCount = 0;

        for (int i = 0; i < SIZE; i++) {
            ParcelRecord current = table[i];
            while (current != null) {
                switch (current.status) {
                    case "Waiting":
                        waitingCount++;
                        break;
                    case "InQueue":
                        inQueueCount++;
                        break;
                    case "Sorted":
                        sortedCount++;
                        break;
                    case "Returned":
                        returnedCount++;
                        break;
                }
                current = current.next;
            }
        }

        // Loglama isteğe bağlı
        Logger.logToFile("DispatchedReturnedsAndReport.txt","[INFO]: Waiting = " + waitingCount + 
                ", InQueue = " + inQueueCount + 
                ", Sorted = " + sortedCount + 
                ", Returned = " + returnedCount);
    }




    // Basit hash fonksiyonu (int kullanarak)
    private int hash(int parcelID) {
        return Math.abs(parcelID) % SIZE;
    }

    // INSERT
    public void insert(int parcelID, String status) {
        int index = hash(parcelID);
        ParcelRecord head = table[index];

        // Aynı ID varsa ekleme
        ParcelRecord current = head;
        while (current != null) {
            if (current.parcelID == parcelID) {
                System.out.println("Parcel ID already exists.");
                return;
            }
            current = current.next;
        }

        // Yeni kayıt başa ekleniyor
        ParcelRecord newRecord = new ParcelRecord(parcelID, status);
        newRecord.next = head;
        table[index] = newRecord;
    }

    public double getLoadFactor() {
        int totalEntries = 0;

        for (int i = 0; i < table.length; i++) {
            ParcelRecord current = table[i];
            while (current != null) {
                totalEntries++;
                current = current.next;
            }
        }

        return (double) totalEntries / table.length;
    }


    public void printTable() {
        for (int i = 0; i < SIZE; i++) {
            ParcelRecord current = table[i];
            System.out.print("Index " + i + ": ");
            if (current == null) {
                System.out.println("empty");
            } else {
                while (current != null) {
                    System.out.print("[" + current.parcelID + " : " + current.status + "] -> ");
                    current = current.next;
                }
                System.out.println("null");
            }
        }
    }

    public void removeDispatchedParcels() {
        for (int i = 0; i < table.length; i++) {
            ParcelRecord current = table[i];
            ParcelRecord prev = null;

            while (current != null) {
                if ("Dispatched".equals(current.status)) {
                    // Bu düğüm silinmeli
                    if (prev == null) {
                        // İlk düğüm siliniyor
                        table[i] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    // İlerlemeye dikkat et!
                    current = (prev == null) ? table[i] : prev.next;
                } else {
                    prev = current;
                    current = current.next;
                }

            }

        }
    }



    // UPDATE
    public void updateStatus(int parcelID, String newStatus) {
        int index = hash(parcelID);
        ParcelRecord current = table[index];

        while (current != null) {
            if (current.parcelID == parcelID) {
                current.status = newStatus;
                return;
            }
            current = current.next;
        }

        System.out.println("Parcel ID not found.");
    }

    // GET
    public String get(int parcelID) {
        int index = hash(parcelID);
        ParcelRecord current = table[index];

        while (current != null) {
            if (current.parcelID == parcelID) {
                return current.status;
            }
            current = current.next;
        }

        return "Parcel ID not found.";
    }

    // EXISTS
    public boolean exists(int parcelID) {
        int index = hash(parcelID);
        ParcelRecord current = table[index];

        while (current != null) {
            if (current.parcelID == parcelID) {
                return true;
            }
            current = current.next;
        }

        return false;
    }
}
