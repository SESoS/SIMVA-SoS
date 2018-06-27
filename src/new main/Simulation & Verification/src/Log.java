import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Log {
  
  // Save the whole information of CS Activity & Environment State per EACH TICK
  private HashMap<Integer, Snapshot> snapshotMap;
  
  // Save the final result of CS & Environment per Simulation
  // Purpose: used for property checking
  private SimulationLog simuLog;
  
  public Log() {
    this.snapshotMap = new HashMap<>();
    this.simuLog = new SimulationLog();
  }
  
  public void addSnapshot(int tick, ArrayList<Event> events, ArrayList<String> sosLogs, ArrayList<Integer> environments) {
    Snapshot tmp = new Snapshot();
    tmp.addEventLog(events);
    tmp.addSosLog(sosLogs);
    tmp.addEnvironmentLog(environments);
    
    snapshotMap.put(tick, tmp);
  }
  
  public void addSimuLog(ArrayList<CS> Css, ArrayList<Integer> Evs) {
    this.simuLog.addCsResultLog(Css);
    this.simuLog.addEnvironmentResultLog(Evs);
  }
  
  public void printSnapshot() {
    Iterator<Integer> keys = snapshotMap.keySet().iterator();
    System.out.println("===================== SNAPSHOT PRINT =====================");
    while(keys.hasNext()) {
      Integer key = keys.next();
      System.out.println("===================== TICK : " + key.toString() + " =====================");
      snapshotMap.get(key).printSnapshotLog();
    }
  }
  
  public SimulationLog getSimuLog() {
    return this.simuLog;
  }
}
