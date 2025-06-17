public class TerminalRotator {
    private TerminalNode currentActiveTerminal; // aktif şehir
    private TerminalNode head; // liste başı

    // Şehir listesiyle dairesel liste oluşturur
    public void initializeFromCityList() {

        String[] cityArray = Config.getCitiesFromConfig("config.txt");

        if (cityArray == null || cityArray.length == 0) return;

        head = new TerminalNode(cityArray[0]);
        TerminalNode current = head;

        for (int i = 1; i < cityArray.length; i++) {
            current.next = new TerminalNode(cityArray[i]);
            current = current.next;
        }

        current.next = head; // Circular bağlantı
        currentActiveTerminal = head; // ilk aktif şehir
    }

    // Aktif terminali sıradaki şehre geçirir
    public void advanceTerminal() {
        if (currentActiveTerminal != null) {
            currentActiveTerminal = currentActiveTerminal.next;
        }
    }

    // Şu anki aktif terminalin adını verir
    public String getActiveTerminal() {
        if (currentActiveTerminal != null) {
            return currentActiveTerminal.cityName;
        }
        return null;
    }

    public void printTerminalOrder() {
        if (head == null) return;

        TerminalNode temp = head;
        do {
            System.out.print(temp.cityName + " -> ");
            temp = temp.next;
        } while (temp != head);
    }

    class TerminalNode {
        String cityName;
        TerminalNode next;

        public TerminalNode(String cityName) {
            this.cityName = cityName;
            this.next = null;
        }
    }
}

