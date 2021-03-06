import java.util.HashMap;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import env3d.advanced.EnvNode;

/**
 * This class is generated by the env3d plugin to make
 * it easier to work with models.  It scans a model
 * directory for all the obj files and organized them.
 */
public class Hunter extends Actor
{
    // The various states of this model.  Each animation is a state
    public static final int FLINCH = 0;
    public static final int MELEE = 1;
    public static final int IDLE = 2;
    public static final int JUMP = 3;
    public static final int RUN = 4;
    public static final int RANGED = 5;
    public static final int DIE = 6;
    public static final int BACKWARDS = 7;    
    
    // The main data structure for animated models, a HashMap of ArrayList
    private HashMap<Integer, ArrayList<String>> modelsMap = new HashMap<Integer, ArrayList<String>>();

    //Fields for state management
    private int state = IDLE;
    private int frame;
    
    private double speed = 0.1;
    private double prevX, prevY, prevZ;
    
    private EnvNode[] gunRange;
    private double meleeRange = 1;
    
    private int kills = 0;
    private int health = 5;
    
    private Shield shield;    
    
    private MiniMath miniMath;    
    
    /**
     * Zero-argument constructor that places the object in location 0, 0, 0
     */
    public Hunter()
    {
        this(0,0,0);
    }

    /**
     * Parameterized constructor - allows arbitary of object
     */
    public Hunter(double x, double y, double z)
    {
        setX(x);
        setY(y);
        setZ(z);
        setScale(1);
        setTexture("models/hunter/player.png");
        setModel("models/hunter/player.obj");
        // initialize the animation HashMap
        init();
    }

    public void revert()
    {
        setX(prevX);
        setY(prevY);
        setZ(prevZ);
    }  
    
    // Setup code
    public void setup()
    {
        if (gunRange == null) {
            gunRange = new EnvNode[100];
            for (int i=0; i<100; i++) {
                gunRange[i] = new EnvNode();                
                // debugging
                // getEnv().addObject(gunRange[i]);
            }
            
            // might as well initalize the miniMath game
            miniMath = new MiniMath(getEnv());
            miniMath.setMode(MiniMath.ADD);
        }        
    }
    
    /**
     * Animating the shield 
     */
    public void shieldMovement()
    {
        // Animating the shield
        if (shield != null) {
            if (shield.active()) {
                shield.setX(this.getX());
                shield.setY(this.getY());
                shield.setZ(this.getZ());                
            } else {
                getEnv().removeObject(shield);
                shield = null;
            }
        }        
    }
    
    /** 
     * The player's movement logic.  Rather complicated because we have to coordinate 
     * between idle, run, and backwards depending on key press
     */
    public void playerMovement()
    {
        if (getState() == BACKWARDS || getState() == RUN || getState() == IDLE) {
            prevX = getX(); prevY = getY(); prevZ = getZ();      
            simpleFPSControl(speed);
            
            // --- hunter run animation --
            boolean keyDown = false;
            if (getEnv().getKeyDown(Keyboard.KEY_W) || getEnv().getKeyDown(Keyboard.KEY_UP)) {
                    setState(RUN);
                    keyDown = true;
            }                
            if (getEnv().getKeyDown(Keyboard.KEY_S) || 
                getEnv().getKeyDown(Keyboard.KEY_A) || 
                getEnv().getKeyDown(Keyboard.KEY_D) || 
                getEnv().getKeyDown(Keyboard.KEY_DOWN)) {            
                    if (getState() != RUN) setState(BACKWARDS);
                    keyDown = true;
            }
            if (!keyDown && (getState() == BACKWARDS || getState() == RUN)) {
                setState(IDLE);
            }
            // -------
                      
            // --- attack animation ---
            if (getEnv().getMouseButtonDown(0) || getEnv().getKeyDown(Keyboard.KEY_SPACE)) {
                if (shield != null) {
                    setState(MELEE);
                } else {
                    setState(RANGED);
                }
            }
        }    }
    
    public void move() 
    {
        setup();
        
        shieldMovement();
        playerMovement();
        
        // setting the animation frame
        setModel(modelsMap.get(state).get(frame));
        frame = (frame+1) % modelsMap.get(state).size();
        
        // action depending on frame counter
        if (frame == 0) {
            if (getState() == DIE) {
                // Keep dead
                frame = modelsMap.get(state).size()-1;
            } else if (getState() != IDLE) {
                setState(IDLE);
            }
        }
                        
        // Attack logic - only activate at the middle of each attack animation
        if (getState() == RANGED) {
            if (frame == (modelsMap.get(getState()).size()/2)-5) {
                // Check if our shot hit any zombies
                checkRangedHit();
            }
        }
        
        if (getState() == MELEE && frame == modelsMap.get(getState()).size()/2) {
            // Kill the closest zombie
            checkMeleeHit();
        }
             
    }    

    /**
     * This method checks if the bullet hits any of the zombies
     */
    private void checkRangedHit() {
        
        // Determine the size of the bullet by asking simple math
        // keep calling play until the play has finished.
        miniMath.play();
        double bulletScale = 0.5;
        // The math game has finished
        if (miniMath.isCorrect()) {
            // the range of our weapon
            bulletScale = bulletScale + (miniMath.getTimeRemain()/150.0);
        }    
        miniMath.reset();
        
        // Every zombie in a 20 unit radius will turn to the gun sound
        for (Zombie zombie : getEnv().getObjects(Zombie.class)) {
            if (zombie.distance(this) < 20 && !zombie.isDead()) {
                zombie.turnToFace(this);
                zombie.setSeekRange(20);
            }
        }
        
      
        for (int i = 0; i < gunRange.length; i++) {             
            // Look at every zombie            
            EnvNode bullet = gunRange[i];            
            for (Zombie zombie : getEnv().getObjects(Zombie.class)) {
                double weaponX = this.getX()+i*Math.sin(Math.toRadians(getRotateY()));
                double weaponZ = this.getZ()+i*Math.cos(Math.toRadians(getRotateY()));
                bullet.setX(weaponX); bullet.setY(getY()); bullet.setZ(weaponZ); bullet.setScale(bulletScale);
                if (zombie.distance(bullet) < bullet.getScale() && !zombie.isDead()) {
                    zombie.turnToFace(this);
                    zombie.setState(Zombie.BLOWNED);
                    kills++;
                    if (kills % 3 == 0) {
                        shield = new Shield(getEnv());       
                        getEnv().addObject(shield);
                    }                    
                    return;
                }
            }           
        }
        // reset kill combo back to 0
        kills = 0;         
    }
    
    /**
     * Check short range target
     */
    private void checkMeleeHit() {
        for (Zombie z : getEnv().getObjects(Zombie.class)) {
            if (z.distance(this) < meleeRange + z.getScale() + this.getScale() && !z.isDead()) {
                if (shield != null) {
                    z.turnToFace(this);
                    z.setState(Zombie.BLOWNED);
                } else {
                    z.hit();
                }
                // Only attack one zombie at a time
                break;
            }
        }    
    }
    
    public Shield getShield()
    {
        return shield;
    }
        
    /**
     * Returns the current animation state
     */
    public int getState()
    {
        return state;
    }

    /**
     * Sets the current animation state.  Resets frame counter to 0
     * Note: it only sets the state if new state is different than
     * the current state
     */
    public void setState(int newState)
    {
        if (state != newState) {
            frame = 0;
            state = newState;
            miniMath.reset();
            if (state == FLINCH) {
                health--;
                if (health <= 0) {
                    setState(DIE);                    
                }
            }            
        }
    }

    // Initialize the modelsMap hash for animation purpose
    private void init() {
        ArrayList<String> Flinch = new ArrayList<String>();
        Flinch.add("models/hunter/Flinch/playerflinch00.obj");
        Flinch.add("models/hunter/Flinch/playerflinch01.obj");
        Flinch.add("models/hunter/Flinch/playerflinch02.obj");
        Flinch.add("models/hunter/Flinch/playerflinch03.obj");
        Flinch.add("models/hunter/Flinch/playerflinch04.obj");
        Flinch.add("models/hunter/Flinch/playerflinch05.obj");
        Flinch.add("models/hunter/Flinch/playerflinch06.obj");
        Flinch.add("models/hunter/Flinch/playerflinch07.obj");
        Flinch.add("models/hunter/Flinch/playerflinch08.obj");
        Flinch.add("models/hunter/Flinch/playerflinch09.obj");
        modelsMap.put(FLINCH,Flinch);
        ArrayList<String> Melee = new ArrayList<String>();
        Melee.add("models/hunter/Melee/playermelee00.obj");
        Melee.add("models/hunter/Melee/playermelee01.obj");
        Melee.add("models/hunter/Melee/playermelee02.obj");
        Melee.add("models/hunter/Melee/playermelee03.obj");
        Melee.add("models/hunter/Melee/playermelee04.obj");
        Melee.add("models/hunter/Melee/playermelee05.obj");
        Melee.add("models/hunter/Melee/playermelee06.obj");
        Melee.add("models/hunter/Melee/playermelee07.obj");
        Melee.add("models/hunter/Melee/playermelee08.obj");
        Melee.add("models/hunter/Melee/playermelee09.obj");
        Melee.add("models/hunter/Melee/playermelee10.obj");
        Melee.add("models/hunter/Melee/playermelee11.obj");
        Melee.add("models/hunter/Melee/playermelee12.obj");
        Melee.add("models/hunter/Melee/playermelee13.obj");
        Melee.add("models/hunter/Melee/playermelee14.obj");
        Melee.add("models/hunter/Melee/playermelee15.obj");
        Melee.add("models/hunter/Melee/playermelee16.obj");
        Melee.add("models/hunter/Melee/playermelee17.obj");
        Melee.add("models/hunter/Melee/playermelee18.obj");
        Melee.add("models/hunter/Melee/playermelee19.obj");
        Melee.add("models/hunter/Melee/playermelee20.obj");
        Melee.add("models/hunter/Melee/playermelee21.obj");
        Melee.add("models/hunter/Melee/playermelee22.obj");
        Melee.add("models/hunter/Melee/playermelee23.obj");
        Melee.add("models/hunter/Melee/playermelee24.obj");
        Melee.add("models/hunter/Melee/playermelee25.obj");
        Melee.add("models/hunter/Melee/playermelee26.obj");
        Melee.add("models/hunter/Melee/playermelee27.obj");
        Melee.add("models/hunter/Melee/playermelee28.obj");
        Melee.add("models/hunter/Melee/playermelee29.obj");
        modelsMap.put(MELEE,Melee);
        ArrayList<String> Idle = new ArrayList<String>();
        Idle.add("models/hunter/Idle/playeridle00.obj");
        Idle.add("models/hunter/Idle/playeridle01.obj");
        Idle.add("models/hunter/Idle/playeridle02.obj");
        Idle.add("models/hunter/Idle/playeridle03.obj");
        Idle.add("models/hunter/Idle/playeridle04.obj");
        Idle.add("models/hunter/Idle/playeridle05.obj");
        Idle.add("models/hunter/Idle/playeridle06.obj");
        Idle.add("models/hunter/Idle/playeridle07.obj");
        Idle.add("models/hunter/Idle/playeridle08.obj");
        Idle.add("models/hunter/Idle/playeridle09.obj");
        Idle.add("models/hunter/Idle/playeridle10.obj");
        Idle.add("models/hunter/Idle/playeridle11.obj");
        Idle.add("models/hunter/Idle/playeridle12.obj");
        Idle.add("models/hunter/Idle/playeridle13.obj");
        Idle.add("models/hunter/Idle/playeridle14.obj");
        Idle.add("models/hunter/Idle/playeridle15.obj");
        Idle.add("models/hunter/Idle/playeridle16.obj");
        Idle.add("models/hunter/Idle/playeridle17.obj");
        Idle.add("models/hunter/Idle/playeridle18.obj");
        Idle.add("models/hunter/Idle/playeridle19.obj");
        modelsMap.put(IDLE,Idle);
        ArrayList<String> Jump = new ArrayList<String>();
        Jump.add("models/hunter/Jump/playerjump00.obj");
        Jump.add("models/hunter/Jump/playerjump01.obj");
        Jump.add("models/hunter/Jump/playerjump02.obj");
        Jump.add("models/hunter/Jump/playerjump03.obj");
        Jump.add("models/hunter/Jump/playerjump04.obj");
        Jump.add("models/hunter/Jump/playerjump05.obj");
        Jump.add("models/hunter/Jump/playerjump06.obj");
        Jump.add("models/hunter/Jump/playerjump07.obj");
        Jump.add("models/hunter/Jump/playerjump08.obj");
        Jump.add("models/hunter/Jump/playerjump09.obj");
        Jump.add("models/hunter/Jump/playerjump10.obj");
        Jump.add("models/hunter/Jump/playerjump11.obj");
        Jump.add("models/hunter/Jump/playerjump12.obj");
        Jump.add("models/hunter/Jump/playerjump13.obj");
        Jump.add("models/hunter/Jump/playerjump14.obj");
        Jump.add("models/hunter/Jump/playerjump15.obj");
        Jump.add("models/hunter/Jump/playerjump16.obj");
        Jump.add("models/hunter/Jump/playerjump17.obj");
        Jump.add("models/hunter/Jump/playerjump18.obj");
        Jump.add("models/hunter/Jump/playerjump19.obj");
        Jump.add("models/hunter/Jump/playerjump20.obj");
        Jump.add("models/hunter/Jump/playerjump21.obj");
        Jump.add("models/hunter/Jump/playerjump22.obj");
        Jump.add("models/hunter/Jump/playerjump23.obj");
        Jump.add("models/hunter/Jump/playerjump24.obj");
        Jump.add("models/hunter/Jump/playerjump25.obj");
        Jump.add("models/hunter/Jump/playerjump26.obj");
        Jump.add("models/hunter/Jump/playerjump27.obj");
        Jump.add("models/hunter/Jump/playerjump28.obj");
        Jump.add("models/hunter/Jump/playerjump29.obj");
        modelsMap.put(JUMP,Jump);
        ArrayList<String> Run = new ArrayList<String>();
        Run.add("models/hunter/Run/playerrun00.obj");
        Run.add("models/hunter/Run/playerrun01.obj");
        Run.add("models/hunter/Run/playerrun02.obj");
        Run.add("models/hunter/Run/playerrun03.obj");
        Run.add("models/hunter/Run/playerrun04.obj");
        Run.add("models/hunter/Run/playerrun05.obj");
        Run.add("models/hunter/Run/playerrun06.obj");
        Run.add("models/hunter/Run/playerrun07.obj");
        Run.add("models/hunter/Run/playerrun08.obj");
        Run.add("models/hunter/Run/playerrun09.obj");
        Run.add("models/hunter/Run/playerrun10.obj");
        Run.add("models/hunter/Run/playerrun11.obj");
        Run.add("models/hunter/Run/playerrun12.obj");
        Run.add("models/hunter/Run/playerrun13.obj");
        Run.add("models/hunter/Run/playerrun14.obj");
        Run.add("models/hunter/Run/playerrun15.obj");
        Run.add("models/hunter/Run/playerrun16.obj");
        Run.add("models/hunter/Run/playerrun17.obj");
        Run.add("models/hunter/Run/playerrun18.obj");
        Run.add("models/hunter/Run/playerrun19.obj");
        modelsMap.put(RUN,Run);
        ArrayList<String> Ranged = new ArrayList<String>();
        Ranged.add("models/hunter/Ranged/playerranged00.obj");
        Ranged.add("models/hunter/Ranged/playerranged01.obj");
        Ranged.add("models/hunter/Ranged/playerranged02.obj");
        Ranged.add("models/hunter/Ranged/playerranged03.obj");
        Ranged.add("models/hunter/Ranged/playerranged04.obj");
        Ranged.add("models/hunter/Ranged/playerranged05.obj");
        Ranged.add("models/hunter/Ranged/playerranged06.obj");
        Ranged.add("models/hunter/Ranged/playerranged07.obj");
        Ranged.add("models/hunter/Ranged/playerranged08.obj");
        Ranged.add("models/hunter/Ranged/playerranged09.obj");
        Ranged.add("models/hunter/Ranged/playerranged10.obj");
        Ranged.add("models/hunter/Ranged/playerranged11.obj");
        Ranged.add("models/hunter/Ranged/playerranged12.obj");
        Ranged.add("models/hunter/Ranged/playerranged13.obj");
        Ranged.add("models/hunter/Ranged/playerranged14.obj");
        Ranged.add("models/hunter/Ranged/playerranged15.obj");
        Ranged.add("models/hunter/Ranged/playerranged16.obj");
        Ranged.add("models/hunter/Ranged/playerranged17.obj");       
        Ranged.add("models/hunter/Ranged/playerranged18.obj");        
        Ranged.add("models/hunter/Ranged/playerranged19.obj");
        Ranged.add("models/hunter/Ranged/playerranged20.obj");
        Ranged.add("models/hunter/Ranged/playerranged21.obj");
        Ranged.add("models/hunter/Ranged/playerranged22.obj");
        Ranged.add("models/hunter/Ranged/playerranged23.obj");
        Ranged.add("models/hunter/Ranged/playerranged24.obj");
        Ranged.add("models/hunter/Ranged/playerranged25.obj");
        Ranged.add("models/hunter/Ranged/playerranged26.obj");
        Ranged.add("models/hunter/Ranged/playerranged27.obj");
        Ranged.add("models/hunter/Ranged/playerranged28.obj");
        Ranged.add("models/hunter/Ranged/playerranged29.obj");
        Ranged.add("models/hunter/Ranged/playerranged30.obj");
        Ranged.add("models/hunter/Ranged/playerranged31.obj");
        Ranged.add("models/hunter/Ranged/playerranged32.obj");
        Ranged.add("models/hunter/Ranged/playerranged33.obj");
        Ranged.add("models/hunter/Ranged/playerranged34.obj");
        Ranged.add("models/hunter/Ranged/playerranged35.obj");
        Ranged.add("models/hunter/Ranged/playerranged36.obj");
        Ranged.add("models/hunter/Ranged/playerranged37.obj");
        modelsMap.put(RANGED,Ranged);
        ArrayList<String> Die = new ArrayList<String>();
        Die.add("models/hunter/Die/playerdie00.obj");
        Die.add("models/hunter/Die/playerdie01.obj");
        Die.add("models/hunter/Die/playerdie02.obj");
        Die.add("models/hunter/Die/playerdie03.obj");
        Die.add("models/hunter/Die/playerdie04.obj");
        Die.add("models/hunter/Die/playerdie05.obj");
        Die.add("models/hunter/Die/playerdie06.obj");
        Die.add("models/hunter/Die/playerdie07.obj");
        Die.add("models/hunter/Die/playerdie08.obj");
        Die.add("models/hunter/Die/playerdie09.obj");
        Die.add("models/hunter/Die/playerdie10.obj");
        Die.add("models/hunter/Die/playerdie11.obj");
        Die.add("models/hunter/Die/playerdie12.obj");
        Die.add("models/hunter/Die/playerdie13.obj");
        Die.add("models/hunter/Die/playerdie14.obj");
        Die.add("models/hunter/Die/playerdie15.obj");
        Die.add("models/hunter/Die/playerdie16.obj");
        Die.add("models/hunter/Die/playerdie17.obj");
        Die.add("models/hunter/Die/playerdie18.obj");
        Die.add("models/hunter/Die/playerdie19.obj");
        Die.add("models/hunter/Die/playerdie20.obj");
        Die.add("models/hunter/Die/playerdie21.obj");
        modelsMap.put(DIE,Die);
        ArrayList<String> Backwards = new ArrayList<String>();
        Backwards.add("models/hunter/Backwards/playerbackwards00.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards01.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards02.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards03.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards04.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards05.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards06.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards07.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards08.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards09.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards10.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards11.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards12.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards13.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards14.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards15.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards16.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards17.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards18.obj");
        Backwards.add("models/hunter/Backwards/playerbackwards19.obj");
        modelsMap.put(BACKWARDS,Backwards);
    }
}
