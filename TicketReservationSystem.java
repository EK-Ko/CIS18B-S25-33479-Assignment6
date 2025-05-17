import java.util.*;

// =============================
// Immutable Ticket Class
// =============================
public final class Ticket {
    private final int id;
    private final String event;

    public Ticket(int id, String event) {
        this.id = id;
        this.event = event;
    }

    public int getId() { return id; }
    public String getEvent() { return event; }

    @Override
    public String toString() {
        return "Ticket[ID=" + id + ", Event=" + event + "]";
    }
}

// =============================
// TicketPool
// =============================
public class TicketPool {
    private int availableTickets;

    public TicketPool(int totalTickets) {
        this.availableTickets = totalTickets;
    }

    public synchronized Ticket reserveTicket(String customerName) {
        if (availableTickets > 0) {
            availableTickets--;
            System.out.println(customerName + " reserved a ticket. Tickets left: " + availableTickets);
            return new Ticket(availableTickets + 1, "Concert");
        } else {
            System.out.println(customerName + " tried to reserve but no tickets are left.");
            return null;
        }
    }
}


// =============================
// Customer (Thread)
// =============================
class Customer implements Runnable {
    private final String name;
    private final TicketPool ticketPool;

    public Customer(String name, TicketPool ticketPool) {
        this.name = name;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        Ticket ticket = ticketPool.reserveTicket(name);
        if (ticket != null) {
            System.out.println(name + " successfully reserved " + ticket);
        }
    }
}

// =============================
// Main Program
// =============================
public class TicketReservationSystem {
    public static void main(String[] args) throws InterruptedException {
        int totalTickets = 10;
        TicketPool ticketPool = new TicketPool(totalTickets);

        // Create and start customer threads
        List<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String customerName = "Customer" + i;
            Thread thread = new Thread(new Customer(customerName, ticketPool));
            threads.add(thread);
            thread.start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(thread.getName() + " was interrupted.");
            }
        }

        System.out.println("All reservations completed.");
    }
}