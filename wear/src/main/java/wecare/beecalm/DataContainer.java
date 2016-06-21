package wecare.beecalm;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zhuosi on 4/24/16.
 */
public class DataContainer {
    private static DataContainer mInstance = null;
    private static List<String> mantrasList = new ArrayList<String>();
    private static List<String> activityList = new ArrayList<String>();
    private static ArrayList<String> yogaList = new ArrayList<String>();
    private static HashMap<String, Integer> yogaColor = new HashMap<String, Integer>();
    private static HashMap<String, Integer> imageResource = new HashMap<String, Integer>();
    private static ArrayList<Boolean> yogaConfig = new ArrayList<Boolean>();

    public static final String MANTRAS = "/MANTRASLIST";
    public static final String ACTIVITY = "/ACTIVITYLIST";
    public static final String YOGACONFIGURE = "/YOGACONFIGURELIST";

    public String[] from = {"image"};

    private static String filename = "userConfigure.txt";
    private static String lineBreaker = "###";

    private DataContainer(){}

    public static DataContainer getInstance(){
        if(mInstance == null){
            mInstance = new DataContainer();
            fillfixedConfig();
        }
        return mInstance;
    }

    private static void fillfixedConfig(){
        yogaList.add("Boat");
        yogaList.add("Camel");
        yogaList.add("Childs");
        yogaList.add("Bow");
        yogaList.add("Fish");
        yogaList.add("Cobra");
        yogaList.add("Bridge");
        yogaList.add("Triangle");
        yogaList.add("Downward Dog");
        yogaList.add("Warrior One");
        yogaList.add("Warrior Two");
        yogaList.add("Gate");

        imageResource.put("Boat", R.drawable.yoga_boat);
        imageResource.put("Camel",R.drawable.yoga_camel);
        imageResource.put("Childs",R.drawable.yoga_childs);
        imageResource.put("Bow", R.drawable.yoga_bow);
        imageResource.put("Bridge", R.drawable.yoga_bridge);
        imageResource.put("Cobra",R.drawable.yoga_cobra);
        imageResource.put("Downward Dog",R.drawable.yoga_downward_dog);
        imageResource.put("Fish",R.drawable.yoga_fish);
        imageResource.put("Gate",R.drawable.yoga_gate);
        imageResource.put("Triangle",R.drawable.yoga_triangle);
        imageResource.put("Warrior One",R.drawable.yoga_warrior_one);
        imageResource.put("Warrior Two",R.drawable.yoga_warrior_two);

        yogaColor.put("Boat", R.color.color0);
        yogaColor.put("Bow", R.color.color0);
        yogaColor.put("Bridge", R.color.color0);
        yogaColor.put("Warrior One",R.color.color0);

        yogaColor.put("Camel",R.color.color2);
        yogaColor.put("Fish",R.color.color2);
        yogaColor.put("Triangle",R.color.color2);
        yogaColor.put("Warrior Two",R.color.color2);

        yogaColor.put("Childs",R.color.color1);
        yogaColor.put("Cobra",R.color.color1);
        yogaColor.put("Downward Dog",R.color.color1);
        yogaColor.put("Gate",R.color.color1);

    }

    public void loadData(Context applicationContext) {
        File file = applicationContext.getFileStreamPath(filename);
        if(file.exists()){
            System.out.println("file exists, will start loading configuration");
            loadConfigure(applicationContext);
        }else{
            fillDefaultConfigure();
            System.out.println("file dose not exists, has initialized lists and will start writing configuration");
            writeConfigure(applicationContext);
        }
    }

    private void loadConfigure(Context applicationContext) {
        mantrasList.clear();
        activityList.clear();
        yogaConfig.clear();

        try {
            FileInputStream fis = applicationContext.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            int stage = 0;  //stage is to what stage the loading process has been
            // 0: load mantras list  1: load activity list  2:load yoga configure list
            while ((line = bufferedReader.readLine()) != null) {
                if(line.equals(lineBreaker)){
                    stage++;
                    continue;
                }
                switch(stage){
                    case 0 :
                        mantrasList.add(line);
                        break;
                    case 1 :
                        activityList.add(line);
                        break;
                    case 2 :
                        if(line.equals("T")){
                            yogaConfig.add(true);
                        }else{
                            yogaConfig.add(false);
                        }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        System.out.println("finished loading file.");

    }

    private static void fillDefaultConfigure(){
        fillMantrasList();
        fillActivityList();
        fillYogaList();
    }

    private static void fillMantrasList() {
        mantrasList.add("This too, will pass.");
        mantrasList.add("It's okay to feel anxious.");
        mantrasList.add("Ride this wave.");
        mantrasList.add("Don't let a bad day scare to you");
    }

    private static void fillActivityList() {
        activityList.add("Biofeedback (Watch Only)");
        activityList.add("Breathing");
        activityList.add("Mantras");
        activityList.add("Simon Swipe");
        activityList.add("Yoga");
    }

    private static void fillYogaList(){

        for(int i = 0 ; i < yogaList.size(); i++){
            yogaConfig.add(true);
        }

    }

    public void writeConfigure(Context applicationContext){
        FileOutputStream outputStream;

        try {
            outputStream = applicationContext.openFileOutput(filename, Context.MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

            //start writing mantras
            for (int i = 0; i < mantrasList.size(); i++) {
                bw.write(mantrasList.get(i));
                bw.newLine();
            }

            //start writing activity sequence
            bw.write(lineBreaker);
            bw.newLine();
            for (int i = 0; i < activityList.size(); i++) {
                bw.write(activityList.get(i));
                bw.newLine();
            }

            //start writing yoga configure
            bw.write(lineBreaker);
            bw.newLine();
            for (int i = 0; i < yogaConfig.size(); i++) {
                if(yogaConfig.get(i)){
                    bw.write("T");
                }else{
                    bw.write("F");
                }
                bw.newLine();
            }
            bw.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("finished writing configuration");
    }


    public String getMantraContent(String pos){
        return mantrasList.get(Integer.parseInt(pos));
    }

    public String getNextActivity(String activityName) {
        int nextPosition = (activityList.indexOf(activityName) + 1) % activityList.size();
        nextPosition %= activityList.size();
        return activityList.get(nextPosition);
    }

    public String getPrevActivity(String activityName) {
        int prevPosition = activityList.indexOf(activityName) - 1;
        if(prevPosition < 0){
            prevPosition += activityList.size();
        }
        return activityList.get(prevPosition);
    }

    public String getActivityOrderByIndex(int index) {
        return activityList.get(index);
    }

    public Intent getIntent(Context context, String title) {
        switch (title) {
            case "Biofeedback (Watch Only)":
                return new Intent(context, BiofeedbackActivity.class);
            case "Breathing":
                return new Intent(context, BreathActivity.class);
            case "Mantras":
                return new Intent(context, MantrasActivity.class);
            case "Simon Swipe":
                return new Intent(context, SimonActivity.class);
            case "Yoga":
                return new Intent(context, YogaActivity.class);
        }
        return null;
    }


    public List<HashMap<String,String>> getYogaHash() {
        List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        for(int i=0; i<yogaList.size() ;i++){
            if(yogaConfig.get(i)) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("image", Integer.toString(imageResource.get(yogaList.get(i))));
                list.add(hm);
            }
        }
        return list;
    }

    public List<HashMap<String,String>> getMantrasHash() {
        List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        for(int i=0; i < mantrasList.size() ;i++){
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("mantra", mantrasList.get(i));
            list.add(hm);
        }
        return list;
    }

    public int getYogaColor(int position) {
        int realPose = 0;
        while(position > 0){
            if( yogaConfig.get(realPose)){
                position--;
            }
            realPose++;
        }
        return yogaColor.get(yogaList.get(realPose));
    }

    public int getColor(int position) {
        switch(position % 3){
            case 0:
                return R.color.color0;
            case 1:
                return R.color.color1;
        }
        return R.color.color2;
    }

    public void updateMantrasFromPhone(String mantras_list) {
        mantrasList.clear();
        String[] mantras = mantras_list.split(lineBreaker);
        for(int i = 0; i < mantras.length; i++ ){
            mantrasList.add(mantras[i]);
        }
    }

    public void updateActivityFromPhone(String activity_list) {
        activityList.clear();
        String[] activities = activity_list.split(lineBreaker);
        for(int i = 0; i < activities.length; i++ ){
            activityList.add(activities[i]);
        }
    }

    public void updateYogaFromPhone(String yoga_list) {
        yogaConfig.clear();
        String[] yogas = yoga_list.split(lineBreaker);
        for(int i = 0; i < yogas.length; i++ ){
            if(yogas[i].equals("T")){
                yogaConfig.add(true);
            }else{
                yogaConfig.add(false);
            }
        }
    }

}
