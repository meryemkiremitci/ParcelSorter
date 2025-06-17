import java.util.Random;


public class SimulationManager {
    int totalReturned = 0; // Toplam iade edilen kargo sayısı
    int totalGeneratedParcels = 0;
    int totalDispatcedParcels = 0; // Toplam gönderilen kargo sayısı
    private int currentTick = 0;
    private int maxTicks = Config.getInt("MaxTicks"); // Maksimum tick sayısı
    private Random random = new Random();
    int StackTransferTick = Config.getInt("StackTransferTick"); // Stack'ten sorter'a aktarım için gereken tick sayısı

    ArrivalBufferQueue arrivalBuffer = new ArrivalBufferQueue(Config.getInt("ArrivalBufferCapacity")); // ArrivalBufferQueue nesnesi oluştur
    ArrivalBufferQueue WaitingQueueforReturns = new ArrivalBufferQueue(-1);     // şehrin parcel listi dolu ise return kargolar için bekleme kuyruğu
    ParcelTracker tracker = new ParcelTracker(); // yeni bir ParcelTracker nesnesi oluştur
    TerminalRotator terminalRotator = new TerminalRotator(); // yeni bir TerminalRotator nesnesi oluştur
    ReturnStack stack = new ReturnStack(); // yeni bir ReturnStack nesnesi oluştur
    ReturnStack BufferStack = new ReturnStack(); // yeni bir ReturnStack nesnesi oluştur
    DestinationSorter sorter = new DestinationSorter(); // önce nesneyi oluştur
    

    public void runSimulation() {

        Logger.log("[INFO]: Simulation started.");

        terminalRotator.initializeFromCityList(); // şehir listesini başlat


        while (currentTick < maxTicks) {
            currentTick++;
            System.out.println("-------------------------");
            System.out.println("Tick #" + currentTick + "************************************************************");

            Logger.log("[INFO]: ************************Tick #" + currentTick + " started.************************");
            //ConsoleHelper.waitForEnter();

            if (!WaitingQueueforReturns.isEmpty()) {
                System.out.println("WaitingQueueforReturns is not empty, transferring parcels to sorter...");
                Logger.log("[INFO]: WaitingQueueforReturns is not empty, transferring parcels to sorter.");
                WaitingQueueforReturns.printQueue();
                System.out.println("");
                //ConsoleHelper.waitForEnter();
                int loop1 = WaitingQueueforReturns.getSize();
                    for (int i=1 ; i <= loop1 ; i++) {

                    Parcel fromWaiting = WaitingQueueforReturns.dequeue();
                    if (sorter.getCityParcels(fromWaiting.destinationCity).isFull()) {
                        System.out.println("City queue is full, " + fromWaiting.parcelID + " will enqueue back.");
                        WaitingQueueforReturns.enqueue(fromWaiting);
                        continue;
                    }
                    System.out.println("Parcel ID " + fromWaiting.getParcelID() + " is transferred from WaitingQueueforReturns to sorter.");
                    sorter.insertParcel(fromWaiting);
                    System.out.println("");
                    tracker.updateStatus(fromWaiting.getParcelID(), "Sorted");
                    Logger.log("[INFO]: Parcel with ID " + fromWaiting.getParcelID() + " is transferred from WaitingQueueforReturns to sorter.");
                }
            }

            // 3. Eğer tick 5’in katıysa, stack’teki tüm kargoları sorter’a aktar
            if (currentTick % StackTransferTick == 0) {
                if(stack.isEmpty()) {
                    System.out.println("Stack is empty, no parcels to transfer."); 
                    Logger.log("[INFO]: Stack is empty, no parcels to transfer.");
                }
                else{
                    System.out.println("\nTick is multiple of 5, Transferring from Stack to DestinationSorter...\n");
                    Logger.log("[INFO]: Size of stack is " + stack.size() + ".");
                    while (!stack.isEmpty()) {
                        Parcel fromStack = stack.pop();
                        if(sorter.getCityParcels(fromStack.destinationCity).isFull()){
                            System.out.println("City queue is full, will transfer to WaitingQueueforReturns.");
                            WaitingQueueforReturns.enqueue(fromStack);
                            tracker.updateStatus(fromStack.getParcelID(), "Waiting");
                            continue;
                        }
                        System.out.println("From Stack Parcel ID: " + fromStack.getParcelID());
                        tracker.updateStatus(fromStack.getParcelID(), "Sorted");
                        System.out.println("New status is 'Sorted'.");
                        sorter.insertParcel(fromStack);
                        sorter.getCityParcels(fromStack.destinationCity).decrementTotal(); // Toplam kargo sayısını azalt
                        
                        Logger.log("[INFO]: Parcel with ID " + fromStack.getParcelID() + " is transferred from stack to sorter.");
                    }
                }
            }

            if(!BufferStack.isEmpty()) {
                System.out.println("BufferStack is not empty, transferring parcels to ArrivalBuffer...");
                Logger.log("[INFO]: BufferStack is not empty, transferring parcels to ArrivalBuffer.");
                BufferStack.printBufferStack();
                //ConsoleHelper.waitForEnter();
                while (!arrivalBuffer.isFull() && !BufferStack.isEmpty()) {
                    System.out.println("\n--------");
                    Parcel fromBufferStack = BufferStack.pop();
                    arrivalBuffer.enqueue(fromBufferStack);
                    tracker.updateStatus(fromBufferStack.getParcelID(), "InQueue");
                    System.out.println("Parcel id=" + fromBufferStack.getParcelID() + " | Status= " + tracker.get(fromBufferStack.getParcelID()));
                    System.out.println("Parcel with ID " + fromBufferStack.getParcelID() + " is transferred from BufferStack to ArrivalBuffer.");
                    //ConsoleHelper.waitForEnter();

                    Logger.log("[INFO]: Parcel with ID " + fromBufferStack.getParcelID() + " is transferred from BufferStack to ArrivalBuffer.");
                }
                System.out.println("");
                arrivalBuffer.printQueue();
            }
            else {
                generateParcels();
            }


            System.out.println("\n<<<<<<<<Enqueuing for BST>>>>>>>>>\n"); 

            int loop = arrivalBuffer.getSize();

            for (int i=1 ; i <= loop ; i++) {
                Parcel p = arrivalBuffer.dequeue(); // ArrivalBuffer'dan kargoyu al  
                if( sorter.cityExists(p.destinationCity) == false || sorter.findCityNode(p.destinationCity).parcelList.getSize()< Config.getInt("CityQueueSize") ) {
                    System.out.println("1 \n");
                    System.out.println("Parcel with ID " + p.getParcelID() + " is being processed.");
                    Logger.log("[INFO]: Parcel with ID " + p.getParcelID() + " is dequeued from ArrivalBuffer.");
                    sorter.insertParcel(p); // sonra metodu çağır
                    tracker.updateStatus(p.getParcelID(), "Sorted");
                }
             
                /*else if(){
                    System.out.println("2 \n");
                    System.out.println("Parcel with ID " + p.getParcelID() + " is being processed.");
                    Logger.log("[INFO]: Parcel with ID " + p.getParcelID() + " is dequeued from ArrivalBuffer.");
                    sorter.insertParcel(p); // sonra metodu çağır
                    tracker.updateStatus(p.getParcelID(), "Sorted");
                }*/

                else{
                    System.out.println("2\n");
                  
                    System.out.println("City queue is full couldn't sort, parcel will be enqueued back to ArrivalBuffer.");

                    arrivalBuffer.enqueue(p);
                    //ConsoleHelper.waitForEnter();
                    Logger.log("[ERROR]: City queue is full and parcel is not sorted.");
                    Logger.log("[INFO]: Parcel is dequeued and enqueued to arrivalBuffer. Parcel is still InQueue.");
                }
            }
            
            Logger.log("[INFO]: All parcels are dequeued from ArrivalBuffer and, inserted and enqueued into DestinationSorter.");
            Logger.log("[INFO]: All parcels are sorted and updated in ParcelTracker.");

            sorter.printCities(); // şehir isimlerini sıralı şekilde yazdır
            Logger.log("[INFO]: All cities in BTS are printed with its content");
            System.out.println("\n");
            System.out.println("The number of cities is " + sorter.countCities());
            Logger.log("[INFO]: The number of cities is " + sorter.countCities());
            System.out.println();
            System.out.println("City with the most parcels: " + sorter.getMaxParcelCity());
            System.out.println();
            tracker.printTable();// güncellenmiş ParcelTracker tablosunu yazdır
        
            // Terminal rotatoru başlat
            // aktif terminalden başlarız 
            System.out.println("\n");

            terminalRotator.printTerminalOrder(); // şehirlerin sıralı listesini yazdır
            Logger.log("[INFO]: Terminal order is printed.");
            System.out.println("\n");

            // burda içeriği olan termial bulana kadar döngüye giriyoruz
            while(!(sorter.cityExists(terminalRotator.getActiveTerminal()) && sorter.countCityParcels(terminalRotator.getActiveTerminal())>=1)){
                terminalRotator.advanceTerminal();

            }
                String activeCity = terminalRotator.getActiveTerminal();

                System.out.println("Active City: " + activeCity);
                System.out.println("\n");
            
                Logger.log("[INFO]: " + activeCity + ", is active city.");

                sorter.getCityParcels(activeCity); // aktif şehirdeki paketleri al
                ArrivalBufferQueue queue = sorter.getCityParcels(activeCity);
                int size = queue.getSize(); // Kuyruğun ilk boyutunu al
                
                for (int i = 0; i < size; i++) {
                    Parcel parcel = queue.dequeue(); // sıradaki kargoyu al

                    // %10 ihtimalle "Returned", %90 ihtimalle "Dispatched"
                    double chance = Math.random(); // 0.0 ile 1.0 arasında bir sayı üretir
                    double returnProbability = Config.getDouble("ReturnProbability");

                    if (chance <= returnProbability) {
                        tracker.updateStatus(parcel.getParcelID(), "Returned");
                        stack.push(parcel); // kargoyu stack'e ekle
                        totalReturned++;
                        Logger.log("[INFO]: Parcel with ID " + parcel.getParcelID() + " is returned and pushed to the stack.");
                        Logger.logToFile("DispatchedReturnedsAndReport.txt","[INFO]: Parcel ID: " + parcel.getParcelID() + "| Status: " + tracker.get(parcel.getParcelID()));          
                    } 
                    else {
                        tracker.updateStatus(parcel.getParcelID(), "Dispatched");
                        totalDispatcedParcels++;
                        Logger.log("[INFO]: Parcel with ID " + parcel.getParcelID() + " is dispatched.");
                        System.out.println("Parcel ID: " + parcel.getParcelID() + "| Status: " + tracker.get(parcel.getParcelID()));
                        Logger.logToFile("DispatchedReturnedsAndReport.txt","[INFO]: Parcel ID: " + parcel.getParcelID() + "| Status: " + tracker.get(parcel.getParcelID()));          
                    }
                }
                Logger.log("[INFO]: Queue that in active city is now  empty.");
                System.out.println();
                // yığın içeriğini yazdır
                stack.printStack();
                Logger.log("[INFO]: Return stack is printed."); 
                System.out.println("\n");                
            

            terminalRotator.advanceTerminal(); // eskide kalan active city değiştirilir 

            Logger.log("[INFO]: Terminal Rotator advanced to the next city.");

            System.out.println("Dispatched parcels are removed from ParcelTracker table.\n");

            tracker.removeDispatchedParcels(); // Dispatched olanları sil

            tracker.printTable(); // güncellenmiş ParcelTracker tablosunu yazdır

            Logger.log("[INFO]: Updated ( Dispatched parcels are deleted ) ParcelTracker table is printed.");
        }

        Logger.logToFile("DispatchedReturnedsAndReport.txt", "[INFO]: " + totalGeneratedParcels + " parcels are generated.");
        Logger.logToFile("DispatchedReturnedsAndReport.txt", "[INFO]: " + currentTick + " ticks executed.");
        Logger.logToFile("DispatchedReturnedsAndReport.txt", "[INFO]: " + totalReturned + " parcels are returned.");
        Logger.logToFile("DispatchedReturnedsAndReport.txt", "[INFO]: " + totalDispatcedParcels + " parcels are dispatched.");
        Logger.logToFile("DispatchedReturnedsAndReport.txt", "[INFO]: " + sorter.findCityWithMaxParcels() + " is the city with the most parcels.");
        Logger.logToFile("DispatchedReturnedsAndReport.txt", "[INFO]: " + arrivalBuffer.getMaxSizeReached() + " is the maximum size of ArrivalBuffer reached during the simulation.");
        Logger.logToFile("DispatchedReturnedsAndReport.txt", "[INFO]: " + stack.getMaxSizeReached() + " is the maximum size of ReturnStack reached during the simulation.");
        Logger.logToFile("DispatchedReturnedsAndReport.txt", "[INFO]: " + sorter.getBSTHeight() + " is the height of the Binary Search Tree (BST) used for sorting parcels by city.");
        Logger.logToFile("DispatchedReturnedsAndReport.txt", "[INFO]: " + tracker.getLoadFactor() + " is the load factor of the ParcelTracker hash table.");
        System.out.println("\nCity with the most parcels: " + sorter.getMaxParcelCity());
        tracker.logLastStatusCounts();
        System.out.println();
        sorter.printAllCityParcelCounts();
        System.out.println("Total parcels returned: " + totalReturned);
        System.out.println("Total generated parcels: " + totalGeneratedParcels);
    }

    private void generateParcels() {
        int min = Config.getInt("MinParcelsPerTick");
        int max = Config.getInt("MaxParcelsPerTick"); ;
        int count = random.nextInt(max - min + 1) + min;
        //totalGeneratedParcels = count + totalGeneratedParcels - totalReturnedAndSortedBackParcels ;

        for (int i = 0; i < count; i++) {

            String city = pickRandomCity();
            int priority = pickRandomPriority();
            String size = pickRandomSize();

            System.out.println("---------------------------");


            Parcel parcel = new Parcel(city, priority, size, currentTick);
            System.out.println("New Parcel: ID=" + parcel.parcelID + "\nDestination City: " + city + "\nPriority: " + priority + "\nSize: " + size + "\nArrival Tick: " + currentTick);
            totalGeneratedParcels++;

            Logger.log("[INFO]: \nNew parcel: ID= " + parcel.parcelID + "\nDestination City= " + city + "\nPriority= " + priority + "\nSize= " + size );
            System.out.println();

            if (arrivalBuffer.isFull()) {
                tracker.insert(parcel.getParcelID(), "Waiting");
                BufferStack.push(parcel); // kargo yığına ekleniyor
                System.out.println("ArrivalBuffer is full. Parcel with ID " + parcel.getParcelID() + " is in BufferStack and waiting.");
                Logger.log("[INFO]: ArrivalBuffer is full. Parcel with ID " + parcel.getParcelID() + " is in BufferStack and waiting.");
                //ConsoleHelper.waitForEnter();
            }
            else {
                tracker.insert(parcel.getParcelID(), "InQueue");
                arrivalBuffer.enqueue(parcel); // kargo ArrivalBuffer'a ekleniyor
            }

        }


        //EN SON EĞER ŞEHİRİN QUEUESİ DOLARSA ARRİVAL BUFFERDA İNQUEUE OLARAK GERİ EKLENECEĞİ YAPTIK

        Logger.log("[INFO]: "+ arrivalBuffer.getSize() + " parcels are generated, enqueued to the ArrivalBuffer and stated as InQueue.");
        System.out.println("Number of elements in ArrivalBuffer: " + arrivalBuffer.getSize());
        Logger.log("[INFO]: Number of elements in ArrivalBuffer: " + arrivalBuffer.getSize());
        arrivalBuffer.printQueue();
        //arrivalBuffer.realcount = 0; // realcount'u sıfırla
        System.out.println();
        System.out.println("Parcel Tracker Table:");
        tracker.printTable();
    }

    // Yardımcı fonksiyonlar:
    private String pickRandomCity() {
        String[] cities = {"Istanbul", "Ankara", "Izmir","Antalya", "Bursa"};
        return cities[random.nextInt(cities.length)];
    }

    private int pickRandomPriority() {
    return random.nextInt(3) + 1;  // 1, 2 veya 3 döner
}


    private String pickRandomSize() {
        String[] sizes = {"Small", "Medium", "Large"};
        return sizes[random.nextInt(sizes.length)];
    }
}
