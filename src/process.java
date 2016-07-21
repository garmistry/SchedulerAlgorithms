/**
 * Created by sagarmistry on 2/20/16.
 */
public class process {
    String name;
    int arrival;
    int originalBurstCount;
    int burstCount;
    int arrivalFlag;
    int finishTime;



    public String getName() {
        return name;
    }

    public int getArrival() {
        return arrival;
    }

    public int getBurstCount() {
        return burstCount;
    }

    public int getOriginalBurstCount() { return originalBurstCount; }
    public int getArrivalFlag() { return arrivalFlag;}

    public void reduceBurstCount() {
       if(burstCount!= 0)
           burstCount--;

    }
    public int getFinishTime(){ return finishTime;}

    public void setName(String name) {
        this.name = name;
    }

    public void setArrival(int arrival) {
        this.arrival = arrival;
    }

    public void setBurstCount(int burstCount) {
        this.burstCount = burstCount;

    }
    public void setOriginalBurstCount(int savedBurstCount){
        this.originalBurstCount = savedBurstCount;
    }

    public void setArrivalFlag(int value){
            this.arrivalFlag = value;
    }

    public void setFinishTime(int value){
        this.finishTime = value;
    }
}
