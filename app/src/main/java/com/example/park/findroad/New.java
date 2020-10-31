package com.example.park.findroad;


import android.content.SharedPreferences;
import android.util.Log;
import android.content.Context;

import com.example.park.MainActivity;
import com.example.park.fragment.InputMap;

import java.util.Deque;
import java.util.List;



public class New {
    public static int[][] message = new int[50][50];
    public static int parking_length = 0;
    public static int parking_width = 0;
    //
    private final Context context;
    public int[] des = new int[3];

    public New(Context context) {
        this.context=context;
    }

    public static void show_message() {
        for(int i = 0; i < message.length; ++i) {
            for(int j = 0; j < message[i].length; ++j) {
                if (message[i][j] != 0) {
                    System.out.println("i:" + i + ",j:" + j + ",值" + message[i][j]);
                }
            }
        }

    }

    public static void simulate() {
        int i;
        for(i = 0; i < message.length; ++i) {
            if (message[0][i] != 0) {
                ++parking_width;
            } else if (message[0][i] == 0) {
                break;
            }
        }

        for(i = 0; i < message.length; ++i) {
            if (message[i][0] != 0) {
                ++parking_length;
            } else if (message[i][0] == 0) {
                break;
            }
        }

    }

    public void Sort(AstarPathPlan a) {
        double minn = 0.0D;
        int temp1 = 0;
        int temp2 = 0;

        int i;
        int j;
        for(i = 0; i < parking_length; ++i) {
            for(j = 0; j < parking_width; ++j) {
                if (a.map.count_path[i][j] != 0.0D) {
                    minn = a.map.count_path[i][j];
                    break;
                }
            }
        }

        des[2] = (int)minn;

        for(i = 0; i < parking_length; ++i) {
            for(j = 0; j < parking_width; ++j) {
                if (a.map.count_path[i][j] < minn && message[i][j] == 2) {
                    minn = a.map.count_path[i][j];
                    temp1 = i;
                    temp2 = j;
                }
            }
        }

        des[0] = temp1;
        des[1] = temp2;
    }

    public String PrintParkingPath(List<InputMap> inputMap,char selection)
    {

        AstarPathPlan astarPathPlan = null;

        for(int i=0;i<200;i++)
        {
            message[inputMap.get(i).getX()][inputMap.get(i).getY()]=inputMap.get(i).getValues();
        }

        simulate();


        if(selection=='A')
        {
            astarPathPlan=new AstarPathPlan(parking_width,parking_length,1,0,0);
            astarPathPlan.map.editObstacle(message);
            astarPathPlan.map.print();

        }
        else if(selection=='B')
        {
            astarPathPlan=new AstarPathPlan(parking_width,parking_length,1,0,1);
            astarPathPlan.map.editObstacle(message);
            astarPathPlan.map.print();
        }


        for(int i=0;i<parking_length;i++)
        {
            for(int j=0;j<parking_width;j++)
            {
                Deque<MapNode> result2 =astarPathPlan.pathPlanning(new MapNode(0,0), new MapNode(j,i));
                astarPathPlan.printSumpath(result2);
                double temp2=astarPathPlan.map.count_path[i][j];
            }
        }

        Sort(astarPathPlan);

        astarPathPlan.change_A(des[1], des[0]);
        Deque<MapNode>result3=astarPathPlan.pathPlanning(new MapNode(0,0), new MapNode(des[1],des[0]));
        String str = astarPathPlan.printResult(result3);
        double temp3=astarPathPlan.map.count_path[des[0]-1][des[1]];

        System.out.println(temp3);

        /*存储本地数据*/
        //获取sharedPreferences对象
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("user", Context.MODE_PRIVATE);
        //获取editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        //SharedPreferences.Editor editor = getSharedPreferences("user", 0).edit();
        editor.putInt("x",des[0]);
        editor.putInt("y",des[1]);
        editor.commit();


        return str; //des[0] is x,des[1] is y,des[2]is path length

    }
/*
    public String mapstringchange()  //return String message[i][j]
    {
        String s1="";
        StringBuffer sb=new StringBuffer(s1);
        for(int i=0;i<parking_length;i++)
        {
            for(int j=0;j<parking_width;j++)
                sb.append(message[i][j]);
            sb.append('\n');
        }
        String s2=sb.toString();
        return s2;
    }*/
public String mapstringchange()
{
    String s1="";
    StringBuffer sb=new StringBuffer(s1);
    for(int i=0;i<parking_length;i++)
    {
        for(int j=0;j<parking_width;j++)
        {
            if(message[i][j]==1)
                sb.append("□ ");
            else if(message[i][j]==2||message[i][j]==4)
                sb.append("▲");
            else if(message[i][j]==3)
                sb.append("■ ");

        }
        sb.append('\n');
    }
    String s2=sb.toString();

    return s2;
}
public String findparking(List<InputMap> inputMap)
{
    simulate();

    AstarPathPlan a = new AstarPathPlan(parking_width,parking_length,1,0,0);
    a.map.editObstacle(message);
   SharedPreferences sharedPreferences =this.context.getSharedPreferences("user",'0');
    //SharedPreferences.Editor editor = this.context.getSharedPreferences("user", 0).edit();
    int x=sharedPreferences.getInt("x",1);
    int y=sharedPreferences.getInt("y",1);

    a.change_A(x,y);
    Deque<MapNode>result3 = a.pathPlanning(new MapNode(0,0),new MapNode(x,y));
    /*a.change_A(7,10);
    Deque<MapNode>result3 = a.pathPlanning(new MapNode(0,0),new MapNode(7,10));*/
    String str =a.printResult(result3);
    return str;
}

}

