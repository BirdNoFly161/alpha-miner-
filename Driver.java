import java.util.*;

public class Driver{
    public static void main(String args[]){

        // activities
        ArrayList<String> TL=new ArrayList<String>(Arrays.asList("a","b","c","d","e","f"));

        /*
        ArrayList<String> TL=new ArrayList<String>(Arrays.asList("a","b","c"));
        */
        //event log
        ArrayList<ArrayList<String>> eventLog=new ArrayList<>();

        eventLog.add(new ArrayList<String>(Arrays.asList("a","b","e","f")));
        eventLog.add(new ArrayList<String>(Arrays.asList("a","b","e","c","d","b","f")));
        eventLog.add(new ArrayList<String>(Arrays.asList("a","b","c","e","d","b","f")));
        eventLog.add(new ArrayList<String>(Arrays.asList("a","b","c","d","e","b","f")));
        eventLog.add(new ArrayList<String>(Arrays.asList("a","e","b","c","d","b","f")));

        int size=eventLog.size();


        //subsets of singletons for the powerset
        ArrayList<ArrayList<String>> powerSet=new ArrayList<>();
        
        ArrayList<ArrayList<String>> subPowerSet=new ArrayList<>();

        for(int i=0;i<TL.size();i++){
            ArrayList<String> subset=new ArrayList<String>();
            subset.add(TL.get(i));
            subPowerSet.add(subset);
            powerSet.add(subset);
        }
        
        //create powerset brute
        for(int i=0;i<TL.size()-1;i++){
            ArrayList<ArrayList<String>> subPowerSet_tmp=new ArrayList<>();

            for(int j=0;j<subPowerSet.size();j++){
                ArrayList<String> subset=subPowerSet.get(j);
                for(int k=0;k<TL.size();k++){
                    ArrayList<String> newSubset=new ArrayList<String>();
                    for(int h=0;h<subset.size();h++){
                        newSubset.add(subset.get(h));
                    }
                    newSubset.add(TL.get(k));
                    subPowerSet_tmp.add(newSubset);
                    powerSet.add(newSubset);
                }
            }
            subPowerSet=subPowerSet_tmp;

        }


        //remove duplicate activities from powerset
        boolean changed=false;
        for(int i=0;i<powerSet.size();i++){
            ArrayList<String> subset=powerSet.get(i);
            if(subset.size()==1){
                continue;
            }
            int j=subset.size();
            for(int k=0;k<j;k++){
                changed=false;
                String act=subset.get(k);
                for(int h=k+1;h<j;h++){
                    if(act.equals(subset.get(h))){
                        subset.remove(h);
                        j--;
                        changed=true;
                    }
                }
                if(changed){
                    k--;
                }
            }
        }

        //sort activities in subsets
        for(int i=0;i<powerSet.size();i++){
            ArrayList<String> subset=powerSet.get(i);
            Collections.sort(subset);
        }
        
        //remove duplicate subsets
        int j=powerSet.size();
        for(int i=0;i<j;i++){
            changed=false;
            ArrayList<String> subset=powerSet.get(i);
            for(int k=i+1;k<j;k++){
                if(subset.equals(powerSet.get(k))){
                    powerSet.remove(k);
                    j--;
                    changed=true;
                }
            }
            if(changed){
                i--;
            }
        }

        //footprint for >
        ArrayList<ArrayList<String>> footprint=new ArrayList<>();

        for(int i=0;i<TL.size();i++){
            ArrayList<String> subFootprint=new ArrayList<>();
            footprint.add(subFootprint);
        }

        for(int i=0;i<eventLog.size();i++){
            ArrayList<String> traversal=eventLog.get(i);
            for(j=0;j<TL.size();j++){
                int index=traversal.indexOf(TL.get(j));
                String act=TL.get(j);
                if(index!=-1){
                    ArrayList<String> subFootprint=footprint.get(j);
                    for(int k=0;k<traversal.size()-1;k++){
                        if(traversal.get(k).equals(act)){
                            subFootprint.add(traversal.get(k+1));
                        }
                    }
                }
            }
        }

        //remove duplicates activities from subfootprints
        changed=false;
        for(int i=0;i<footprint.size();i++){
            ArrayList<String> subFootprint=footprint.get(i);
            if(subFootprint.size()==1){
                continue;
            }
            j=subFootprint.size();
            for(int k=0;k<j;k++){
                changed=false;
                String act=subFootprint.get(k);
                for(int h=k+1;h<j;h++){
                    if(act.equals(subFootprint.get(h))){
                        subFootprint.remove(h);
                        j--;
                        changed=true;
                    }
                }
                if(changed){
                    k--;
                }
            }
        }

        //footprint for ->
        ArrayList<ArrayList<String>> footprintArrow=new ArrayList<>();
        for(int i=0;i<TL.size();i++){
            ArrayList<String> subFootprint=new ArrayList<>();
            footprintArrow.add(subFootprint);
        }
        for(int i=0;i<footprint.size();i++){
            ArrayList<String> subFootprint=footprint.get(i);
            String act=TL.get(i);
            for(j=0;j<subFootprint.size();j++){
                if(footprint.get(TL.indexOf(subFootprint.get(j))).contains(act)==false){
                    footprintArrow.get(i).add(subFootprint.get(j));
                }
            }
        }

        //footprint for #
        ArrayList<ArrayList<String>> footprintSharp=new ArrayList<>();
        for(int i=0;i<TL.size();i++){
            ArrayList<String> subFootprint=new ArrayList<>();
            footprintSharp.add(subFootprint);
        }
        for(int i=0;i<TL.size();i++){
            String act=TL.get(i);
            ArrayList<String> subfootprint=footprint.get(i);
            for(j=0;j<TL.size();j++){
                if(subfootprint.contains(TL.get(j))==false){
                    ArrayList<String> newSubfootprint=footprint.get(j);
                    if(newSubfootprint.contains(act)==false){
                        footprintSharp.get(i).add(TL.get(j));
                    }
                }
            }
        }



        //show powerset
        /*
        for(int i=0;i<powerSet.size();i++){
            ArrayList<String> subset=powerSet.get(i);
            for(String activity : subset){
                System.out.print(activity+" ");
            }
            System.out.print("\n");
        }
        System.out.println("powerset size: "+powerSet.size());
        */

        //show footprint
        
        System.out.print("footprint: \n");
        for(int i=0;i<footprint.size();i++){
            ArrayList<String> subFootprint=footprint.get(i);
            System.out.print(TL.get(i)+": ");
            for(String activity : subFootprint){
                System.out.print(activity+" ");
            }
            System.out.print("\n");
        }
        

        //show footprint arrow
        
        System.out.print("footprint ->: \n");
        for(int i=0;i<footprintArrow.size();i++){
            ArrayList<String> subFootprint=footprintArrow.get(i);
            System.out.print(TL.get(i)+": ");
            for(String activity : subFootprint){
                System.out.print(activity+" ");
            }
            System.out.print("\n");
        }
        

        //show footprint sharp
        /*
        System.out.print("footprint #: \n");
        for(int i=0;i<footprintSharp.size();i++){
            ArrayList<String> subFootprint=footprintSharp.get(i);
            System.out.print(TL.get(i)+": ");
            for(String activity : subFootprint){
                System.out.print(activity+" ");
            }
            System.out.print("\n");
        }
        */

        //build powerset of non-interconnected subsets
        boolean validSubset;
        ArrayList<ArrayList<String>> powerSet_nonInterconnected=new ArrayList<>();
        for(int i=0;i<powerSet.size();i++){
            validSubset=true;
            ArrayList<String> subset=powerSet.get(i);
            for(j=0;j<subset.size();j++){
                if(validSubset==false){
                    break;
                }
                String activity=subset.get(j);
                for(int k=0;k<subset.size();k++){
                    if(validSubset==false){
                        break;
                    }
                    if(footprintSharp.get(TL.indexOf(activity)).contains(subset.get(k))==false){
                        validSubset=false;
                        break;
                    }
                }
            }
            if(validSubset==true){
                powerSet_nonInterconnected.add(subset);
            }
        }

        //show powerset with interconnected subsets removed
        /*
        for(int i=0;i<powerSet_nonInterconnected.size();i++){
            ArrayList<String> subset=powerSet_nonInterconnected.get(i);
            for(String activity : subset){
                System.out.print(activity+" ");
            }
            System.out.print("\n");
        }
        System.out.println("powerset_nonInterconnected size: "+powerSet_nonInterconnected.size());
        */

        // build XL off of the footprint
        boolean validPair;
        ArrayList<ArrayList<ArrayList<String>>> XL=new ArrayList<>();
        for(int i=0;i<powerSet_nonInterconnected.size();i++){
            ArrayList<String> first=powerSet_nonInterconnected.get(i);
            for(j=0;j<powerSet_nonInterconnected.size();j++){
                if(i==j){
                    continue;
                    
                }
                validPair=true;
                ArrayList<String> second=powerSet_nonInterconnected.get(j);
                for(int k=0;k<first.size();k++){
                    if(validPair==false){
                        break;
                    }
                    String activity=first.get(k);
                    for(int h=0;h<second.size();h++){
                        if(validPair==false){
                            break;
                        }
                        if(footprintArrow.get(TL.indexOf(activity)).contains(second.get(h))==false){
                            validPair=false;
                            break;
                        }
                    }
                }
                if(validPair==true){
                    ArrayList<ArrayList<String>> setTmp=new ArrayList<>();
                    setTmp.add(first);
                    setTmp.add(second);
                    XL.add(setTmp);
                }
            }
        }

        //show XL
        /*
        for(int i=0;i<XL.size();i++){
            ArrayList<ArrayList<String>> pair=XL.get(i);
            System.out.print("pair: "+i+"\n");

            System.out.print("first: "+"\n");
            ArrayList<String> first=pair.get(0);
            for(String activity : first){
                System.out.print(activity+" ");
            }
            System.out.print("\n");

            System.out.print("second: "+"\n");
            ArrayList<String> second=pair.get(1);
            for(String activity : second){
                System.out.print(activity+" ");
            }
            System.out.print("\n");


        }
        */

        //build YL by removing any pair of subsets where both members of the pairs are subsets of members of another subset
        ArrayList<ArrayList<ArrayList<String>>> YL=new ArrayList<>();
        for(int i=0;i<XL.size();i++){
            validPair=true;
            ArrayList<ArrayList<String>> pair_one=XL.get(i);
            ArrayList<String> pair_one_first=pair_one.get(0);
            ArrayList<String> pair_one_second=pair_one.get(1);
            for(j=0;j<XL.size();j++){
                if(i==j){
                    continue;
                }
                ArrayList<ArrayList<String>> pair_two=XL.get(j);
                ArrayList<String> pair_two_first=pair_two.get(0);
                ArrayList<String> pair_two_second=pair_two.get(1);
                if(pair_two_first.size()<pair_one_first.size() || pair_two_second.size()<pair_one_second.size()){
                    continue;
                }
                for(int k=0;k<pair_one_first.size();k++){
                    if(pair_two_first.contains(pair_one_first.get(k))==false){
                        break;
                    }
                    if(k==pair_one_first.size()-1);
                    {
                        for(int h=0;h<pair_one_second.size();h++){
                            if(pair_two_second.contains(pair_one_second.get(h))==false){
                                break;
                            }
                            if(h==pair_one_second.size()-1){
                                validPair=false;
                            }
                        }
                    }
                }

            }
            if(validPair==true){
                YL.add(pair_one);
            }
        }

        //show YL
        for(int i=0;i<YL.size();i++){
            ArrayList<ArrayList<String>> pair=YL.get(i);
            System.out.print("pair: "+i+"\n");

            System.out.print("first: "+"\n");
            ArrayList<String> first=pair.get(0);
            for(String activity : first){
                System.out.print(activity+" ");
            }
            System.out.print("\n");

            System.out.print("second: "+"\n");
            ArrayList<String> second=pair.get(1);
            for(String activity : second){
                System.out.print(activity+" ");
            }
            System.out.print("\n");


        }

        // show powerset
        /*
        for(int i=0;i<powerSet.size();i++){
            ArrayList<ArrayList<String>> subset=powerSet.get(i);
            for(String activity : subset){
                System.out.print(activity+" ");
            }
            System.out.print("\n");
        }
        */





        /*
        for(int i=0;i<size;i++){
            ArrayList<String> event=eventLog.get(i);
            for(String activity : event){
             System.out.print(activity+" ");
            }
         System.out.print("\n");
        }

        */
    }


}