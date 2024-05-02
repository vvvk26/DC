import java.util.HashSet;
import java.util.Set;

class Process {
    private final int processId;
    private boolean requestingCriticalSection;
    private Set<Integer> deferredReplies;

    public Process(int processId) {
        this.processId = processId;
        this.requestingCriticalSection = false;
        this.deferredReplies = new HashSet<>();
    }

    public void requestCriticalSection() {
        requestingCriticalSection = true;
        System.out.println("Process " + processId + " requests critical section.");
        // Broadcast REQUEST message to all other processes
        // Simulate sending messages
    }

    public void receiveRequest(int senderId) {
        if (!requestingCriticalSection || senderId != processId) {
            // Send a REPLY message to the sender
            System.out.println("Process " + processId + " sends REPLY to Process " + senderId);
        } else {
            // Defer the reply
            deferredReplies.add(senderId);
        }
    }

    public void releaseCriticalSection() {
        requestingCriticalSection = false;
        System.out.println("Process " + processId + " releases critical section.");
        // Send REPLY messages to all deferred processes
        for (int deferredProcess : deferredReplies) {
            System.out.println("Process " + processId + " sends REPLY to Process " + deferredProcess);
        }
        deferredReplies.clear();
    }
}

public class RicartAgrawalaAlgorithm {
    public static void main(String[] args) {
        int numProcesses = 5; // Number of processes in the ring

        // Create processes
        Process[] processes = new Process[numProcesses];
        for (int i = 0; i < numProcesses; i++) {
            processes[i] = new Process(i);
        }

        // Simulate process requests
        processes[2].requestCriticalSection(); // Example: Process 2 requests critical section
        // ... (other processes request as needed)

        // Simulate process replies
        processes[1].receiveRequest(2); // Example: Process 1 receives request from Process 2
        // ... (other processes receive requests as needed)

        // Simulate process releases
        processes[2].releaseCriticalSection(); // Example: Process 2 releases critical section
    }
}
