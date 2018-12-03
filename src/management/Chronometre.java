package management;

public class Chronometre {
    private long begin;
    private long end;
    private long current;
    private int limite;

    public Chronometre(int limite) {
        
        this.begin=0;
        this.end=0;
        this.current=0;
        this.limite=limite;
    }
    
    public void start(){
        begin = System.currentTimeMillis();
    }
 
    public void stop(){
        end = System.currentTimeMillis();
    }
 
    public long getTime() {
        return System.currentTimeMillis();
    }
 
    public long getMilliseconds() {
        return end-begin;
    }
 
    public int getSeconds() {
        return (int) ((((end - begin) / 1000.0) % 60) - 1);
    }
 
    public double getMinutes() {
        return (int) ((end - begin) / 60000.0);
    }
 
    public double getHours() {
        return (int) ((end - begin) / 3600000.0);
    }
    
    /**
    * Method to know if it remains time.
    */
    public int remainsTime() {
      //  current = System.currentTimeMillis();
     //   int timeSpent;
       // timeSpent = (int) ((current - this.begin)/1000.0);
        
      //  if(timeSpent<this.end){
      //      return true;
     //   }
      //  else{
      //      return false;
      //  }
  //  }
       current = getTime();
       int timeSpent;
       timeSpent = (int) ((begin + (limite*1000) - current)/1000.0);
       return timeSpent;
    }
}
