public class ReturnStack {
    private Node top;
    private int size;
    private int maxSizeReached = 0;


    // İç içe Node sınıfı
    private class Node {
        Parcel parcel;
        Node next;

        Node(Parcel parcel) {
            this.parcel = parcel;
            this.next = null;
        }
    }

    public ReturnStack() {
        top = null;
        size = 0;
    }

    // Push: Yığına ekle
    public void push(Parcel parcel) {
        if (parcel == null) {
            Logger.log("[ERROR]: Tried to push null parcel to BufferStack.");
            return;
        }

        Node newNode = new Node(parcel);
        newNode.next = top;
        top = newNode;
        size++;

        // En yüksek stack boyutunu takip et
        if (size > maxSizeReached) {
            maxSizeReached = size;
        }

        Logger.log("[INFO]: Parcel with ID " + parcel.getParcelID() + " pushed to BufferStack.");
    }

    public int getMaxSizeReached() {
        return maxSizeReached;
    }



    // Pop: En üstten çıkar
    public Parcel pop() {
        if (isEmpty()) return null;
        Parcel removed = top.parcel;
        top = top.next;
        size--;
        return removed;
    }

    // Peek: En üstteki öğeyi getir ama çıkarma
    public Parcel peek() {
        if (isEmpty()) return null;
        return top.parcel;
    }

    // isEmpty: Boş mu?
    public boolean isEmpty() {
        return top == null;
    }

    // size: Eleman sayısı
    public int size() {
        return size;
    }

    public void printStack() {
        if (isEmpty()) {
            System.out.println("Return stack is empty.");
            return;
        }

        Node current = top;
        
        System.out.println("Parcels in Return Stack:");

        while (current != null) {
            Parcel parcel = current.parcel;
            System.out.println("Parcel ID: " + parcel.getParcelID() + " | Status: Returned");
            current = current.next;
        }
    }
    
    public void printBufferStack() {
        if (isEmpty()) {
            System.out.println("Return stack is empty.");
            return;
        }

        Node current = top;
        
        System.out.println("Parcels in Buffer Stack:");
        while (current != null) {
            Parcel parcel = current.parcel;
            System.out.println("Parcel ID: " + parcel.getParcelID() + " | Status: Waiting");
            current = current.next;
        }
    }
}
