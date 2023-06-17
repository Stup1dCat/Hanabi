package cn.hanabi.utils.waypoints;

import cn.hanabi.Hanabi;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WaypointManager {
    private final File WAYPOINT_DIR = getConfigFile("Waypoints");
    private final List<Waypoint> waypoints = new CopyOnWriteArrayList<>();

    public WaypointManager() {
        this.loadWaypoints();
    }

    public static List<String> read(File inputFile) {
        ArrayList<String> readContent = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));

            String str;
            while ((str = in.readLine()) != null) {
                readContent.add(str);
            }

            in.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return readContent;
    }

    public static File getConfigFile(String name) {
        File file = new File(getConfigDir(), String.format("%s.txt", name));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

        return file;
    }

    public static File getConfigDir() {
        File file = new File(Minecraft.getMinecraft().mcDataDir, Hanabi.CLIENT_NAME);
        if (!file.exists()) {
            file.mkdir();
        }

        return file;
    }

    public static void write(File outputFile, List writeContent, boolean overrideContent) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8));
            for (Object o : writeContent) {
                String outputLine = (String) o;
                out.write(outputLine + System.getProperty("line.separator"));
            }

            out.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public void loadWaypoints() {
        try {
            List<? extends String> fileContent = read(this.WAYPOINT_DIR);

            for (String line : fileContent) {
                String[] split = line.split(":");
                String waypointName = split[0];
                assert split.length == 6;
                this.createWaypoint(waypointName, new Vec3(Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3])), Integer.parseInt(split[4]), split[5]);
            }
        } catch (Exception var6) {
            Hanabi.INSTANCE.println("[ERROR] Failed loading waypoints! Please check that waypoints.txt is in the valid format!");
            var6.printStackTrace();
        }

    }

    public void saveWaypoints() {
        List<String> fileContent = new ArrayList<>();

        for (Waypoint waypoint : this.getWaypoints()) {
            String waypointName = waypoint.getName();
            String x = String.valueOf(waypoint.getVec3().xCoord);
            String y = String.valueOf(waypoint.getVec3().yCoord);
            String z = String.valueOf(waypoint.getVec3().zCoord);
            fileContent.add(String.format("%s:%s:%s:%s:%s:%s", waypointName, x, y, z, waypoint.getColor(), waypoint.getAddress()));
        }

        write(this.WAYPOINT_DIR, fileContent, true);
    }

    public void deleteWaypoint(Waypoint waypoint) {
        this.waypoints.remove(waypoint);
    }

    public void clearWaypoint() {
        this.waypoints.clear();
    }

    public void createWaypoint(String name, Vec3 vec3, int color, String address) {
        this.waypoints.add(new Waypoint(name, vec3, color, address));
        this.saveWaypoints();
    }

    public List<Waypoint> getWaypoints() {
        return this.waypoints;
    }

    public boolean containsName(String name) {
        Iterator<Waypoint> var2 = this.getWaypoints().iterator();

        Waypoint waypoint;
        do {
            if (!var2.hasNext()) {
                return true;
            }

            waypoint = var2.next();
        } while (!waypoint.getName().equalsIgnoreCase(name));

        return false;
    }
}
