public class Main2 {
    public static void main(String[] args) {
        // Config dosyasını yükle
        Config.load("config.txt");
        Logger.log("Dosya içeriği temizleme...");
        System.out.println("Welcome to the Parcel Sorting Simulation!");
        ConsoleHelper.waitForEnter();
        SimulationManager Start = new SimulationManager();
        Start.runSimulation();
    }
}
