package wecare.beecalm;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
    private static ArrayList<String> mantrasList = new ArrayList<String>();
    private static ArrayList<String> activityList = new ArrayList<String>();
    private static HashMap<Integer,String> mantra_record = new HashMap<Integer,String>();

    private static ArrayList<String> yogaList = new ArrayList<String>();
    private static HashMap<String, Integer> imageResource = new HashMap<String, Integer>();
    private static HashMap<String, Integer> yogaColor = new HashMap<String, Integer>();
    private static HashMap<String, Integer> yogaTextColor = new HashMap<String, Integer>();
    private static ArrayList<Boolean> yogaConfig = new ArrayList<Boolean>();

    private static String filename = "userConfigure.txt";
    private static String lineBreaker = "###";

    public String[] from = {"text", "image"};

    public final int SWIPE_MIN_DISTANCE = 120;
    public final int SWIPE_MAX_OFF_PATH = 250;
    public final int SWIPE_THRESHOLD_VELOCITY = 200;

    public static final String MANTRAS = "/MANTRASLIST";
    public static final String ACTIVITY = "/ACTIVITYLIST";
    public static final String YOGACONFIGURE = "/YOGACONFIGURELIST";

    private DataContainer(){}

    public static DataContainer getInstance(){
        if(mInstance == null){
            mInstance = new DataContainer();
            fillfixedConfig();
        }
        return mInstance;
    }

    public void loadData(Context applicationContext) {
        File file = applicationContext.getFileStreamPath(filename);
        if(file.exists()){
            System.out.println("fill exists, will start loading configuration");
            loadConfigure(applicationContext);
        }else{
            fillDefaultConfigure();
            System.out.println("fill dose not exists, has initialized lists and will start writing configuration");
            writeConfigure(applicationContext);
        }
    }

    private void loadConfigure(Context applicationContext) {
        mantrasList.clear();
        activityList.clear();
        yogaConfig.clear();
        mantra_record.clear();

        System.out.println("after clear lists, about to open the file.");
        try {
            FileInputStream fis = applicationContext.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            int stage = 0;  //stage is to what stage the loading process has been
            // 0: load mantras list  1: load activity list  2:load yoga configure list
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if(line.equals(lineBreaker)){
                    stage++;
                }else {
                    switch (stage) {
                        case 0:
                            mantrasList.add(line);
                            break;
                        case 1:
                            activityList.add(line);
                            break;
                        case 2:
                            if (line.equals("T")) {
                                yogaConfig.add(true);
                            } else {
                                yogaConfig.add(false);
                            }
                            break;
                        case 3:
                            if(line.length() > 3) {
                                String[] audio = line.split(lineBreaker);
                                mantra_record.put(Integer.parseInt(audio[0]), audio[1]);
                            }
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("finished loading the file. The size of mantrasList is " + mantrasList.size());
        System.out.println("finished loading the file. The size of activityList is " + activityList.size());
        System.out.println("finished loading the file. The size of yogaConfig is " + yogaConfig.size());
        System.out.println("finished loading the file. The size of mantra_record is " + mantra_record.size());

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
            for (int i = 0; i < activityList.size(); i++) {
                bw.newLine();
                bw.write(activityList.get(i));
            }

            //start writing yoga configure
            bw.newLine();
            bw.write(lineBreaker);
            for (int i = 0; i < yogaConfig.size(); i++) {
                bw.newLine();
                if(yogaConfig.get(i)){
                    bw.write("T");
                }else{
                    bw.write("F");
                }
            }

            //start writing mantras record configure
            bw.newLine();
            bw.write(lineBreaker);
            for (int i = 0; i < mantrasList.size(); i++) {
                if(mantra_record.get(i) != null){
                    bw.newLine();
                    System.out.println("mantra_record " + String.valueOf(i) + " >>>>>  " + mantra_record.get(i));
                    bw.write(String.valueOf(i));
                    bw.write(lineBreaker);
                    bw.write(mantra_record.get(i));
                }
            }

            bw.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("finished writing configuration");

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

        yogaTextColor.put("Boat", 0);
        yogaTextColor.put("Bow", 0);
        yogaTextColor.put("Bridge", 0);
        yogaTextColor.put("Warrior One", 0);

        yogaTextColor.put("Camel", 2);
        yogaTextColor.put("Fish", 2);
        yogaTextColor.put("Triangle", 2);
        yogaTextColor.put("Warrior Two", 2);

        yogaTextColor.put("Childs", 1);
        yogaTextColor.put("Cobra", 1);
        yogaTextColor.put("Downward Dog", 1);
        yogaTextColor.put("Gate", 1);
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

    public List<HashMap<String,String>> getYogaHash() {
        List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        for(int i=0; i<yogaList.size() ;i++){
            if(yogaConfig.get(i)) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put(from[0], yogaList.get(i) + " Pose");
                hm.put(from[1], Integer.toString(imageResource.get(yogaList.get(i))));
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

    public ArrayList<Boolean> getYogaConfig() { return yogaConfig; }

    public void updateYogaConfig(int position, boolean b) { yogaConfig.set(position, b); }

    public ArrayList<String> getYogaList() {return yogaList; }

    public HashMap<String, Integer> getYogaImages() {return imageResource; }

    public ArrayList<String> getMantrasString() {
        return mantrasList;
    }

    public void updateMantraList(int position, String text) {
        mantrasList.set(position, text);
    }

    public String insertNewMantra(String content) {
        mantrasList.add(content);
        return Integer.toString(mantrasList.size() - 1);
    }

    public String getMantraContent(String pos){
        return mantrasList.get(Integer.parseInt(pos));
    }

    public void updateActivityListOrder(int position, String text) {
        activityList.set(position, text);
    }

    public ArrayList<String> getActivityString() {
        ArrayList<String> activityContent = new ArrayList<String>();
        for(int i = 0; i < activityList.size(); i++){
            activityContent.add(activityList.get(i));
        }
        return activityContent;
    }

    public String getNextActivity(String activityName) {
        int nextPosition = (activityList.indexOf(activityName) + 1) % activityList.size();
        if(activityList.get(nextPosition).equals("Biofeedback (Watch Only)")){
            nextPosition ++;
        }
        nextPosition %= activityList.size();
        return activityList.get(nextPosition);
    }

    public String getPrevActivity(String activityName) {
        int prevPosition = activityList.indexOf(activityName) - 1;
        if(prevPosition < 0){
            prevPosition += activityList.size();
        }
        if(activityList.get(prevPosition).equals("Biofeedback (Watch Only)")){
            prevPosition --;
        }
        if(prevPosition < 0){
            prevPosition += activityList.size();
        }
        return activityList.get(prevPosition);
    }

    public String getActivityOrderByIndex(int index) {
        return activityList.get(index);
    }

    public ListFragment getFragment(String title) {
        switch (title) {
            case "Breathing":
                return new BreathingFragment();
            case "Mantras":
                return new MantrasFragment();
            case "Simon Swipe":
                return new SimonFragment();
            case "Yoga":
                return new YogaFragment();
        }
        return null;
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

    public int getFixedYogaColor(int position){
        return yogaColor.get(yogaList.get(position));
    }

    public int getColor(int position) {
        switch(position % 3){
            case 0:
                return R.color.color0;
            case 1:
                return R.color.color1;
            case 2:
                return R.color.color2;
        }
        return R.color.color3;
    }

    public void saveRecordHash(int position, String recordName) {
        mantra_record.put(position, recordName);
    }

    public void swapRecord(int fromPosition, int toPosition) {
        String pre = null;
        if (fromPosition < toPosition) {
            String beginRecord = mantra_record.get(fromPosition);
            for ( int i = fromPosition; i < toPosition ; i++){
                pre = mantra_record.get( i + 1);
                mantra_record.put(i, pre);
            }
            if(beginRecord != null) {
                mantra_record.put(toPosition, beginRecord);
            }
        } else {
            String lastRecord = mantra_record.get(fromPosition);
            for ( int i = fromPosition; i > toPosition ; i--){
                pre = mantra_record.get( i - 1);
                mantra_record.put(i, pre);
            }
            if(lastRecord != null) {
                mantra_record.put(toPosition, lastRecord);
            }
        }
    }

    public String getRecordHash(int position) {
        return mantra_record.get(position);
    }

    public int getYogaTextColor(int position) {
        int realPose = 0;
        while(position > 0){
            if( yogaConfig.get(realPose)){
                position--;
            }
            realPose++;
        }
        return yogaTextColor.get(yogaList.get(realPose));
    }

    public String getMantrasListToWatch() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < mantrasList.size(); i++){
            sb.append(mantrasList.get(i));
            sb.append(lineBreaker);
        }
        return sb.toString();
    }

    public String getActivityOrderToWatch() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < activityList.size(); i++){
            sb.append(activityList.get(i));
            sb.append(lineBreaker);
        }
        return sb.toString();
    }

    public String getYogaConfigToWatch() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < yogaConfig.size(); i++){
            if(yogaConfig.get(i)){
                sb.append("T");
            }else{
                sb.append("F");
            }
            sb.append(lineBreaker);
        }
        return sb.toString();
    }


}
