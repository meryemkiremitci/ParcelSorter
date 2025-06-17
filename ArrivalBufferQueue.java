public class ArrivalBufferQueue {
    private Node front;
    private Node rear;
    private int size;
    private int maxCapacity;
    private int totalParcels = 0; // Toplam kargo sayısı
    private int maxSizeReached = 0;


    public ArrivalBufferQueue(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.front = null;
        this.rear = null;
        this.size = 0;
        this.totalParcels = 0; // Toplam kargo sayısını başlat
    }

    private static class Node {
        Parcel parcel;
        Node next;

        public Node(Parcel parcel) {
            this.parcel = parcel;
            this.next = null;
        }
    }



    public int getSize() {
        return size;
    }

    public void decrementTotal() {
        this.totalParcels--;
    }


    public int totalParcels () {
        return totalParcels;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == maxCapacity;
    }

    public void enqueue(Parcel parcel) {
        if (isFull()) {
            System.out.println("ArrivalBuffer is full. Cannot enqueue " + parcel.getParcelID());
            Logger.log("[ERROR]: ArrivalBuffer is full. Cannot enqueue parcel with ID: " + parcel.getParcelID());
            return;
        }

        Node newNode = new Node(parcel);

        if (isEmpty()) {
            front = newNode;
        } else {
            rear.next = newNode;
        }

        rear = newNode;
        size++;
        totalParcels++;

        // Burada maksimum boyutu kontrol ediyoruz
        if (size > maxSizeReached) {
            maxSizeReached = size;
        }

        System.out.println("Enqueued parcel with ID: " + parcel.getParcelID());
        Logger.log("[INFO]: Enqueued parcel with ID: " + parcel.getParcelID());
        System.out.println();
    }

    public int getMaxSizeReached() {
        return maxSizeReached;
    }



    public void printQueue() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        Node current = front;
        System.out.print("All parcels in Queue: ");
        while (current != null) {
            System.out.print(current.parcel.getParcelID() + " ");
            current = current.next;
        }
        System.out.println();
    }

    public Parcel dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty. Cannot dequeue.");
            return null;
        }

        Parcel removedParcel = front.parcel;  // En öndeki Parcel'ı al
        front = front.next;                  // Head bir sonraki düğüme geçer
        size--;

        if (front == null) {
            rear = null;  // Kuyruk tamamen boşsa tail de null olmalı
        }

        return removedParcel;
    }

    public Parcel peek(){
        if(front == null) return null;
        return front.parcel;
    }


}

    /*public void enqueueforBuffer(Parcel parcel) {

        if (isFull()) {
            System.out.println("ArrivalBuffer is full. Cannot enqueue " + parcel.getParcelID());
            Logger.log("[ERROR]: ArrivalBuffer is full. Cannot enqueue parcel with ID: " + parcel.getParcelID());
            return;
        }
        Node newNode = new Node(parcel);
        
        if (isEmpty()) {
            front = newNode;
        } else {
            rear.next = newNode;
        }
        realcount++;

        System.out.println("Enqueued parcel with ID: " + parcel.getParcelID());
        Logger.log("[INFO]: Enqueued parcel with ID: " + parcel.getParcelID());
        System.out.println();
        rear = newNode;
    }
    public Parcel dequeueC() {
        if (isEmpty()) {
            System.out.println("Queue is empty. Cannot dequeue.");
            return null;
        }

        Parcel removedParcel = front.parcel;  // En öndeki Parcel'ı al
        front = front.next;                  // Head bir sonraki düğüme geçer
        size--;

        if (front == null) {
            rear = null;  // Kuyruk tamamen boşsa tail de null olmalı
        }

        return removedParcel;
    }
    public void enqueueForCities(Parcel parcel) {

            if (isFullForCities()) {
                System.out.println("City queue is full. Cannot enqueue " + parcel.getParcelID());
                Logger.log("[ERROR]: City queue is full. Cannot enqueue parcel with ID: " + parcel.getParcelID());
                return;
            }
            Node newNode = new Node(parcel);
            if (isEmptyCities()) {
                front = newNode;
            } else {
                rear.next = newNode;
            }

            System.out.println("Enqueued parcel with ID: " + parcel.getParcelID());
            Logger.log("[INFO]: Enqueued parcel with ID: " + parcel.getParcelID());
            System.out.println();
            rear = newNode;
            size++;
            totalsize++;
    }*/
    
    /*public boolean isEmpty() {
        return realcount == 0;
    }

    public boolean isEmptyCities() {
        return size == 0;
    }
    public void printQueueC() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        Node current = front;
        System.out.print("All parcels in Queue: ");
        while (current != null) {
            System.out.print(current.parcel.getParcelID() + " ");
            current = current.next;
        }
        System.out.println();
    }
    public boolean isFull() {
        return realcount == MAX_SIZE;
    }
    public int realcount() {
        return realcount;
    }
    public boolean isFullForCities() {
        return size == MAX_SIZE_C;
    } */    //private final int MAX_SIZE = 5; // buffer için 
    //private final int MAX_SIZE_C = 5; // Maksimum şehir sayısı
    //int realcount = 0;
    //int totalsize = 0;