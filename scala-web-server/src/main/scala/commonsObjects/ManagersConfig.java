package commonsObjects;

/**
 * Created by Axel on 29-04-16.
 */
public class ManagersConfig {

    private final HeatManager heatManager;
    private final LightManager lightManager;
    private final InterfaceKit interfaceKit;
    private final DetecteurIncendie detecteurIncendie;
    private final RoueEpices roueEpices;

    private static ManagersConfig ourInstance = new ManagersConfig();

    public static ManagersConfig getInstance() {
        return ourInstance;
    }

    private ManagersConfig() {
        heatManager = new HeatManager(1, 0.0);
        lightManager = new LightManager(1, 0);
        detecteurIncendie = new DetecteurIncendie(20);
        interfaceKit = new InterfaceKit(heatManager, lightManager, detecteurIncendie);
        roueEpices = new RoueEpices();
    }

    public HeatManager getHeatManager(){return heatManager;}
    public LightManager getLightManager(){return lightManager;}
    public RoueEpices getRoueEpices(){return roueEpices;}
}
